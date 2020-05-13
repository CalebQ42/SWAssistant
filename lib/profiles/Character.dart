import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/items/Duty.dart';
import 'package:swassistant/items/ForcePower.dart';
import 'package:swassistant/items/Obligation.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/CharacterInfo.dart';

import 'utils/Creature.dart';

class Character extends Editable with Creature{

  String species;
  String career;
  List<String> specializations;
  List<ForcePower> forcePowers;
  String motivation;
  String emotionalStr, emotionalWeak;
  List<Duty> duties;
  List<Obligation> obligations;
  int strainThresh, strainCur;
  int xpTot, xpCur;
  int force;
  int credits;
  int morality, conflict;
  bool darkSide;
  int age;
  int encumCap;

  int get cardNum => 16;
  String get fileExtension => ".swcharacter";

  Character({@required int id, String name}) : super(id: id, name: name);

  Character.load(FileSystemEntity file) : super.load(file);

  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    this.creatureLoadJson(json);
    this.species = json["species"];
    this.career = json["career"];
    specializations = new List();
    for(dynamic s in json["Specializations"])
      specializations.add(s);
    this.forcePowers = List();
    for(dynamic dy in json["Force Powers"])
      this.forcePowers.add(ForcePower.fromJson(dy));
    this.motivation = json["motivation"];
    emotionalStr = json["emotional strength"];
    emotionalWeak = json["emotional weakness"];
    this.duties = List();
    for(dynamic dy in json["Dutys"])
      this.duties.add(Duty.fromJson(dy));
    this.obligations  = List();
    for(dynamic dy in json["Obligations"])
      this.obligations.add(Obligation.fromJson(dy));
    this.strainThresh = json["strain threshold"];
    this.strainCur = json["strain current"];
    this.xpTot = json["xp total"];
    this.xpCur = json["xp current"];
    this.force = json["force rating"];
    this.credits = json["credits"];
    this.morality = json["morality"];
    this.conflict = json["conflict"];
    this.darkSide = json["dark side"];
    this.age = json["age"];
    this.encumCap = json["encumbrance capacity"];
  }

  Map<String,dynamic> toJson(){
    var map = super.toJson();
    map.addAll(creatureSaveJson());
    map["species"] = species;
    map["career"] = career;
    map["Specializations"] = specializations;
    var temp = new List<dynamic>();
    forcePowers.forEach((element) {temp.add(element.toJson());});
    map["Force Powers"] = temp;
    map["motivation"] = motivation;
    map["emotional strength"] = emotionalStr;
    map["emotional weakness"] = emotionalWeak;
    temp = new List<dynamic>();
    duties.forEach((element) {temp.add(element.toJson());});
    map["Dutys"] = temp;
    temp = new List<dynamic>();
    obligations.forEach((element) {temp.add(element.toJson);});
    map["Obligations"] = temp;
    map["strain threshold"] = strainThresh;
    map["strain current"] = strainCur;
    map["xp total"] = xpTot;
    map["xp current"] = xpCur;
    map["force rating"] = force;
    map["credits"] = credits;
    map["morality"] = morality;
    map["conflict"] = conflict;
    map["dark side"] = darkSide;
    map["age"] = age;
    map["encumbrance capacity"] = encumCap;
    return map;
  }

  List<Widget> cardContents() {
    var out = new List<Widget>();
    out.add(EditableContent(builder: (bool b, Editable editable){
      return CharacterInfo(editing: b, character: editable);
    }, editable: this));
    for(int i = 2;i<cardNum;i++){
      out.add(EditableContent(builder: (bool b,Editable editable){
          return Text("card " + i.toString());
        },
        editable: this,
      ));
    }
    print(out.length);
    return out;
  }
}