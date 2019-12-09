import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

import 'utils/Creature.dart';

class Minion extends Editable with Creature{
  int get cardNum => 9;
  String get fileExtension => ".swminion";

  Minion({@required int id, String name}) : super(id: id, name: name);

  Minion.fromJson(Map<String, dynamic> json) : super.fromJson(json);

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}