import 'package:swassistant/profiles/utils/JsonSavable.dart';

class Obligation implements JsonSavable{

  String name;
  int value;
  String desc;

  Obligation({this.name = "", this.value = 0});

  Obligation.fromJson(Map<String,dynamic> json) :
    name = json["name"],
    value = json["value"],
    desc = json["description"] ?? "";

  Obligation.from(Obligation obligation) :
    name = obligation.name,
    value = obligation.value,
    desc = obligation.desc;
  
  Obligation.nulled() :
    name = "",
    value = null,
    desc = "";

  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "description" : desc
  };
}