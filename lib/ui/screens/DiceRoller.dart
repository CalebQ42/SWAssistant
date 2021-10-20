import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/dice/Dice.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/dice/Sides.dart';
import 'package:swassistant/ui/Common.dart';
import 'package:swassistant/ui/misc/DiceSelector.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class DiceRoller extends StatelessWidget{

  final SWDiceHolder holder = SWDiceHolder();

  @override
  Widget build(BuildContext context) {
    double width = min(MediaQuery.of(context).size.height, 250);
    int rows = (MediaQuery.of(context).size.width / width).floor();
    width = MediaQuery.of(context).size.width / rows;
    return Scaffold(
      drawer: SWDrawer(),
      appBar: SWAppBar(
        context,
        title: Text(AppLocalizations.of(context)!.dice),
        backgroundColor: Theme.of(context).primaryColor,
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: (){
          holder.getDice(context).roll().showCombinedResults(context, noSuccess: holder.ability == 0 && holder.proficiency == 0
              && holder.difficulty == 0 && holder.challenge == 0 && holder.boost == 0 && holder.challenge == 0);
        },
        child: Icon(Icons.casino),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.only(bottom: 20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Container(height: 10),
            Wrap(
              alignment: WrapAlignment.spaceEvenly,
              children: List<Widget>.generate(
                7,
                (i) =>
                  ConstrainedBox(
                    constraints: BoxConstraints(maxWidth: width),
                    child: DiceSelector(
                      holder: holder,
                      type: i
                    )
                  )
              )
            ),
            Container(height: 10),
            Wrap(
              alignment: WrapAlignment.spaceEvenly,
              children: [
                _InstantDiceCard(sides: 100, width: width),
                _InstantDiceCard(sides: 10, width: width)
              ]
            )
          ]
        )
      )
    );
  }
}

class _InstantDiceCard extends StatefulWidget{

  final int sides;
  final double width;

  _InstantDiceCard({required this.sides, required this.width});

  @override
  State<StatefulWidget> createState() => _InstantState(sides, width);

}

class _InstantState extends State{

  final int sides;
  final double width;
  int result = -1;

  _InstantState(this.sides, this.width);

  @override
  Widget build(BuildContext context) =>
    ConstrainedBox(
      constraints: BoxConstraints(maxWidth: width),
      child: Card(
        margin: EdgeInsets.all(4),
        child: Padding(
          padding: EdgeInsets.all(5),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text(
                "d" + sides.toString(),
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.headline6
              ),
              Row(
                children: [
                  Expanded(child: Text(
                    result == -1 ? "" : result.toString(),
                    textAlign: TextAlign.center,
                  )),
                  Expanded(child: TextButton(
                    onPressed: (){
                      var res = Die(
                        name: "die",
                        sides: List<SimpleSide>.generate(sides, (i) => SimpleSide((i+1).toString()))
                      ).roll();
                      setState(() => result = int.tryParse(res.toString()) ?? -1);
                    },
                    child: Text(AppLocalizations.of(context)!.roll)
                  ))
                ],
              )
            ]
          ),
        )
      )
    );
}