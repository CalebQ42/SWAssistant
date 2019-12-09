import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class Vehicle extends Editable{
  int get cardNum => 6;
  String get fileExtension => ".swvehicle";

  Vehicle({@required int id, String name}) : super(id: id, name: name);

  Vehicle.fromJson(Map<String, dynamic> json) : super.fromJson(json);

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}