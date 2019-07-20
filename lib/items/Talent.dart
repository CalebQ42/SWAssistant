import 'package:swassistant/profiles/utils/JsonSavable.dart';

class Talent implements JsonSavable{
  String name;
  String desc;
  int value;

  Talent({this.name = "", this.desc = "", this.value = 0});

  Talent.fromJson(Map<String, dynamic> json) :
      name = json["name"],
      desc = json["description"],
      value = json["value"];

  Map<String, dynamic> toJson() => {
    "name" : name,
    "description" : desc,
    "value" : value
  };
}