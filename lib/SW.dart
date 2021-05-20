import 'dart:io';

import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:path_provider/path_provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:swassistant/main.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

import 'profiles/Minion.dart';
import 'profiles/Character.dart';
import 'profiles/Vehicle.dart';
import 'Preferences.dart' as preferences;

class SW{
  List<Minion> _minions = [];
  List<String> minCats = [];

  List<Character> _characters = [];
  List<String> charCats = [];

  List<Vehicle> _vehicles = [];
  List<String> vehCats = [];

  SharedPreferences prefs;

  bool firebaseAvailable = false;
  late Observatory observatory;

  String saveDir = "";

  bool devMode = false;

  SW({required this.prefs});

  void loadAll(){
    _minions.clear();
    _characters.clear();
    _vehicles.clear();
    List<Editable> defered = [];
    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".backup"))
        return;
      var backup = File(element.path+".backup");
      if(backup.existsSync()){
        File(element.path).deleteSync();
        backup.copySync(element.path);
        backup.deleteSync();
      }
      if(element.path.endsWith(".swcharacter")){
        var temp = Character.load(element, this);
        if(temp.id != null){
          _characters.add(temp);
        }else
          defered.add(temp);
      }else if(element.path.endsWith(".swminion")){
        var temp = Minion.load(element, this);
        if(temp.id != null){
          _minions.add(temp);
        }else
          defered.add(temp);
      }else if(element.path.endsWith(".swvehicle")){
        var temp = Vehicle.load(element, this);
        if(temp.id != null){
          _vehicles.add(temp);
        }else
          defered.add(temp);
      }
    });
    if(defered.length >0){
      var charId = 0;
      var minId = 0;
      var vehId = 0;
      defered.forEach((temp) {
        if(temp is Character){
          while(defered.any((e)=>e.id==charId)){
            charId++;
          }
          temp.id = charId;
          _characters.add(temp);
        }else if (temp is Minion){
          while(defered.any((e)=>e.id==minId)){
            minId++;
          }
          temp.id = minId;
          _minions.add(temp);
        }else if (temp is Vehicle){
          while(defered.any((e)=>e.id==vehId)){
            vehId++;
          }
          temp.id = vehId;
          _vehicles.add(temp);
        }
      });
    }
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
    if(editable is Character)
      addCharacter(editable);
    if(editable is Minion)
      addMinion(editable);
    if(editable is Vehicle)
      addVehicle(editable);
  }

  bool remove(Editable editable, BuildContext context){
    if(editable.route != null && observatory.containsRoute(route: editable.route) != null && editable.route != null)
      Navigator.removeRoute(context, editable.route!);
    if(editable is Character)
      return removeCharacter(character: editable);
    if(editable is Minion)
      return removeMinion(minion: editable);
    if(editable is Vehicle)
      return removeVehicle(vehicle: editable);
    return false;
  }
  
  List<Character> characters({String search = "", String category = ""}){
    if(search == "" && category == "")
      return _characters;
    else if(search == "")
      return _characters.where((element) => element.category == category).toList();
    else if(category == "")
      return _characters.where((element) => element.name.contains(search)).toList();
    else
      return _characters.where((element) => element.category == category)
          .where((element) => element.name.contains(search)).toList();
  }

  bool removeCharacter({int id = -1, Character? character}){
    var success = false;
    if(character != null)
      success = _characters.remove(character);
    if(id != -1){
      character = _characters.firstWhere((element) => element.id == id);
      success = _characters.remove(character);
    }
    if(success && character != null && characters(category: character.category).length == 0)
      updateCharacterCategories();
    return success;
  }

  void addCharacter(Character character){
    _characters.add(character);
    updateCharacterCategories();
  }

  void updateCharacterCategories(){
    charCats.clear();
    _characters.forEach((element) {
      if(element.category != "" && !charCats.contains(element.category))
        charCats.add(element.category);
    });
  }

  List<Minion> minions({String search = "", String category = ""}){
    if(search == "" && category == "")
      return _minions;
    else if(search == "")
      return _minions.where((element) => element.category == category).toList();
    else if(category == "")
      return _minions.where((element) => element.name.contains(search)).toList();
    else
      return _minions.where((element) => element.category == category)
          .where((element) => element.name.contains(search)).toList();
  }

  bool removeMinion({int id = -1, Minion? minion}){
    var success = false;
    if(minion != null)
      success = _minions.remove(minion);
    if(id != -1){
      minion = _minions.firstWhere((element) => element.id == id);
      success = _minions.remove(minion);
    }
    if(success && minion != null && minions(category: minion.category).length == 0)
      updateMinionCategories();
    return success;
  }

  void addMinion(Minion minion){
    _minions.add(minion);
    updateMinionCategories();
  }

  void updateMinionCategories(){
    minCats.clear();
    _minions.forEach((element) {
      if(element.category != "" && !minCats.contains(element.category))
        minCats.add(element.category);
    });
  }

  List<Vehicle> vehicles({String search = "", String category = ""}){
    if(search == "" && category == "")
      return _vehicles;
    else if(search == "")
      return _vehicles.where((element) => element.category == category).toList();
    else if(category == "")
      return _vehicles.where((element) => element.name.contains(search)).toList();
    else
      return _vehicles.where((element) => element.category == category)
          .where((element) => element.name.contains(search)).toList();
  }

  bool removeVehicle({int id = -1, Vehicle? vehicle}){
    var success = false;
    if(vehicle != null)
      success = _vehicles.remove(vehicle);
    if(id != -1){
      vehicle = _vehicles.firstWhere((element) => element.id == id);
      success = _vehicles.remove(vehicle);
    }
    if(success && vehicle != null && vehicles(category: vehicle.category).length == 0)
      updateVehicleCategories();
    return success;
  }

  void addVehicle(Vehicle vehicle){
    _vehicles.add(vehicle);
    updateVehicleCategories();
  }
  
  void updateVehicleCategories(){
    vehCats.clear();
    _vehicles.forEach((element) {
      if(element.category != "" && !vehCats.contains(element.category))
        vehCats.add(element.category);
    });
  }
  
  void syncCloud(){
    //TODO: cload loading AND saving
  }

  dynamic getPreference(String preference, dynamic defaultValue) =>
    prefs.get(preference) ?? defaultValue;

  Future<void> initialize() async{
    var dir = await getExternalStorageDirectory();
    if (dir == null){

    }else {
      saveDir = dir.path + "/SWChars";
    }
    if(!Directory(saveDir).existsSync())
      Directory(saveDir).createSync();
    if(kDebugMode || kProfileMode)
      await testing(saveDir);
    observatory = Observatory();
    loadAll();
    if(getPreference(preferences.crashlytics, true)){
      try{
        await Firebase.initializeApp();
        firebaseAvailable = true;
        if(getPreference(preferences.crashlytics, true) && kDebugMode == false)
          FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(true);
        else
          FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(false);
      }catch (e){
        print(e);
        firebaseAvailable = false;
      }
    }
  }

  static Future<void> testing(String saveDir) async{
    var testFiles = ["Big Game Hunter [Nemesis][Testing].swcharacter","Incom T-47 Airspeeder [Testing].swvehicle","Pirate Crew [Testing].swminion"];
    for(String st in testFiles){
      String json = await rootBundle.loadString("assets/testing/"+st);
      File testFile = File(saveDir+"/"+st);
      if(testFile.existsSync())
        testFile.deleteSync();
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
  
  SWWidget({Widget? child, required this.app}) : super(child: child ?? Text("Hello There! Something went wrong"));

  @override
  bool updateShouldNotify(covariant InheritedWidget oldWidget) => false;
}