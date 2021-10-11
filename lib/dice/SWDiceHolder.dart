import 'package:flutter/material.dart';
import 'package:swassistant/dice/Dice.dart';
import 'package:swassistant/dice/SWDice.dart' as SWDice;
import 'package:swassistant/ui/misc/BottomSheetTemplate.dart';
import 'package:swassistant/ui/misc/DiceSelector.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class SWDiceHolder{

  int ability, proficiency, difficulty, challenge, boost, setback, force;

  SWDiceHolder({this.ability=0, this.proficiency=0, this.difficulty=0, this.challenge=0, this.boost=0, this.setback=0, this.force=0});
  
  Dice getDice(BuildContext context){
    var dice = <Die>[];
    if(ability>0)
      dice.addAll(List.filled(ability, SWDice.ability(context)));
    if(proficiency>0)
      dice.addAll(List.filled(proficiency, SWDice.proficiency(context)));
    if(difficulty>0)
      dice.addAll(List.filled(difficulty, SWDice.difficulty(context)));
    if(challenge>0)
      dice.addAll(List.filled(challenge, SWDice.challenge(context)));
    if(boost>0)
      dice.addAll(List.filled(boost, SWDice.boost(context)));
    if(setback>0)
      dice.addAll(List.filled(setback, SWDice.setback(context)));
    if(force>0)
      dice.addAll(List.filled(force, SWDice.force(context)));
    return Dice(name: "SWDice Dice", dies: dice);
  }

  void showDialog(BuildContext context){
    Bottom(
      child: Column(
        children: List.generate(
          7, (index) =>
            DiceSelector(holder: this, type: index)
        )
      ),
      buttons: [
        TextButton(
          child: Text(AppLocalizations.of(context)!.roll),
          onPressed: (){
            Navigator.of(context).pop();
            var res = getDice(context).roll();
            res.showCombinedResults(context,
              noSuccess: ability == 0 && challenge == 0 && difficulty == 0 &&
                proficiency == 0 && boost == 0 && setback == 0
            );
          },
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: () => Navigator.of(context).pop(),
        )
      ],
    ).show(context);
  }
}