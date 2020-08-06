
//Displays a dialog to roll a specified number of SW Dice
import 'package:flutter/material.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/dice/SWDice.dart' as SWDice;
import 'package:swassistant/ui/items/UpDownStat.dart';

class SWDiceDialog extends AlertDialog{
  final SWDiceHolder holder;
  final BuildContext context;

  SWDiceDialog({@required this.holder, @required this.context}) : super(
    content: SingleChildScrollView(
      child: Column(
        children: List.generate(SWDice.SWDice.length, (index) => Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Expanded(
              child:Text(SWDice.SWDice[index])
            ),
            Expanded(
              child:UpDownStat(
                onUpPressed: (){
                  switch(index){
                    case 0:
                      holder.ability++;
                      break;
                    case 1:
                      holder.proficiency++;
                      break;
                    case 2:
                      holder.difficulty++;
                      break;
                    case 3:
                      holder.challenge++;
                      break;
                    case 4:
                      holder.boost++;
                      break;
                    case 5:
                      holder.setback++;
                      break;
                    default:
                      holder.force++;
                  }
                },
                onDownPressed: (){
                  switch(index){
                    case 0:
                      holder.ability--;
                      break;
                    case 1:
                      holder.proficiency--;
                      break;
                    case 2:
                      holder.difficulty--;
                      break;
                    case 3:
                      holder.challenge--;
                      break;
                    case 4:
                      holder.boost--;
                      break;
                    case 5:
                      holder.setback--;
                      break;
                    default:
                      holder.force--;
                  }
                },
                getValue: (){
                  switch(index){
                    case 0:
                      return holder.ability;
                    case 1:
                      return holder.proficiency;
                    case 2:
                      return holder.difficulty;
                    case 3:
                      return holder.challenge;
                    case 4:
                      return holder.boost;
                    case 5:
                      return holder.setback;
                    default:
                      return holder.force;
                  }
                },
                getMin: ()=>0,
              )
            )
          ],
        )),
      )
    ),
    actions: <Widget>[
      FlatButton(
        child: Text("Roll"),
        onPressed: (){
          Navigator.of(context).pop();
          print(SWDice.ability);
          var res = holder.getDice().roll();
          print(res);
          print(res.resList);
          res.showResultDialog(context:context);
        },
      ),
      FlatButton(
        child: Text("Cancel"),
        onPressed: () => Navigator.of(context).pop(),
      )
    ],
  );
}