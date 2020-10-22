import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/Duty.dart';
import 'package:swassistant/items/ForcePower.dart';
import 'package:swassistant/items/Obligation.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/characters/Duties.dart';
import 'package:swassistant/ui/items/characters/Morality.dart';
import 'package:swassistant/ui/items/characters/Obligations.dart';
import 'package:swassistant/ui/items/characters/XP.dart';
import 'package:swassistant/ui/items/characters/ForcePowers.dart';
import 'package:swassistant/ui/items/characters/Specializations.dart';
import 'package:swassistant/ui/items/characters/Talents.dart';
import 'package:swassistant/ui/items/editable/CriticalInjuries.dart';
import 'package:swassistant/ui/items/editable/Description.dart';
import 'package:swassistant/ui/items/editable/Weapons.dart';
import 'package:swassistant/ui/items/creatures/Characteristics.dart';
import 'package:swassistant/ui/items/characters/CharacterInfo.dart';
import 'package:swassistant/ui/items/characters/WoundStrain.dart';
import 'package:swassistant/ui/items/creatures/Defense.dart';
import 'package:swassistant/ui/items/creatures/Inventory.dart';
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

  bool disableForce = false;
  bool disableDuty = false;
  bool disableObligation = false;
  bool disableMorality = false;

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
    if(!disableForce) "Force Powers",
    "XP",
    "Inventory",
    if(!disableMorality) "Morality",
    if(!disableDuty) "Duty",
    if(!disableObligation) "Obligation",
    "Description"
  ];

  Character({@required int id, String name = "New Character", bool saveOnCreation = false, SW app}) :
      super(id: id, name: name, saveOnCreation: saveOnCreation, app: app);

  Character.load(FileSystemEntity file, SW app) : super.load(file, app: app);

  Character.from(Character character, {int id}) :
      species = character.species,
      career = character.career,
      specializations = List.from(character.specializations),
      forcePowers = List.from(character.forcePowers),
      motivation = character.motivation,
      emotionalStr = character.emotionalStr,
      emotionalWeak = character.emotionalWeak,
      duties = List.from(character.duties),
      obligations = List.from(character.obligations),
      strainThresh = character.strainThresh,
      strainCur = character.strainCur,
      xpTot = character.xpTot,
      xpCur = character.xpCur,
      force = character.force,
      credits = character.credits,
      morality = character.morality,
      conflict = character.conflict,
      darkSide = character.darkSide,
      age = character.age,
      encumCap = character.encumCap,
      disableDuty = character.disableDuty,
      disableForce = character.disableForce,
      disableObligation = character.disableObligation,
      disableMorality = character.disableMorality,
      super.from(character, id: id){
    creatureFrom(character);
  }

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
    this.disableDuty = json["disable duty"] ?? false;
    this.disableForce = json["disable force"] ?? false;
    this.disableObligation = json["disable obligation"] ?? false;
    this.disableMorality = json["disable morality"] ?? false;
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
    map["disable force"] = disableForce;
    map["disable duty"] = disableDuty;
    map["disable obligation"] = disableObligation;
    map["disable morality"] = disableMorality;
    return map;
  }

  List<Widget> cardContents(BuildContext context, Function updateList) => 
    <Widget>[
      EditableContent(
        builder: (b, refresh, state) =>
          CharacterInfo(editing: b, state: state, updateList: updateList,)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          WoundStrain(editing: b, state: state),
        defaultEditingState: () => soak == 0 && woundThresh == 0 && strainThresh == 0
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Characteristics(editing: b, state: state),
        defaultEditingState: () => charVals.every((element) => element == 0)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Skills(editing:b, refresh: refresh),
        defaultEditingState: () => skills.length == 0
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Defense(editing: b, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Weapons(editing: b, refresh: refresh),
        defaultEditingState: () => weapons.length == 0
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          CriticalInjuries(editing: b, refresh: refresh),
        defaultEditingState: () => criticalInjuries.length == 0
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Specializations(editing: b, refresh: refresh,),
        defaultEditingState: () => specializations.length == 0
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Talents(editing: b, refresh: refresh,),
        defaultEditingState: () => talents.length == 0
      ),
      if(!disableForce) EditableContent(
        builder: (b, refresh, state) =>
          ForcePowers(editing: b, refresh: refresh, state: state),
        defaultEditingState: () => forcePowers.length == 0 && force == 0
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          XP(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Inventory(editing: b, refresh: refresh, state: state),
        defaultEditingState: () => inventory.length == 0
      ),
      if(!disableMorality) EditableContent(
        stateful: Morality()
      ),
      if(!disableDuty) EditableContent(
        builder: (b, refresh, state) =>
          Duties(editing: b, refresh: refresh),
        defaultEditingState: () => duties.length == 0
      ),
      if(!disableObligation) EditableContent(
        builder: (b, refresh, state) =>
          Obligations(editing: b, refresh: refresh),
        defaultEditingState: () => obligations.length == 0
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Description(editing: b, state: state),
        defaultEditingState: () => desc == ""
      )
    ];

  static Character of(BuildContext context) => Editable.of(context);
}