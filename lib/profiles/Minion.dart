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
    //TODO: load the above values
  }

  Minion.load(String filename) : super.load(filename);

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}