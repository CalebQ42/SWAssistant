import 'package:swassistant/profiles/utils/JsonSavable.dart';

class Item implements JsonSavable{
  String name;
  String desc;
  int count;
  int encum;

  Item({this.name = "", this.desc = "", this.count = 0, this.encum = 0});

  Item.from(Item item) :
      name = item.name,
      desc = item.desc,
      count = item.count,
      encum = item.encum;

  Item.fromJson(Map<String, dynamic> json) :
      name = json["name"],
      desc = json["description"],
      count = json["count"],
      encum = json["encumbrance"];

  Map<String, dynamic> toJson() => {
    "name" : name,
    "description" : desc,
    "count" : count,
    "encumbrance" : encum
  };
}