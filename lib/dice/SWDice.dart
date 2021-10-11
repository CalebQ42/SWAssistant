import 'package:flutter/material.dart';
import 'package:swassistant/dice/Dice.dart';
import 'package:swassistant/dice/Sides.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

Die ability(BuildContext context) => Die(name: AppLocalizations.of(context)!.ability, sides: [
  SimpleSide(""),
  SimpleSide(AppLocalizations.of(context)!.advantage),
  SimpleSide(AppLocalizations.of(context)!.advantage),
  SimpleSide(AppLocalizations.of(context)!.success),
  SimpleSide(AppLocalizations.of(context)!.success),
  ComplexSide(parts:[ComplexSidePart(name: AppLocalizations.of(context)!.advantage, value: 1),ComplexSidePart(name: AppLocalizations.of(context)!.success, value:1)]),
  ComplexSide(parts:[ComplexSidePart(name: AppLocalizations.of(context)!.success, value: 2)]),
  ComplexSide(parts:[ComplexSidePart(name: AppLocalizations.of(context)!.advantage, value: 2)])
]);
Die proficiency(BuildContext context) => Die(name:AppLocalizations.of(context)!.proficiency, sides: [
    SimpleSide(""),
    SimpleSide(AppLocalizations.of(context)!.success),
    SimpleSide(AppLocalizations.of(context)!.success),
    SimpleSide(AppLocalizations.of(context)!.advantage),
    SimpleSide(AppLocalizations.of(context)!.triumph),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.success,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.success,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.advantage,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.advantage,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.advantage,value:1),ComplexSidePart(name:AppLocalizations.of(context)!.success,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.advantage,value:1),ComplexSidePart(name:AppLocalizations.of(context)!.success,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.advantage,value:1),ComplexSidePart(name:AppLocalizations.of(context)!.success,value:1)]),
]);
Die difficulty(BuildContext context) => Die(name: AppLocalizations.of(context)!.difficulty, sides: [
    SimpleSide(""),
    SimpleSide(AppLocalizations.of(context)!.threat),
    SimpleSide(AppLocalizations.of(context)!.threat),
    SimpleSide(AppLocalizations.of(context)!.threat),
    SimpleSide(AppLocalizations.of(context)!.failure),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.threat,value:1),ComplexSidePart(name:AppLocalizations.of(context)!.failure,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.failure,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.threat,value:2)])
]);
Die challenge(BuildContext context) => Die(name: AppLocalizations.of(context)!.challenge, sides: [
    SimpleSide(""),
    SimpleSide(AppLocalizations.of(context)!.failure),
    SimpleSide(AppLocalizations.of(context)!.failure),
    SimpleSide(AppLocalizations.of(context)!.threat),
    SimpleSide(AppLocalizations.of(context)!.threat),
    SimpleSide(AppLocalizations.of(context)!.despair),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.failure,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.failure,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.threat,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.threat,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.threat,value:1),ComplexSidePart(name:AppLocalizations.of(context)!.failure,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.threat,value:1),ComplexSidePart(name:AppLocalizations.of(context)!.failure,value:1)]),
]);
Die boost(BuildContext context) => Die(name: AppLocalizations.of(context)!.boost, sides: [
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(AppLocalizations.of(context)!.advantage),
    SimpleSide(AppLocalizations.of(context)!.success),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.advantage,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:AppLocalizations.of(context)!.advantage,value:1),ComplexSidePart(name:AppLocalizations.of(context)!.success,value:1)])
]);
Die setback(BuildContext context) => Die(name: AppLocalizations.of(context)!.setback, sides: [
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(AppLocalizations.of(context)!.failure),
    SimpleSide(AppLocalizations.of(context)!.failure),
    SimpleSide(AppLocalizations.of(context)!.threat),
    SimpleSide(AppLocalizations.of(context)!.threat)
]);
Die force(BuildContext context) => Die(name: AppLocalizations.of(context)!.force, sides: [
    SimpleSide(AppLocalizations.of(context)!.darkSide),
    SimpleSide(AppLocalizations.of(context)!.darkSide),
    SimpleSide(AppLocalizations.of(context)!.darkSide),
    SimpleSide(AppLocalizations.of(context)!.darkSide),
    SimpleSide(AppLocalizations.of(context)!.darkSide),
    SimpleSide(AppLocalizations.of(context)!.darkSide),
    SimpleSide(AppLocalizations.of(context)!.lightSide),
    SimpleSide(AppLocalizations.of(context)!.lightSide),
    ComplexSide(parts: [ComplexSidePart(name:AppLocalizations.of(context)!.lightSide,value:2)]),
    ComplexSide(parts: [ComplexSidePart(name:AppLocalizations.of(context)!.lightSide,value:2)]),
    ComplexSide(parts: [ComplexSidePart(name:AppLocalizations.of(context)!.lightSide,value:2)]),
    ComplexSide(parts: [ComplexSidePart(name:AppLocalizations.of(context)!.darkSide,value:2)]),
]);