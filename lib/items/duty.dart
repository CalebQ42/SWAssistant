
import 'package:swassistant/utils/json_savable.dart';

class Duty implements JsonSavable{

  String name = "";
  int? value;
  String desc = "";

  Duty();

  Duty.fromJson(Map<String,dynamic> json) :
      name = json["name"] ?? "",
      value = json["value"],
      desc = json["description"] ?? "";

  Duty.from(Duty duty) :
      name = duty.name,
      value = duty.value,
      desc = duty.desc;
  
  @override
  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : null,
    "description" : desc
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "name": "",
    "description": "",
  };

  @override
  operator ==(other) => other is Duty && other.name == name && other.desc == desc && other.value == value;

  @override
  int get hashCode => Object.hash(name.hashCode, desc.hashCode, value.hashCode);
}