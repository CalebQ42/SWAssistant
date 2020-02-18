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
  int xpTot, xCur;
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
    //TODO: load the above values
  }

  Character.load(String filename) : super.load(filename);

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}