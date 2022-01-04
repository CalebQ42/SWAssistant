import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/CriticalInjury.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Note.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/ui/Card.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/screens/EditingEditable.dart';
import 'package:swassistant/utils/JsonSavable.dart';
import 'package:uuid/uuid.dart';

import '../Character.dart';
import '../Minion.dart';
import '../Vehicle.dart';

//Editable holds all common components of Vehicles, Minions, and Characters and
//provides a framework on how to display, load, and save them.
abstract class Editable extends JsonSavable{

  //Common components
  late String uid;
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
  int get cardNum;

  //Saving variables
  bool _saving = false;
  String? loc;
  bool _defered = false;

  Editable({this.name = "", bool saveOnCreation = false, required SW app}) : this.uid = Uuid().v4(){
    showCard = List.filled(cardNum, false, growable: false);
    if(saveOnCreation)
      this.save(filename: getFileLocation(app));
  }

  Editable.load(FileSystemEntity file, {SW? app, BuildContext? context, this.name = ""}){
    if (app == null && context == null)
      throw Exception("Must specify app or context");
    app ??= SW.of(context!);
    var jsonMap = jsonDecode(File.fromUri(file.uri).readAsStringSync());
    loadJson(jsonMap);
    if(getFileLocation(app) != file.path)
      loc = file.path;
  }

  Editable.from(Editable editable) :
      uid = Uuid().v4(),
      name = editable.name,
      notes = List.from(editable.notes),
      weapons = List.from(editable.weapons),
      category = editable.category,
      criticalInjuries = List.from(editable.criticalInjuries),
      desc = editable.desc,
      inventory = editable.inventory {
      showCard = List.filled(cardNum, false);
      if (!(this is Character || this is Vehicle || this is Minion))
        throw("Must be overridden by child");
  }

  @mustCallSuper
  void loadJson(Map<String,dynamic> json){
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
    uid = json["uid"] ?? Uuid().v4();
    name = json["name"] ?? "";
    if (json["Notes"] != null){
      notes = [];
      for (Map<String, dynamic> arrMap in json["Notes"]) 
        notes.add(Note.fromJson(arrMap));
    }
    if (json["Weapons"] != null){
      weapons = [];
      for(Map<String,dynamic> arrMap in json["Weapons"])
        weapons.add(Weapon.fromJson(arrMap));
    }
    category = json["category"] ?? "";
    if (json["Critical Injuries"] != null){
      criticalInjuries = [];
      for(Map<String,dynamic> arrMap in json["Critical Injuries"])
        criticalInjuries.add(CriticalInjury.fromJson(arrMap));
    }
    desc = json["description"] ?? "";
    if (json["show cards"] != null){
      showCard = json["show cards"].cast<bool>();
      if(showCard.length != cardNum)
        showCard.addAll(List.filled(cardNum - showCard.length, false));
    }
    if (json["Inventory"] != null){
      inventory = [];
      if(json["Inventory"] != null)
        for(Map<String, dynamic> arrMap in json["Inventory"])
          inventory.add(Item.fromJson(arrMap));
    }
  }

  @mustCallSuper
  Map<String, dynamic> toJson(){
    if (!(this is Character || this is Vehicle || this is Minion))
      throw("Must be overridden by child");
    return {
      "uid": uid,
      "Notes": List.generate(notes.length, (index) => notes[index].toJson()),
      "Weapons": List.generate(weapons.length, (index) => weapons[index].toJson()),
      "Critical Injuries" : List.generate(criticalInjuries.length, (index) => criticalInjuries[index].toJson()),
      "name" : name,
      "category" : category,
      "description" : desc,
      "show cards" : showCard,
      "Inventory" : List.generate(inventory.length, (index) => inventory[index].toJson()),
    };
  }

  List<Widget> cards(BuildContext context, Function() refreshList){
    var cards = <Widget>[];
    var contents = cardContents(context, refreshList);
    cards.add(
      Padding(
        padding: EdgeInsets.all(10.0),
        child: NameCardContent(refreshList)
      )
    );
    var names = cardNames(context);
    for (int i = 0; i < contents.length; i++)
      cards.add(
        InfoCard(
          shown: showCard[i],
          contents: contents[i],
          title: names[i],
          onHideChange: (bool b, refresh) {
            showCard[i]=b;
            if (!b){
              contents[i].stateful?.getHolder().editing = false;
              if(contents[i].stateful?.getHolder().reloadFunction != null)
                contents[i].stateful?.getHolder().reloadFunction!();
            }
          }
        )
      );
    return cards;
  }

  Route setRoute(Function() refreshCallback){
    return MaterialPageRoute(
      builder: (BuildContext bc) => EditingEditable(this, refreshCallback),
      settings: RouteSettings(name: uid.toString() + fileExtension)
    );
  }

  String getFileLocation(SW sw) => loc ?? sw.saveDir + "/" + uid.toString() + fileExtension;

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

  List<EditableContent> cardContents(BuildContext context, Function() listUpdate);

  List<String> cardNames(BuildContext context);

  static Editable of(BuildContext context){
    var ed = context.dependOnInheritedWidgetOfExactType<InheritedEditable>()?.editable;
    if (ed == null){
      throw "Editable.of called outside of InheritedEditable heirarchy";
    }
    return ed;
  }
}