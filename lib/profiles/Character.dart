import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/Duty.dart';
import 'package:swassistant/items/ForcePower.dart';
import 'package:swassistant/items/Obligation.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/characters/Specializations.dart';
import 'package:swassistant/ui/items/common/CriticalInjuries.dart';
import 'package:swassistant/ui/items/common/Description.dart';
import 'package:swassistant/ui/items/common/Weapons.dart';
import 'package:swassistant/ui/items/creatures/Characteristics.dart';
import 'package:swassistant/ui/items/characters/CharacterInfo.dart';
import 'package:swassistant/ui/items/characters/WoundStrain.dart';
import 'package:swassistant/ui/items/creatures/Defense.dart';
import 'package:swassistant/ui/items/creatures/Skills.dart';

import 'utils/Creature.dart';

class Character extends Editable with Creature{

  String species = "";
  String career = "";
  List<String> specializations = new List();
  List<ForcePower> forcePowers = new List();
  String motivation = "";
  String emotionalStr = "";
  String emotionalWeak = "";
  List<Duty> duties = new List();
  List<Obligation> obligations = new List();
  int strainThresh = 0, strainCur = 0;
  int xpTot = 0, xpCur = 0;
  int force = 0;
  int credits = 0;
  int morality = 0, conflict = 0;
  bool darkSide = false;
  int age = 0;
  int encumCap = 0;

  String get fileExtension => ".swcharacter";
  List<String> get cardNames => [
    "Basic Information",
    "Wound and Strain",
    "Characteristics",
    "Skill",
    "Defense",
    "Weapons",
    "Critical Injuries",
    "Specializations",
    "Talents",
    "Force Powers",
    "XP",
    "Inventory",
    "Morality",
    "Duty",
    "Obligation",
    "Description"
  ];

  Character({@required int id, String name = "New Character", bool saveOnCreation = false, SW app}) :
      super(id: id, name: name, saveOnCreation: saveOnCreation, app: app);

  Character.load(FileSystemEntity file, SW app) : super.load(file, app);

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
    out.add(EditableContent(builder: (b, refresh) =>
      CharacterInfo(editing: b)
    ));
    out.add(EditableContent(builder: (b, refresh) =>
      WoundStrain(editing: b)
    ));
    out.add(EditableContent(builder: (b, refresh) =>
      Characteristics(editing: b)
    ));
    out.add(EditableContent(builder: (b, refresh) =>
      Skills(editing:b, refresh: refresh)
    , defaultEditingState: () => skills.length == 0));
    out.add(EditableContent(builder: (b, refresh) =>
      Defense(editing: b)
    ));
    out.add(EditableContent(builder: (b, refresh) =>
      Weapons(editing: b, refresh: refresh)
    , defaultEditingState: () => weapons.length == 0));
    out.add(EditableContent(builder: (b, refresh) =>
      CriticalInjuries(editing: b, refresh: refresh)
    , defaultEditingState: () => criticalInjuries.length == 0));
    out.add(EditableContent(builder: (b, refresh) =>
      Specializations(editing: b, refresh: refresh,)
    , defaultEditingState: () => specializations.length == 0,));
    for(int i = out.length;i<cardNames.length-1;i++){
      out.add(EditableContent(builder: (b, refresh) =>
        Text("card " + i.toString())
      ));
    }
    out.add(EditableContent(builder: (b, refresh) =>
      Description(editing: b)
    , defaultEditingState: () => desc == "",));
    return out;
  }

  static Character of(BuildContext context) => Editable.of(context);
}