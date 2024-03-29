import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/items/item.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/items/creatures/talents.dart';
import 'package:swassistant/ui/items/editable/critical_injuries.dart';
import 'package:swassistant/ui/items/editable/description.dart';
import 'package:swassistant/ui/items/editable/weapons.dart';
import 'package:swassistant/ui/items/creatures/characteristics.dart';
import 'package:swassistant/ui/items/creatures/defense.dart';
import 'package:swassistant/ui/items/editable/inventory.dart';
import 'package:swassistant/ui/items/creatures/skills.dart';
import 'package:swassistant/ui/items/minion/minion_info.dart';
import 'package:swassistant/ui/items/minion/minion_wound.dart';
import 'package:swassistant/ui/misc/mini_icon_button.dart';

import 'utils/creature.dart';

class Minion extends Editable with Creature{

  int woundThreshInd = 0;
  int minionNum = 0;
  List<Item> savedInv = [];
  List<Weapon> savedWeapons = [];

  // int woundCurTemp = 0;

  @override
  String get fileExtension => ".swminion";
  @override
  int get cardNum => 10;
  @override
  List<String> cardNames(BuildContext context) => [
    SW.of(context).locale.basicInfo,
    SW.of(context).locale.wound,
    SW.of(context).locale.characteristicPlural,
    SW.of(context).locale.skillPlural,
    SW.of(context).locale.defense,
    SW.of(context).locale.weaponPlural,
    SW.of(context).locale.talentPlural,
    SW.of(context).locale.inventory,
    SW.of(context).locale.criticalInj,
    SW.of(context).locale.desc
  ];

  Minion({String name = "New Minion", bool saveOnCreation = false, required SW app}) :
      super(name: name, saveOnCreation: saveOnCreation, app: app);

  Minion.load(FileSystemEntity file, SW app) : super.load(file, app: app);

  Minion.from(Minion minion) :
      woundThreshInd = minion.woundThreshInd,
      minionNum = minion.minionNum,
      savedInv = List.of(minion.savedInv),
      savedWeapons = List.of(minion.savedWeapons),
      super.from(minion){
    creatureFrom(minion);
  }

  @override
  void loadJson(Map<String,dynamic> json, bool subtractMode){
    super.loadJson(json, subtractMode);
    creatureLoadJson(json, subtractMode);
    woundThreshInd = json["wound threshold per minion"] ?? 0;
    minionNum = json["minion number"] ?? 0;
    if(json["Saved"] != null){
      Map<String,dynamic> saved = json["Saved"];
      if(saved["Inventory"] != null){
        savedInv = [];
        for(dynamic d in saved["Inventory"]){
          savedInv.add(Item.fromJson(d));
        }
      }
      if(saved["Weapons"] != null){
        savedWeapons = [];
        for(dynamic d in saved["Weapons"]){
          savedWeapons.add(Weapon.fromJson(d));
        }
      }
    }
  }

  @override
  Map<String,dynamic> toJson() => {
    ...super.toJson(),
    "wound threshold per minion": woundThreshInd,
    "minion number": minionNum,
    "Saved": {
      "Inventory" : List.generate(savedInv.length, (index) => savedInv[index].toJson()),
      "Weapons" : List.generate(savedWeapons.length, (index) => savedWeapons[index].toJson())
    }
  }..addAll(creatureSaveJson())..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String,dynamic> get zeroValue => {
    ...super.zeroValue,
    ...creatureZeroValue,
    "wound threshold per minion": 0,
    "minion number": 0,
    "Saved": {
      "Inventory": <Map<String,dynamic>>[],
      "Weapons": <Map<String,dynamic>>[]
    }
  };

  var infoKey = GlobalKey<MinInfoState>();
  var woundKey = GlobalKey<MinionWoundState>();
  var charKey = GlobalKey<CharacteristicsState>();
  var skillKey = GlobalKey<SkillsState>();
  var defKey = GlobalKey<DefenseState>();
  var talentKey = GlobalKey<TalentsState>();

