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

class SW extends InheritedWidget{
  final List<Minion> minions = new List();
  final List<String> minCats = new List();

  final List<Character> characters = new List();
  final List<String> charCats = new List();

  final List<Vehicle> vehicles = new List();
  final List<String> vehCats = new List();

  final SharedPreferences prefs;

  final String saveDir;

  SW({Widget child, this.prefs, this.saveDir}): super(child: child);

  void loadAll(){
    minions.clear();
    minCats.clear();
    characters.clear();
    charCats.clear();
    vehicles.clear();
    vehCats.clear();
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
    minions.clear();
    minCats.clear();

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
    characters.clear();
    charCats.clear();

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
    vehicles.clear();
    vehCats.clear();

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

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) => false;

  static Future<SW> initialize(Widget child) async{
    WidgetsFlutterBinding.ensureInitialized();
    var prefs = await SharedPreferences.getInstance();
    String saveDir;
    if(prefs.containsKey(preferences.saveLocation)){
      saveDir = prefs.getString(preferences.saveLocation);
    }else{
      var dir = await getExternalStorageDirectory();
      saveDir = dir.path+"/SWChars";
    }
    if(!Directory(saveDir).existsSync())
      Directory(saveDir).createSync();
    if(kDebugMode || kProfileMode)
      await testing(saveDir);
    SW out = SW(child: child, prefs: prefs, saveDir: saveDir);
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

  static SW of(BuildContext context) => context.dependOnInheritedWidgetOfExactType(aspect: SW);
}