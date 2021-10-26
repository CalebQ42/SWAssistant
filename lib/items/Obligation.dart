import 'package:swassistant/utils/JsonSavable.dart';

class Obligation implements JsonSavable{

  String name;
  int value;
  String desc;

  Obligation({this.name = "", this.value = -1, this.desc = ""});

  Obligation.fromJson(Map<String,dynamic> json) :
    name = json["name"] ?? "",
    value = json["value"] ?? -1,
    desc = json["description"] ?? "";

  Obligation.from(Obligation obligation) :
    name = obligation.name,
    value = obligation.value,
    desc = obligation.desc;

  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "description" : desc
  };
}