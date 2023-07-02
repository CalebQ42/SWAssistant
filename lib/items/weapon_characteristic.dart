import 'package:flutter/cupertino.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/utils/json_savable.dart';

class WeaponCharacteristic implements JsonSavable{

  String name;
  int? value;
  int? advantage;

  WeaponCharacteristic({this.name = "", this.value, this.advantage});

  WeaponCharacteristic.from(WeaponCharacteristic wc) :
      name = wc.name,
      value = wc.value,
      advantage = wc.advantage;

  WeaponCharacteristic.fromJson(Map<String,dynamic> json) :
      name = json["name"] ?? "",
      value = json["value"],
      advantage = json["advantage"];

  @override
  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "advantage" : advantage
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "name": "",
  };

  @override
  operator ==(other) => other is WeaponCharacteristic && other.name == name && other.value == value && other.advantage == advantage;

  @override
  int get hashCode => Object.hash(name, value, advantage);

  static List<String> passive(BuildContext context) => [
    SW.of(context).locale.characteristicAccurate,
    SW.of(context).locale.characteristicBreach,
    SW.of(context).locale.characteristicCumbersome,
    SW.of(context).locale.characteristicInaccurate,
    SW.of(context).locale.characteristicInferior,
    SW.of(context).locale.characteristicPierce,
    SW.of(context).locale.characteristicVicious,
  ];
}