
import 'package:flutter/material.dart';
import 'package:swassistant/utils/JsonSavable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import 'WeaponCharacteristic.dart';

class Weapon implements JsonSavable{

  String name;
  int damage;
  int critical;
  int hp;
  //0-Engaged, 1-Short, 2-Medium, 3-Long, 4-Extreme
  int range;
  //0-Brawl, 1-Gunner, 2-Lightsaber, 3-Melee, 4-Ranged(Light), 5-Ranged(Heavy)
  int skill;
  int skillBase;
  List<WeaponCharacteristic> characteristics;
  bool addBrawn;
  bool loaded;
  bool limitedAmmo;
  //0-None, 1-Minor, 2-Major, 3-Maor
  int itemState;
  int ammo;
  String firingArc;
  int encumbrance;

  Weapon({this.name = "", this.damage = -1, this.critical = -1, this.hp = 0, this.range = 0,
    this.skill = -1, this.skillBase = -1, this.characteristics = const [], this.addBrawn = false,
    this.loaded = true, this.limitedAmmo = false, this.itemState = 0, this.ammo = 0,
    this.firingArc = "", this.encumbrance = 0});

  Weapon.fromJson(Map<String,dynamic> json) :
      name = json["name"] ?? "",
      damage = json["damage"] ?? -1,
      critical = json["critical rating"] ?? -1,
      hp = json["hard points"] ?? 0,
      range = json["range"] ?? 0,
      skill = json["skill"] ?? -1,
      skillBase = json["base"] ?? -1,
      addBrawn = json["add brawn"] ?? false,
      loaded = json["loaded"] ?? true,
      limitedAmmo = json["limited ammo"] ?? false,
      itemState = json["item state"] ?? 0,
      ammo = json["ammo"] ?? 0,
      firingArc = json["firing arc"] ?? "",
      encumbrance = json["encumbrance"] ?? 0,
      characteristics = []{
        if(json["Weapon Characteristics"] != null)
          for(Map<String,dynamic> map in json["Weapon Characteristics"]){
            characteristics.add(WeaponCharacteristic.fromJson(map));
          }
      }

  Weapon.from(Weapon from) : 
      name = from.name,
      damage = from.damage,
      critical = from.critical, 
      hp = from.hp,
      range = from.range,
      skill = from.skill,
      skillBase = from.skillBase,
      addBrawn = from.addBrawn,
      loaded = from.loaded,
      limitedAmmo = from.limitedAmmo,
      itemState = from.itemState,
      ammo = from.ammo,
      firingArc = from.firingArc,
      encumbrance = from.encumbrance,
      characteristics = List.from(from.characteristics);

  Map<String,dynamic> toJson() => {
    "name" : name,
    "damage" : damage,
    "critical rating" : critical,
    "hard points" : hp,
    "range" : range,
    "skill" : skill,
    "base" : skillBase,
    "Weapon Characteristics" : List.generate(characteristics.length, (index) => characteristics[index].toJson()),
    "add brawn" : addBrawn,
    "loaded" : loaded,
    "limited ammo" : limitedAmmo,
    "item state" : itemState,
    "ammo" : ammo,
    "firing arc" : firingArc,
    "encumbrance" : encumbrance
  };

  static List<String> weaponSkills(BuildContext context) => [
    AppLocalizations.of(context)!.skills3,
    AppLocalizations.of(context)!.skills11,
    AppLocalizations.of(context)!.skills13,
    AppLocalizations.of(context)!.skills16,
    AppLocalizations.of(context)!.skills23,
    AppLocalizations.of(context)!.skills22,
  ];
}