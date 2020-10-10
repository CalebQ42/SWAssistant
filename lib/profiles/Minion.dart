import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/characters/Talents.dart';
import 'package:swassistant/ui/items/editable/CriticalInjuries.dart';
import 'package:swassistant/ui/items/editable/Description.dart';
import 'package:swassistant/ui/items/editable/Weapons.dart';
import 'package:swassistant/ui/items/creatures/Characteristics.dart';
import 'package:swassistant/ui/items/creatures/Defense.dart';
import 'package:swassistant/ui/items/creatures/Inventory.dart';
import 'package:swassistant/ui/items/creatures/Skills.dart';

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
    "Critical Injuries",
    "Description"
  ];

  Minion({@required int id, String name = "New Minion", bool saveOnCreation = false, SW app}) :
      super(id: id, name: name, saveOnCreation: saveOnCreation, app: app);

  Minion.load(FileSystemEntity file, SW app) : super.load(file, app: app);

  Minion.from(Minion minion, {int id}) :
      woundThreshInd = minion.woundThreshInd,
      minionNum = minion.minionNum,
      savedInv = List.from(minion.savedInv),
      savedWeapons = List.from(minion.savedWeapons),
      super.from(minion, id: id){
    creatureFrom(minion);
  }

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

  List<Widget> cardContents() =>
    <Widget> [
      EditableContent(builder: (b, refresh, state) =>
        Text("Minion Numbers")
        //TODO: MinionNumber(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(builder: (b, refresh, state) =>
        Text("Wound")
        //TODO: MinionWound(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(builder: (b, refresh, state) =>
        Characteristics(editing: b, state: state)
      ),
      EditableContent(builder: (b, refresh, state) =>
        Skills(editing: b, refresh: refresh)
      ),
      EditableContent(builder: (b, refresh, state) =>
        Defense(editing: b, state: state)
      ),
      EditableContent(builder: (b, refresh, state) =>
        Weapons(editing: b, refresh: refresh)
      ),
      EditableContent(builder: (b, refresh, state) =>
        Talents(editing: b, refresh: refresh)
      ),
      EditableContent(builder: (b, refresh, state) =>
        Inventory(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(builder: (b, refresh, state) =>
        CriticalInjuries(editing: b, refresh: refresh)
      ),
      EditableContent(builder: (b, refresh, state) =>
        Description(editing: b, state: state)
      ),
    ];

  static Minion of(BuildContext context) => Editable.of(context);
}