import 'package:swassistant/profiles/utils/JsonSavable.dart';

class Duty implements JsonSavable{

  String name;
  int value;
  String desc;

  Duty({this.name = "", this.value = 0, this.desc = ""});

  Duty.fromJson(Map<String,dynamic> json) :
      name = json["name"],
      value = json["value"],
      desc = json["description"] ?? "";

  Duty.from(Duty duty) :
      name = duty.name,
      value = duty.value,
      desc = duty.desc;
  
  Duty.nulled() :
      name = "",
      value = null,
      desc = "";

  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "description" : desc
  };
}