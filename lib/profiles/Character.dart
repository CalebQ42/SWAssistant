import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/items/Duty.dart';
import 'package:swassistant/items/ForcePower.dart';
import 'package:swassistant/items/Obligation.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/characters/Duties.dart';
import 'package:swassistant/ui/items/characters/Morality.dart';
import 'package:swassistant/ui/items/characters/Obligations.dart';
import 'package:swassistant/ui/items/characters/XP.dart';
import 'package:swassistant/ui/items/characters/ForcePowers.dart';
import 'package:swassistant/ui/items/characters/Specializations.dart';
import 'package:swassistant/ui/items/creatures/Talents.dart';
import 'package:swassistant/ui/items/editable/CriticalInjuries.dart';
import 'package:swassistant/ui/items/editable/Description.dart';
import 'package:swassistant/ui/items/editable/Weapons.dart';
import 'package:swassistant/ui/items/creatures/Characteristics.dart';
import 'package:swassistant/ui/items/characters/CharacterInfo.dart';
import 'package:swassistant/ui/items/characters/WoundStrain.dart';
import 'package:swassistant/ui/items/creatures/Defense.dart';
import 'package:swassistant/ui/items/editable/Inventory.dart';
import 'package:swassistant/ui/items/creatures/Skills.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import 'utils/Creature.dart';

class Character extends Editable with Creature{

  String species = "";
  String career = "";
  List<String> specializations = [];
  List<ForcePower> forcePowers = [];
  String motivation = "";
  String emotionalStr = "";
  String emotionalWeak = "";
  List<Duty> duties = [];
  List<Obligation> obligations = [];
  int strainThresh = 0, strainCur = 0;
  int xpTot = 0, xpCur = 0;
  int force = 0;
  int credits = 0;
  int morality = 0, conflict = 0;
  bool darkSide = false;
  int age = 0;
  int encumCap = 0;

  bool disableForce = false;
  bool disableDuty = false;
  bool disableObligation = false;
  bool disableMorality = false;

  String get fileExtension => ".swcharacter";
  int get cardNum {
    var out = 16;
    if (disableForce)
      out--;
    if (disableMorality)
      out--;
    if (disableDuty)
      out--;
    if (disableObligation)
      out--;
    return out;
  }
  
  List<String> cardNames(BuildContext context) => [
    AppLocalizations.of(context)!.basicInfo,
    AppLocalizations.of(context)!.woundStrain,
    AppLocalizations.of(context)!.characteristicPlural,
    AppLocalizations.of(context)!.skillPlural,
    AppLocalizations.of(context)!.defense,
    AppLocalizations.of(context)!.weaponPlural,
    AppLocalizations.of(context)!.criticalInj,
    AppLocalizations.of(context)!.specializationPlural,
    AppLocalizations.of(context)!.talentPlural,
    if(!disableForce) AppLocalizations.of(context)!.forcePowerPlural,
    AppLocalizations.of(context)!.xp,
    AppLocalizations.of(context)!.inventory,
    if(!disableMorality) AppLocalizations.of(context)!.morality,
    if(!disableDuty) AppLocalizations.of(context)!.duty,
    if(!disableObligation) AppLocalizations.of(context)!.obligation,
    AppLocalizations.of(context)!.desc
  ];

  Character({String name = "New Character", bool saveOnCreation = false, required SW app}) :
      super(name: name, saveOnCreation: saveOnCreation, app: app);

  Character.load(FileSystemEntity file, SW app) : super.load(file, app: app);

