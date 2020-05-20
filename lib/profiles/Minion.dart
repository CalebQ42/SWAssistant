import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

import 'utils/Creature.dart';

class Minion extends Editable with Creature{

  int woundThreshInd;
  int minionNum;
  List<Item> savedInv;
  List<Weapon> savedWeapons;

  int get cardNum => 9;
  String get fileExtension => ".swminion";

  Minion({@required int id, String name = "New Minion"}) : super(id: id, name: name);

  Minion.load(FileSystemEntity file) : super.load(file);

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
    return List.filled(cardNum,
      EditableContent(builder: (bool b, Editable editable){
          return Text("Yo. It's a card");
        }, editable: this
      )
    );
  }
}