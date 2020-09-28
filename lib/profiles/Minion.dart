import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/creatures/Characteristics.dart';

import 'utils/Creature.dart';

class Minion extends Editable with Creature{

  int woundThreshInd = 0;
  int minionNum = 0;
  List<Item> savedInv = new List();
  List<Weapon> savedWeapons = new List();

  String get fileExtension => ".swminion";
  List<String> get cardNames => [
    "Number of Minions",
    "Wound",
    "Characteristics",
    "Skill",
    "Defense",
    "Weapons",
    "Talents",
    "Inventory",
    "Critical Injuries"
  ];

  Minion({@required int id, String name = "New Minion", bool saveOnCreation = false, SW app}) :
      super(id: id, name: name, saveOnCreation: saveOnCreation, app: app);

  Minion.load(FileSystemEntity file, SW app) : super.load(file, app);

  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    this.creatureLoadJson(json);
    this.woundThreshInd = json["wound threshold per minion"];
    this.minionNum = json["minion number"];
    Map<String,dynamic> saved = json["Saved"];
    this.savedInv = new List();
    for(dynamic d in saved["Inventory"])
      this.savedInv.add(Item.fromJson(d));
    this.savedWeapons = new List();
    for(dynamic d in saved["Weapons"])
      this.savedWeapons.add(Weapon.fromJson(d));
  }

  Map<String,dynamic> toJson(){
    var map = super.toJson();
    map.addAll(creatureSaveJson());
    map["wound threshold per minion"] = woundThreshInd;
    map["minion number"] = minionNum;
    var savedInvJson = new List();
    savedInv.forEach((element) {savedInvJson.add(element.toJson());});
    var savedWeapJson = new List();
    savedWeapons.forEach((element) {savedWeapJson.add(element.toJson());});
    map["Saved"] = {
      "Inventory" : savedInvJson,
      "Weapons" : savedWeapJson
    };
    return map;
  }

  List<Widget> cardContents() {
    var out = List.filled(cardNames.length,
      EditableContent(builder: (bool b, refresh, state){
          return Text("Yo. It's a card");
        }
      )
    );
    out[2] = EditableContent(builder: (b, refresh, state) =>
      Characteristics(editing: b)
    );
    return out;
  }

  static Minion of(BuildContext context) => Editable.of(context);
}