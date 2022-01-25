import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/items/item.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/editable_common.dart';
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
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import 'utils/creature.dart';

class Minion extends Editable with Creature{

  int woundThreshInd = 0;
  int minionNum = 0;
  List<Item> savedInv = [];
  List<Weapon> savedWeapons = [];

  int woundCurTemp = 0;

  @override
  String get fileExtension => ".swminion";
  @override
  int get cardNum => 10;
  @override
  List<String> cardNames(BuildContext context) => [
    AppLocalizations.of(context)!.basicInfo,
    AppLocalizations.of(context)!.wound,
    AppLocalizations.of(context)!.characteristicPlural,
    AppLocalizations.of(context)!.skillPlural,
    AppLocalizations.of(context)!.defense,
    AppLocalizations.of(context)!.weaponPlural,
    AppLocalizations.of(context)!.talentPlural,
    AppLocalizations.of(context)!.inventory,
    AppLocalizations.of(context)!.criticalInj,
    AppLocalizations.of(context)!.desc
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
  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    creatureLoadJson(json);
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
  var skillKey = GlobalKey<SkillsState>();
  var talentKey = GlobalKey<TalentsState>();

  @override
  List<EditContent> cardContents(BuildContext context) => [
    EditContent(
      content: MinInfo(key: infoKey),
      contentKey: infoKey,
    ),
    EditContent(
      content: MinionWound(key: woundKey),
      contentKey: woundKey,
    ),
    EditContent(
      contentBuilder: (b) => Characteristics(editing: b)
    ),
    EditContent(
      content: Skills(key: skillKey),
      contentKey: skillKey,
      defaultEdit: () => skills.isEmpty
    ),
    EditContent(
      contentBuilder: (b) => Defense(editing: b)
    ),
    EditContent(
      content: Weapons(key: weaponKey),
      contentKey: weaponKey,
      defaultEdit: () => weapons.isEmpty,
      extraButtons: (context, b) => [
        Tooltip(
          message: savedWeapons.isEmpty ? AppLocalizations.of(context)!.saveWeaponsFirst : AppLocalizations.of(context)!.restoreWeapons,
          child: IconButton(
            iconSize: 20.0,
            splashRadius: 20,
            padding: const EdgeInsets.all(5.0),
            constraints: BoxConstraints.tight(const Size.square(30.0)),
            icon: const Icon(Icons.restore),
            onPressed: savedWeapons.isEmpty ? null : (){
              var tmp = List.of(weapons);
              weaponKey.currentState?.update(() => weapons = List.of(savedWeapons));
              ScaffoldMessenger.of(context).clearSnackBars();
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(AppLocalizations.of(context)!.weaponsRestored),
                  action: SnackBarAction(
                    label: AppLocalizations.of(context)!.undo,
                    onPressed: () =>
                      weaponKey.currentState?.update(() => weapons = tmp),
                  ),
                )
              );
            }
          )
        ),
        Tooltip(
          message: savedWeapons.isEmpty ? AppLocalizations.of(context)!.saveWeapons : AppLocalizations.of(context)!.overwriteWeapons,
          child: IconButton(
            iconSize: 20.0,
            splashRadius: 20,
            padding: const EdgeInsets.all(5.0),
            constraints: BoxConstraints.tight(const Size.square(30.0)),
            icon: Icon(savedWeapons.isEmpty ? Icons.save_outlined : Icons.save),
            onPressed: (){
              var reload = (savedWeapons.isEmpty && weapons.isNotEmpty) || (savedWeapons.isNotEmpty && weapons.isEmpty);
              if(reload) weaponKey.currentState?.update(() => savedWeapons = List.of(weapons));
              ScaffoldMessenger.of(context).clearSnackBars();
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(AppLocalizations.of(context)!.weaponsSaved),
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
          message: savedInv.isEmpty ? AppLocalizations.of(context)!.saveInventoryFirst : AppLocalizations.of(context)!.restoreInventory,
          child: IconButton(
            iconSize: 20.0,
            splashRadius: 20,
            padding: const EdgeInsets.all(5.0),
            constraints: BoxConstraints.tight(const Size.square(30.0)),
            icon: Icon(savedInv.isEmpty ? Icons.restore_outlined : Icons.restore),
            onPressed: savedInv.isEmpty ? null : (){
              var tmp = List.of(inventory);
              invKey.currentState?.update(() => inventory = List.of(savedInv));
              ScaffoldMessenger.of(context).clearSnackBars();
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(AppLocalizations.of(context)!.inventoryRestored),
                  action: SnackBarAction(
                    label: AppLocalizations.of(context)!.undo,
                    onPressed: () =>
                      invKey.currentState?.update(() => inventory = tmp),
                  ),
                )
              );
            }
          )
        ),
        Tooltip(
          message: savedInv.isEmpty ? AppLocalizations.of(context)!.saveInventory : AppLocalizations.of(context)!.overwriteInventory,
          child: IconButton(
            iconSize: 20.0,
            splashRadius: 20,
            padding: const EdgeInsets.all(5.0),
            constraints: BoxConstraints.tight(const Size.square(30.0)),
            icon: Icon(savedInv.isEmpty ? Icons.save_outlined : Icons.save),
            onPressed: (){
              var refresh = (savedInv.isEmpty && inventory.isNotEmpty) || (savedInv.isNotEmpty && inventory.isEmpty);
              if(refresh) invKey.currentState?.update(() => savedInv = List.of(inventory));
              ScaffoldMessenger.of(context).clearSnackBars();
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(AppLocalizations.of(context)!.inventorySaved),
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
      contentBuilder: (b) =>
        Description(editing: b),
      defaultEdit: () => desc == ""
    ),
  ];

  static Minion? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Minion) return ed;
    return null;
  }
}