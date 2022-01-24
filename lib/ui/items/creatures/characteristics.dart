import 'package:flutter/material.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/ui/editable_common.dart';

class Characteristics extends StatelessWidget{

  final bool editing;

  const Characteristics({required this.editing, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(0, Theme.of(context).textTheme.headline6!, context),
            charBuilder(1, Theme.of(context).textTheme.headline6!, context)
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(2, Theme.of(context).textTheme.headline6!, context),
            charBuilder(3, Theme.of(context).textTheme.headline6!, context)
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(4, Theme.of(context).textTheme.headline6!, context),
            charBuilder(5, Theme.of(context).textTheme.headline6!, context)
          ],
        ),
      ],
    );
  }

  //0-Brawn,1-Agility,2-Intellect,3-Cunning,4-Willpower,5-Presence
  Widget charBuilder(int charNum, TextStyle style, BuildContext context){
    var creature = Creature.of(context);
    if (creature == null) throw "Characteristics card used on non Creature";
    return Expanded(
      child: EditingText(
        onTap:() =>
          SWDiceHolder(ability: creature.charVals[charNum]).showDialog(context),
        editing: editing,
        initialText: creature.charVals[charNum].toString(),
        controller: (){
          var controller = TextEditingController(text: creature.charVals[charNum].toString());
          controller.addListener(() =>
            creature.charVals[charNum] = int.tryParse(controller.text) ?? 0
          );
          return controller;
        }(),
        fieldAlign: TextAlign.center,
        style: style,
        textType: TextInputType.number,
        defaultSave: true,
        title: Creature.characteristics(context)[charNum]
      )
    );
  }
}