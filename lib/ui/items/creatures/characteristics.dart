import 'package:flutter/material.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class Characteristics extends StatefulWidget{

  const Characteristics({Key? key}) : super(key: key);

  @override
  State<Characteristics> createState() => CharacteristicsState();
}

class CharacteristicsState extends State<Characteristics> with StatefulCard {

  bool edit = false;

  @override
  set editing(bool b) => setState(() => edit = b);

  @override
  bool get defaultEdit => Creature.of(context)!.charVals.every((element) => element == 0);


  List<TextEditingController>? charValControllers;

  @override
  Widget build(BuildContext context) {
    if(charValControllers == null){
      charValControllers = Creature.of(context)!.charVals.map((element) => TextEditingController(text: element.toString())).toList();
      for(var i = 0; i < charValControllers!.length; i++){
        charValControllers![i].addListener(() => Creature.of(context)!.charVals[i] = int.tryParse(charValControllers![i].text) ?? 0);
      }
    }
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(0, Theme.of(context).textTheme.titleLarge!, context),
            charBuilder(1, Theme.of(context).textTheme.titleLarge!, context)
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(2, Theme.of(context).textTheme.titleLarge!, context),
            charBuilder(3, Theme.of(context).textTheme.titleLarge!, context)
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            charBuilder(4, Theme.of(context).textTheme.titleLarge!, context),
            charBuilder(5, Theme.of(context).textTheme.titleLarge!, context)
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
        editing: edit,
        initialText: creature.charVals[charNum].toString(),
        controller: charValControllers![charNum],
        fieldAlign: TextAlign.center,
        style: style,
        textType: TextInputType.number,
        defaultSave: true,
        title: Creature.characteristics(context)[charNum]
      )
    );
  }
}