  Character.from(Character character) :
      species = character.species,
      career = character.career,
      specializations = List.from(character.specializations),
      forcePowers = List.from(character.forcePowers),
      motivation = character.motivation,
      emotionalStr = character.emotionalStr,
      emotionalWeak = character.emotionalWeak,
      duties = List.from(character.duties),
      obligations = List.from(character.obligations),
      strainThresh = character.strainThresh,
      strainCur = character.strainCur,
      xpTot = character.xpTot,
      xpCur = character.xpCur,
      force = character.force,
      credits = character.credits,
      morality = character.morality,
      conflict = character.conflict,
      darkSide = character.darkSide,
      age = character.age,
      encumCap = character.encumCap,
      disableDuty = character.disableDuty,
      disableForce = character.disableForce,
      disableObligation = character.disableObligation,
      disableMorality = character.disableMorality,
      super.from(character){
    creatureFrom(character);
  }

  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    this.creatureLoadJson(json);
    this.species = json["species"] ?? "";
    this.career = json["career"] ?? "";
    if(json["Specializations"] != null){
      specializations = [];
      for(dynamic s in json["Specializations"])
        specializations.add(s);
    }
    if(json["Force Powers"] != null){
      this.forcePowers = [];
      for(dynamic dy in json["Force Powers"])
        this.forcePowers.add(ForcePower.fromJson(dy));
    }
    this.motivation = json["motivation"] ?? "";
    emotionalStr = json["emotional strength"] ?? "";
    emotionalWeak = json["emotional weakness"] ?? "";
    if(json["Dutys"] != null){
      this.duties = [];
      for(dynamic dy in json["Dutys"])
        this.duties.add(Duty.fromJson(dy));
    }
    if(json["Obligations"] != null){
      this.obligations  = [];
      for(dynamic dy in json["Obligations"])
        this.obligations.add(Obligation.fromJson(dy));
    }
    this.strainThresh = json["strain threshold"] ?? 0;
    this.strainCur = json["strain current"] ?? 0;
    this.xpTot = json["xp total"] ?? 0;
    this.xpCur = json["xp current"] ?? 0;
    this.force = json["force rating"] ?? 0;
    this.credits = json["credits"] ?? 0;
    this.morality = json["morality"] ?? 0;
    this.conflict = json["conflict"] ?? 0;
    this.darkSide = json["dark side"] ?? false;
    this.age = json["age"] ?? 0;
    this.encumCap = json["encumbrance capacity"] ?? 0;
    this.disableDuty = json["disable duty"] ?? false;
    this.disableForce = json["disable force"] ?? false;
    this.disableObligation = json["disable obligation"] ?? false;
    this.disableMorality = json["disable morality"] ?? false;
  }

  Map<String,dynamic> toJson() => 
    super.toJson()..addAll({
      "species": species,
      "career": career,
      "Specializations": specializations,
      "Force Powers": List.generate(forcePowers.length, (index) => forcePowers[index].toJson()),
      "motivation": motivation,
      "emotional strength": emotionalStr,
      "emotional weakness": emotionalWeak,
      "Dutys": List.generate(duties.length, (index) => duties[index].toJson()),
      "Obligations": List.generate(obligations.length, (index) => obligations[index].toJson()),
      "strain threshold": strainThresh,
      "strain current": strainCur,
      "xp total": xpTot,
      "xp current": xpCur,
      "force rating": force,
      "credits": credits,
      "morality": morality,
      "conflict": conflict,
      "dark side": darkSide,
      "age": age,
      "encumbrance capacity": encumCap,
      "disable force": disableForce,
      "disable duty": disableDuty,
      "disable obligation": disableObligation,
      "disable morality": disableMorality,
    })..addAll(creatureSaveJson());

  List<EditableContent> cardContents(BuildContext context, Function() updateList) => 
    <EditableContent>[
      EditableContent(
        key: Key("info"),
        builder: (b, refresh, state) =>
          CharacterInfo(editing: b, state: state, updateList: updateList,)
      ),
      EditableContent(
        key: Key("wound"),
        builder: (b, refresh, state) =>
          WoundStrain(editing: b, state: state),
        defaultEditingState: () => soak == 0 && woundThresh == 0 && strainThresh == 0
      ),
      EditableContent(
        key: Key("characteristics"),
        builder: (b, refresh, state) =>
          Characteristics(editing: b, state: state),
        defaultEditingState: () => charVals.every((element) => element == 0)
      ),
      EditableContent(
        key: Key("skills"),
        builder: (b, refresh, state) =>
          Skills(editing:b, refresh: refresh),
        defaultEditingState: () => skills.length == 0
      ),
      EditableContent(
        key: Key("defense"),
        builder: (b, refresh, state) =>
          Defense(editing: b, state: state)
      ),
      EditableContent(
        key: Key("weapons"),
        builder: (b, refresh, state) =>
          Weapons(editing: b, refresh: refresh),
        defaultEditingState: () => weapons.length == 0
      ),
      EditableContent(
        key: Key("critInj"),
        builder: (b, refresh, state) =>
          CriticalInjuries(editing: b, refresh: refresh),
        defaultEditingState: () => criticalInjuries.length == 0
      ),
      EditableContent(
        key: Key("special"),
        builder: (b, refresh, state) =>
          Specializations(editing: b, refresh: refresh,),
        defaultEditingState: () => specializations.length == 0
      ),
      EditableContent(
        key: Key("tal"),
        builder: (b, refresh, state) =>
          Talents(editing: b, refresh: refresh,),
        defaultEditingState: () => talents.length == 0
      ),
      if(!disableForce) EditableContent(
        key: Key("fp"),
        builder: (b, refresh, state) =>
          ForcePowers(editing: b, refresh: refresh, state: state),
        defaultEditingState: () => forcePowers.length == 0 && force == 0
      ),
      EditableContent(
        key: Key("xp"),
        builder: (b, refresh, state) =>
          XP(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(
        key: Key("inv"),
        stateful: Inventory(holder: EditableContentStatefulHolder()),
        defaultEditingState: () => inventory.length == 0
      ),
      if(!disableMorality) EditableContent(
        key: Key("morality"),
        stateful: Morality()
      ),
      if(!disableDuty) EditableContent(
        key: Key("duty"),
        builder: (b, refresh, state) =>
          Duties(editing: b, refresh: refresh),
        defaultEditingState: () => duties.length == 0
      ),
      if(!disableObligation) EditableContent(
        key: Key("obli"),
        builder: (b, refresh, state) =>
          Obligations(editing: b, refresh: refresh),
        defaultEditingState: () => obligations.length == 0
      ),
      EditableContent(
        key: Key("desc"),
        builder: (b, refresh, state) =>
          Description(editing: b, state: state),
        defaultEditingState: () => desc == ""
      )
    ];

  static Character? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Character)
      return ed;
    return null;
  }
}