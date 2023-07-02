import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/utils/json_savable.dart';

class Skill implements JsonSavable{
  String? name;
  int? value;
  int? base;
  bool career = false;

  Skill();

  Skill.fromJson(Map<String,dynamic> json) :
      name = json["name"] ?? "",
      value = json["value"],
      base = json["base"],
      career = json["career"] ?? false;

  Skill.from(Skill skill) :
      name = skill.name,
      value = skill.value,
      base = skill.base,
      career = skill.career;

  @override
  Map<String, dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "base" : base,
    "career" : career
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "name": "",
    "career": false,
  };

  SWDiceHolder getDice(Creature creature) {
    if(creature is Character){
      return SWDiceHolder(
        ability: (creature.charVals[base!] - value!).abs(),
        proficiency: min(creature.charVals[base!], value!)
      );
    }else if(creature is Minion){
      return SWDiceHolder(
        ability: (creature.charVals[base!] - creature.minionNum).abs(),
        proficiency: min(creature.charVals[base!], creature.minionNum)
      );
    }
    return SWDiceHolder();
  }

  @override
  String toString(){
    return "${name!} $value based on: $base is career: $career";
  }

  @override
  operator ==(other) => other is Skill && other.name == name && other.value == value && other.base == base && other.career == career;

  @override
  int get hashCode => Object.hash(name, value, base, career);

  //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
  static Map<String, int> skillsList(BuildContext context) => {
      SW.of(context).locale.skills1 : 2,
      SW.of(context).locale.skills2 : 0,
      SW.of(context).locale.skills3 : 0,
      SW.of(context).locale.skills4 : 5,
      SW.of(context).locale.skills5 : 4,
      SW.of(context).locale.skills6 : 2,
      SW.of(context).locale.skills7 : 5,
      SW.of(context).locale.skills8 : 1,
      SW.of(context).locale.skills9 : 3,
      SW.of(context).locale.skills10 : 4,
      SW.of(context).locale.skills11 : 1,
      SW.of(context).locale.skills12 : 5,
      SW.of(context).locale.skills13 : 0,
      SW.of(context).locale.skills14 : 2,
      SW.of(context).locale.skills15 : 2,
      SW.of(context).locale.skills16 : 0,
      SW.of(context).locale.skills17 : 5,
      SW.of(context).locale.skills18 : 3,
      SW.of(context).locale.skills19 : 1,
      SW.of(context).locale.skills20 : 1,
      SW.of(context).locale.skills21 : 1,
      SW.of(context).locale.skills22 : 1,
      SW.of(context).locale.skills23 : 0,
      SW.of(context).locale.skills24 : 3,
      SW.of(context).locale.skills25 : 1,
      SW.of(context).locale.skills26 : 3,
      SW.of(context).locale.skills27 : 3,
      SW.of(context).locale.skills28 : 4,
      SW.of(context).locale.skills29 : 2,
      SW.of(context).locale.skills30 : 2,
      SW.of(context).locale.skills31 : 2,
      SW.of(context).locale.skills32 : 2,
      SW.of(context).locale.skills33 : 2,
      SW.of(context).locale.skills34 : 2,
      SW.of(context).locale.skills35 : 0
    };
}