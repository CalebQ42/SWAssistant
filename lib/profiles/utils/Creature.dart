import 'package:flutter/material.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/items/Talent.dart';

mixin Creature{
  List<int> charVals;
  List<Skill> skills;
  List<Talent> talents;
  List<Item> inventory;
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

  void creatureSaveJson(Map<String,dynamic> json){
    //TODO: save Json values
  }

  Card woundStrainCard(){
    return new Card();
  }
  Card charCard(){
    return new Card();
  }
  Card skilsCard(){
    return new Card();
  }
  Card defenseCard(){
    return new Card();
  }
  Card talentCard(){
    return new Card();
  }
  Card inventoryCard(){
    return new Card();
  }
}