  @override
  List<EditContent> cardContents(BuildContext context) {
    var app = SW.of(context);
    return [
      EditContent(
        content: MinInfo(key: infoKey),
        contentKey: infoKey,
      ),
      EditContent(
        content: MinionWound(key: woundKey),
        contentKey: woundKey,
      ),
      EditContent(
        contentKey: charKey,
        content: Characteristics(key: charKey),
      ),
      EditContent(
        content: Skills(key: skillKey),
        contentKey: skillKey,
        defaultEdit: () => skills.isEmpty
      ),
      EditContent(
        contentKey: defKey,
        content: Defense(key: defKey),
      ),
      EditContent(
        content: Weapons(key: weaponKey),
        contentKey: weaponKey,
        defaultEdit: () => weapons.isEmpty,
        extraButtons: (context, b) => [
          Tooltip(
            message: savedWeapons.isEmpty ? app.locale.saveWeaponsFirst : app.locale.restoreWeapons,
            child: MiniIconButton(
              icon: const Icon(Icons.restore),
              onPressed: savedWeapons.isEmpty ? null : (){
                var tmp = List.of(weapons);
                weaponKey.currentState?.update(() => weapons = List.of(savedWeapons));
                ScaffoldMessenger.of(context).clearSnackBars();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(app.locale.weaponsRestored),
                    action: SnackBarAction(
                      label: app.locale.undo,
                      onPressed: () =>
                        weaponKey.currentState?.update(() => weapons = tmp),
                    ),
                  )
                );
              }
            )
          ),
          Tooltip(
            message: savedWeapons.isEmpty ? app.locale.saveWeapons : app.locale.overwriteWeapons,
            child: MiniIconButton(
              icon: Icon(savedWeapons.isEmpty ? Icons.save_outlined : Icons.save),
              onPressed: (){
                var reload = (savedWeapons.isEmpty && weapons.isNotEmpty) || (savedWeapons.isNotEmpty && weapons.isEmpty);
                if(reload) weaponKey.currentState?.update(() => savedWeapons = List.of(weapons));
                ScaffoldMessenger.of(context).clearSnackBars();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(app.locale.weaponsSaved),
                  )
                );
              }
            )
          ),
        ]
      ),
      EditContent(
        content: Talents(key: talentKey),
        contentKey: talentKey,
        defaultEdit: () => talents.isEmpty,
      ),
      EditContent(
        content: Inventory(key: invKey),
        contentKey: invKey,
        defaultEdit: () => inventory.isEmpty,
        extraButtons: (context, b) => [
          Tooltip(
            message: savedInv.isEmpty ? app.locale.saveInventoryFirst : app.locale.restoreInventory,
            child: MiniIconButton(
              icon: Icon(savedInv.isEmpty ? Icons.restore_outlined : Icons.restore),
              onPressed: savedInv.isEmpty ? null : (){
                var tmp = List.of(inventory);
                invKey.currentState?.update(() => inventory = List.of(savedInv));
                ScaffoldMessenger.of(context).clearSnackBars();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(app.locale.inventoryRestored),
                    action: SnackBarAction(
                      label: app.locale.undo,
                      onPressed: () =>
                        invKey.currentState?.update(() => inventory = tmp),
                    ),
                  )
                );
              }
            )
          ),
          Tooltip(
            message: savedInv.isEmpty ? app.locale.saveInventory : app.locale.overwriteInventory,
            child: MiniIconButton(
              icon: Icon(savedInv.isEmpty ? Icons.save_outlined : Icons.save),
              onPressed: (){
                var refresh = (savedInv.isEmpty && inventory.isNotEmpty) || (savedInv.isNotEmpty && inventory.isEmpty);
                if(refresh) invKey.currentState?.update(() => savedInv = List.of(inventory));
                ScaffoldMessenger.of(context).clearSnackBars();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(app.locale.inventorySaved),
                  )
                );
              }
            )
          )
        ],
      ),
      EditContent(
        content: CriticalInjuries(key: injKey),
        contentKey: injKey,
        defaultEdit: () => criticalInjuries.isEmpty
      ),
      EditContent(
        contentKey: descKey,
        content: Description(key: descKey),
        defaultEdit: () => desc == ""
      ),
    ];
  }

  static Minion? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Minion) return ed;
    return null;
  }
}