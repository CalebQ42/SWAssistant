import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/dice/SWDice.dart' as SWDice;
import 'package:swassistant/ui/UpDownStat.dart';

class DiceSelector extends StatelessWidget{

  final SWDiceHolder holder;
  final int type;
  final bool small;

  DiceSelector({required this.holder, required this.type, this.small = true});

  @override
  Widget build(BuildContext context){
    Color? background;
    Color? text;
    if (SW.of(context).getPreference(preferences.colorDice, true)) {
      switch(type){
        case 0:
          background = Colors.green;
          text = Colors.white;
          break;
        case 1:
          background = Colors.yellow;
          text = Colors.black;
          break;
        case 2:
          background = Colors.purple;
          text = Colors.white;
          break;
        case 3:
          background = Colors.red;
          text = Colors.white;
          break;
        case 4:
          background = Colors.lightBlue;
          text = Colors.black;
          break;
        case 5:
          background = Colors.black;
          text = Colors.white;
          break;
        case 6:
          background = Colors.white;
          text = Colors.black;
          break;
      }
    }
    var title = Text(
      SWDice.SWDice[type],
      textAlign: TextAlign.center,
      style: small ? Theme.of(context).textTheme.bodyText1?.copyWith(color: text) : Theme.of(context).textTheme.headline6?.copyWith(color: text),
    );
    var upDown = UpDownStat(
      getValue: () => getValue(),
      onUpPressed: () => up(),
      onDownPressed: () => down(),
      textColor: text,
    );
    Widget body;
    if (small)
      body = Row(
        children: [
          Expanded(child: title),
          Expanded(child: upDown),
        ],
      );
    else
      body = Column(
        children: [
          title, upDown
        ],
      );
    body = Padding(
      padding: EdgeInsets.all(5),
      child: body,
    );
    if (small){
      body = Container(
        child: body,
        color: background,
      );
    }
    return small ? body : Card(
      margin: EdgeInsets.all(4),
      color: background,
      child: body,
    );
  }

  int getValue(){
    switch(type){
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
      case 6:
        return holder.force;
    }
    return -1;
  }

  void up(){
    switch(type){
      case 0:
        holder.ability++;
        return;
      case 1:
        holder.proficiency++;
        return;
      case 2:
        holder.difficulty++;
        return;
      case 3:
        holder.challenge++;
        return;
      case 4:
        holder.boost++;
        return;
      case 5:
        holder.setback++;
        return;
      case 6:
        holder.force++;
        return;
    }
  }

  void down(){
    switch(type){
      case 0:
        holder.ability--;
        return;
      case 1:
        holder.proficiency--;
        return;
      case 2:
        holder.difficulty--;
        return;
      case 3:
        holder.challenge--;
        return;
      case 4:
        holder.boost--;
        return;
      case 5:
        holder.setback--;
        return;
      case 6:
        holder.force--;
        return;
    }
  }
}