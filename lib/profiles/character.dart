import 'dart:io';

import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/items/duty.dart';
import 'package:swassistant/items/force_power.dart';
import 'package:swassistant/items/obligation.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
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

  bool useRepair = false;
  int healsToday = 0;

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
    disableDuty = json["disable duty"] ?? false;
    disableForce = json["disable force"] ?? false;
    disableObligation = json["disable obligation"] ?? false;
    disableMorality = json["disable morality"] ?? false;
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
    useRepair = json["use repair"] ?? false;
    healsToday = json["heals today"] ?? 0;
  }

  @override
  Map<String,dynamic> toJson() => {
    ...super.toJson(),
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
    "use repair": useRepair,
    "heals today": healsToday,
  }..addAll(creatureSaveJson())..removeWhere((key, value) {
    if (value is List && value.isEmpty) return true;
    return zeroValue[key] == value;
  });

  @override
  Map<String,dynamic> get zeroValue => {
    ...super.zeroValue,
    ...creatureZeroValue,
    "disable duty": false,
    "disable force": false,
    "disable obligation": false,
    "disable morality": false,
    "species": "",
    "career": "",
    "motivation": "",
    "emotional strength": "",
    "emotional weakness": "",
    "strain threshold": 0,
    "strain current": 0,
    "xp total": 0,
    "xp current": 0,
    "force rating": 0,
    "credits": 0,
    "morality": 0,
    "conflict": 0,
    "dark side": false,
    "age": 0,
    "encumbrance capacity": 0,
    "use repair": false,
    "heals today": 0,
  };

  var morKey = GlobalKey<MoralityState>();
  var woundStrainKey = GlobalKey<WoundStrainState>();
  var skillKey = GlobalKey<SkillsState>();
  var specKey = GlobalKey<SpecializationsState>();
  var talentKey = GlobalKey<TalentsState>();
  var fpKey = GlobalKey<ForcePowerState>();
  var xpKey = GlobalKey<XPState>();
  var dutyKey = GlobalKey<DutiesState>();
  var obliKey = GlobalKey<ObligationsState>();

  @override
  List<EditContent> cardContents(BuildContext context) => [
    EditContent(
      key: const Key("info"),
      contentBuilder: (b) => CharacterInfo(editing: b)
    ),
    EditContent(
      key: const Key("wound"),
      content: WoundStrain(key: woundStrainKey),
      contentKey: woundStrainKey,
      defaultEdit: () => soak == 0 && woundThresh == 0 && strainThresh == 0
    ),
    EditContent(
      key: const Key("characteristics"),
      contentBuilder: (b) => Characteristics(editing: b),
      defaultEdit: () => charVals.every((element) => element == 0)
    ),
    EditContent(
      key: const Key("skills"),
      content: Skills(key: skillKey),
      contentKey: skillKey,
      defaultEdit: () => skills.isEmpty
    ),
    EditContent(
      key: const Key("defense"),
      contentBuilder: (b) => Defense(editing: b)
    ),
    EditContent(
      key: const Key("weapons"),
      content: Weapons(key: weaponKey),
      contentKey: weaponKey,
      defaultEdit: () => weapons.isEmpty
    ),
    EditContent(
      key: const Key("critInj"),
      content: CriticalInjuries(key: injKey),
      contentKey: injKey,
      defaultEdit: () => criticalInjuries.isEmpty
    ),
    EditContent(
      key: const Key("special"),
      content: Specializations(key: specKey),
      contentKey: specKey,
      defaultEdit: () => specializations.isEmpty
    ),
    EditContent(
      key: const Key("tal"),
      content: Talents(key: talentKey),
      contentKey: talentKey,
      defaultEdit: () => talents.isEmpty
    ),
    if(!disableForce) EditContent(
      key: const Key("fp"),
      content: ForcePowers(key: fpKey),
      contentKey: fpKey,
      defaultEdit: () => forcePowers.isEmpty && force == 0
    ),
    EditContent(
      key: const Key("xp"),
      content: XP(key: xpKey),
      contentKey: xpKey,
    ),
    EditContent(
      key: const Key("inv"),
      content: Inventory(key: invKey),
      contentKey: invKey,
      defaultEdit: () => inventory.isEmpty
    ),
    if(!disableMorality) EditContent(
      key: const Key("morality"),
      content: Morality(key: morKey),
      contentKey: morKey,
    ),
    if(!disableDuty) EditContent(
      key: const Key("duty"),
      content: Duties(key: dutyKey),
      contentKey: dutyKey,
      defaultEdit: () => duties.isEmpty
    ),
    if(!disableObligation) EditContent(
      key: const Key("obli"),
      content: Obligations(key: obliKey),
      contentKey: obliKey,
      defaultEdit: () => obligations.isEmpty
    ),
    EditContent(
      key: const Key("desc"),
      contentBuilder: (b) =>
        Description(editing: b),
      defaultEdit: () => desc == ""
    )
  ];

  static Character? of(BuildContext context){
    var ed = Editable.of(context);
    if (ed is Character) return ed;
    return null;
  }
}