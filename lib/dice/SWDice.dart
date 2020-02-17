import 'package:swassistant/dice/Dice.dart';
import 'package:swassistant/dice/Sides.dart';

String suc = "success";
String fai = "failure";
String adv = "advantage";
String thr = "threat";
String tri = "triumpth";
String des = "despair";
//Force die sides
String lig = "light";
String dar = "dark";

Die boost = Die.withSides("boost",[
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(adv),
    SimpleSide(suc),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:1),ComplexSidePart(name:suc,value:1)])
]);
Die setback = Die.withSides("setback",[
    SimpleSide(""),
    SimpleSide(""),
    SimpleSide(fai),
    SimpleSide(fai),
    SimpleSide(thr),
    SimpleSide(thr)
]);
Die ability = Die.withSides("ability",[
    SimpleSide(""),
    SimpleSide(adv),
    SimpleSide(adv),
    SimpleSide(suc),
    SimpleSide(suc),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:1),ComplexSidePart(name:suc,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:suc,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:adv,value:2)])
]);
Die difficulty = Die.withSides("difficulty",[
    SimpleSide(""),
    SimpleSide(thr),
    SimpleSide(thr),
    SimpleSide(thr),
    SimpleSide(fai),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:1),ComplexSidePart(name:fai,value:1)]),
    ComplexSide(parts:[ComplexSidePart(name:fai,value:2)]),
    ComplexSide(parts:[ComplexSidePart(name:thr,value:2)])
]);
Die proficiency = Die.withSides("proficiency",[
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
Die challenge = Die.withSides("challenge",[
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
Die force = Die.withSides("force",[
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