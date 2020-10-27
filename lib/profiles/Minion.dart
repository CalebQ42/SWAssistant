import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/characters/Talents.dart';
import 'package:swassistant/ui/items/editable/CriticalInjuries.dart';
import 'package:swassistant/ui/items/editable/Description.dart';
import 'package:swassistant/ui/items/editable/Weapons.dart';
import 'package:swassistant/ui/items/creatures/Characteristics.dart';
import 'package:swassistant/ui/items/creatures/Defense.dart';
import 'package:swassistant/ui/items/editable/Inventory.dart';
import 'package:swassistant/ui/items/creatures/Skills.dart';
import 'package:swassistant/ui/items/minion/MinNum.dart';
import 'package:swassistant/ui/items/minion/MinionWound.dart';

import 'utils/Creature.dart';

class Minion extends Editable with Creature{

  int woundThreshInd = 0;
  int minionNum = 0;
  List<Item> savedInv = new List();
  List<Weapon> savedWeapons = new List();

  int woundCurTemp = 0;

  String get fileExtension => ".swminion";
  List<String> get cardNames => [
    "Number of Minions",
    "Wound",
    "Characteristics",
    "Skill",
    "Defense",
    "Weapons",
    "Talents",
    "Inventory",
    "Critical Injuries",
    "Description"
  ];

  Minion({@required int id, String name = "New Minion", bool saveOnCreation = false, SW app}) :
      super(id: id, name: name, saveOnCreation: saveOnCreation, app: app);

  Minion.load(FileSystemEntity file, SW app) : super.load(file, app: app);

  Minion.from(Minion minion, {int id}) :
      woundThreshInd = minion.woundThreshInd,
      minionNum = minion.minionNum,
      savedInv = List.of(minion.savedInv),
      savedWeapons = List.of(minion.savedWeapons),
      super.from(minion, id: id){
    creatureFrom(minion);
  }

  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    this.creatureLoadJson(json);
    this.woundThreshInd = json["wound threshold per minion"];
    this.minionNum = json["minion number"];
    Map<String,dynamic> saved = json["Saved"];
    this.savedInv = new List();
    for(dynamic d in saved["Inventory"])
      this.savedInv.add(Item.fromJson(d));
    this.savedWeapons = new List();
    for(dynamic d in saved["Weapons"])
      this.savedWeapons.add(Weapon.fromJson(d));
  }

  Map<String,dynamic> toJson(){
    var map = super.toJson();
    map.addAll(creatureSaveJson());
    map["wound threshold per minion"] = woundThreshInd;
    map["minion number"] = minionNum;
    var savedInvJson = new List();
    savedInv.forEach((element) {savedInvJson.add(element.toJson());});
    var savedWeapJson = new List();
    savedWeapons.forEach((element) {savedWeapJson.add(element.toJson());});
    map["Saved"] = {
      "Inventory" : savedInvJson,
      "Weapons" : savedWeapJson
    };
    return map;
  }

  List<Widget> cardContents(BuildContext context, Function updateList) {
    Function weaponsRefresh;
    Function invRefresh;
    EditableContentStatefulHolder woundHolder = EditableContentStatefulHolder();
    EditableContentStatefulHolder numHolder = EditableContentStatefulHolder();
    woundCurTemp = woundCur;
    return [
      EditableContent(
        stateful: MinNum(woundHolder: woundHolder, holder: numHolder),
        editButton: false
      ),
      EditableContent(
        stateful: MinionWound(holder: woundHolder, numHolder: numHolder),
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Characteristics(editing: b, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Skills(editing: b, refresh: refresh),
        defaultEditingState: () => skills.length == 0
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Defense(editing: b, state: state)
      ),
      EditableContent(
        builder: (b, refresh, state){
          weaponsRefresh = refresh;
          return Weapons(editing: b, refresh: refresh);
        },
        defaultEditingState: () => weapons.length == 0,
        additonalButtons: () => [
          Tooltip(
            message: savedWeapons == null ? "Save weapons first!" : "Restore saved weapons",
            child: IconButton(
              iconSize: 20.0,
              padding: EdgeInsets.all(5.0),
              constraints: BoxConstraints.tight(Size.square(30.0)),
              icon: Icon(Icons.restore),
              onPressed: savedWeapons.length == 0 ? null : (){
                var tmp = List.of(weapons);
                weapons = List.of(savedWeapons);
                weaponsRefresh();
                Scaffold.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Weapons Restored"),
                    action: SnackBarAction(
                      label: "Undo",
                      onPressed: (){
                        weapons = tmp;
                        weaponsRefresh();
                      },
                    ),
                  )
                );
              }
            )
          ),
          Tooltip(
            message: savedWeapons == null ? "Save weapons" : "Overwrite saved weapons",
            child: IconButton(
              iconSize: 20.0,
              padding: EdgeInsets.all(5.0),
              constraints: BoxConstraints.tight(Size.square(30.0)),
              icon: Icon(savedWeapons.length == 0 ? Icons.save_outlined : Icons.save),
              onPressed: (){
                var reload = (savedWeapons.length == 0 && weapons.length != 0) || (savedWeapons.length != 0 && weapons.length == 0);
                savedWeapons = List.of(weapons);
                if(reload)
                  weaponsRefresh();
                Scaffold.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Weapons Saved"),
                  )
                );
              }
            )
          ),
        ]
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Talents(editing: b, refresh: refresh),
        defaultEditingState: () => talents.length == 0,
      ),
      EditableContent(
        builder: (b, refresh, state) {
          invRefresh = refresh;
          return Inventory(editing: b, refresh: refresh, state: state);
        },
        defaultEditingState: () => inventory.length == 0,
        additonalButtons: () => [
          Tooltip(
            message: savedInv == null ? "Save inventory first!" : "Restore saved inventory",
            child: IconButton(
              iconSize: 20.0,
              padding: EdgeInsets.all(5.0),
              constraints: BoxConstraints.tight(Size.square(30.0)),
              icon: Icon(savedInv.length == 0 ? Icons.restore_outlined : Icons.restore),
              onPressed: savedInv.length == 0 ? null : (){
                var tmp = List.of(inventory);
                inventory = List.of(savedInv);
                invRefresh();
                Scaffold.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Inventory Restored"),
                    action: SnackBarAction(
                      label: "Undo",
                      onPressed: (){
                        inventory = tmp;
                        invRefresh();
                      },
                    ),
                  )
                );
              }
            )
          ),
          Tooltip(
            message: savedInv == null ? "Save inventory" : "Overwrite saved inventory",
            child: IconButton(
              iconSize: 20.0,
              padding: EdgeInsets.all(5.0),
              constraints: BoxConstraints.tight(Size.square(30.0)),
              icon: Icon(savedInv.length == 0 ? Icons.save_outlined : Icons.save),
              onPressed: (){
                var refresh = (savedInv.length == 0 && inventory.length != 0) || (savedInv.length != 0 && inventory.length == 0);
                savedInv = List.of(inventory);
                if(refresh)
                  invRefresh();
                Scaffold.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Inventory Saved"),
                  )
                );
              }
            )
          )
        ],
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          CriticalInjuries(editing: b, refresh: refresh),
        defaultEditingState: () => criticalInjuries.length == 0
      ),
      EditableContent(
        builder: (b, refresh, state) =>
          Description(editing: b, state: state),
        defaultEditingState: () => desc == ""
      ),
    ];
  }

  static Minion of(BuildContext context) => Editable.of(context);
}