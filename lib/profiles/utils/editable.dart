import 'dart:convert';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/sw.dart';
import 'package:swassistant/items/critical_injury.dart';
import 'package:swassistant/items/item.dart';
import 'package:swassistant/items/note.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/ui/items/editable/description.dart';
import 'package:swassistant/ui/misc/info_card.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/items/editable/critical_injuries.dart';
import 'package:swassistant/ui/items/editable/inventory.dart';
import 'package:swassistant/ui/items/editable/weapons.dart';
import 'package:swassistant/ui/misc/name_card.dart';
import 'package:swassistant/ui/screens/editable_notes.dart';
import 'package:swassistant/ui/screens/editing_editable.dart';
import 'package:swassistant/utils/json_savable.dart';
import 'package:uuid/uuid.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import '../character.dart';
import '../minion.dart';
import '../vehicle.dart';

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

  Map<String,bool> showCard = {};
  Route? route;

  bool trashed = false;
  DateTime? trashTime;

  String get fileExtension;
  int get cardNum;

  //Saving variables
  bool _saving = false;
  String? loc;
  bool _defered = false;

  //Cloud Saving variables
  bool _cloudSaving = false;
  String? driveId;
  bool _cloudDefered = false;
  int _cloudVersion = -1;
  bool _syncing = false;
  bool _endSync = false;

  //Universal Keys
  var nameKey = GlobalKey<NameCardState>(); 
  var invKey = GlobalKey<InventoryState>();
  var injKey = GlobalKey<CritState>();
  var weaponKey = GlobalKey<WeaponsState>();
  var descKey = GlobalKey<DescriptionState>();
  var notesKey = GlobalKey<EditableNotesState>();

  Function()? notesUpdate;

  Editable({this.name = "", bool saveOnCreation = false, required SW app}) : uid = const Uuid().v4(){
    if(saveOnCreation){
      save(app: app);
    }
  }

  Editable.load(FileSystemEntity file, {SW? app, BuildContext? context, this.name = ""}){
    if (app == null && context == null){
      throw Exception("Must specify app or context");
    }
    app ??= SW.of(context!);
    var jsonMap = jsonDecode(File.fromUri(file.uri).readAsStringSync());
    loadJson(jsonMap);
    if(getFileLocation(app) != file.path){
      loc = file.path;
    }
  }

  Editable.from(Editable editable) :
    uid = const Uuid().v4(),
    name = editable.name,
    notes = List.from(editable.notes),
    weapons = List.from(editable.weapons),
    category = editable.category,
    criticalInjuries = List.from(editable.criticalInjuries),
    desc = editable.desc,
    inventory = editable.inventory {
    showCard = {};
    if (!(this is Character || this is Vehicle || this is Minion)){
      throw("Must be overridden by child");
    }
  }

  @mustCallSuper
  void loadJson(Map<String,dynamic> json){
    if (!(this is Character || this is Vehicle || this is Minion)){
      throw("Must be overridden by child");
    }
    uid = json["uid"] ?? const Uuid().v4();
    name = json["name"] ?? "";
    if (json["Notes"] != null){
      notes = [];
      for (Map<String, dynamic> arrMap in json["Notes"]){
        notes.add(Note.fromJson(arrMap));
      }
    }
    if (json["Weapons"] != null){
      weapons = [];
      for(Map<String,dynamic> arrMap in json["Weapons"]){
        weapons.add(Weapon.fromJson(arrMap));
      }
    }
    category = json["category"] ?? "";
    if (json["Critical Injuries"] != null){
      criticalInjuries = [];
      for(Map<String,dynamic> arrMap in json["Critical Injuries"]){
        criticalInjuries.add(CriticalInjury.fromJson(arrMap));
      }
    }
    desc = json["description"] ?? "";
    showCard = ((json["show cards v2"] ?? <String,dynamic>{}) as Map<String,dynamic>).cast();
    if (json["Inventory"] != null){
      inventory = [];
      if(json["Inventory"] != null){
        for(Map<String, dynamic> arrMap in json["Inventory"]){
          inventory.add(Item.fromJson(arrMap));
        }
      }
    }
    trashed = json["trashed"] ?? false;
    if(json["trashed time"] != null){
      trashTime = DateTime.tryParse(json["trashed time"]) ?? DateTime.now();
    }else if(trashed){
      trashTime = DateTime.now();
    }
  }

  @override
  @mustCallSuper
  Map<String, dynamic> toJson(){
    if (!(this is Character || this is Vehicle || this is Minion)){
      throw("Must be overridden by child");
    }
    return {
      "uid": uid,
      "Notes": List.generate(notes.length, (index) => notes[index].toJson()),
      "Weapons": List.generate(weapons.length, (index) => weapons[index].toJson()),
      "Critical Injuries" : List.generate(criticalInjuries.length, (index) => criticalInjuries[index].toJson()),
      "name" : name,
      "category" : category,
      "description" : desc,
      "show cards v2" : showCard,
      "Inventory" : List.generate(inventory.length, (index) => inventory[index].toJson()),
      "trashed time" : trashed ? trashTime?.toUtc().toIso8601String() ?? DateTime.now().toUtc().toIso8601String() : null,
      "trashed": trashed,
    }..removeWhere((key, value){
      if (key == "show cards v2" && (value as Map<String, bool>).values.every((element) => !element)) return true;
      if (value is List && value.isEmpty) return true;
      return false;
    });
  }

  @override
  @mustCallSuper
  Map<String,dynamic> get zeroValue => {
    "uid": "",
    "name": "",
    "category": "",
    "description": "",
    "trashed": false,
  };

  List<Widget> cards(BuildContext context){
    var cards = <Widget>[];
    var contents = cardContents(context);
    cards.add(
      Padding(
        padding: const EdgeInsets.all(4),
        child: EditContent(
          contentKey: nameKey,
          content: NameCard(key: nameKey),
          defaultEdit: () {
            if(this is Character){
              return name == AppLocalizations.of(context)!.newCharacter;
            }else if(this is Minion){
              return name == AppLocalizations.of(context)!.newMinion;
            }
            return name == AppLocalizations.of(context)!.newVehicle;
          }
        )
      )
    );
    var names = cardNames(context);
    for (int i = 0; i < contents.length; i++){
      cards.add(
        InfoCard(
          shown: showCard[names[i]] ?? false,
          contents: contents[i],
          title: names[i],
          onShowChanged: (b) {
            showCard[names[i]] = b;
            save(context: context);
          }
        )
      );
    }
    return cards;
  }

  String getFileLocation(SW sw) => loc ?? sw.saveDir + "/" + uid.toString() + fileExtension;
  
  Future<void> save({String filename = "", BuildContext? context, SW? app, bool localOnly = false}) async{
    if(filename == "") {
      if (app == null && context == null){
        throw("Either filename or context needs to be given");
      }
      app ??= SW.of(context!);
      filename = getFileLocation(app);
    }
    if(!localOnly && app != null && app.getPreference(preferences.googleDrive, false)) {
      cloudSave(app);
    }
    if(kIsWeb) return;
    if(!_saving && !_defered && !_syncing){
      _saving = true;
      var file = File(filename);
      File? backup;
      if(file.existsSync()){
        backup = file.renameSync(filename +".backup");
      }
      file.createSync();
      file.writeAsStringSync(const JsonEncoder.withIndent("  ").convert(toJson()));
      if(backup!=null){
        backup.deleteSync();
      }
      _saving = false;
    }else if(!_defered && !_syncing){
      _defered = true;
      while(_saving){
        await Future.delayed(const Duration(milliseconds: 250));
      }
      _defered = false;
      save(filename: filename);
    }
  }

  Future<String?> getDriveId(SW app) async {
    if (driveId == null){
      if(app.driver == null || !await app.driver!.ready()) return null;
      var newId = await app.driver!.getID(uid+fileExtension);
      newId ??= await app.driver!.createFile(
        uid + fileExtension,
        appProperties: {"uid" : uid},
        mimeType: "application/json"
      );
      if (newId == null) return null;
      driveId = newId;
    }
    return driveId;
  }

  Future<void> cloudSave(SW app) async {
    if(app.driver == null || !await app.driver!.ready()){
      await Future.delayed(const Duration(milliseconds: 250));
      return;
    }
    if(!_cloudSaving && !_cloudDefered && !_syncing) {
      _cloudSaving = true;
      try{
        var id = await getDriveId(app);
        if (id == null) {
          _cloudSaving = false;
          return;
        }
        var data = const JsonEncoder.withIndent("  ").convert(toJson()).codeUnits;
        await app.driver!.updateContents(id, Stream.value(data), dataLength: data.length);
        var curFil = await app.driver!.getFile(id);
        if(curFil != null) {
          _cloudVersion = int.tryParse(curFil.version ?? "") ?? 0;
        }
      }catch(e){
        await Future.delayed(const Duration(milliseconds: 250));
        cloudSave(app);
      }
      _cloudSaving = false;
    }else if (!_cloudDefered && !_syncing) {
      _cloudDefered = true;
      while(_cloudSaving) {
        await Future.delayed(const Duration(milliseconds: 500));
      }
      _cloudDefered = false;
      cloudSave(app);
    }
  }

  // TODO: real-ish time updating
  //
  // Future<void> startSync(SW app) async{
  //   _endSync = false;
  //   var id = await getDriveId(app);
  //   if (id == null) {
  //     Future.delayed(const Duration(milliseconds: 2000));
  //     startSync(app);
  //     return;
  //   }
  //   while(!_endSync){
  //     if(app.driver == null || !await app.driver!.ready()) Future.delayed(const Duration(milliseconds: 2000));
  //     _syncing = true;
  //     while(_cloudSaving || _saving){
  //       await Future.delayed(const Duration(milliseconds: 200));
  //     }
  //     var curFil = await app.driver!.getFile(id);
  //     if(curFil == null) {
  //       _syncing = false;
  //       Future.delayed(const Duration(milliseconds: 2000));
  //       continue;
  //     }
  //     if((int.tryParse(curFil.version ?? "") ?? 0) > _cloudVersion){
  //       var media = await app.driver!.getContents(id);
  //       if (media == null) return;
  //       List<int> out = [];
  //       await for(var tmp in media.stream){
  //         out.addAll(tmp);
  //       }
  //       await baseNewVersion((jsonDecode(String.fromCharCodes(out)) as Map<String,dynamic>));
  //       _cloudVersion = int.tryParse(curFil.version ?? "") ?? 0;
  //     }
  //     _syncing = false;
  //   }
  // }

  // void endSync(){
  //   _endSync = true;
  // }

  // Future<void> baseNewVersion(Map<String,dynamic> json) async{
  //   List<void Function()> updates = [];
  //   //TODO: Add more updates
  //   if(json["name"] ?? "" != name) updates.add(() => nameKey.currentState?.refresh());
  //   var newNotes = <Note>[];
  //   if (json["Notes"] != null){
  //     for (Map<String, dynamic> arrMap in json["Notes"]){
  //       newNotes.add(Note.fromJson(arrMap));
  //     }
  //   }
  //   if(notes.length != newNotes.length || notes != newNotes){
  //     updates.add(() {
  //       if(notesUpdate != null) notesUpdate!();
  //     });
  //   }
  //   var newWeapons = <Weapon>[];
  //   if (json["Weapons"] != null){
  //     for(Map<String,dynamic> arrMap in json["Weapons"]){
  //       newWeapons.add(Weapon.fromJson(arrMap));
  //     }
  //   }
  //   category = json["category"] ?? "";
  //   if (json["Critical Injuries"] != null){
  //     criticalInjuries = [];
  //     for(Map<String,dynamic> arrMap in json["Critical Injuries"]){
  //       criticalInjuries.add(CriticalInjury.fromJson(arrMap));
  //     }
  //   }
  //   desc = json["description"] ?? "";
  //   showCard = ((json["show cards v2"] ?? <String,dynamic>{}) as Map<String,dynamic>).cast();
  //   if (json["Inventory"] != null){
  //     inventory = [];
  //     if(json["Inventory"] != null){
  //       for(Map<String, dynamic> arrMap in json["Inventory"]){
  //         inventory.add(Item.fromJson(arrMap));
  //       }
  //     }
  //   }
  //   trashed = json["trashed"] ?? false;
  //   if(json["trashed time"] != null){
  //     trashTime = DateTime.tryParse(json["trashed time"]) ?? DateTime.now();
  //   }else if(trashed){
  //     trashTime = DateTime.now();
  //   }
  //   await newVersion(json, updates);
  //   loadJson(json);
  //   for(var func in updates) {
  //     func();
  //   }
  // }

  // @mustCallSuper
  // Future<void> newVersion(Map<String, dynamic> json, List<Function()> updateKeys);

  void load(String filename){
    var file = File(filename);
    loadJson(jsonDecode(file.readAsStringSync()));
  }

  Future<void> cloudLoad(SW app, String id, {bool overwriteId = true}) async {
    if(app.driver == null || !await app.driver!.ready()) return;
    var fil = await app.driver!.getFile(id);
    if(fil == null) return;
    _cloudVersion = int.tryParse(fil.version ?? "") ?? 0;
    var media = await app.driver!.getContents(id);
    if (media == null) return;
    List<int> out = [];
    await for(var tmp in media.stream){
      out.addAll(tmp);
    }
    loadJson((jsonDecode(String.fromCharCodes(out)) as Map<String,dynamic>));
    if(overwriteId) driveId = id;
  }

  void trash(SW app){
    app.remove(this);
    app.trashCan.add(this);
    if(!_saving && !_defered && !_cloudDefered && !_cloudSaving){
      trashed = true;
      trashTime = DateTime.now();
      save(app: app);
    }else{
      Future(() async {
        while(_saving || _defered || _cloudDefered || _cloudSaving){
          await Future.delayed(const Duration(milliseconds: 200));
        }
        _saving = true;
        _cloudSaving = true;
        var fil = File(getFileLocation(app));
        fil.deleteSync();
        if(driveId != null) await app.driver?.delete(driveId!);
        _saving = false;
        _cloudSaving = false;
        save(app: app);
      });
    }
  }

  void deletePermanently(SW app){
    if(!_saving && !_defered && !_cloudDefered && !_cloudSaving){
      if(!kIsWeb) {
        var fil = File(getFileLocation(app));
        fil.deleteSync();
      }
      if(driveId != null) app.driver?.delete(driveId!);
      driveId = null;
    }else{
      Future(() async {
        while(_saving || _defered || _cloudDefered || _cloudSaving){
          await Future.delayed(const Duration(milliseconds: 200));
        }
        _saving = true;
        _cloudSaving = true;
        var fil = File(getFileLocation(app));
        fil.deleteSync();
        if(driveId != null) await app.driver?.delete(driveId!);
        driveId = null;
        _saving = false;
        _cloudSaving = false;
      });
    }
  }

  void addShortcut(){}
  bool hasShortcut(){ return false; }
  void updateShortcut(){}
  void deleteShortcut(){}

  List<EditContent> cardContents(BuildContext context);

  List<String> cardNames(BuildContext context);

  static Editable of(BuildContext context){
    var ed = context.dependOnInheritedWidgetOfExactType<InheritedEditable>()?.editable;
    if (ed == null){
      throw "Editable.of called outside of InheritedEditable heirarchy";
    }
    return ed;
  }
}