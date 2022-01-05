import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/creatures/Talents.dart';
import 'package:swassistant/ui/items/editable/CriticalInjuries.dart';
import 'package:swassistant/ui/items/editable/Description.dart';
import 'package:swassistant/ui/items/editable/Weapons.dart';
import 'package:swassistant/ui/items/creatures/Characteristics.dart';
import 'package:swassistant/ui/items/creatures/Defense.dart';
import 'package:swassistant/ui/items/editable/Inventory.dart';
import 'package:swassistant/ui/items/creatures/Skills.dart';
import 'package:swassistant/ui/items/minion/MinInfo.dart';
import 'package:swassistant/ui/items/minion/MinionWound.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import 'utils/Creature.dart';

class Minion extends Editable with Creature{

  int woundThreshInd = 0;
  int minionNum = 0;
  List<Item> savedInv = [];
  List<Weapon> savedWeapons = [];

  int woundCurTemp = 0;

  String get fileExtension => ".swminion";
  int get cardNum => 10;
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

  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    this.creatureLoadJson(json);
    this.woundThreshInd = json["wound threshold per minion"] ?? 0;
    this.minionNum = json["minion number"] ?? 0;
    if(json["Saved"] != null){
      Map<String,dynamic> saved = json["Saved"];
      if(saved["Inventory"] != null){
        this.savedInv = [];
        for(dynamic d in saved["Inventory"])
          this.savedInv.add(Item.fromJson(d));
      }
      if(saved["Weapons"] != null){
        this.savedWeapons = [];
        for(dynamic d in saved["Weapons"])
          this.savedWeapons.add(Weapon.fromJson(d));
      }
    }
  }

  Map<String,dynamic> toJson() =>
    super.toJson()..addAll({
      "wound threshold per minion": woundThreshInd,
      "minion number": minionNum,
      "Saved": {
        "Inventory" : List.generate(savedInv.length, (index) => savedInv[index].toJson()),
        "Weapons" : List.generate(savedWeapons.length, (index) => savedWeapons[index].toJson())
      }
    })..addAll(creatureSaveJson());

  List<EditableContent> cardContents(BuildContext context, Function() updateList) {
    Function()? weaponsRefresh;
    EditableContentStatefulHolder woundHolder = EditableContentStatefulHolder();
    EditableContentStatefulHolder numHolder = EditableContentStatefulHolder();
    EditableContentStatefulHolder invHolder = EditableContentStatefulHolder();
    woundCurTemp = woundCur;
    return [
      EditableContent(
        stateful: MinInfo(woundHolder: woundHolder, holder: numHolder),
        editButton: true
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
          if (weaponsRefresh != null)
            weaponsRefresh = refresh;
          return Weapons(editing: b, refresh: refresh);
        },
        defaultEditingState: () => weapons.length == 0,
        additonalButtons: () => [
          Tooltip(
            message: savedWeapons.length == 0 ? AppLocalizations.of(context)!.saveWeaponsFirst : AppLocalizations.of(context)!.restoreWeapons,
            child: IconButton(
              iconSize: 20.0,
              splashRadius: 20,
              padding: EdgeInsets.all(5.0),
              constraints: BoxConstraints.tight(Size.square(30.0)),
              icon: Icon(Icons.restore),
              onPressed: savedWeapons.length == 0 ? null : (){
                var tmp = List.of(weapons);
                weapons = List.of(savedWeapons);
                if (weaponsRefresh != null)
                  weaponsRefresh!();
                ScaffoldMessenger.of(context).clearSnackBars();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(AppLocalizations.of(context)!.weaponsRestored),
                    action: SnackBarAction(
                      label: AppLocalizations.of(context)!.undo,
                      onPressed: (){
                        weapons = tmp;
                        if (weaponsRefresh != null)
                          weaponsRefresh!();
                      },
                    ),
                  )
                );
              }
            )
          ),
          Tooltip(
            message: savedWeapons.length == 0 ? AppLocalizations.of(context)!.saveWeapons : AppLocalizations.of(context)!.overwriteWeapons,
            child: IconButton(
              iconSize: 20.0,
              splashRadius: 20,
              padding: EdgeInsets.all(5.0),
              constraints: BoxConstraints.tight(Size.square(30.0)),
              icon: Icon(savedWeapons.length == 0 ? Icons.save_outlined : Icons.save),
              onPressed: (){
                var reload = (savedWeapons.length == 0 && weapons.length != 0) || (savedWeapons.length != 0 && weapons.length == 0);
                savedWeapons = List.of(weapons);
                if(reload && weaponsRefresh != null)
                  weaponsRefresh!();
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
      EditableContent(
        builder: (b, refresh, state) =>
          Talents(editing: b, refresh: refresh),
        defaultEditingState: () => talents.length == 0,
      ),
      EditableContent(
        stateful: Inventory(holder: invHolder),
        defaultEditingState: () => inventory.length == 0,
        additonalButtons: () => [
          Tooltip(
            message: savedInv.length == 0 ? AppLocalizations.of(context)!.saveInventoryFirst : AppLocalizations.of(context)!.restoreInventory,
            child: IconButton(
              iconSize: 20.0,
              splashRadius: 20,
              padding: EdgeInsets.all(5.0),
              constraints: BoxConstraints.tight(Size.square(30.0)),
              icon: Icon(savedInv.length == 0 ? Icons.restore_outlined : Icons.restore),
              onPressed: savedInv.length == 0 ? null : (){
                var tmp = List.of(inventory);
                inventory = List.of(savedInv);
                if(invHolder.reloadFunction != null)
                  invHolder.reloadFunction!();
                ScaffoldMessenger.of(context).clearSnackBars();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(AppLocalizations.of(context)!.inventoryRestored),
                    action: SnackBarAction(
                      label: AppLocalizations.of(context)!.undo,
                      onPressed: (){
                        inventory = tmp;
                        if(invHolder.reloadFunction != null)
                          invHolder.reloadFunction!();
                      },
                    ),
                  )
                );
              }
            )
          ),
          Tooltip(
            message: savedInv.length == 0 ? AppLocalizations.of(context)!.saveInventory : AppLocalizations.of(context)!.overwriteInventory,
            child: IconButton(
              iconSize: 20.0,
              splashRadius: 20,
              padding: EdgeInsets.all(5.0),
              constraints: BoxConstraints.tight(Size.square(30.0)),
              icon: Icon(savedInv.length == 0 ? Icons.save_outlined : Icons.save),
              onPressed: (){
                var refresh = (savedInv.length == 0 && inventory.length != 0) || (savedInv.length != 0 && inventory.length == 0);
                savedInv = List.of(inventory);
                if(refresh && invHolder.reloadFunction != null)
                  invHolder.reloadFunction!();
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

  static Minion? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Minion)
      return ed;
    return null;
  }
}