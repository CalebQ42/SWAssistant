import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/ui/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
class DiceSelector extends StatelessWidget{

  final SWDiceHolder holder;
  final int type;
  final bool small;

  const DiceSelector({required this.holder, required this.type, this.small = true, Key? key}) : super(key: key);

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
      getText(context),
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
    if (small){
      body = Row(
        children: [
          Expanded(child: title),
          Expanded(child: upDown),
        ],
      );
    }else{
      body = Column(
        children: [
          title, upDown
        ],
      );
    }
    body = Padding(
      padding: const EdgeInsets.all(5),
      child: body,
    );
    if (small){
      body = Container(
        child: body,
        color: background,
      );
    }
    return small ? body : Card(
      margin: const EdgeInsets.all(4),
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

  String getText(BuildContext context){
    switch(type){
      case 0:
        return AppLocalizations.of(context)!.ability;
      case 1:
        return AppLocalizations.of(context)!.proficiency;
      case 2:
        return AppLocalizations.of(context)!.difficulty;
      case 3:
        return AppLocalizations.of(context)!.challenge;
      case 4:
        return AppLocalizations.of(context)!.boost;
      case 5:
        return AppLocalizations.of(context)!.setback;
      case 6:
        return AppLocalizations.of(context)!.force;
    }
    return "";
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