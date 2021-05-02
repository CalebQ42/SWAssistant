
import 'package:swassistant/profiles/utils/JsonSavable.dart';

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

  Weapon({this.name = "", this.damage = 0, this.critical = 0, this.hp = 0, this.range = 0,
    this.skill = 0, this.skillBase = 0, this.characteristics, this.addBrawn = false,
    this.loaded = true, this.limitedAmmo = false, this.itemState = 0, this.ammo = 0,
    this.firingArc = "", this.encumbrance = 0}){
    if (characteristics == null){
      characteristics = [];
    }
  }

  Weapon.nulled() : 
      name = null,
      damage = null,
      critical = null,
      hp = null,
      range = null,
      skill = null,
      skillBase = null,
      addBrawn = false,
      loaded = true,
      limitedAmmo = false,
      itemState = 0,
      ammo = 0,
      firingArc = null,
      encumbrance = null,
      characteristics = [];

  Weapon.fromJson(Map<String,dynamic> json) :
      name = json["name"],
      damage = json["damage"],
      critical = json["critical rating"],
      hp = json["hard points"],
      range = json["range"],
      skill = json["skill"],
      skillBase = json["base"],
      addBrawn = json["add brawn"],
      loaded = json["loaded"],
      limitedAmmo = json["limited ammo"],
      itemState = json["item state"],
      ammo = json["ammo"],
      firingArc = json["firing arc"],
      encumbrance = json["encumbrance"],
      characteristics = [] {
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

  Map<String,dynamic> toJson(){
    var characteristicsMap = <Map<String,dynamic>>[];
    for (WeaponCharacteristic wc in characteristics){
      characteristicsMap.add(wc.toJson());
    }
    return{
      "name" : name,
      "damage" : damage,
      "critical rating" : critical,
      "hard points" : hp,
      "range" : range,
      "skill" : skill,
      "base" : skillBase,
      "Weapon Characteristics" : characteristicsMap,
      "add brawn" : addBrawn,
      "loaded" : loaded,
      "limited ammo" : limitedAmmo,
      "item state" : itemState,
      "ammo" : ammo,
      if(firingArc != null) "firing arc" : firingArc,
      if(encumbrance != null) "encumbrance" : encumbrance
    };
  }

  static List<String> weaponSkills = [
    "Brawl",
    "Gunnery",
    "Lightsaber",
    "Melee",
    "Ranged(Light)",
    "Ranged(Heavy)",
  ];
}