import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:meta/meta.dart';
import 'package:swassistant/items/CriticalInjury.dart';
import 'package:swassistant/items/Note.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/ui/Card.dart';

import '../Character.dart';
import '../Minion.dart';
import '../Vehicle.dart';
import 'JsonSavable.dart';

//Editable holds all common components of Vehicles, Minions, and Characters and
//provides a framework on how to display, load, and save profiles
abstract class Editable extends JsonSavable{

  //Common components

  int id = 0;
  String name;
  List<Note> nts;
  List<Weapon> weapons;
  String category;
  List<CriticalInjury> criticalInjuries;
  String desc;

  List<bool> showCard;

  String get fileExtension;
  int get cardNum;

  //Saving variables

  bool _editing = false;
  bool _saving = false;
  String _loc;
  bool _external;

  Editable({@required this.id, this.name = "", this.nts, this.weapons, this.category = "", this.criticalInjuries, this.desc = ""}){
    nts ??= new List();
    weapons ??= new List();
    criticalInjuries ??= new List();
    showCard = List.filled(cardNum, false);
  }

  Editable.fromJson(Map<String,dynamic> json){
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
    this.loadJson(json);
  }

  Editable.load(FileSystemEntity file){
    var jsonMap = jsonDecode(File.fromUri(file.uri).readAsStringSync());
    loadJson(jsonMap);
  }

  @mustCallSuper
  void loadJson(Map<String,dynamic> json){
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
    id = json["id"];
    name = json["name"];
    nts = new List<Note>();
    for (Map<String, dynamic> arrMap in json["Notes"]) 
      nts.add(Note.fromJson(arrMap));
    weapons = new List<Weapon>();
    for(Map<String,dynamic> arrMap in json["Weapons"])
      weapons.add(Weapon.fromJson(arrMap));
    category = json["category"];
    criticalInjuries = new List<CriticalInjury>();
    for(Map<String,dynamic> arrMap in json["Critical Injuries"])
      criticalInjuries.add(CriticalInjury.fromJson(arrMap));
    desc = json["description"];
    showCard = new List();
    for(dynamic b in json["show cards"])
      showCard.add(b);
  }

  @mustCallSuper
  Map<String, dynamic> toJson(){
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
    var json = new Map<String,dynamic>();
    json["Notes"] = List.generate(nts.length, (index) => nts[index].toJson());
    json["Weapons"] = List.generate(weapons.length, (index) => weapons[index].toJson());
    json["Critical Injuries"] = List.generate(criticalInjuries.length, (index) => criticalInjuries[index].toJson());
    json["name"] = name;
    json["category"] = category;
    json["description"] = desc;
    json["card hidden"] = showCard;
    return json;
  }

  List<Widget> cards(){
    var cards = List<Widget>();
    var contents = cardContents();
    for (int i = 0; i < cardNum; i++){
      cards.add(
        InfoCard(shown: showCard[i],contents: contents[i], title: "Woolooloo")
      );
    }
    return cards;
  }

  List<Widget> cardContents();

  void exportTo(String folder){}
  String getFileLocation(){
    if(_loc = null)
      return null;
    else
      return _loc;
  }
  String getCloudFileLocation(){return null;}
  void save(String filename){}
  void cloudSave(){}
  void load(String filename){}
  void cloudLoad(){}
  void startEditing(){}
  void delete(){}
  void stopEditing(){
    _editing = false;
  }

  void addShortcut(){}
  bool hasShortcut(){ return false; }
  void updateShortcut(){}
  void deleteShortcut(){}
}