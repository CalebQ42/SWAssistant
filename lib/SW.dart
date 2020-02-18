import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:path_provider/path_provider.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'profiles/Minion.dart';
import 'profiles/Character.dart';
import 'profiles/Vehicle.dart';
import 'Preferences.dart';

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
    prefs = await SharedPreferences.getInstance();
    if(prefs.containsKey(saveLocation)){
      saveDir = prefs.getString(saveLocation);
    }else{
      var dir = await getExternalStorageDirectory();
      saveDir = dir.path+"SWChars";
    }
    if(kDebugMode)
      testing();
    loadAll();
  }
  Future<void> loadPrefs() async{
    prefs = await SharedPreferences.getInstance();
  }
  void loadAll() async{
    minions = List();
    minCats = List();
    characters = List();
    charCats = List();
    vehicles = List();
    vehCats = List();

    await Directory(saveDir).list().forEach((element) {
      if(element.path.endsWith(".swcharacter")){
        
      }else if(element.path.endsWith(".swminion")){

      }else if(element.path.endsWith(".swvehicle")){

      }
    });
  }
  void loadMinions(){
    minions = List();
    minCats = List();
  }
  void loadCharacters(){
    characters = List();
    charCats = List();
  }
  void loadVehicles(){
    vehicles = List();
    vehCats = List();
  }

  void testing(){
    //TODO: extract testing files!
  }
}