import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/items/duty.dart';
import 'package:swassistant/items/force_power.dart';
import 'package:swassistant/items/obligation.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:swassistant/ui/items/characters/duties.dart';
import 'package:swassistant/ui/items/characters/morality.dart';
import 'package:swassistant/ui/items/characters/obligations.dart';
import 'package:swassistant/ui/items/characters/xp.dart';
import 'package:swassistant/ui/items/characters/force_powers.dart';
import 'package:swassistant/ui/items/characters/specializations.dart';
import 'package:swassistant/ui/items/creatures/talents.dart';
import 'package:swassistant/ui/items/editable/critical_injuries.dart';
import 'package:swassistant/ui/items/editable/description.dart';
import 'package:swassistant/ui/items/editable/weapons.dart';
import 'package:swassistant/ui/items/creatures/characteristics.dart';
import 'package:swassistant/ui/items/characters/character_info.dart';
import 'package:swassistant/ui/items/characters/wound_strain.dart';
import 'package:swassistant/ui/items/creatures/defense.dart';
import 'package:swassistant/ui/items/editable/inventory.dart';
import 'package:swassistant/ui/items/creatures/skills.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import 'utils/creature.dart';

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

  @override
  String get fileExtension => ".swcharacter";
  @override
  int get cardNum {
    var out = 16;
    if(disableForce) out--;
    if(disableMorality) out--;
    if(disableDuty) out--;
    if(disableObligation) out--;
    return out;
  }
  
  @override
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

  @override
  void loadJson(Map<String,dynamic> json){
    super.loadJson(json);
    creatureLoadJson(json);
    species = json["species"] ?? "";
    career = json["career"] ?? "";
    if(json["Specializations"] != null){
      specializations = [];
      for(dynamic s in json["Specializations"]){
        specializations.add(s);
      }
    }
    if(json["Force Powers"] != null){
      forcePowers = [];
      for(dynamic dy in json["Force Powers"]){
        forcePowers.add(ForcePower.fromJson(dy));
      }
    }
    motivation = json["motivation"] ?? "";
    emotionalStr = json["emotional strength"] ?? "";
    emotionalWeak = json["emotional weakness"] ?? "";
    if(json["Dutys"] != null){
      duties = [];
      for(dynamic dy in json["Dutys"]){
        duties.add(Duty.fromJson(dy));
      }
    }
    if(json["Obligations"] != null){
      obligations  = [];
      for(dynamic dy in json["Obligations"]){
        obligations.add(Obligation.fromJson(dy));
      }
    }
    strainThresh = json["strain threshold"] ?? 0;
    strainCur = json["strain current"] ?? 0;
    xpTot = json["xp total"] ?? 0;
    xpCur = json["xp current"] ?? 0;
    force = json["force rating"] ?? 0;
    credits = json["credits"] ?? 0;
    morality = json["morality"] ?? 0;
    conflict = json["conflict"] ?? 0;
    darkSide = json["dark side"] ?? false;
    age = json["age"] ?? 0;
    encumCap = json["encumbrance capacity"] ?? 0;
    disableDuty = json["disable duty"] ?? false;
    disableForce = json["disable force"] ?? false;
    disableObligation = json["disable obligation"] ?? false;
    disableMorality = json["disable morality"] ?? false;
  }

  @override
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

  @override
  List<EditableContent> cardContents(BuildContext context, Function() listUpdate) => 
    <EditableContent>[
      EditableContent(
        key: const Key("info"),
        builder: (b, refresh, state) =>
          CharacterInfo(editing: b, state: state, updateList: listUpdate,)
      ),
      EditableContent(
        key: const Key("wound"),
        builder: (b, refresh, state) =>
          WoundStrain(editing: b, state: state),
        defaultEditingState: () => soak == 0 && woundThresh == 0 && strainThresh == 0
      ),
      EditableContent(
        key: const Key("characteristics"),
        builder: (b, refresh, state) =>
          Characteristics(editing: b, state: state),
        defaultEditingState: () => charVals.every((element) => element == 0)
      ),
      EditableContent(
        key: const Key("skills"),
        builder: (b, refresh, state) =>
          Skills(editing:b, refresh: refresh),
        defaultEditingState: () => skills.isEmpty
      ),
      EditableContent(
        key: const Key("defense"),
        builder: (b, refresh, state) =>
          Defense(editing: b, state: state)
      ),
      EditableContent(
        key: const Key("weapons"),
        builder: (b, refresh, state) =>
          Weapons(editing: b, refresh: refresh),
        defaultEditingState: () => weapons.isEmpty
      ),
      EditableContent(
        key: const Key("critInj"),
        builder: (b, refresh, state) =>
          CriticalInjuries(editing: b, refresh: refresh),
        defaultEditingState: () => criticalInjuries.isEmpty
      ),
      EditableContent(
        key: const Key("special"),
        builder: (b, refresh, state) =>
          Specializations(editing: b, refresh: refresh,),
        defaultEditingState: () => specializations.isEmpty
      ),
      EditableContent(
        key: const Key("tal"),
        builder: (b, refresh, state) =>
          Talents(editing: b, refresh: refresh,),
        defaultEditingState: () => talents.isEmpty
      ),
      if(!disableForce) EditableContent(
        key: const Key("fp"),
        builder: (b, refresh, state) =>
          ForcePowers(editing: b, refresh: refresh, state: state),
        defaultEditingState: () => forcePowers.isEmpty && force == 0
      ),
      EditableContent(
        key: const Key("xp"),
        builder: (b, refresh, state) =>
          XP(editing: b, refresh: refresh, state: state)
      ),
      EditableContent(
        key: const Key("inv"),
        stateful: Inventory(holder: EditableContentStatefulHolder()),
        defaultEditingState: () => inventory.isEmpty
      ),
      if(!disableMorality) EditableContent(
        key: const Key("morality"),
        stateful: Morality()
      ),
      if(!disableDuty) EditableContent(
        key: const Key("duty"),
        builder: (b, refresh, state) =>
          Duties(editing: b, refresh: refresh),
        defaultEditingState: () => duties.isEmpty
      ),
      if(!disableObligation) EditableContent(
        key: const Key("obli"),
        builder: (b, refresh, state) =>
          Obligations(editing: b, refresh: refresh),
        defaultEditingState: () => obligations.isEmpty
      ),
      EditableContent(
        key: const Key("desc"),
        builder: (b, refresh, state) =>
          Description(editing: b, state: state),
        defaultEditingState: () => desc == ""
      )
    ];

  static Character? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Character) return ed;
    return null;
  }
}