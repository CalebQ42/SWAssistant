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
}