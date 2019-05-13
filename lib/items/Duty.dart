import 'package:swassistant/profiles/utils/JsonSavable.dart';

class Duty implements JsonSavable{

  String name;
  int value;

  Duty({this.name = "", this.value = 0});

  Duty.fromJson(Map<String,dynamic> json) :
      name = json["name"],
      value = json["value"];

  Map<String,dynamic> toJson() => {
    "name" : name,
    "value" : value
  };
}