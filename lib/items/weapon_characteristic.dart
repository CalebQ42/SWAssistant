import 'package:swassistant/utils/json_savable.dart';

class WeaponCharacteristic implements JsonSavable{

  String name;
  int value;
  int advantage;

  WeaponCharacteristic({this.name = "", this.value = -1, this.advantage = -1});

  WeaponCharacteristic.from(WeaponCharacteristic wc) :
      name = wc.name,
      value = wc.value,
      advantage = wc.advantage;

  WeaponCharacteristic.fromJson(Map<String,dynamic> json) :
      name = json["name"] ?? "",
      value = json["value"] ?? -1,
      advantage = json["advantage"] ?? -1;

  @override
  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "advantage" : advantage
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "name": "",
    "value": -1,
    "advantage": -1,
  };
}