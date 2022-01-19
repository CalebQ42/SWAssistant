import 'package:swassistant/utils/json_savable.dart';

class ForcePower implements JsonSavable {
  String name;
  String desc;

  ForcePower({this.name = "", this.desc = ""});

  ForcePower.fromJson(Map<String, dynamic> json)
      : name = json["name"] ?? "",
        desc = json["description"] ?? "";

  ForcePower.from(ForcePower fp)
      : name = fp.name,
        desc = fp.desc;

  @override
  Map<String, dynamic> toJson() => {"name": name, "description": desc};
}
