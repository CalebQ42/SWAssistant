import 'package:swassistant/dice/Dice.dart';
import 'package:swassistant/dice/Sides.dart';

const String suc = "Success";
const String fai = "Failure";
const String adv = "Advantage";
const String thr = "Threat";
const String tri = "Triumpth";
const String des = "Despair";
//Force die sides
const String lig = "Light Side";
const String dar = "Dark Side";

const List<String> SWDice = ["Ability", "Proficiency", "Difficulty", "Challenge", "Boost", "Setback", "Force"];

Die ability = Die(name: SWDice[0], sides: [
    SimpleSide(""),
    SimpleSide(adv),
    SimpleSide(adv),
    SimpleSide(suc),
    SimpleSide(suc),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:1),ComplexSidePart(name:suc,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:suc,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:2)])
]);
Die proficiency = Die(name: SWDice[1], sides: [
    SimpleSide(""),
    SimpleSide(suc),
    SimpleSide(suc),
    SimpleSide(adv),
    SimpleSide(tri),
    ComplexSide(parts:[ComplexSidePart(name:suc,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:suc,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:1),ComplexSidePart(name:suc,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:1),ComplexSidePart(name:suc,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:1),ComplexSidePart(name:suc,value:1)]),
]);
Die difficulty = Die(name: SWDice[2], sides: [
    SimpleSide(""),
    SimpleSide(thr),
    SimpleSide(thr),
    SimpleSide(thr),
    SimpleSide(fai),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:1),ComplexSidePart(name:fai,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:fai,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:2)])
]);
Die challenge = Die(name: SWDice[3], sides: [
    SimpleSide(""),
    SimpleSide(fai),
    SimpleSide(fai),
    SimpleSide(thr),
    SimpleSide(thr),
    SimpleSide(des),
    ComplexSide(parts:[ComplexSidePart(name:fai,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:fai,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:1),ComplexSidePart(name:fai,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:1),ComplexSidePart(name:fai,value:1)]),
]);
Die boost = Die(name: SWDice[4], sides: [
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(adv),
    SimpleSide(suc),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:1),ComplexSidePart(name:suc,value:1)])
]);
Die setback = Die(name: SWDice[5], sides: [
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(fai),
    SimpleSide(fai),
    SimpleSide(thr),
    SimpleSide(thr)
]);
Die force = Die(name: SWDice[6], sides: [
    SimpleSide(dar),
    SimpleSide(dar),
    SimpleSide(dar),
    SimpleSide(dar),
    SimpleSide(dar),
    SimpleSide(dar),
    SimpleSide(lig),
    SimpleSide(lig),
    ComplexSide(parts: [ComplexSidePart(name:lig,value:2)]),
    ComplexSide(parts: [ComplexSidePart(name:lig,value:2)]),
    ComplexSide(parts: [ComplexSidePart(name:lig,value:2)]),
    ComplexSide(parts: [ComplexSidePart(name:dar,value:2)]),
]);