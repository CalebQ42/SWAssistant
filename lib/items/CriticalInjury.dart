import 'package:swassistant/profiles/utils/JsonSavable.dart';

class CriticalInjury implements JsonSavable{
  String name;
  String desc;
  int severity;

  CriticalInjury({this.name = "", this.desc = "", this.severity = 0});

  CriticalInjury.fromJson(Map<String,dynamic> json) :
      name = json["name"],
      desc = json["description"],
      severity = json["severity"];

  Map<String, dynamic> toJson() => {
    "name" : name,
    "description" : desc,
    "severity" : severity
  };
}