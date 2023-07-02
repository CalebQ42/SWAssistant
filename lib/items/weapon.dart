
import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/utils/json_savable.dart';

import 'weapon_characteristic.dart';

class Weapon implements JsonSavable{

  String name = "";
  int? damage;
  int? critical;
  int hp = 0;
  //0-Engaged, 1-Short, 2-Medium, 3-Long, 4-Extreme
  int range = 0;
  //0-Brawl, 1-Gunner, 2-Lightsaber, 3-Melee, 4-Ranged(Heavy), 5-Ranged(Light)
  int? skill;
  int? skillBase;
  List<WeaponCharacteristic> characteristics = [];
  bool addBrawn = false;
  bool loaded = true;
  bool limitedAmmo = false;
  //0-None, 1-Minor, 2-Major, 3-Maor
  int itemState = 0;
  int ammo = 0;
  String firingArc = "";
  int encumbrance = 0;

  Weapon();

  Weapon.fromJson(Map<String,dynamic> json) :
    name = json["name"] ?? "",
    damage = json["damage"],
    critical = json["critical rating"],
    hp = json["hard points"] ?? 0,
    range = json["range"] ?? 0,
    skill = json["skill"],
    skillBase = json["base"],
    addBrawn = json["add brawn"] ?? false,
    loaded = json["loaded"] ?? true,
    limitedAmmo = json["limited ammo"] ?? false,
    itemState = json["item state"] ?? 0,
    ammo = json["ammo"] ?? 0,
    firingArc = json["firing arc"] ?? "",
    encumbrance = json["encumbrance"] ?? 0,
    characteristics = []{
      if(json["Weapon Characteristics"] != null){
        for(Map<String,dynamic> map in json["Weapon Characteristics"]){
          characteristics.add(WeaponCharacteristic.fromJson(map));
        }
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

  @override
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
  }..removeWhere((key, value) {
    if (value is List && value.isEmpty) return true;
    return zeroValue[key] == value;
  });

  @override
  Map<String, dynamic> get zeroValue => {
    "name": "",
    "hard points": 0,
    "range": 0,
    "add brawn": false,
    "loaded": true,
    "limited ammo": false,
    "item state": 0,
    "ammo": 0,
    "firing arc": "",
    "encumbrance": 0,
  };

  @override
  operator ==(other) => other is Weapon && other.name == name && other.damage == damage && other.critical == critical &&
      other.hp == hp && other.range == range && other.skill == skill && other.skillBase == skillBase &&
      other.addBrawn == addBrawn && other.loaded == loaded && other.limitedAmmo == limitedAmmo &&other.itemState == itemState &&
      other.ammo == ammo && other.firingArc == firingArc && other.encumbrance == encumbrance && other.characteristics == characteristics;


  @override
  int get hashCode => Object.hash(name, damage, critical, hp, range, skill, skillBase, addBrawn, loaded, limitedAmmo, itemState, ammo, firingArc, encumbrance, Object.hashAll(characteristics));

  static List<String> weaponSkills(BuildContext context) => [
    SW.of(context).locale.skills3,
    SW.of(context).locale.skills11,
    SW.of(context).locale.skills13,
    SW.of(context).locale.skills16,
    SW.of(context).locale.skills21,
    SW.of(context).locale.skills22,
  ];
}