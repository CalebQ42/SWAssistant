import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/ui/Common.dart';
import 'package:swassistant/ui/UpDownStat.dart';
import 'package:swassistant/Preferences.dart' as preferences;

//TODO
class DiceRoller extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    double width = min(MediaQuery.of(context).size.height, 250);
    int rows = (MediaQuery.of(context).size.width / width).floor();
    width = MediaQuery.of(context).size.width / rows;
    var holder = SWDiceHolder();
    return Scaffold(
      appBar: SWAppBar(
        title: Text("Dice")
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Container(height: 10),
            Wrap(
              alignment: WrapAlignment.spaceEvenly,
              children: [
                _DiceCard(
                  cardColor: Colors.green,
                  textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.white,
                  title: "Ability",
                  upDown: UpDownStat(
                    getValue: () => holder.ability,
                    onDownPressed: () => holder.ability--,
                    onUpPressed: () => holder.ability++,
                    textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.white,
                  ),
                  width: width
                ),
                _DiceCard(
                  cardColor: Colors.yellow,
                  textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.black,
                  title: "Proficiency",
                  upDown: UpDownStat(
                    getValue: () => holder.proficiency,
                    onDownPressed: () => holder.proficiency--,
                    onUpPressed: () => holder.proficiency++,
                    textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.black,
                  ),
                  width: width
                ),
                _DiceCard(
                  cardColor: Colors.purple,
                  textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.white,
                  title: "Difficulty",
                  upDown: UpDownStat(
                    getValue: () => holder.difficulty,
                    onDownPressed: () => holder.difficulty--,
                    onUpPressed: () => holder.difficulty++,
                    textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.white,
                  ),
                  width: width
                ),
                _DiceCard(
                  cardColor: Colors.red,
                  textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.white,
                  title: "Challenge",
                  upDown: UpDownStat(
                    getValue: () => holder.challenge,
                    onDownPressed: () => holder.challenge--,
                    onUpPressed: () => holder.challenge++,
                    textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.white,
                  ),
                  width: width
                ),
                _DiceCard(
                  cardColor: Colors.lightBlue,
                  textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.black,
                  title: "Boost",
                  upDown: UpDownStat(
                    getValue: () => holder.boost,
                    onDownPressed: () => holder.boost--,
                    onUpPressed: () => holder.boost++,
                    textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.black,
                  ),
                  width: width
                ),
                _DiceCard(
                  cardColor: Colors.black,
                  textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.white,
                  title: "Setback",
                  upDown: UpDownStat(
                    getValue: () => holder.setback,
                    onDownPressed: () => holder.setback--,
                    onUpPressed: () => holder.setback++,
                    textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.white,
                  ),
                  width: width
                ),
                _DiceCard(
                  cardColor: Colors.white,
                  textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.black,
                  title: "Force",
                  upDown: UpDownStat(
                    getValue: () => holder.force,
                    onDownPressed: () => holder.force--,
                    onUpPressed: () => holder.force++,
                    textColor: !SW.of(context).getPreference(preferences.colorDice, true) ? null : Colors.black,
                  ),
                  width: width
                ),
              ]
            )
          ]
        )
      )
    );
  }
}

class _DiceCard extends StatelessWidget{

  final Color cardColor;
  final Color? textColor;
  final String title;
  final UpDownStat upDown;
  final double width;

  _DiceCard({required this.cardColor, required this.textColor, required this.title, required this.upDown, required this.width});

  @override
  Widget build(BuildContext context) {
    return ConstrainedBox(
      constraints: BoxConstraints(maxWidth: width),
      child: Padding(
        padding: EdgeInsets.all(4),
        child: Card(
          color: SW.of(context).getPreference(preferences.colorDice, true) ? cardColor : null,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text(
                title,
                style: Theme.of(context).textTheme.headline6?.copyWith(color: textColor)
              ),
              upDown
            ]
          ),
        ),
      )
    );
  }
}