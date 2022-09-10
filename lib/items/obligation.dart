import 'package:swassistant/utils/json_savable.dart';

class Obligation implements JsonSavable{

  String name = "";
  int? value;
  String desc = "";

  Obligation();

  Obligation.fromJson(Map<String,dynamic> json) :
    name = json["name"] ?? "",
    value = json["value"],
    desc = json["description"] ?? "";

  Obligation.from(Obligation obligation) :
    name = obligation.name,
    value = obligation.value,
    desc = obligation.desc;

  @override
  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : value,
    "description" : desc
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "name": "",
    "description": "",
  };

  @override
  operator ==(other) => other is Obligation && name == other.name && value == other.value && desc == other.desc;

  @override
  int get hashCode => Object.hash(name.hashCode, value.hashCode, desc.hashCode);
}