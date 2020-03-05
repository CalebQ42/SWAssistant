import 'package:flutter/material.dart';
import 'package:swassistant/items/Duty.dart';
import 'package:swassistant/items/ForcePower.dart';
import 'package:swassistant/items/Obligation.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

import 'utils/Creature.dart';

class Character extends Editable with Creature{

  String species;
  String career;
  List<String> specializations;
  List<ForcePower> forcePowers;
  String motivation;
  List<String> emotionalStr, emotionalWeak;
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

  Character.fromJson(Map<String, dynamic> json) : super.fromJson(json){
    this.creatureLoadJson(json);
    this.species = json["species"];
    this.career = json["career"];
    this.specializations = List();
    for(dynamic dy in json["Specializations"])
      this.specializations.add(dy);
    this.forcePowers = List();
    for(dynamic dy in json["Force Powers"])
      this.forcePowers.add(ForcePower.fromJson(dy));
    this.motivation = json["motivation"];
    this.emotionalStr = List();
    for(dynamic dy in json["emotional strength"])
      this.emotionalStr.add(dy);
    this.emotionalWeak = List();
    for(dynamic dy in json["emotional weakness"])
      this.emotionalWeak.add(dy);
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

  Character.load(String filename) : super.load(filename);

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}