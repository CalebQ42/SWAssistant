import 'package:flutter/material.dart';
import 'package:swassistant/dice/dice.dart';
import 'package:swassistant/dice/sides.dart';
import 'package:swassistant/sw.dart';

Die ability(BuildContext context) => Die(name: SW.of(context).locale.ability, sides: [
  SimpleSide(""),
  SimpleSide(SW.of(context).locale.advantage),
  SimpleSide(SW.of(context).locale.advantage),
  SimpleSide(SW.of(context).locale.success),
  SimpleSide(SW.of(context).locale.success),
  ComplexSide(parts:[ComplexSidePart(name: SW.of(context).locale.advantage, value: 1),ComplexSidePart(name: SW.of(context).locale.success, value:1)]),
  ComplexSide(parts:[ComplexSidePart(name: SW.of(context).locale.success, value: 2)]),
  ComplexSide(parts:[ComplexSidePart(name: SW.of(context).locale.advantage, value: 2)])
]);
Die proficiency(BuildContext context) => Die(name:SW.of(context).locale.proficiency, sides: [
    SimpleSide(""),
    SimpleSide(SW.of(context).locale.success),
    SimpleSide(SW.of(context).locale.success),
    SimpleSide(SW.of(context).locale.advantage),
    SimpleSide(SW.of(context).locale.triumph),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.success,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.success,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.advantage,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.advantage,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.advantage,value:1),ComplexSidePart(name:SW.of(context).locale.success,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.advantage,value:1),ComplexSidePart(name:SW.of(context).locale.success,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.advantage,value:1),ComplexSidePart(name:SW.of(context).locale.success,value:1)]),
]);
Die difficulty(BuildContext context) => Die(name: SW.of(context).locale.difficulty, sides: [
    SimpleSide(""),
    SimpleSide(SW.of(context).locale.threat),
    SimpleSide(SW.of(context).locale.threat),
    SimpleSide(SW.of(context).locale.threat),
    SimpleSide(SW.of(context).locale.failure),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.threat,value:1),ComplexSidePart(name:SW.of(context).locale.failure,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.failure,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.threat,value:2)])
]);
Die challenge(BuildContext context) => Die(name: SW.of(context).locale.challenge, sides: [
    SimpleSide(""),
    SimpleSide(SW.of(context).locale.failure),
    SimpleSide(SW.of(context).locale.failure),
    SimpleSide(SW.of(context).locale.threat),
    SimpleSide(SW.of(context).locale.threat),
    SimpleSide(SW.of(context).locale.despair),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.failure,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.failure,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.threat,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.threat,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.threat,value:1),ComplexSidePart(name:SW.of(context).locale.failure,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.threat,value:1),ComplexSidePart(name:SW.of(context).locale.failure,value:1)]),
]);
Die boost(BuildContext context) => Die(name: SW.of(context).locale.boost, sides: [
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(SW.of(context).locale.advantage),
    SimpleSide(SW.of(context).locale.success),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.advantage,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:SW.of(context).locale.advantage,value:1),ComplexSidePart(name:SW.of(context).locale.success,value:1)])
]);
Die setback(BuildContext context) => Die(name: SW.of(context).locale.setback, sides: [
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(SW.of(context).locale.failure),
    SimpleSide(SW.of(context).locale.failure),
    SimpleSide(SW.of(context).locale.threat),
    SimpleSide(SW.of(context).locale.threat)
]);
Die force(BuildContext context) => Die(name: SW.of(context).locale.force, sides: [
    SimpleSide(SW.of(context).locale.darkSide),
    SimpleSide(SW.of(context).locale.darkSide),
    SimpleSide(SW.of(context).locale.darkSide),
    SimpleSide(SW.of(context).locale.darkSide),
    SimpleSide(SW.of(context).locale.darkSide),
    SimpleSide(SW.of(context).locale.darkSide),
    SimpleSide(SW.of(context).locale.lightSide),
    SimpleSide(SW.of(context).locale.lightSide),
    ComplexSide(parts: [ComplexSidePart(name:SW.of(context).locale.lightSide,value:2)]),
    ComplexSide(parts: [ComplexSidePart(name:SW.of(context).locale.lightSide,value:2)]),
    ComplexSide(parts: [ComplexSidePart(name:SW.of(context).locale.lightSide,value:2)]),
    ComplexSide(parts: [ComplexSidePart(name:SW.of(context).locale.darkSide,value:2)]),
]);

String getName(BuildContext context, int index) {
  switch(index){
    case 0:
      return SW.of(context).locale.ability;
    case 1:
      return SW.of(context).locale.proficiency;
    case 2:
      return SW.of(context).locale.difficulty;
    case 3:
      return SW.of(context).locale.challenge;
    case 4:
      return SW.of(context).locale.boost;
    case 5:
      return SW.of(context).locale.setback;
    default:
      return SW.of(context).locale.force;
  }
}