
//Displays a dialog to roll a specified number of SW Dice
import 'package:flutter/material.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/dice/SWDice.dart' as SWDice;

class SWDiceDialog extends AlertDialog{
  final SWDiceHolder holder;
  SWDiceDialog({@required this.holder}){
    AlertDialog(
      content: Column(
        children: List.generate(SWDice.SWDice.length, (index){
          //TODO generate up down list for dice
        }),
      ),
      actions: <Widget>[
        FlatButton(
          child: Text("Roll"),
          onPressed: (){
            //TODO: on roll press
          },
        ),
        FlatButton(
          child: Text("Cancel"),
          onPressed: (){
            //TODO: on cancel press
          },
        )
      ],
    );
  }
  @override
  Widget build(BuildContext context){
  }
}