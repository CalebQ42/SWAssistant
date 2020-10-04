import 'package:flutter/material.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/items/Talent.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

mixin Creature on Editable{
  static final List<String> characteristics = ["Brawn", "Agility", "Intellect", "Cunning", "Willpower", "Presence"];

  List<int> charVals = new List();
  List<Skill> skills = new List();
  List<Talent> talents = new List();
  List<Item> inventory = new List();
  int woundThresh;
  int woundCur;
  int defMelee,defRanged;
  int soak;

  void creatureLoadJson(Map<String,dynamic> json){
    this.charVals = new List();
    for(dynamic dy in json["characteristics"])
      this.charVals.add(dy);
    this.skills = new List();
    for(dynamic dy in json["Skills"])
      this.skills.add(Skill.fromJson(dy));
    this.talents = new List();
    for(dynamic dy in json["Talents"])
      this.talents.add(Talent.fromJson(dy));
    this.inventory = new List();
    for(dynamic dy in json["Inventory"])
      this.inventory.add(Item.fromJson(dy));
    woundThresh = json["wound threshold"];
    woundCur = json["wound current"];
    defMelee = json["melee defense"];
    defRanged = json["ranged defense"];
    soak = json["soak"];
  }

  Map<String,dynamic> creatureSaveJson(){
    var json = new Map<String,dynamic>();
    json["characteristics"] = charVals;
    json["Skills"] = List.generate(skills.length, (index) => skills[index].toJson());
    json["Talents"] = List.generate(talents.length, (index) => talents[index].toJson());
    json["Inventory"] = List.generate(inventory.length, (index) => inventory[index].toJson());
    json["wound threshold"] = woundThresh;
    json["wound current"] = woundCur;
    json["melee defense"] = defMelee;
    json["ranged defense"] = defRanged;
    json["soak"] = soak;
    return json;
  }

  static Creature of(BuildContext context) => Editable.of(context) as Creature;
}