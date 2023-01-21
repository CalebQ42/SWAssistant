import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:firebase_core/firebase_core.dart' deferred as firebasecore;
import 'package:firebase_crashlytics/firebase_crashlytics.dart' deferred as crash;
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:googleapis/drive/v3.dart' as drive;
import 'package:in_app_purchase/in_app_purchase.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:path_provider/path_provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:swassistant/firebase_options.dart' deferred as firebaseoptions;
import 'package:swassistant/main.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/utils/driver/driver.dart';
import 'package:uuid/uuid.dart';

import 'profiles/minion.dart';
import 'profiles/character.dart';
import 'profiles/vehicle.dart';
import 'preferences.dart' as preferences;

class SW{
  final List<Minion> _minions = [];
  List<String> minCats = [];

  final List<Character> _characters = [];
  List<String> charCats = [];

  final List<Vehicle> _vehicles = [];
  List<String> vehCats = [];

  SharedPreferences prefs;

  bool firebaseAvailable = false;
  String saveDir = "";
  bool devMode = false;
  bool syncing = false;

  late Function() topLevelUpdate;
  late PackageInfo package;
  Observatory? observatory;

  bool initialized = false;

  Driver? driver;

  GlobalKey<NavigatorState> navy = GlobalKey();

  final List<Editable> trashCan = [];

  SW({required this.prefs});

  NavigatorState? nav() => navy.currentState;

