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

  Map<String, dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "base" : base,
    "career" : career
  };

  String toString(){
    return name + " " + value.toString() + " based on: " + base.toString() + " is career: " + career.toString();
  }


  static Map<String, String> SkillsList = {
    "Astrogation" : "Intellect",
    "Athletics" : "Brawn",
    "Brawl" : "Brawn",
    "Charm" : "Presence",
    "Coercion" : "Willpower",
    "Computers" : "Intellect",
    "Cool" : "Presence",
    "Coordination" : "Agility",
    "Deception" : "Cunning",
    "Discipline" : "Willpower",
    "Gunnery" : "Agility",
    "Leadership" : "Presence",
    "Lightsaber" : "Brawn",
    "Mechanics" : "Intellect",
    "Medicine" : "Intellect",
    "Melee" : "Brawn",
    "Negotiation" : "Presence",
    "Perception" : "Cunning",
    "Piloting (Planetary)" : "Agility",
    "Piloting (Space)" : "Agility",
    "Ranged (Heavy)" : "Agility",
    "Ranged (Light)" : "Agility",
    "Resilience" : "Brawn",
    "Skulduggery" : "Cunning",
    "Stealth" : "Agility",
    "Streetwise" : "Cunning",
    "Survival" : "Cunning",
    "Vigilance" : "Willpower",
    "Knowledge (Core Worlds)" : "Intellect",
    "Knowledge (Education)" : "Intellect",
    "Knowledge (Lore)" : "Intellect",
    "Knowledge (Outer Rim)" : "Intellect",
    "Knowledge (Underworld)" : "Intellect",
    "Knowledge (Xenology)" : "intellect",
  };
}