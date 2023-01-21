import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/dice/dice.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/dice/sides.dart';
import 'package:swassistant/ui/frame_content.dart';
import 'package:swassistant/ui/misc/dice_selector.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class DiceRoller extends StatelessWidget{

  final SWDiceHolder holder = SWDiceHolder();

  DiceRoller({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    double width = min(MediaQuery.of(context).size.height, 250);
    int rows = (MediaQuery.of(context).size.width / width).floor();
    width = MediaQuery.of(context).size.width / rows;
    return FrameContent(
      fab: FloatingActionButton(
        onPressed: (){
          holder.getDice(context).roll().showCombinedResults(context, noSuccess: holder.ability == 0 && holder.proficiency == 0
              && holder.difficulty == 0 && holder.challenge == 0 && holder.boost == 0 && holder.challenge == 0);
        },
        child: const Icon(Icons.casino),
      ),
      child: SingleChildScrollView(
        physics: const BouncingScrollPhysics(),
        padding: const EdgeInsets.only(bottom: 20),
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

  const _InstantDiceCard({required this.sides, required this.width});

  @override
  State<StatefulWidget> createState() => _InstantState();

}

class _InstantState extends State<_InstantDiceCard>{

  int? result;

  @override
  Widget build(BuildContext context) =>
    ConstrainedBox(
      constraints: BoxConstraints(maxWidth: widget.width),
      child: Card(
        margin: const EdgeInsets.all(4),
        child: Padding(
          padding: const EdgeInsets.all(5),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Text(
                "d${widget.sides}",
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.titleLarge
              ),
              Row(
                children: [
                  Expanded(child: Text(
                    result?.toString() ?? "",
                    textAlign: TextAlign.center,
                  )),
                  Expanded(child: TextButton(
                    onPressed: (){
                      var res = Die(
                        name: "die",
                        sides: List<SimpleSide>.generate(widget.sides, (i) => SimpleSide((i+1).toString()))
                      ).roll();
                      setState(() => result = int.tryParse(res.toString()));
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