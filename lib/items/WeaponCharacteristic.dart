import 'package:swassistant/profiles/utils/JsonSavable.dart';

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
      name = json["name"],
      value = json["value"],
      advantage = json["advantage"];

  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "advantage" : advantage
  };
}