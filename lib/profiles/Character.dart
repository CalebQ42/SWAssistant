import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

import 'utils/Creature.dart';

class Character extends Editable with Creature{
  int get cardNum => 17;
  String get fileExtension => ".swcharacter";

  Character({@required int id, String name}) : super(id: id, name: name);

  Character.fromJson(Map<String, dynamic> json) : super.fromJson(json);

  List<Widget> cardContents() {
    var cards = List<Widget>();
    for (int i = 0; i < cardNum; i++){
      new Center(
        child: Text("Card " + (i+1).toString()),
      );
    }
    return cards;
  }
}