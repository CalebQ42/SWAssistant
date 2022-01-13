import 'package:flutter/material.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/items/talent.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

mixin Creature on Editable{
  static List<String> characteristics(BuildContext context) => [
      AppLocalizations.of(context)!.brawn,
      AppLocalizations.of(context)!.agility,
      AppLocalizations.of(context)!.intellect,
      AppLocalizations.of(context)!.cunning,
      AppLocalizations.of(context)!.willpower,
      AppLocalizations.of(context)!.presence
    ];

  List<int> charVals = List.filled(6, 0, growable: false);
  List<Skill> skills = [];
  List<Talent> talents = [];
  int woundThresh = 0;
  int woundCur = 0;
  int defMelee = 0, defRanged = 0;
  int soak = 0;

  void creatureFrom(Creature creature){
    charVals = List.from(creature.charVals);
    skills = List.from(creature.skills);
    talents = List.from(creature.talents);
    woundThresh = creature.woundThresh;
    woundCur = creature.woundCur;
    defMelee = creature.defMelee;
    defRanged = creature.defRanged;
    soak = creature.soak;
  }

  void creatureLoadJson(Map<String,dynamic> json){
    if(json["characteristics"] != null){
      charVals = [];
      for(dynamic dy in json["characteristics"]){
        charVals.add(dy);
      }
    }
    if(json["Skills"] != null){
      skills = [];
      for(dynamic dy in json["Skills"]){
        skills.add(Skill.fromJson(dy));
      }
    }
    if(json["Talents"] != null){
      talents = [];
      for(dynamic dy in json["Talents"]){
        talents.add(Talent.fromJson(dy));
      }
    }
    woundThresh = json["wound threshold"] ?? 0;
    woundCur = json["wound current"] ?? 0;
    defMelee = json["defense melee"] ?? 0;
    defRanged = json["defense ranged"] ?? 0;
    soak = json["soak"] ?? 0;
  }

  Map<String,dynamic> creatureSaveJson() => {
    "characteristics": charVals,
    "Skills": List.generate(skills.length, (index) => skills[index].toJson()),
    "Talents": List.generate(talents.length, (index) => talents[index].toJson()),
    "wound threshold": woundThresh,
    "wound current": woundCur,
    "melee defense": defMelee,
    "ranged defense": defRanged,
    "soak": soak
  };

  static Creature? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Creature){
      return ed;
    }
    return null;
  }
}