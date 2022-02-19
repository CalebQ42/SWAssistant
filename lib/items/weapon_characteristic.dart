import 'package:flutter/cupertino.dart';
import 'package:swassistant/utils/json_savable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

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
  int get hashCode => hashValues(name, value, advantage);

  static List<String> passive(BuildContext context) => [
    AppLocalizations.of(context)!.characteristicAccurate,
    AppLocalizations.of(context)!.characteristicBreach,
    AppLocalizations.of(context)!.characteristicCumbersome,
    AppLocalizations.of(context)!.characteristicInaccurate,
    AppLocalizations.of(context)!.characteristicInferior,
    AppLocalizations.of(context)!.characteristicPierce,
    AppLocalizations.of(context)!.characteristicVicious,
  ];
}