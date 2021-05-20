import 'dart:math';

import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/profiles/utils/JsonSavable.dart';

class Skill implements JsonSavable{
  String name;
  int value;
  int base;
  bool career;

  Skill({this.name = "", this.value = 0, this.base = 0, this.career = false});

  Skill.fromJson(Map<String,dynamic> json) :
      name = json["name"],
      value = json["value"],
      base = json["base"],
      career = json["career"];

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
    return name + " " + value.toString() + " based on: " + base.toString() + " is career: " + career.toString();
  }

  //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
  static Map<String, int> skillsList = {
    "Astrogation" : 2,
    "Athletics" : 0,
    "Brawl" : 0,
    "Charm" : 5,
    "Coercion" : 4,
    "Computers" : 2,
    "Cool" : 5,
    "Coordination" : 1,
    "Deception" : 3,
    "Discipline" : 4,
    "Gunnery" : 1,
    "Leadership" : 5,
    "Lightsaber" : 0,
    "Mechanics" : 2,
    "Medicine" : 2,
    "Melee" : 0,
    "Negotiation" : 5,
    "Perception" : 3,
    "Piloting(Planetary)" : 1,
    "Piloting(Space)" : 1,
    "Ranged(Heavy)" : 1,
    "Ranged(Light)" : 1,
    "Resilience" : 0,
    "Skulduggery" : 3,
    "Stealth" : 1,
    "Streetwise" : 3,
    "Survival" : 3,
    "Vigilance" : 4,
    "Knowledge(Core Worlds)" : 2,
    "Knowledge(Education)" : 2,
    "Knowledge(Lore)" : 2,
    "Knowledge(Outer Rim)" : 2,
    "Knowledge(Underworld)" : 2,
    "Knowledge(Xenology)" : 2,
    "Other..." : 0
  };
}