import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/utils/JsonSavable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Skill implements JsonSavable{
  String? name;
  int value;
  int base;
  bool career;

  Skill({this.name, this.value = -1, this.base = -1, this.career = false});

  Skill.fromJson(Map<String,dynamic> json) :
      name = json["name"] ?? "",
      value = json["value"] ?? -1,
      base = json["base"] ?? -1,
      career = json["career"] ?? false;

  Skill.from(Skill skill) :
      name = skill.name,
      value = skill.value,
      base = skill.base,
      career = skill.career;

  Map<String, dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "base" : base,
    "career" : career
  };

  SWDiceHolder getDice(Creature creature) {
    if(creature is Character)
      return SWDiceHolder(
        ability: (creature.charVals[base] - value).abs(),
        proficiency: min(creature.charVals[base], value)
      );
    else if(creature is Minion)
      return SWDiceHolder(
        ability: (creature.charVals[base] - creature.minionNum).abs(),
        proficiency: min(creature.charVals[base], creature.minionNum)
      );
    return SWDiceHolder();
  }

  String toString(){
    return name! + " " + value.toString() + " based on: " + base.toString() + " is career: " + career.toString();
  }

  //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
  static Map<String, int> skillsList(BuildContext context) => {
      AppLocalizations.of(context)!.skills1 : 2,
      AppLocalizations.of(context)!.skills2 : 0,
      AppLocalizations.of(context)!.skills3 : 0,
      AppLocalizations.of(context)!.skills4 : 5,
      AppLocalizations.of(context)!.skills5 : 4,
      AppLocalizations.of(context)!.skills6 : 2,
      AppLocalizations.of(context)!.skills7 : 5,
      AppLocalizations.of(context)!.skills8 : 1,
      AppLocalizations.of(context)!.skills9 : 3,
      AppLocalizations.of(context)!.skills10 : 4,
      AppLocalizations.of(context)!.skills11 : 1,
      AppLocalizations.of(context)!.skills12 : 5,
      AppLocalizations.of(context)!.skills13 : 0,
      AppLocalizations.of(context)!.skills14 : 2,
      AppLocalizations.of(context)!.skills15 : 2,
      AppLocalizations.of(context)!.skills16 : 0,
      AppLocalizations.of(context)!.skills17 : 5,
      AppLocalizations.of(context)!.skills18 : 3,
      AppLocalizations.of(context)!.skills19 : 1,
      AppLocalizations.of(context)!.skills20 : 1,
      AppLocalizations.of(context)!.skills21 : 1,
      AppLocalizations.of(context)!.skills22 : 1,
      AppLocalizations.of(context)!.skills23 : 0,
      AppLocalizations.of(context)!.skills24 : 3,
      AppLocalizations.of(context)!.skills25 : 1,
      AppLocalizations.of(context)!.skills26 : 3,
      AppLocalizations.of(context)!.skills27 : 3,
      AppLocalizations.of(context)!.skills28 : 4,
      AppLocalizations.of(context)!.skills29 : 2,
      AppLocalizations.of(context)!.skills30 : 2,
      AppLocalizations.of(context)!.skills31 : 2,
      AppLocalizations.of(context)!.skills32 : 2,
      AppLocalizations.of(context)!.skills33 : 2,
      AppLocalizations.of(context)!.skills34 : 2,
      AppLocalizations.of(context)!.skills35 : 0
    };
}