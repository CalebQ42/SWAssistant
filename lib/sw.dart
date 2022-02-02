import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:googleapis/drive/v3.dart' as drive;
import 'package:in_app_purchase/in_app_purchase.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:path_provider/path_provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:swassistant/firebase_options.dart';
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
  late Observatory observatory;
  late PackageInfo package;

  Driver? driver;

  SW({required this.prefs});

  void loadAll(){
    _minions.clear();
    _characters.clear();
    _vehicles.clear();
    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".backup")){
        return;
      }
      var backup = File(element.path+".backup");
      if(backup.existsSync()){
        File(element.path).deleteSync();
        backup.copySync(element.path);
        backup.deleteSync();
        element = File(element.path);
      }
      if(element.path.endsWith(".swcharacter")){
        _characters.add(Character.load(element, this));
      }else if(element.path.endsWith(".swminion")){
        _minions.add(Minion.load(element, this));
      }else if(element.path.endsWith(".swvehicle")){
        _vehicles.add(Vehicle.load(element, this));
      }
    });
    updateCharacterCategories();
    updateVehicleCategories();
    updateMinionCategories();
  }

  void loadMinions(){
    _minions.clear();
    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".swminion")){
        var temp = Minion.load(element, this);
        _minions.add(temp);
      }
    });
    updateMinionCategories();
  }

  void loadCharacters(){
    _characters.clear();
    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".swcharacter")){
        var temp = Character.load(element, this);
        _characters.add(temp);
      }
    });
    updateCharacterCategories();
  }

  void loadVehicles(){
    _vehicles.clear();
    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".swvehicle")){
        var temp = Vehicle.load(element, this);
        _vehicles.add(temp);
      }
    });
    updateVehicleCategories();
  }

  void add(Editable editable){
    if(editable is Character){
      addCharacter(editable);
    }else if(editable is Minion){
      addMinion(editable);
    }else if(editable is Vehicle){
      addVehicle(editable);
    }
  }

  bool remove(Editable editable, BuildContext? context){
    if(context != null && editable.route != null && observatory.containsRoute(route: editable.route) != null){
      Navigator.removeRoute(context, editable.route!);
    }
    if(editable is Character){
      return removeCharacter(character: editable);
    }else if(editable is Minion){
      return removeMinion(minion: editable);
    }else if(editable is Vehicle){
      return removeVehicle(vehicle: editable);
    }
    return false;
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
  
  List<Character> characters({String search = "", String? category}){
    if(search == "" && category == null){
      return List.of(_characters);
    }else if(category == null){
      return _characters.where((element) => element.name.toLowerCase().contains(search.toLowerCase())).toList();
    }
    return _characters.where((element) => element.name.toLowerCase().contains(search.toLowerCase()) && element.category == category).toList();
  }

  bool removeCharacter({String? uid, Character? character}){
    var success = false;
    if(character != null){
      success = _characters.remove(character);
    }else if(uid != null){
      character = _characters.firstWhere((element) => element.uid == uid);
      success = _characters.remove(character);
    }
    if(success && character != null && characters(category: character.category).isEmpty){
      updateCharacterCategories();
    }
    return success;
  }

  void addCharacter(Character character){
    _characters.add(character);
    //TODO: Better insertion.
    updateCharacterCategories();
  }

  void updateCharacterCategories(){
    charCats.clear();
    for(var char in _characters){
      if(char.category != "" && !charCats.contains(char.category)){
        charCats.add(char.category);
      }
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

  bool removeMinion({String? uid, Minion? minion}){
    var success = false;
    if(minion != null){
      success = _minions.remove(minion);
    }else if(uid != null){
      minion = _minions.firstWhere((element) => element.uid == uid);
      success = _minions.remove(minion);
    }
    if(success && minion != null && minions(category: minion.category).isEmpty){
      updateMinionCategories();
    }
    return success;
  }

  void addMinion(Minion minion){
    _minions.add(minion);
    //TODO: Better insertion.
    updateMinionCategories();
  }

  void updateMinionCategories(){
    minCats.clear();
    for(var min in _minions){
      if(min.category != "" && !minCats.contains(min.category)){
        minCats.add(min.category);
      }
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

  bool removeVehicle({String? uid, Vehicle? vehicle}){
    var success = false;
    if(vehicle != null){
      success = _vehicles.remove(vehicle);
    }else if(uid != null){
      vehicle = _vehicles.firstWhere((element) => element.uid == uid);
      success = _vehicles.remove(vehicle);
    }
    if(success && vehicle != null && vehicles(category: vehicle.category).isEmpty){
      updateVehicleCategories();
    }
    return success;
  }

  void addVehicle(Vehicle vehicle){
    _vehicles.add(vehicle);
    //TODO: Better insertion.
    updateVehicleCategories();
  }
  
  void updateVehicleCategories(){
    vehCats.clear();
    for(var veh in _vehicles){
      if(veh.category != "" && !vehCats.contains(veh.category)){
        vehCats.add(veh.category);
      }
    }
  }

  Future<List<Editable>?> downloadAndMatch() async{
    driver ??= Driver();
    var okay = await driver!.ready("SWChars");
    if(!okay) return null;
    var all = <Editable>[
      ..._characters,
      ..._minions,
      ..._vehicles
    ];
    var loadWaiting = 0;
    for(var fil in await driver!.listFiles("") ?? <drive.File>[]){
      if(fil.id == null || fil.name == null) continue;
      var uid = fil.appProperties?["uid"];
      if (uid == null) {
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
        await ed.cloudLoad(this, fil.id!).then((value) async {
          Editable? matchEd;
          try {
            matchEd = all.firstWhere((element) => element.uid == ed.uid);
          }catch(e){
            matchEd = null;
          }
          if(matchEd == null){
            await ed.save(app: this, localOnly: true);
            add(ed);
            loadWaiting--;
            return;
          }
          matchEd.driveId = fil.id!;
          var local = File(matchEd.getFileLocation(this));
          if(fil.modifiedTime == null || local.lastModifiedSync().isBefore(fil.modifiedTime!)){
            await matchEd.cloudLoad(this, fil.id!);
            await matchEd.save(app: this, localOnly: true);
          } else {
            await matchEd.cloudSave(this);
          }
          all.remove(matchEd);
          loadWaiting--;
        });
      } else {
        Editable? matchEd;
        try {
          matchEd = all.firstWhere((element) => element.uid == uid);
        }catch(e){
          matchEd = null;
        }
        if(matchEd == null){
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
          await ed.cloudLoad(this, fil.id!).then((value) async {
            await ed.save(app: this, localOnly: true);
            add(ed);
            loadWaiting--;
          });
          continue;
        }
        matchEd.driveId = fil.id!;
        var local = File(matchEd.getFileLocation(this));
        if(fil.modifiedTime == null || local.lastModifiedSync().isBefore(fil.modifiedTime!)){
          loadWaiting++;
          await matchEd.cloudLoad(this, fil.id!).then((value) async {
            await matchEd!.save(app: this, localOnly: true);
            loadWaiting--;
          });
        } else {
          loadWaiting++;
          matchEd.cloudSave(this).then((value) => loadWaiting--);
        }
        all.remove(matchEd);
      }
    }
    while(loadWaiting > 0) {
      await Future.delayed(const Duration(milliseconds: 100));
    }
    return all;
  }

  Future<bool> initialSync([BuildContext? context]) async{
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
    var toUpload = await downloadAndMatch();
    if(toUpload == null){
      if(context != null) {
        Navigator.pop(context);
      }
      syncing = false;
      return false;
    }
    var uploadWaiting = 0;
    for (var ed in toUpload) {
      uploadWaiting++;
      ed.cloudSave(this).then((value) => uploadWaiting--);
    }
    while(uploadWaiting > 0) {
      await Future.delayed(const Duration(milliseconds: 100));
    }
    if(context != null) Navigator.pop(context);
    syncing = false;
    return true;
  }
  
  Future<bool> syncCloud([BuildContext? context]) async{
    syncing = true;
    var toDelete = await downloadAndMatch();
    if(toDelete == null){
      syncing = false;
      return false;
    }
    for(var ed in toDelete) {
      ed.delete(this);
      remove(ed, context);
    }
    syncing = false;
    return true;
  }

  dynamic getPreference(String preference, dynamic defaultValue) =>
    prefs.get(preference) ?? defaultValue;

  static Future<SW> baseInit() async{
    WidgetsFlutterBinding.ensureInitialized();
    var prefs = await SharedPreferences.getInstance();
    var app = SW(prefs: prefs);
    if(!kIsWeb){
      InAppPurchase.instance.purchaseStream.listen((event) {
        for(var e in event){
          if (e.pendingCompletePurchase){
            InAppPurchase.instance.completePurchase(e);
          }
        }
      });
    }
    if (prefs.getBool(preferences.dev) ?? false || kDebugMode || kProfileMode){
      app.devMode = true;
    }
    if(!kIsWeb){
      var dir = await getApplicationDocumentsDirectory();
      app.saveDir = dir.path + "/SWChars";
      if(!Directory(app.saveDir).existsSync()){
        Directory(app.saveDir).createSync();
      }
      app.loadAll();
    }
    app.observatory = Observatory(app);
    app.package = await PackageInfo.fromPlatform();
    prefs.setInt(preferences.startCount, app.getPreference(preferences.startCount, 0) + 1);
    return app;
  }

  Future<void> postInit(BuildContext context) async{
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
                AppLocalizations.of(context)!.loadingDialog,
                textAlign: TextAlign.center,
              )
            ]
          )
        )
    );
    if(getPreference(preferences.firebase, true)){
      try{
        await Firebase.initializeApp(
          options: DefaultFirebaseOptions.currentPlatform,
        );
        firebaseAvailable = true;
        if(!kIsWeb && getPreference(preferences.crashlytics, true) && kDebugMode == false){
          FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(true);
          FlutterError.onError = FirebaseCrashlytics.instance.recordFlutterError;
        }else{
          FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(false);
        }
      }catch (e){
        firebaseAvailable = false;
      }
    }
    if (getPreference(preferences.googleDrive, false)){
      if(getPreference(preferences.driveFirstLoad, true)){
        await initialSync();
        prefs.setBool(preferences.driveFirstLoad, false);
      }else{
        await syncCloud();
      }
    }
    Navigator.of(context).pop();
  }

  void manualImport(BuildContext context){
    var message = ScaffoldMessenger.of(context);
    var locs = AppLocalizations.of(context)!;
    var nav = Navigator.of(context);
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