  void loadAll(){
    _minions.clear();
    _characters.clear();
    _vehicles.clear();
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
        trashCan.add(ed);
      }else{
        add(ed);
      }
    }
  }

  // void loadMinions(){
  //   _minions.clear();
  //   Directory(saveDir).listSync().forEach((element) {
  //     if(element.path.endsWith(".swminion")){
  //       var temp = Minion.load(element, this);
  //       _minions.add(temp);
  //     }
  //   });
  //   updateMinionCategories();
  // }

  // void loadCharacters(){
  //   _characters.clear();
  //   Directory(saveDir).listSync().forEach((element) {
  //     if(element.path.endsWith(".swcharacter")){
  //       var temp = Character.load(element, this);
  //       _characters.add(temp);
  //     }
  //   });
  //   updateCharacterCategories();
  // }

  // void loadVehicles(){
  //   _vehicles.clear();
  //   Directory(saveDir).listSync().forEach((element) {
  //     if(element.path.endsWith(".swvehicle")){
  //       var temp = Vehicle.load(element, this);
  //       _vehicles.add(temp);
  //     }
  //   });
  //   updateVehicleCategories();
  // }

  void add(Editable editable){
    if(editable is Character){
      addCharacter(editable);
    }else if(editable is Minion){
      addMinion(editable);
    }else if(editable is Vehicle){
      addVehicle(editable);
    }
  }

  void remove(Editable editable, [BuildContext? context]){
    if(context != null && editable.route != null && observatory?.containsRoute(route: editable.route) != null){
      Navigator.removeRoute(context, editable.route!);
    }
    if(editable is Character){
      removeCharacter(editable);
    }else if(editable is Minion){
      removeMinion(editable);
    }else if(editable is Vehicle){
      removeVehicle(editable);
    }
  }

  Editable? findEditable(String uid) {
    try {
      return <Editable>[
        ..._characters,
        ..._minions,
        ..._vehicles
      ].firstWhere((element) => element.uid == uid);
    }catch(e){
      return null;
    }
  }

  void updateCategory(Editable ed, String newCat) {
    switch(ed.runtimeType){
      case Character:
        if(characters(category: ed.category).length == 1){
          charCats.remove(ed.category);
        }
        if(newCat != "" && !charCats.contains(newCat)) charCats.add(newCat);
        break;
      case Minion:
        if(minions(category: ed.category).length == 1){
          minCats.remove(ed.category);
        }
        if(newCat != "" && !minCats.contains(newCat)) minCats.add(newCat);
        break;
      case Vehicle:
        if(vehicles(category: ed.category).length == 1){
          vehCats.remove(ed.category);
        }
        if(newCat != "" && !vehCats.contains(newCat)) vehCats.add(newCat);
        break;
    }
    ed.category = newCat;
  }
  
  List<Character> characters({String search = "", String? category}){
    if(search == "" && category == null){
      return List.of(_characters);
    }else if(category == null){
      return _characters.where((element) => element.name.toLowerCase().contains(search.toLowerCase())).toList();
    }
    return _characters.where((element) => element.name.toLowerCase().contains(search.toLowerCase()) && element.category == category).toList();
  }

  void removeCharacter(Character character){
    _characters.remove(character);
    if(character.category != "" && characters(category: character.category).isEmpty){
      charCats.remove(character.category);
    }
  }

  void addCharacter(Character character){
    _characters.add(character);
    if(character.category != "" && !charCats.contains(character.category)){
      charCats.add(character.category);
    }
  }

  List<Minion> minions({String search = "", String? category}){
    if(search == "" && category == null){
      return List.of(_minions);
    }else if(category == null){
      return _minions.where((element) => element.name.toLowerCase().contains(search.toLowerCase())).toList();
    }
    return _minions.where((element) => element.name.toLowerCase().contains(search.toLowerCase()) && element.category == category).toList();
  }

  void removeMinion(Minion minion){
    _minions.remove(minion);
    if(minion.category != "" && minions(category: minion.category).isEmpty){
      minCats.remove(minion.category);
    }
  }

  void addMinion(Minion minion){
    _minions.add(minion);
    if(minion.category != "" && !minCats.contains(minion.category)){
      minCats.add(minion.category);
    }
  }

  List<Vehicle> vehicles({String search = "", String? category}){
    if(search == "" && category == null){
      return List.of(_vehicles);
    }else if(category == null){
      return _vehicles.where((element) => element.name.toLowerCase().contains(search.toLowerCase())).toList();
    }
    return _vehicles.where((element) => element.name.toLowerCase().contains(search.toLowerCase()) && element.category == category).toList();
  }

  void removeVehicle(Vehicle vehicle){
    _vehicles.remove(vehicle);
    if(vehicle.category != "" && vehicles(category: vehicle.category).isEmpty){
      vehCats.remove(vehicle.category);
    }
  }

  void addVehicle(Vehicle vehicle){
    _vehicles.add(vehicle);
    if(vehicle.category != "" && !vehCats.contains(vehicle.category)){
      vehCats.add(vehicle.category);
    }
  }

  Future<List<Editable>?> downloadAndMatch([String scope = drive.DriveApi.driveFileScope]) async{
    if(driver != null && driver!.scope != scope){
      await driver!.changeScope(scope);
      var okay = await driver!.setWD(scope == drive.DriveApi.driveAppdataScope ? "SWChars" : "SWAssistant");
      if(!okay) return null;
    }
    driver ??= Driver(scope);
    var okay = await driver!.ready();
    if(!okay) return null;
    okay = await driver!.setWD(scope == drive.DriveApi.driveAppdataScope ? "SWChars" : "SWAssistant");
    if(!okay) return null;
    var all = <Editable>[
      ..._characters,
      ..._minions,
      ..._vehicles,
      ...trashCan,
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
            trashCan.add(ed);
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
            trashCan.add(matchEd);
          }else{
            trashCan.remove(matchEd);
            add(matchEd);
          }
        }
        loadWaiting--;
      }, onError: (e) => loadWaiting--);
    }
    while(loadWaiting > 0) {
      await Future.delayed(const Duration(milliseconds: 50));
    }
    return all;
  }

  Future<bool> initialSync({BuildContext? context, NavigatorState? nav,  String scope = drive.DriveApi.driveFileScope}) async{
    syncing = true;
    if(context != null){
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
    var toUpload = await downloadAndMatch(scope);
    if(toUpload == null){
      if(nav != null) {
        nav.pop();
      }
      syncing = false;
      return false;
    }
    for (var ed in toUpload) {
      ed.cloudSave(this);
    }
    if(nav != null) nav.pop();
    syncing = false;
    return true;
  }
  
  Future<bool> syncCloud({BuildContext? context, String scope = drive.DriveApi.driveFileScope}) async{
    syncing = true;
    var toDelete = await downloadAndMatch(scope);
    if(toDelete == null){
      syncing = false;
      return false;
    }
    for(var ed in toDelete) {
      if(ed.trashed){
        trashCan.remove(ed);
        ed.deletePermanently(this);
      }else{
        ed.trash(this);
        ed.cloudSave(this);
      }
    }
    syncing = false;
    return true;
  }

  dynamic getPreference(String preference, dynamic defaultValue) =>
    prefs.get(preference) ?? defaultValue;

  Future<bool> sync() async{
    if (getPreference(preferences.googleDrive, false)){
      if(getPreference(preferences.driveFirstLoad, true)){
        if(await initialSync()){
          prefs.setBool(preferences.driveFirstLoad, false);
          prefs.setBool(preferences.newDrive, true);
          return true;
        }
        driver = null;
        return false;
      }else if(!getPreference(preferences.newDrive, false)){
        if(await syncCloud(scope: drive.DriveApi.driveAppdataScope)){
          if(await initialSync(scope: drive.DriveApi.driveFileScope)){
            prefs.setBool(preferences.newDrive, true);
            return true;
          }
        }
        driver = null;
        return false;
      }
      return await syncCloud();
    }
    return true;
  }

  bool isMobile(){
    if(kIsWeb){
      return false;
    }
    return Platform.isAndroid || Platform.isIOS;
  }

  static Future<SW> baseInit() async{
    WidgetsFlutterBinding.ensureInitialized();
    var prefs = await SharedPreferences.getInstance();
    var app = SW(prefs: prefs);
    if(app.isMobile()){
      InAppPurchase.instance.purchaseStream.listen((event) {
        for(var e in event){
          if (e.pendingCompletePurchase){
            InAppPurchase.instance.completePurchase(e);
          }
        }
      });
    }
    if (!app.getPreference(preferences.googleDrive, false) || app.getPreference(preferences.driveFirstLoad, true)) {
      app.prefs.setBool(preferences.newDrive, true);
    }
    if (prefs.getBool(preferences.dev) ?? false || kDebugMode || kProfileMode){
      app.devMode = true;
    }
    if(!kIsWeb){
      var dir = await getApplicationDocumentsDirectory();
      app.saveDir = "${dir.path}/SWChars";
      if(!Directory(app.saveDir).existsSync()){
        Directory(app.saveDir).createSync();
      }
      app.loadAll();
    }
    app.package = await PackageInfo.fromPlatform();
    prefs.setInt(preferences.startCount, app.getPreference(preferences.startCount, 0) + 1);
    return app;
  }

  Future<void> postInit() async{
    if(getPreference(preferences.firebase, true)){
      await firebaseoptions.loadLibrary();
      await firebasecore.loadLibrary();
      try{
        await firebasecore.Firebase.initializeApp(
          options: firebaseoptions.DefaultFirebaseOptions.currentPlatform,
        );
        firebaseAvailable = true;
        if(isMobile() && getPreference(preferences.crashlytics, true) && !kDebugMode){
          await crash.loadLibrary();
          crash.FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(true);
          FlutterError.onError = crash.FirebaseCrashlytics.instance.recordFlutterError;
        }
      }catch (e){
        firebaseAvailable = false;
      }
    }
    if (kIsWeb) prefs.setBool(preferences.googleDrive, true);
    await sync();
    for(var ed in trashCan){
      if(ed.trashTime!.isBefore(DateTime.now().subtract(const Duration(days: 30)))){
        trashCan.remove(ed);
        ed.deletePermanently(this);
      }
    }
    initialized = true;
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
    FilePicker.platform.pickFiles(allowMultiple: true).then((value) {
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