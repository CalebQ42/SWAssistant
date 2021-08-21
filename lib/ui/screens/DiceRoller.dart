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
                ConstrainedBox(
                  constraints: BoxConstraints(maxWidth: width),
                  child: Container(
                    color: SW.of(context).getPreference(preferences.colorDice, true) ? Colors.green : null,
                    child: Column(
                      children: [
                        Text(
                          "Ability",
                          textAlign: TextAlign.center,
                          style: Theme.of(context).textTheme.headline6
                        ),
                        UpDownStat(
                          onUpPressed: () => holder.ability++,
                          onDownPressed: () => holder.ability--,
                          getValue: () => holder.ability
                        )
                      ]
                    )
                  ),
                ),
                ConstrainedBox(
                  constraints: BoxConstraints(maxWidth: width),
                  child: Container(
                    color: SW.of(context).getPreference(preferences.colorDice, true) ? Colors.yellow : null,
                    child: Column(
                      children: [
                        Text(
                          "Proficiency",
                          textAlign: TextAlign.center,
                          style: Theme.of(context).textTheme.headline6
                        ),
                        UpDownStat(
                          onUpPressed: () => holder.proficiency++,
                          onDownPressed: () => holder.proficiency--,
                          getValue: () => holder.proficiency
                        )
                      ]
                    )
                  ),
                ),
                ConstrainedBox(
                  constraints: BoxConstraints(maxWidth: width),
                  child: Container(
                    color: SW.of(context).getPreference(preferences.colorDice, true) ? Colors.purple : null,
                    child: Column(
                      children: [
                        Text(
                          "Difficulty",
                          textAlign: TextAlign.center,
                          style: Theme.of(context).textTheme.headline6
                        ),
                        UpDownStat(
                          onUpPressed: () => holder.difficulty++,
                          onDownPressed: () => holder.difficulty--,
                          getValue: () => holder.difficulty
                        )
                      ]
                    )
                  ),
                ),
                ConstrainedBox(
                  constraints: BoxConstraints(maxWidth: width),
                  child: Container(
                    color: SW.of(context).getPreference(preferences.colorDice, true) ? Colors.red : null,
                    child: Column(
                      children: [
                        Text(
                          "Challenge",
                          textAlign: TextAlign.center,
                          style: Theme.of(context).textTheme.headline6
                        ),
                        UpDownStat(
                          onUpPressed: () => holder.challenge++,
                          onDownPressed: () => holder.challenge--,
                          getValue: () => holder.challenge
                        )
                      ]
                    )
                  ),
                ),
                ConstrainedBox(
                  constraints: BoxConstraints(maxWidth: width),
                  child: Container(
                    color: SW.of(context).getPreference(preferences.colorDice, true) ? Colors.lightBlue : null,
                    child: Column(
                      children: [
                        Text(
                          "Boost",
                          textAlign: TextAlign.center,
                          style: Theme.of(context).textTheme.headline6
                        ),
                        UpDownStat(
                          onUpPressed: () => holder.boost++,
                          onDownPressed: () => holder.boost--,
                          getValue: () => holder.boost
                        )
                      ]
                    )
                  ),
                ),
                ConstrainedBox(
                  constraints: BoxConstraints(maxWidth: width),
                  child: Container(
                    color: SW.of(context).getPreference(preferences.colorDice, true) ? Colors.black : null,
                    child: Column(
                      children: [
                        Text(
                          "Setback",
                          textAlign: TextAlign.center,
                          style: Theme.of(context).textTheme.headline6
                        ),
                        UpDownStat(
                          onUpPressed: () => holder.setback++,
                          onDownPressed: () => holder.setback--,
                          getValue: () => holder.setback
                        )
                      ]
                    )
                  ),
                ),
                ConstrainedBox(
                  constraints: BoxConstraints(maxWidth: width),
                  child: Container(
                    color: SW.of(context).getPreference(preferences.colorDice, true) ? Colors.white : null,
                    child: Column(
                      children: [
                        Text(
                          "Force",
                          textAlign: TextAlign.center,
                          style: Theme.of(context).textTheme.headline6
                        ),
                        UpDownStat(
                          onUpPressed: () => holder.force++,
                          onDownPressed: () => holder.force--,
                          getValue: () => holder.force
                        )
                      ]
                    )
                  ),
                ),
              ]
            )
          ]
        )
      )
    );
  }
}