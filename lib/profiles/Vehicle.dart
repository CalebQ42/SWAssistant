import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/editable/CriticalInjuries.dart';
import 'package:swassistant/ui/items/editable/Description.dart';
import 'package:swassistant/ui/items/editable/Inventory.dart';
import 'package:swassistant/ui/items/editable/Weapons.dart';
import 'package:swassistant/ui/items/vehicle/VehicleDamage.dart';
import 'package:swassistant/ui/items/vehicle/VehicleInfo.dart';

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
  int encumCap = 0;
  int passengerCapacity = 0;
  int hp = 0;
  String model = "";

  String get fileExtension => ".swvehicle";
  List<String> get cardNames => [
    "Basic Information",
    "Defense",
    "Damage",
    "Weapons",
    "Inventory",
    "Critical Injuries",
    "Description"
  ];

  Vehicle({required int id, String name = "New Vehicle", bool saveOnCreation = false, required SW app}) :
      super(id: id, name: name, saveOnCreation: saveOnCreation, app: app);

  Vehicle.load(FileSystemEntity file, SW app) : super.load(file, app: app);

  Vehicle.from(Vehicle vehicle, {required int id}) :
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
      encumCap = vehicle.encumCap,
      passengerCapacity = vehicle.passengerCapacity,
      hp = vehicle.hp,
      model = vehicle.model,
      super.from(vehicle, id: id);

  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    this.silhouette = json["silhouette"] ?? 0;
    this.speed = json["speed"] ?? 0;
    this.handling = json["handling"] ?? 0;
    this.armor = json["armor"] ?? 0;
    if(json["defense"] != null){
      defense = [];
      for(dynamic i in json["defense"])
        defense.add(i);
    }
    this.totalDefense = json["total defense"] ?? 0;
    this.hullTraumaThresh  = json["hull trauma threshold"] ?? 0;
    this.hullTraumaCur = json["hull trauma current"] ?? 0;
    this.sysStressThresh = json["system stress threshold"] ?? 0;
    this.sysStressCur = json["system stress current"] ?? 0;
    this.encumCap = json["encumbrance capacity"] ?? 0;
    this.passengerCapacity = json["passenger capacity"] ?? 0;
    this.hp = json["hard points"] ?? 0;
    this.model = json["model"] ?? "";
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
    map["encumbrance capacity"] = encumCap;
    map["passenger capacity"] = passengerCapacity;
    map["hard points"] = hp;
    map["model"] = model;
    return map;
  }

  List<EditableContent> cardContents(BuildContext context, Function() updateList) =>
    <EditableContent>[
      EditableContent(
        builder: (b, refresh, state) =>
          VehicleInfo(editing: b, state: state, updateList: updateList)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Text("Defense")
        //TODO: VehicleDefese(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          VehicleDamage(editing: b, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Weapons(editing: b, refresh: refresh),
        defaultEditingState: () => weapons.length == 0,
      ),
      EditableContent(
        stateful: Inventory(holder: EditableContentStatefulHolder()),
        defaultEditingState: () => inventory.length == 0,
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          CriticalInjuries(editing: b, refresh: refresh),
        defaultEditingState: () => criticalInjuries.length == 0,
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Description(editing: b, state: state),
        defaultEditingState: () => desc == "",
      ),
    ];

  static Vehicle? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Vehicle)
      return ed;
    return null;
  }
}