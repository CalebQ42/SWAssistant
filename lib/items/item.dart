import 'dart:ui';

import 'package:swassistant/utils/json_savable.dart';

class Item implements JsonSavable{
  String name = "";
  String desc = "";
  int count = 1;
  int encum = 0;

  Item({this.name = "", this.desc = "", this.count = 1, this.encum = 0});

  Item.from(Item item) :
      name = item.name,
      desc = item.desc,
      count = item.count,
      encum = item.encum;

  Item.fromJson(Map<String, dynamic> json) :
      name = json["name"] ?? "",
      desc = json["description"] ?? "",
      count = json["count"] ?? 1,
      encum = json["encumbrance"] ?? 0;

  @override
  Map<String, dynamic> toJson() => {
    "name" : name,
    "description" : desc,
    "count" : count,
    "encumbrance" : encum
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "name": "",
    "description": "",
    "count": 1,
    "encumbrance": 0,
  };

  @override
  operator ==(other) => other is Item && other.name == name && other.desc == desc && other.count == count && other.encum == encum;

  @override
  int get hashCode => hashValues(name.hashCode, desc.hashCode, count.hashCode, encum.hashCode);
}