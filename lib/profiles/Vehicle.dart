import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/editable/CriticalInjuries.dart';
import 'package:swassistant/ui/items/editable/Description.dart';
import 'package:swassistant/ui/items/editable/Weapons.dart';

class Vehicle extends Editable{

  int silhouette = 0;
  int speed = 0;
  int handling = 0;
  int armor = 0;
  List<int> defense = new List.filled(4, 0, growable: false);
  int totalDefense = 0;
  int hullTraumaThresh = 0;
  int hullTraumaCur = 0;
  int sysStressThresh = 0;
  int sysStressCur = 0;
  int encumCapacity = 0;
  int passengerCapacity = 0;
  int hp = 0;
  String model = "";

  String get fileExtension => ".swvehicle";
  List<String> get cardNames => [
    "Basic Information",
    "Defense",
    "Damage",
    "Weapons",
    "Critical Injuries",
    "Description"
  ];

  Vehicle({@required int id, String name = "New Vehicle", bool saveOnCreation = false, SW app}) :
      super(id: id, name: name, saveOnCreation: saveOnCreation, app: app);

  Vehicle.load(FileSystemEntity file, SW app) : super.load(file, app: app);

  Vehicle.from(Vehicle vehicle, {int id}) :
      silhouette = vehicle.silhouette,
      speed = vehicle.speed,
      handling = vehicle.handling,
      armor = vehicle.armor,
      defense = List.from(vehicle.defense),
      totalDefense = vehicle.totalDefense,
      hullTraumaThresh = vehicle.hullTraumaThresh,
      hullTraumaCur = vehicle.hullTraumaCur,
      sysStressThresh = vehicle.sysStressThresh,
      sysStressCur = vehicle.sysStressCur,
      encumCapacity = vehicle.encumCapacity,
      passengerCapacity = vehicle.passengerCapacity,
      hp = vehicle.hp,
      model = vehicle.model,
      super.from(vehicle, id: id);

  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    this.silhouette = json["silhouette"];
    this.speed = json["speed"];
    this.handling = json["handling"];
    this.armor = json["armor"];
    defense = new List();
    for(dynamic i in json["defense"])
      defense.add(i);
    this.totalDefense = json["total defense"];
    this.hullTraumaThresh  = json["hull trauma threshold"];
    this.hullTraumaCur = json["hull trauma current"];
    this.sysStressThresh = json["system stress threshold"];
    this.sysStressCur = json["system stress current"];
    this.encumCapacity = json["encumbrance capacity"];
    this.passengerCapacity = json["passenger capacity"];
    this.hp = json["hard points"];
    this.model = json["model"];
  }

  Map<String,dynamic> toJson(){
    var map = super.toJson();
    map["silhouette"] = silhouette;
    map["speed"] = speed;
    map["handling"] = handling;
    map["armor"] = armor;
    map["defense"] = defense;
    map["total defense"] = totalDefense;
    map["hull trauma threshold"] = hullTraumaThresh;
    map["hull trauma current"] = hullTraumaCur;
    map["system stress threshold"] = sysStressThresh;
    map["system stress current"] = sysStressCur;
    map["encumbrance capacity"] = encumCapacity;
    map["passenger capacity"] = passengerCapacity;
    map["hard points"] = hp;
    map["model"] = model;
    return map;
  }

  List<Widget> cardContents(BuildContext context) =>
    <Widget>[
      EditableContent(
        builder: (b, refresh, state) =>
          Text("Info")
        //TODO: VehicleInfo(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Text("Defense")
        //TODO: VehicleDefese(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Text("Damage")
        //TODO: VehicleDamage(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Weapons(editing: b, refresh: refresh)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          CriticalInjuries(editing: b, refresh: refresh)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Description(editing: b, state: state)
      ),
    ];

  static Vehicle of(BuildContext context) => Editable.of(context);
}