import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:meta/meta.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/CriticalInjury.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Note.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/ui/Card.dart';
import 'package:swassistant/ui/screens/EditingEditable.dart';

import '../Character.dart';
import '../Minion.dart';
import '../Vehicle.dart';
import 'JsonSavable.dart';

//Editable holds all common components of Vehicles, Minions, and Characters and
//provides a framework on how to display, load, and save them.
abstract class Editable extends JsonSavable{

  //Common components
  int id = 0;
  String name;
  List<Note> notes = [];
  List<Weapon> weapons = [];
  String category = "";
  List<CriticalInjury> criticalInjuries = [];
  String desc = "";
  List<Item> inventory = [];

  List<bool> showCard = [];
  Route? route;

  String get fileExtension;
  List<String> get cardNames;

  //Saving variables
  bool _saving = false;
  String _loc = "";
  bool _defered = false;

  Editable({required this.id, this.name = "", bool saveOnCreation = false, required SW app}){
    showCard = List.filled(cardNames.length, false, growable: false);
    if(saveOnCreation)
      this.save(filename: getFileLocation(app));
  }

  Editable.load(FileSystemEntity file, {SW? app, BuildContext? context, this.name = ""}){
    if (app == null && context != null)
      app ??= SW.of(context);
    else
      throw Exception("Must specify app or context");
    var jsonMap = jsonDecode(File.fromUri(file.uri).readAsStringSync());
    loadJson(jsonMap);
    if(getFileLocation(app)!= file.path)
      _loc = file.path;
  }

  Editable.from(Editable editable, {int id = 0}) :
      this.id = id,
      name = editable.name,
      notes = List.from(editable.notes),
      weapons = List.from(editable.weapons),
      category = editable.category,
      criticalInjuries = List.from(editable.criticalInjuries),
      desc = editable.desc,
      inventory = editable.inventory {
    showCard = List.filled(cardNames.length, false);
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
  }

  @mustCallSuper
  void loadJson(Map<String,dynamic> json){
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
    id = json["id"];
    name = json["name"];
    notes = <Note>[];
    for (Map<String, dynamic> arrMap in json["Notes"]) 
      notes.add(Note.fromJson(arrMap));
    weapons = <Weapon>[];
    for(Map<String,dynamic> arrMap in json["Weapons"])
      weapons.add(Weapon.fromJson(arrMap));
    category = json["category"];
    criticalInjuries = <CriticalInjury>[];
    for(Map<String,dynamic> arrMap in json["Critical Injuries"])
      criticalInjuries.add(CriticalInjury.fromJson(arrMap));
    desc = json["description"];
    showCard = json["show cards"].cast<bool>();
    if(showCard.length != cardNames.length)
      showCard.addAll(List.filled(cardNames.length - showCard.length, false));
    inventory = [];
    if(json["Inventory"] != null)
      for(Map<String, dynamic> arrMap in json["Inventory"])
        inventory.add(Item.fromJson(arrMap));
  }

  @mustCallSuper
  Map<String, dynamic> toJson(){
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
    var json = new Map<String,dynamic>();
    json["Notes"] = List.generate(notes.length, (index) => notes[index].toJson());
    json["Weapons"] = List.generate(weapons.length, (index) => weapons[index].toJson());
    json["Critical Injuries"] = List.generate(criticalInjuries.length, (index) => criticalInjuries[index].toJson());
    json["name"] = name;
    json["category"] = category;
    json["description"] = desc;
    json["show cards"] = showCard;
    json["Inventory"] = List.generate(inventory.length, (index) => inventory[index].toJson());
    return json;
  }

  List<Widget> cards(Function() refreshList, BuildContext context){
    var cards = <Widget>[];
    var contents = cardContents(context, refreshList);
    cards.add(
      Padding(
        padding: EdgeInsets.all(10.0),
        child: NameCardContent(refreshList)
      )
    );
    for (int i = 0; i < contents.length; i++)
      cards.add(
        InfoCard(shown: showCard[i],contents: contents[i], title: cardNames[i], onHideChange: (bool b, refresh) => showCard[i]=b)
      );
    return cards;
  }

  Route? setRoute(Function() refreshCallback){
    route = MaterialPageRoute(builder: (BuildContext bc) => EditingEditable(this,refreshCallback),fullscreenDialog: false);
    return route;
  }

  String getFileLocation(SW sw){
    if(_loc == "")
      return sw.saveDir+ "/" + id.toString() + fileExtension;
    else
      return _loc;
  }

  String? getCloudFileLocation() => null;
  
  void save({String filename = "", BuildContext? context, SW? app}) async{
    if(filename == "") {
      if (app == null && context == null)
        throw("Either filename or context needs to be given");
      filename = getFileLocation(app ?? SW.of(context!));
    }
    if(!_saving){
      _saving = true;
      var file = File(filename);
      File? backup;
      if(file.existsSync())
        backup = file.renameSync(filename +".backup");
      file.createSync();
      file.writeAsStringSync(jsonEncode(toJson()));
      if(backup!=null)
        backup.deleteSync();
      _saving = false;
    }else{
      if(!_defered){
        _defered = true;
        while(_saving)
          sleep(Duration(milliseconds: 250));
        save(filename: filename);
        _defered = false;
      }
    }
  }

  void cloudSave(){}
  void load(String filename){
    var file = File(filename);
    loadJson(jsonDecode(file.readAsStringSync()));
  }
  void cloudLoad(){}
  void delete(SW app){
    var fil = File(getFileLocation(app));
    fil.deleteSync();
  }

  void addShortcut(){}
  bool hasShortcut(){ return false; }
  void updateShortcut(){}
  void deleteShortcut(){}

  List<Widget> cardContents(BuildContext context, Function() listUpdate);

  static Editable of(BuildContext context){
    var ed = context.dependOnInheritedWidgetOfExactType<InheritedEditable>()?.editable;
    if (ed == null){
      throw "Editable.of called outside of InheritedEditable heirarchy";
    }
    return ed;
  }
}