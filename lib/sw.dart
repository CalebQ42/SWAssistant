// ignore_for_file: use_build_context_synchronously

import 'dart:io';


import 'package:firebase_core/firebase_core.dart' deferred as firebasecore;
import 'package:firebase_crashlytics/firebase_crashlytics.dart' deferred as crash;
import 'package:swassistant/firebase_options.dart' deferred as firebaseoptions;
import 'package:in_app_purchase/in_app_purchase.dart' deferred as inapp;
import 'package:path_provider/path_provider.dart' deferred as pathprov;
import 'package:googleapis/drive/v3.dart' as drive;

import 'package:swassistant/preferences.dart' as preferences;

import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart';
import 'package:file_picker/file_picker.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/ui/screens/loading.dart';
import 'package:swassistant/utils/driver/driver.dart';
import 'package:swassistant/utils/observatory.dart';
import 'package:uuid/uuid.dart';

import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class SW {
  final List<Minion> _min = [];
  final List<Character> _char = [];
  final List<Vehicle> _veh = [];

  final List<String> cats = [];

  final List<Editable> trash = [];


  Observatory? observatory;
  bool devMode = false;
  String saveDir = "";
  SharedPreferences prefs;
  late PackageInfo package;
  bool firebaseAvailable = false;
  late Function() topLevelUpdate;
  GlobalKey<NavigatorState> navKey = GlobalKey();

  Driver? driver;
  bool syncing = false;

  bool initialized = false;

  NavigatorState? get nav => navKey.currentState;
  bool get crashReporting => isMobile() && firebaseAvailable && getPref(preferences.crashlytics);

  SW(this.prefs);

  static Future<SW> baseInit() async {
    WidgetsFlutterBinding.ensureInitialized();
    var app = SW(await SharedPreferences.getInstance());
    if(app.isMobile()){
      await inapp.loadLibrary();
      inapp.InAppPurchase.instance.purchaseStream.listen((event) {
        for(var e in event){
          if (e.pendingCompletePurchase){
            inapp.InAppPurchase.instance.completePurchase(e);
          }
        }
      });
    }
    if(!app.getPref(preferences.googleDrive) || app.getPref(preferences.driveFirstLoad)){
      app.prefs.setBool(preferences.newDrive, true);
    }
    if(kDebugMode) app.devMode = true;
    if(!kIsWeb){
      await pathprov.loadLibrary();
      app.saveDir = "${await pathprov.getApplicationDocumentsDirectory()}/SWChars";
      if(!Directory(app.saveDir).existsSync()){
        Directory(app.saveDir).createSync();
      }
      app.loadLocal();
    }
    app.package = await PackageInfo.fromPlatform();
    app.prefs.setInt(preferences.startCount, app.getPref(preferences.startCount)+1);
    return app;
  }

  Future<void> postInit(LoadingState loadingState) async{
    if(getPref(preferences.firebase)){
      await firebaseoptions.loadLibrary();
      await firebasecore.loadLibrary();
      try{
        await firebasecore.Firebase.initializeApp(
          options:firebaseoptions.DefaultFirebaseOptions.currentPlatform
        );
        firebaseAvailable = true;
        if(!kDebugMode && !kProfileMode && isMobile() && getPref(preferences.crashlytics)){
          await crash.loadLibrary();
          crash.FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(true);
          FlutterError.onError = crash.FirebaseCrashlytics.instance.recordFlutterError;
        }
      }finally{}
    }
    if(kIsWeb) prefs.setBool(preferences.googleDrive, true);
    if(getPref(preferences.googleDrive)){
      loadingState.loadText = AppLocalizations.of(loadingState.context)!.driveSyncing;
      if(!await syncRemote()){
        driver = null;
        loadingState.driveErr = true;
      }
    }
    for(var ed in trash){
      if(ed.trashTime!.isBefore(DateTime.now().subtract(const Duration(days: 30)))){
        trash.remove(ed);
        ed.deletePermanently(this);
      }
    }
  }

  void loadLocal(){
    _min.clear();
    _char.clear();
    _veh.clear();
    trash.clear();
    for(var element in Directory(saveDir).listSync()) {
      var backup = File("${element.path}.backup");
      if(backup.existsSync()){
        File(element.path).deleteSync();
        backup.rename(element.path);
      }
      Editable? ed;
      if(element.path.endsWith(".swcharacter")){
        ed = Character.load(element, this);
      }else if(element.path.endsWith(".swminion")){
        ed = Minion.load(element, this);
      }else if(element.path.endsWith(".swvehicle")){
        ed = Vehicle.load(element, this);
      }
      if(ed == null) return;
      if(ed.trashed){
        trash.add(ed);
      }else{
        add(ed);
      }
    }
  }

  Future<bool> syncRemote({BuildContext? context, NavigatorState? nav}) async{
    var driveSuccess = false;
    if(getPref(preferences.driveFirstLoad)){
      driveSuccess = await _googleDriveSync(true, context: context, nav: nav);
    }else if(!getPref(preferences.newDrive)){
      if(await _googleDriveSync(false, scope: drive.DriveApi.driveAppdataScope, context: context, nav: nav)){
        driveSuccess = await _googleDriveSync(true, context: context, nav: nav);
      }
    }else{
      driveSuccess = await _googleDriveSync(false, context: context, nav: nav);
    }
    if(driveSuccess){
      prefs.setBool(preferences.driveFirstLoad, false);
      prefs.setBool(preferences.newDrive, true);
    }
    return driveSuccess;
  }

  Future<bool> _googleDriveSync(bool initial, {String scope = drive.DriveApi.driveFileScope, BuildContext? context, NavigatorState? nav}) async{
    syncing = true;
    if(context != null && nav != null){
      showDialog(
        barrierDismissible: false,
        context: context,
        builder: (c) =>
          AlertDialog(
            content: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const CircularProgressIndicator(),
                Container(height: 10),
                Text(
                  AppLocalizations.of(context)!.driveSyncing,
                  textAlign: TextAlign.center,
                )
              ]
            )
          )
      );
    }
    if(driver != null && driver!.scope != scope){
      await driver!.changeScope(scope);
      var okay = await driver!.setWD(scope == drive.DriveApi.driveAppdataScope ? "SWChars" : "SWAssistant");
      if(!okay){
        if(context != null) nav?.pop();
        return false;
      }
    }
    driver ??= Driver(scope, crashReporting);
    var okay = await driver!.ready();
    if(!okay){
      if(context != null) nav?.pop();
      return false;
    }
    okay = await driver!.setWD(scope == drive.DriveApi.driveAppdataScope ? "SWChars" : "SWAssistant");
    if(!okay){
      if(context != null) nav?.pop();
      return false;
    }
    var all = <Editable>[
      ..._char,
      ..._min,
      ..._veh,
      ...trash,
    ];
    var loadWaiting = 0;
    for(var fil in await driver!.listFiles("") ?? <drive.File>[]){
      if(fil.id == null || fil.name == null) continue;
      Editable ed;
      if(fil.name!.endsWith(".swcharacter")){
        ed = Character(app: this);
      } else if(fil.name!.endsWith(".swvehicle")){
        ed = Vehicle(app: this);
      } else if(fil.name!.endsWith(".swminion")){
        ed = Minion(app: this);
      } else {
        continue;
      }
      loadWaiting++;
      ed.cloudLoad(this, fil.id!).then((value) async {
        Editable? matchEd;
        try {
          matchEd = all.firstWhere((element) => element.uid == ed.uid);
        }catch(e){
          matchEd = null;
        }
        if(matchEd == null){
          await ed.save(app: this, localOnly: true);
          if(ed.trashed){
            trash.add(ed);
          }else{
            add(ed);
          }
          loadWaiting--;
          return;
        }
        all.remove(matchEd);
        var preTrashed = matchEd.trashed;
        matchEd.driveId = fil.id!;
        if(ed.lastMod == null || matchEd.lastMod == null){
          var local = File(matchEd.getFileLocation(this));
          if(fil.modifiedTime == null || local.lastModifiedSync().isBefore(fil.modifiedTime!)){
            loadWaiting++;
            matchEd.cloudLoad(this, fil.id!).then((value) async {
              await matchEd!.save(app: this, localOnly: true);
              loadWaiting--;
            }, onError: (e) => loadWaiting--);
          } else {
            matchEd.cloudSave(this);
          }
        }else if(matchEd.lastMod!.isBefore(ed.lastMod!)){
            matchEd.cloudLoad(this, fil.id!).then((value) async {
              await matchEd!.save(app: this, localOnly: true);
              loadWaiting--;
            }, onError: (e) => loadWaiting--);
        }else{
          matchEd.cloudSave(this);
        }
        if(matchEd.trashed != preTrashed){
          if(matchEd.trashed){
            remove(matchEd);
            trash.add(matchEd);
          }else{
            trash.remove(matchEd);
            add(matchEd);
          }
        }
        loadWaiting--;
      }, onError: (e) => loadWaiting--);
    }
    while(loadWaiting > 0){
      await Future.delayed(const Duration(milliseconds: 100));
    }
    if(initial){
      for(var ed in all){
        ed.cloudSave(this);
      }
      if(context != null) nav?.pop();
    }else{
      for(var ed in all){
        if(ed.trashed){
          trash.remove(ed);
          ed.deletePermanently(this);
        }else{
          ed.trash(this);
          ed.cloudSave(this);
        }
      }
    }
    syncing = false;
    return true;
  }

  void add(Editable ed){
    switch(ed.runtimeType){
      case Character:
        _char.add(ed as Character);
        break;
      case Minion:
        _min.add(ed as Minion);
        break;
      default:
        _veh.add(ed as Vehicle);
    }
    if(ed.category != "" && !cats
.contains(ed.category)) cats
.add(ed.category);
  }

  void remove(Editable ed, [BuildContext? context]){
    if(context != null && ed.route != null && observatory?.containsRoute(route:ed.route) != null){
      Navigator.removeRoute(context, ed.route!);
    }
    switch(ed.runtimeType){
      case Character:
        _char.remove(ed);
        break;
      case Minion:
        _min.remove(ed);
        break;
      default:
        _veh.remove(ed);
    }
    if(ed.category != "" && getList(category: ed.category).isEmpty){
      cats
  .remove(ed.category);
    }
  }

  void updateCategory(Editable ed, String newCat){
    if(!cats
.contains(newCat)) cats
.add(newCat);
    if(getList(category: ed.category).length == 1){
      cats
  .remove(ed.category);
    }
    ed.category = newCat;
  }

  List<Editable> getList({Type? type, String? search, String? category}){
    List<Editable> searchList;
    if(type == null){
      searchList = <Editable>[
        ..._char,
        ..._min,
        ..._veh
      ];
    }else{
      switch(type){
        case Character:
          searchList = _char;
          break;
        case Minion:
          searchList = _min;
          break;
        case Vehicle:
          searchList = _veh;
          break;
        default:
          return [];
      }
    }
    if(search != null){
      searchList = searchList.where((ed) => ed.name.toLowerCase().contains(search.toLowerCase())).toList();
    }
    if(category != null){
      searchList = searchList.where((ed) => ed.category == category).toList();
    }
    return searchList;
  }

  Editable? getEditable(String uid){
    try {
      return <Editable>[
        ..._char,
        ..._min,
        ..._veh
      ].firstWhere((ed) => ed.uid == uid);
    }catch(e){
      return null;
    }
  }

  dynamic getPref(String key) {
    return prefs.get(key) ?? preferences.defaultPreference[key];
  }

  bool isMobile(){
    if(kIsWeb){
      return false;
    }
    return Platform.isAndroid || Platform.isIOS;
  }

  void manualImport(BuildContext context){
    var message = ScaffoldMessenger.of(context);
    var locs = AppLocalizations.of(context)!;
    var nav = Navigator.of(context, rootNavigator: true);
    showDialog(
      barrierDismissible: false,
      context: context,
      builder: (context) =>
        AlertDialog(
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const CircularProgressIndicator(),
              Container(height: 10),
              Text(
                AppLocalizations.of(context)!.importDialog,
                textAlign: TextAlign.center,
              )
            ]
          )
        )
    );
    FilePicker.platform.pickFiles(
      allowMultiple: true,
      allowedExtensions: [
        "swcharacter",
        "swvehicle",
        "swminion"
      ]
    ).then((value) {
      if(value != null && value.files.isNotEmpty){
        var uuid = const Uuid();
        for(var f in value.files){
          var fil = File(f.path!);
          Editable ed;
          switch (f.extension){
            case "swminion":
              ed = Minion.load(fil, this);
              break;
            case "swcharacter":
              ed = Character.load(fil, this);
              break;
            case "swvehicle":
              ed = Vehicle.load(fil, this);
              break;
            default:
              continue;
          }
          ed.uid = uuid.v4();
          ed.loc = "";
          ed.save(app: this);
          add(ed);
        }
      }
      nav.pop();
      if (value == null || value.files.isEmpty){
        message.clearSnackBars();
        message.showSnackBar(
          SnackBar(content: Text(locs.importNone))
        );
      }else{
        message.clearSnackBars();
        message.showSnackBar(
          SnackBar(
            content: Text(locs.importSuccess(value.files.length))
          )
        );
      }
    });
  }

  static SW of(BuildContext context){
    var app = context.dependOnInheritedWidgetOfExactType<SWWidget>()?.app;
    if (app == null){
      throw "Widget is not a child of SWWidget";
    }
    return app;
  }
}

class SWWidget extends InheritedWidget{
  final SW app;
  
  const SWWidget({Key? key, required Widget child, required this.app}) : super(key: key, child: child);

  @override
  bool updateShouldNotify(covariant InheritedWidget oldWidget) => false;
}