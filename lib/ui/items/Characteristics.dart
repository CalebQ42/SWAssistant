import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Characteristics extends StatelessWidget{

  final Editable creature;
  final SW app;
  final bool editing;

  final List<String> chars = ["Brawn:", "Agility:", "Intellect:", "Cunning:", "Willpower:", "Presence:"];

  Characteristics({this.editing, this.creature, this.app}){
    if(!(creature is Creature))
      throw("Characteristics card needs to be a creature");
  }
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(0, Theme.of(context).textTheme.headline6),
            charBuilder(1, Theme.of(context).textTheme.headline6)
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(2, Theme.of(context).textTheme.headline6),
            charBuilder(3, Theme.of(context).textTheme.headline6)
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(4, Theme.of(context).textTheme.headline6),
            charBuilder(5, Theme.of(context).textTheme.headline6)
          ],
        ),
      ],
    );
  }
  //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
  Widget charBuilder(int charNum, TextStyle style){
    return Expanded(
      child: InkResponse(
        containedInkWell: true,
        onTap:(){
          //TODO: Roll characteristic
        },
        child:Column(
          children: <Widget>[
            Text(chars[charNum]),
            EditingText(
              editing: editing,
              initialText: (creature as Creature).charVals[charNum].toString(),
              controller: (){
                var controller = TextEditingController(text: (creature as Creature).charVals[charNum].toString());
                if(controller.text == "")
                  (creature as Creature).charVals[charNum] = 0;
                else
                  (creature as Creature).charVals[charNum] = int.parse(controller.text);
                return controller;
              }(),
              style: style,
              textType: TextInputType.number,
              defaultSave: true,
              editable: creature,
              app: app
            )
          ],
        )
      )
    );
  }
}