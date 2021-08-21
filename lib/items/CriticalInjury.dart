import 'package:swassistant/profiles/utils/JsonSavable.dart';

class CriticalInjury implements JsonSavable{
  String name;
  String desc;
  int severity;

  CriticalInjury({this.name = "", this.desc = "", this.severity = 0});
  
  CriticalInjury.from(CriticalInjury criticalInjury) :
    this.name = criticalInjury.name,
    this.desc = criticalInjury.desc,
    this.severity = criticalInjury.severity;

  CriticalInjury.fromJson(Map<String,dynamic> json) :
      name = json["name"] ?? "",
      desc = json["description"] ?? "",
      severity = json["severity"] ?? 0;

  Map<String, dynamic> toJson() => {
    "name" : name,
    "description" : desc,
    "severity" : severity
  };
}