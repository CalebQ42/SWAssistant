
import 'package:swassistant/utils/json_savable.dart';

class Note implements JsonSavable{
  String title;
  String note;
  int align; //0-left, 1-center, 2-right

  Note({this.title = "", this.note = "", this.align = 0});

  Note.fromJson(Map<String, dynamic> map) :
      title = map["title"] ?? "",
      note = map["note"] ?? "",
      align = map["align"] ?? 0;

  @override
  Map<String,dynamic> toJson()=>{
    "title": title,
    "note": note,
    "align": align
  };
}