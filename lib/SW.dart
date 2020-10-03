import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:path_provider/path_provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

import 'profiles/Minion.dart';
import 'profiles/Character.dart';
import 'profiles/Vehicle.dart';
import 'Preferences.dart' as preferences;

class SW{

  final List<Minion> _minions = new List();
  final List<String> minCats = new List();

  final List<Character> _characters = new List();
  final List<String> charCats = new List();

  final List<Vehicle> _vehicles = new List();
  final List<String> vehCats = new List();

  final SharedPreferences prefs;

  String saveDir;

  bool devMode;

  SW({this.prefs, this.saveDir, this.devMode});

  void loadAll(){
    _minions.clear();
    _characters.clear();
    _vehicles.clear();
    List<Editable> defered = new List();
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
  }
  void loadMinions(){
    _minions.clear();
    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".swminion")){
        var temp = Minion.load(element, this);
        _minions.add(temp);
      }
    });
  }
  void loadCharacters(){
    _characters.clear();
    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".swcharacter")){
        var temp = Character.load(element, this);
        _characters.add(temp);
      }
    });
  }
  void loadVehicles(){
    _vehicles.clear();
    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".swvehicle")){
        var temp = Vehicle.load(element, this);
        _vehicles.add(temp);
      }
    });
  }

  void add(Editable editable){
    if(editable is Character)
      addCharacter(editable);
    if(editable is Minion)
      addMinion(editable);
    if(editable is Vehicle)
      addVehicle(editable);
  }

  bool remove(Editable editable){
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
    if(search == "")
      return _characters.where((element) => element.category == category).toList();
    if(category == "")
      return _characters.where((element) => element.name.contains(search)).toList();
    return _characters.where((element) => element.category == category)
        .where((element) => element.name.contains(search)).toList();
  }

  bool removeCharacter({int id, Character character}){
    var success = false;
    if(character != null)
      success = _characters.remove(character);
    if(id != null){
      character = _characters.firstWhere((element) => element.id == id, orElse: ()=>null);
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
      if(!charCats.contains(element.category))
        charCats.add(element.category);
    });
  }

  List<Minion> minions({String search = "", String category = ""}){
    if(search == "" && category == "")
      return _minions;
    if(search == "")
      return _minions.where((element) => element.category == category).toList();
    if(category == "")
      return _minions.where((element) => element.name.contains(search)).toList();
    return _minions.where((element) => element.category == category)
        .where((element) => element.name.contains(search)).toList();
  }

  bool removeMinion({int id, Minion minion}){
    var success = false;
    if(minion != null)
      success = _minions.remove(minion);
    if(id != null){
      minion = _minions.firstWhere((element) => element.id == id, orElse: ()=>null);
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
      if(!minCats.contains(element.category))
        minCats.add(element.category);
    });
  }

  List<Vehicle> vehicles({String search = "", String category = ""}){
    if(search == "" && category == "")
      return _vehicles;
    if(search == "")
      return _vehicles.where((element) => element.category == category).toList();
    if(category == "")
      return _vehicles.where((element) => element.name.contains(search)).toList();
    return _vehicles.where((element) => element.category == category)
        .where((element) => element.name.contains(search)).toList();
  }

  bool removeVehicle({int id, Vehicle vehicle}){
    var success = false;
    if(vehicle != null)
      success = _vehicles.remove(vehicle);
    if(id != null){
      vehicle = _vehicles.firstWhere((element) => element.id == id, orElse: ()=>null);
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
      if(!vehCats.contains(element.category))
        vehCats.add(element.category);
    });
  }
  
  void syncCloud(){
    //TODO: cload loading AND saving
  }

  static Future<SW> initialize() async{
    WidgetsFlutterBinding.ensureInitialized();
    var prefs = await SharedPreferences.getInstance();
    String saveDir;
    if(prefs.containsKey(preferences.saveLocation))
      saveDir = prefs.getString(preferences.saveLocation);
    else{
      var dir = await getExternalStorageDirectory();
      saveDir = dir.path+"/SWChars";
    }
    bool devMode = false;
    if(!Directory(saveDir).existsSync())
      Directory(saveDir).createSync();
    if((prefs.containsKey(preferences.dev) && prefs.getBool(preferences.dev)) || kDebugMode || kProfileMode){
      devMode = true;
      await testing(saveDir);
    }
    SW out = SW(prefs: prefs, saveDir: saveDir, devMode: devMode,);
    out.loadAll();
    return out;
  }

  static Future<void> testing(String saveDir) async{
    var testFiles = ["Big Game Hunter [Nemesis].swcharacter","Incom T-47 Airspeeder.swvehicle","Pirate Crew.swminion"];
    for(String st in testFiles){
      String json = await rootBundle.loadString("assets/testing/"+st);
      File testFile = File(saveDir+"/"+st);
      if(testFile.existsSync())
        testFile.deleteSync();
      testFile.writeAsStringSync(json);
    }
  }

  static SW of(BuildContext context) => context.dependOnInheritedWidgetOfExactType<SWWidget>().app;
}

class SWWidget extends InheritedWidget{
  final SW app;

  SWWidget({Widget child, this.app}) : super(child: child);

  @override
  bool updateShouldNotify(covariant InheritedWidget oldWidget) => false;
}