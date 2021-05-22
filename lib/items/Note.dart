
import 'package:swassistant/profiles/utils/JsonSavable.dart';

class Note implements JsonSavable{
  String title;
  String note;

  Note({this.title = "", this.note = ""});

  Note.fromJson(Map<String, dynamic> map) :
      title = map["title"] ?? "",
      note = map["note"] ?? "";

  Map<String,dynamic> toJson()=>{
    "title" : title,
    "note" : note
  };
}