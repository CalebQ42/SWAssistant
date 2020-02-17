import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class Vehicle extends Editable{

  int silhouette;
  int speed;
  int handling;
  int armor;
  List<int> defense;
  int totalDefense;
  int hullTraumaThresh;
  int hullTraumaCur;
  int sysStressThresh;
  int sysStressCur;
  int encumCapacity;
  int passengerCapacity;
  int hp;
  String model;

  int get cardNum => 6;
  String get fileExtension => ".swvehicle";

  Vehicle({@required int id, String name}) : super(id: id, name: name);

  Vehicle.fromJson(Map<String, dynamic> json) : super.fromJson(json);

  List<Widget> cardContents() {
    return List.filled(cardNum,Text("Card"));
  }
}