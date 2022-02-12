import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/dice/dice.dart';
import 'package:swassistant/dice/dice_results.dart';
import 'package:swassistant/dice/swdice.dart' as swdice;
import 'package:swassistant/ui/misc/bottom.dart';
import 'package:swassistant/ui/misc/dice_selector.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/utils/json_savable.dart';

class SWDiceHolder extends JsonSavable{

  int ability, proficiency, difficulty, challenge, boost, setback, force;
  WeaponPack? weaponPack;

  SWDiceHolder({this.ability=0, this.proficiency=0, this.difficulty=0, this.challenge=0, this.boost=0, this.setback=0, this.force=0, this.weaponPack});
  SWDiceHolder.fromJson(Map<String,dynamic> json) :
    ability = json["ability"] ?? 0,
    proficiency = json["proficiency"] ?? 0,
    difficulty = json["difficulty"] ?? 0,
    challenge = json["challenge"] ?? 0,
    boost = json["boost"] ?? 0,
    setback = json["setback"] ?? 0,
    force = json["force"] ?? 0;

  @override
  Map<String, dynamic> toJson() => {
    "ability" : ability,
    "proficiency" : proficiency,
    "difficulty" : difficulty,
    "challenge" : challenge,
    "boost" : boost,
    "setback" : setback,
    "force" : setback
  }..removeWhere((key, value) => zeroValue[key] == value);

  @override
  Map<String, dynamic> get zeroValue => {
    "ability" : 0,
    "proficiency" : 0,
    "difficulty" : 0,
    "challenge" : 0,
    "boost" : 0,
    "setback" : 0,
    "force" : 0
  };

  void addDice(SWDiceHolder toAdd){
    ability += toAdd.ability;
    proficiency += toAdd.proficiency;
    difficulty += toAdd.difficulty;
    challenge += toAdd.challenge;
    boost += toAdd.boost;
    setback += toAdd.setback;
    force += toAdd.force;
  }
  
  Dice getDice(BuildContext context){
    var dice = <Die>[];
    if(ability>0){
      dice.addAll(List.filled(ability, swdice.ability(context)));
    }
    if(proficiency>0){
      dice.addAll(List.filled(proficiency, swdice.proficiency(context)));
    }
    if(difficulty>0){
      dice.addAll(List.filled(difficulty, swdice.difficulty(context)));
    }
    if(challenge>0){
      dice.addAll(List.filled(challenge, swdice.challenge(context)));
    }
    if(boost>0){
      dice.addAll(List.filled(boost, swdice.boost(context)));
    }
    if(setback>0){
      dice.addAll(List.filled(setback, swdice.setback(context)));
    }
    if(force>0){
      dice.addAll(List.filled(force, swdice.force(context)));
    }
    return Dice(name: "swdice Dice", dies: dice);
  }

  void showDialog(BuildContext context, {bool showInstant = false}){
    var bot = Bottom(
      child: (context) => _DiceDialog(holder: this, showInstant: showInstant),
      buttons: (context) => [
        TextButton(
          child: Text((weaponPack == null) ? AppLocalizations.of(context)!.roll : AppLocalizations.of(context)!.fire),
          onPressed: (){
            Navigator.of(context).pop();
            var res = getDice(context).roll();
            res.showCombinedResults(context,
              noSuccess: ability == 0 && challenge == 0 && difficulty == 0 &&
                proficiency == 0 && boost == 0 && setback == 0, weaponPack: weaponPack
            );
          },
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: () => Navigator.of(context).pop(),
        )],
    );
    return bot.show(context);
  }
}

class _DiceDialog extends StatefulWidget{

  final bool showInstant;
  final SWDiceHolder holder;

  const _DiceDialog({Key? key, required this.holder, this.showInstant = false}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _DiceDialogState();
}

class _DiceDialogState extends State<_DiceDialog>{

  int? d10Result;
  int? d100Result;

  @override
  Widget build(BuildContext context) =>
    Column(
      children: List.generate(
        7, (index) =>
          DiceSelector(holder: widget.holder, type: index)
      )..addAll([
        if(widget.showInstant) Padding(
          padding: const EdgeInsets.all(15),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children:[
              Text(
                "d10",
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.headline6,
              ),
              Row(
                children: [
                  Expanded(child: TextButton(
                    child: Text(AppLocalizations.of(context)!.roll),
                    onPressed: () => setState(() => d10Result = Random().nextInt(10) + 1),
                  )),
                  Expanded(child: Text(
                    d10Result?.toString() ?? "",
                    textAlign: TextAlign.center,
                  ))
                ]
              )
            ]
          )
        ),
        if(widget.showInstant) Padding(
          padding: const EdgeInsets.all(15),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children:[
              Text(
                "d100",
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.headline6,
              ),
              Row(
                children: [
                  Expanded(child: TextButton(
                    child: Text(AppLocalizations.of(context)!.roll),
                    onPressed: () => setState(() => d100Result = Random().nextInt(100) + 1),
                  )),
                  Expanded(child: Text(
                    d100Result?.toString() ?? "",
                    textAlign: TextAlign.center,
                  ))
                ]
              )
            ]
          )
        )
      ])
    );
}