import 'package:flutter/material.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/items/Talent.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
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

  List<int> charVals = new List.filled(6, 0, growable: false);
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
      this.charVals = [];
      for(dynamic dy in json["characteristics"])
        this.charVals.add(dy);
    }
    if(json["Skills"] != null){
      this.skills = [];
      for(dynamic dy in json["Skills"])
        this.skills.add(Skill.fromJson(dy));
    }
    if(json["Talents"] != null){
      this.talents = [];
      for(dynamic dy in json["Talents"])
        this.talents.add(Talent.fromJson(dy));
    }
    woundThresh = json["wound threshold"] ?? 0;
    woundCur = json["wound current"] ?? 0;
    defMelee = json["defense melee"] ?? 0;
    defRanged = json["defense ranged"] ?? 0;
    soak = json["soak"] ?? 0;
  }

  Map<String,dynamic> creatureSaveJson(){
    var json = new Map<String,dynamic>();
    json["characteristics"] = charVals;
    json["Skills"] = List.generate(skills.length, (index) => skills[index].toJson());
    json["Talents"] = List.generate(talents.length, (index) => talents[index].toJson());
    json["wound threshold"] = woundThresh;
    json["wound current"] = woundCur;
    json["melee defense"] = defMelee;
    json["ranged defense"] = defRanged;
    json["soak"] = soak;
    return json;
  }

  static Creature? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Creature){
      return ed;
    }
    return null;
  }
}