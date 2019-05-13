import 'package:swassistant/profiles/utils/JsonSavable.dart';

class Obligation implements JsonSavable{

  String name;
  int value;

  Obligation({this.name = "", this.value = 0});

  Obligation.fromJson(Map<String,dynamic> json) :
        name = json["name"],
        value = json["value"];

  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : value
  };
}