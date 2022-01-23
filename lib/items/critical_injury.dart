import 'package:swassistant/utils/json_savable.dart';

class CriticalInjury implements JsonSavable{
  String name;
  String desc;
  int severity;

  CriticalInjury({this.name = "", this.desc = "", this.severity = 0});
  
  CriticalInjury.from(CriticalInjury criticalInjury) :
    name = criticalInjury.name,
    desc = criticalInjury.desc,
    severity = criticalInjury.severity;

  CriticalInjury.fromJson(Map<String,dynamic> json) :
      name = json["name"] ?? "",
      desc = json["description"] ?? "",
      severity = json["severity"] ?? 0;

  @override
  Map<String, dynamic> toJson() => {
    "name" : name,
    "description" : desc,
    "severity" : severity
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "name": "",
    "description": "",
    "severity": 0,
  };
}