abstract class JsonSavable{

  JsonSavable();
  JsonSavable.fromJson(Map<String,dynamic> json);

  Map<String,dynamic> toJson();
}