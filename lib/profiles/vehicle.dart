import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:swassistant/ui/items/editable/CriticalInjuries.dart';
import 'package:swassistant/ui/items/editable/Description.dart';
import 'package:swassistant/ui/items/editable/Inventory.dart';
import 'package:swassistant/ui/items/editable/weapons.dart';
import 'package:swassistant/ui/items/vehicle/VehicleDamage.dart';
import 'package:swassistant/ui/items/vehicle/VehicleDefense.dart';
import 'package:swassistant/ui/items/vehicle/VehicleInfo.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Vehicle extends Editable{

  int silhouette = 0;
  int speed = 0;
  int handling = 0;
  int armor = 0;
  //0-Fore,1-Port,2-Starboard,3-Aft;
  List<int> defense = List.filled(4, 0, growable: false);
  int totalDefense = 0;
  int hullTraumaThresh = 0;
  int hullTraumaCur = 0;
  int sysStressThresh = 0;
  int sysStressCur = 0;
  int encumCap = 0;
  int passengerCapacity = 0;
  int hp = 0;
  String model = "";

  @override
  String get fileExtension => ".swvehicle";
  @override
  int get cardNum => 7;
  @override
  List<String> cardNames(BuildContext context) => [
    AppLocalizations.of(context)!.basicInfo,
    AppLocalizations.of(context)!.defense,
    AppLocalizations.of(context)!.damage,
    AppLocalizations.of(context)!.weaponPlural,
    AppLocalizations.of(context)!.inventory,
    AppLocalizations.of(context)!.criticalHits,
    AppLocalizations.of(context)!.desc
  ];

  Vehicle({String name = "New Vehicle", bool saveOnCreation = false, required SW app}) :
      super(name: name, saveOnCreation: saveOnCreation, app: app);

  Vehicle.load(FileSystemEntity file, SW app) : super.load(file, app: app);

  Vehicle.from(Vehicle vehicle) :
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
      super.from(vehicle);

  @override
  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    silhouette = json["silhouette"] ?? 0;
    speed = json["speed"] ?? 0;
    handling = json["handling"] ?? 0;
    armor = json["armor"] ?? 0;
    if(json["defense"] != null){
      defense = [];
      for(dynamic i in json["defense"]){
        defense.add(i);
      }
    }
    totalDefense = json["total defense"] ?? 0;
    hullTraumaThresh  = json["hull trauma threshold"] ?? 0;
    hullTraumaCur = json["hull trauma current"] ?? 0;
    sysStressThresh = json["system stress threshold"] ?? 0;
    sysStressCur = json["system stress current"] ?? 0;
    encumCap = json["encumbrance capacity"] ?? 0;
    passengerCapacity = json["passenger capacity"] ?? 0;
    hp = json["hard points"] ?? 0;
    model = json["model"] ?? "";
  }

  @override
  Map<String,dynamic> toJson() => 
    super.toJson()..addAll({
      "silhouette": silhouette,
      "speed": speed,
      "handling": handling,
      "armor": armor,
      "defense": defense,
      "total defense": totalDefense,
      "hull trauma threshold": hullTraumaThresh,
      "hull trauma current": hullTraumaCur,
      "system stress threshold": sysStressThresh,
      "system stress current": sysStressCur,
      "encumbrance capacity": encumCap,
      "passenger capacity": passengerCapacity,
      "hard points": hp,
      "model": model,
    });

  @override
  List<EditableContent> cardContents(BuildContext context, Function() listUpdate) =>
    <EditableContent>[
      EditableContent(
        builder: (b, refresh, state) =>
          VehicleInfo(editing: b, state: state, updateList: listUpdate)
      ),
      EditableContent(
        stateful: VehicleDefense()
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          VehicleDamage(editing: b, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Weapons(editing: b, refresh: refresh),
        defaultEditingState: () => weapons.isEmpty,
      ),
      EditableContent(
        stateful: Inventory(holder: EditableContentStatefulHolder()),
        defaultEditingState: () => inventory.isEmpty,
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          CriticalInjuries(editing: b, refresh: refresh),
        defaultEditingState: () => criticalInjuries.isEmpty,
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Description(editing: b, state: state),
        defaultEditingState: () => desc == "",
      ),
    ];

  static Vehicle? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Vehicle) return ed;
    return null;
  }
}