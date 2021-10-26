import 'package:swassistant/utils/JsonSavable.dart';

class Talent implements JsonSavable{
  String name;
  String desc;
  int value;

  Talent({this.name = "", this.desc = "", this.value = -1});

  Talent.fromJson(Map<String, dynamic> json) :
      name = json["name"] ?? "",
      desc = json["description"] ?? "",
      value = json["value"] ?? -1;

  Talent.from(Talent talent) :
      name = talent.name,
      desc = talent.desc,
      value = talent.value;

  Map<String, dynamic> toJson() => {
    "name" : name,
    "description" : desc,
    "value" : value
  };
}