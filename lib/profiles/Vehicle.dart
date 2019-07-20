import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class Vehicle extends Editable{
  int get cardNum => 7;
  String get fileExtension => ".swvehicle";

  Vehicle({@required int id, String name}) : super(id: id, name: name);

  Vehicle.fromJson(Map<String, dynamic> json) : super.fromJson(json);

  List<Widget> cardContents() {
    var cards = List<Widget>();
    for (int i = 0; i < cardNum; i++){
      Center(
        child: Text("Card " + cardNum.toString())
      );
    }
    return cards;
  }
}