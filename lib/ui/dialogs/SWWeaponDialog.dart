
//Displays a dialog to roll a specified number of SW Dice
import 'package:flutter/material.dart';
import 'package:swassistant/dice/DiceResults.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/dice/SWDice.dart' as SWDice;
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/ui/UpDownStat.dart';

class SWWeaponDialog extends StatelessWidget{
  final SWDiceHolder holder;
  final BuildContext context;
  final Weapon weapon;

  SWWeaponDialog({@required this.holder, @required this.context, @required this.weapon});

  @override
  Widget build(BuildContext context) =>
    Column(
      children: List.generate(
        SWDice.SWDice.length, (index) =>
          Row(
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
        )
      )..add(ButtonBar(
        children: [
          FlatButton(
            child: Text("Fire!"),
            onPressed: (){
              Navigator.of(context).pop();
              var res = holder.getDice().roll();
              res.showResultDialog(context:context);
            },
          ),
          FlatButton(
            child: Text("Cancel"),
            onPressed: () => Navigator.of(context).pop(),
          )
        ],
      )
    ));
}

class _WeaponResults extends StatelessWidget{
  final Weapon weapon;
  final DiceResults results;

  _WeaponResults({this.weapon, this.results});

  @override
  Widget build(BuildContext context){} //TODO
}

class _WeaponResultsEdit extends StatelessWidget{
  final Weapon weapon;
  final DiceResults results;

  _WeaponResultsEdit({this.weapon, this.results});

  @override
  Widget build(BuildContext context) {
    //TODO
  }
}