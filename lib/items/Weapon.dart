
import 'package:swassistant/profiles/utils/JsonSavable.dart';

import 'WeaponCharacteristic.dart';

class Weapon extends JsonSavable{

  String name;
  int damage;
  int critical;
  int hp;
  int range;
  int skill;
  int skillBase;
  List<WeaponCharacteristic> characteristics;
  bool addBrawn;
  bool loaded;
  bool limitedAmmo;
  int itemState;
  int ammo;
  String firingArc;
  int encumbrance;

  Weapon({this.name = "", this.damage = 0, this.critical = 0, this.hp = 0, this.range = 0,
    this.skill = 0, this.skillBase = 0, this.characteristics, this.addBrawn = false,
    this.loaded = true, this.limitedAmmo = false, this.itemState = 0, this.ammo = 0,
    this.firingArc = "", this.encumbrance = 0}){
    if (characteristics == null){
      characteristics = new List();
    }
  }

  Weapon.fromJson(Map<String,dynamic> json){
    name = json["name"];
    damage = json["damage"];
    critical = json["critical rating"];
    hp = json["hard points"];
    range = json["range"];
    skill = json["skill"];
    skillBase = json["base"];
    characteristics = new List();
    for(Map<String,dynamic> map in json["Weapon Characteristics"]){
      characteristics.add(WeaponCharacteristic.fromJson(map));
    }
    addBrawn = json["add brawn"];
    loaded = json["loaded"];
    limitedAmmo = json["limited ammo"];
    itemState = json["item state"];
    ammo = json["ammo"];
    firingArc = json["firing arc"];
    encumbrance = json["encumbrance"];
  }

  Map<String,dynamic> toJson(){
    var characteristicsMap = new List<Map<String,dynamic>>();
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
      "firing arc" : firingArc,
      "encumbrance" : encumbrance
    };
  }
}