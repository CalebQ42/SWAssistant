import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
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
    WidgetsFlutterBinding.ensureInitialized();
    prefs = await SharedPreferences.getInstance();
    if(prefs.containsKey(saveLocation)){
      saveDir = prefs.getString(saveLocation);
    }else{
      var dir = await getExternalStorageDirectory();
      saveDir = dir.path+"/SWChars";
    }
    if(!Directory(saveDir).existsSync())
      Directory(saveDir).createSync();
    if(kDebugMode)
      testing();
    loadAll();
  }

  Future<void> loadPrefs() async => prefs = await SharedPreferences.getInstance();

  void loadAll() async{
    minions = List<Minion>();
    minCats = List<String>();
    characters = List<Character>();
    charCats = List<String>();
    vehicles = List<Vehicle>();
    vehCats = List<String>();

    await Directory(saveDir).list().forEach((element) {
      if(element.path.endsWith(".swcharacter")){
        var temp = Character.load(element);
        if(temp.category != "" && !charCats.contains(temp.category))
          charCats.add(temp.category);
      }else if(element.path.endsWith(".swminion")){
        var temp = Minion.load(element);
        if(temp.category != "" && !minCats.contains(temp.category))
          minCats.add(temp.category);
      }else if(element.path.endsWith(".swvehicle")){
        var temp = Vehicle.load(element);
        if(temp.category != "" && !vehCats.contains(temp.category))
          vehCats.add(temp.category);
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