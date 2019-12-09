import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

import 'utils/Creature.dart';

class Character extends Editable with Creature{
  int get cardNum => 16;
  String get fileExtension => ".swcharacter";

  Character({@required int id, String name}) : super(id: id, name: name);

  Character.fromJson(Map<String, dynamic> json) : super.fromJson(json);

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}