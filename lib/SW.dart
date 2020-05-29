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
  List<Minion> minions;
  List<String> minCats;

  List<Character> characters;
  List<String> charCats;

  List<Vehicle> vehicles;
  List<String> vehCats;

  SharedPreferences prefs;

  String saveDir;

  Future<void> initialize() async{
    WidgetsFlutterBinding.ensureInitialized();
    prefs = await SharedPreferences.getInstance();
    if(prefs.containsKey(preferences.saveLocation)){
      saveDir = prefs.getString(preferences.saveLocation);
    }else{
      var dir = await getExternalStorageDirectory();
      saveDir = dir.path+"/SWChars";
    }
    if(!Directory(saveDir).existsSync())
      Directory(saveDir).createSync();
    if(kDebugMode || kProfileMode)
      await testing();
    loadAll();
  }

  Future<void> loadPrefs() async => prefs = await SharedPreferences.getInstance();

  void loadAll(){
    minions = List<Minion>();
    minCats = List<String>();
    characters = List<Character>();
    charCats = List<String>();
    vehicles = List<Vehicle>();
    vehCats = List<String>();
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
          characters.add(temp);
          if(temp.category != "" && !charCats.contains(temp.category))
            charCats.add(temp.category);
        }else
          defered.add(temp);
      }else if(element.path.endsWith(".swminion")){
        var temp = Minion.load(element, this);
        if(temp.id != null){
          minions.add(temp);
          if(temp.category != "" && !minCats.contains(temp.category))
            minCats.add(temp.category);
        }else
          defered.add(temp);
      }else if(element.path.endsWith(".swvehicle")){
        var temp = Vehicle.load(element, this);
        if(temp.id != null){
          vehicles.add(temp);
          if(temp.category != "" && !vehCats.contains(temp.category))
            vehCats.add(temp.category);
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
          characters.add(temp);
          if(temp.category != "" && !charCats.contains(temp.category))
            charCats.add(temp.category);
        }else if (temp is Minion){
          while(defered.any((e)=>e.id==minId)){
            minId++;
          }
          temp.id = minId;
          minions.add(temp);
          if(temp.category != "" && !minCats.contains(temp.category))
            minCats.add(temp.category);
        }else if (temp is Vehicle){
          while(defered.any((e)=>e.id==vehId)){
            vehId++;
          }
          temp.id = vehId;
          vehicles.add(temp);
          if(temp.category != "" && !vehCats.contains(temp.category))
            vehCats.add(temp.category);
        }
      });
    }
  }
  void loadMinions(){
    minions = List();
    minCats = List();

    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".swminion")){
        var temp = Minion.load(element, this);
        minions.add(temp);
        if(temp.category != "" && !minCats.contains(temp.category))
          minCats.add(temp.category);
      }
    });
  }
  void loadCharacters(){
    characters = List();
    charCats = List();

    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".swcharacter")){
        var temp = Character.load(element, this);
        characters.add(temp);
        if(temp.category != "" && !charCats.contains(temp.category))
          charCats.add(temp.category);
      }
    });
  }
  void loadVehicles(){
    vehicles = List();
    vehCats = List();

    Directory(saveDir).listSync().forEach((element) {
      if(element.path.endsWith(".swvehicle")){
        var temp = Vehicle.load(element, this);
        vehicles.add(temp);
        if(temp.category != "" && !vehCats.contains(temp.category))
          vehCats.add(temp.category);
      }
    });
  }

  void syncCloud(){
    //TODO: cload loading AND saving
  }

  Future<void> testing() async{
    var testFiles = ["Big Game Hunter [Nemesis].swcharacter","Incom T-47 Airspeeder.swvehicle","Pirate Crew.swminion"];
    for(String st in testFiles){
      String json = await rootBundle.loadString("assets/testing/"+st);
      File testFile = File(saveDir+"/"+st);
      if(testFile.existsSync())
        testFile.deleteSync();
      testFile.writeAsStringSync(json);
    }
  }
}