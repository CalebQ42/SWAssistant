import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:googleapis/drive/v3.dart' as drive;
import 'package:in_app_purchase/in_app_purchase.dart';
import 'package:path_provider/path_provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
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

  late Function() topLevelUpdate;
  late Observatory observatory;

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

  bool remove(Editable editable, BuildContext context){
    if(editable.route != null && observatory.containsRoute(route: editable.route) != null){
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

  Future<bool> initialSync(BuildContext context) async{
    showDialog(
      barrierDismissible: false,
      context: context,
      builder: (c) =>
        BackButtonListener(
          onBackButtonPressed: () => Future.value(true),
          child: AlertDialog(
            content: Column(
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
        )
    );
    var okay = await driveInit();
    if(!okay) return false;
    for(var fil in await driver!.listFiles("") ?? <drive.File>[]){
      if(fil.name == null) continue;
      if(fil.name!.endsWith(".swcharacter") || fil.name!.endsWith(".swminion") || fil.name!.endsWith(".swvehicle")){
        //TODO
      }
    }
    return false;
  }

  Future<bool> driveInit() async{
    if(driver != null && driver!.isReady()) return true;
    driver ??= Driver();
    if(!driver!.isReady()){
      if(!await driver!.init()){
        return false;
      }
    }
    var okay = await driver!.setWD("SWChars");
    return okay;
  }
  
  Future<bool> syncCloud() async{
    driver ??= Driver();
    if(!driver!.isReady()){
      if(!await driver!.init()){
        return false;
      }
    }
    //TODO
    return false;
  }

  dynamic getPreference(String preference, dynamic defaultValue) =>
    prefs.get(preference) ?? defaultValue;

  static Future<SW> baseInit() async{
    WidgetsFlutterBinding.ensureInitialized();
    var prefs = await SharedPreferences.getInstance();
    var app = SW(prefs: prefs);
    InAppPurchase.instance.purchaseStream.listen((event) {
      for(var e in event){
        if (e.pendingCompletePurchase){
          InAppPurchase.instance.completePurchase(e);
        }
      }
    });
    if (prefs.getBool(preferences.dev) ?? false || kDebugMode || kProfileMode){
      app.devMode = true;
    }
    var dir = await getApplicationDocumentsDirectory();
    app.saveDir = dir.path + "/SWChars";
    if(!Directory(app.saveDir).existsSync()){
      Directory(app.saveDir).createSync();
    }
    if(app.devMode){
      await testing(app.saveDir);
    }
    app.loadAll();
    app.observatory = Observatory(app);
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
    if (getPreference(preferences.googleDrive, false)){
      await syncCloud();
    }
    if(getPreference(preferences.firebase, true)){
      try{
        await Firebase.initializeApp();
        firebaseAvailable = true;
        if(getPreference(preferences.crashlytics, true) && kDebugMode == false){
          FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(true);
          FlutterError.onError = FirebaseCrashlytics.instance.recordFlutterError;
        }else{
          FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(false);
        }
      }catch (e){
        firebaseAvailable = false;
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

  static Future<void> testing(String saveDir) async{
    var testFiles = ["Big Game Hunter [Nemesis][Testing].swcharacter","Incom T-47 Airspeeder [Testing].swvehicle","Pirate Crew [Testing].swminion"];
    for(String st in testFiles){
      String json = await rootBundle.loadString("assets/testing/"+st);
      File testFile = File(saveDir+"/"+st);
      if(testFile.existsSync()){
        testFile.deleteSync();
      }
      testFile.writeAsStringSync(json);
    }
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