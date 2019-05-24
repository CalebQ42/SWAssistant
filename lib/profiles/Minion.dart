import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

import 'utils/Creature.dart';

class Minion extends Editable with Creature{
  int get cardNum => 10;
  String get fileExtension => ".swminion";

  Minion({@required int id, String name}) : super(id: id, name: name);

  Minion.fromJson(Map<String, dynamic> json) : super.fromJson(json);

  List<Widget> cards() {
    var cards = List<Widget>();
    for (int i = 0; i < cardNum; i++){
      cards.add(Card(
          child: Center(
              child: Text("Card " + cardNum.toString())
          )
      ));
    }
    return cards;
  }
}