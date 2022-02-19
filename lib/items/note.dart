
import 'dart:ui';

import 'package:swassistant/utils/json_savable.dart';

class Note implements JsonSavable{
  String title = "";
  String note = "";
  int align = 0; //0-left, 1-center, 2-right

  Note();

  Note.fromJson(Map<String, dynamic> map) :
      title = map["title"] ?? "",
      note = map["note"] ?? "",
      align = map["align"] ?? 0;

  @override
  Map<String,dynamic> toJson()=>{
    "title": title,
    "note": note,
    "align": align
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "title": "",
    "note": "",
    "align": 0,
  };

  @override
  operator ==(other) => other is Note && other.title == title && other.note == note && other.align == align;

  @override
  int get hashCode => hashValues(title.hashCode, note.hashCode, align.hashCode);
}