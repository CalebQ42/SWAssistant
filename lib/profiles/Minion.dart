import 'package:flutter/material.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

import 'utils/Creature.dart';

class Minion extends Editable with Creature{

  int woundThreshInd;
  int minionNum;
  List<Item> origInv;
  List<Weapon> origWeapons;

  int get cardNum => 9;
  String get fileExtension => ".swminion";

  Minion({@required int id, String name}) : super(id: id, name: name);

  Minion.fromJson(Map<String, dynamic> json) : super.fromJson(json){
    this.creatureLoadJson(json);
    this.woundThreshInd = json["wound threshold individual"];
    this.minionNum = json["minion number"];
    this.origInv = new List();
    for(dynamic d in json["original inventory"])
      this.origInv.add(d);
    for(dynamic d in json["orginal weapons"])
      this.origWeapons.add(d);
  }

  Minion.load(String filename) : super.load(filename);

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}