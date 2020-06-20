import 'package:swassistant/dice/Dice.dart';
import 'package:swassistant/dice/Sides.dart';

const String suc = "success";
const String fai = "failure";
const String adv = "advantage";
const String thr = "threat";
const String tri = "triumpth";
const String des = "despair";
//Force die sides
const String lig = "light";
const String dar = "dark";

const List<String> SWDice = ["Ability", "Proficiency", "Difficulty", "Challenge", "Boost", "Setback", "Force"];

Die boost = Die.withSides(SWDice[4],[
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(adv),
    SimpleSide(suc),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:1),ComplexSidePart(name:suc,value:1)])
]);
Die setback = Die.withSides(SWDice[5],[
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(fai),
    SimpleSide(fai),
    SimpleSide(thr),
    SimpleSide(thr)
]);
Die ability = Die.withSides(SWDice[0],[
    SimpleSide(""),
    SimpleSide(adv),
    SimpleSide(adv),
    SimpleSide(suc),
    SimpleSide(suc),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:1),ComplexSidePart(name:suc,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:suc,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:2)])
]);
Die difficulty = Die.withSides(SWDice[2],[
    SimpleSide(""),
    SimpleSide(thr),
    SimpleSide(thr),
    SimpleSide(thr),
    SimpleSide(fai),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:1),ComplexSidePart(name:fai,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:fai,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:2)])
]);
Die proficiency = Die.withSides(SWDice[1],[
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
Die challenge = Die.withSides(SWDice[3],[
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
Die force = Die.withSides(SWDice[6],[
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