import 'package:swassistant/dice/Dice.dart';
import 'package:swassistant/dice/SWDice.dart' as SW;

class SWDiceHolder{
    int ability, proficiency, difficulty, challenge, boost, setback, force;
    SWDiceHolder({this.ability=0, this.proficiency=0, this.difficulty=0, this.challenge=0, this.boost=0, this.setback=0, this.force=0});
    Dice getDice(){
      var dice = List<Die>();
      if(ability>0)
        dice.addAll(List.filled(ability, SW.ability));
      if(proficiency>0)
        dice.addAll(List.filled(proficiency, SW.proficiency));
      if(difficulty>0)
        dice.addAll(List.filled(difficulty, SW.difficulty));
      if(challenge>0)
        dice.addAll(List.filled(challenge, SW.challenge));
      if(boost>0)
        dice.addAll(List.filled(boost, SW.boost));
      if(setback>0)
        dice.addAll(List.filled(setback, SW.setback));
      if(force>0)
        dice.addAll(List.filled(force, SW.force));
      return Dice(name: "SW Dice", dies: dice);
    }
}