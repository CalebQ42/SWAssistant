import 'dart:ui';

import 'package:swassistant/utils/json_savable.dart';

class ForcePower implements JsonSavable{
  String name = "";
  String desc = "";

  ForcePower();

  ForcePower.fromJson(Map<String,dynamic> json) :
      name = json["name"] ?? "",
      desc = json["description"] ?? "";

  ForcePower.from(ForcePower fp) :
      name = fp.name,
      desc = fp.desc;

  @override
  Map<String, dynamic> toJson() => {
    "name" : name,
    "description" : desc
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "name": "",
    "description": ""
  };

  @override
  operator ==(other) => other is ForcePower && other.name == name && other.desc == desc;

  @override
  int get hashCode => hashValues(name.hashCode, desc.hashCode);
}