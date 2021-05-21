import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/ui/UpDownStat.dart';

import '../../EditableCommon.dart';

class MinInfo extends StatefulWidget with StatefulCard{

  final EditableContentStatefulHolder holder;
  final EditableContentStatefulHolder woundHolder;

  MinInfo({required this.holder, required this.woundHolder});

  @override
  State<StatefulWidget> createState() => MinInfoState(holder: holder, woundHolder: woundHolder);

  @override
  EditableContentStatefulHolder getHolder() => holder;
}

class MinInfoState extends State{

  final EditableContentStatefulHolder holder;
  final EditableContentStatefulHolder woundHolder;

  MinInfoState({required this.woundHolder, required this.holder}){
    holder.reloadFunction = () => setState((){});
  }

  @override
  Widget build(BuildContext context){
    var minion = Minion.of(context);
    if (minion == null)
      throw "MinInfo card used on non Minion";
    return Column(
      children: [
        EditingText(
          editing: holder.editing,
          initialText: minion.category,
          controller: (){
            var cont = TextEditingController(text: minion.category);
            cont.addListener(() {
              minion.category = cont.text;
            });
            return cont;
          }(),
          textCapitalization: TextCapitalization.words,
          defaultSave: true,
          textAlign: TextAlign.center,
          fieldAlign: TextAlign.center,
          title: "Category"
        ),
        Container(height: 10,),
        Text("Number of Minions:"),
        UpDownStat(
          onDownPressed: (){
            minion.minionNum--;
            minion.woundCur -= minion.woundThreshInd;
            if(minion.woundCur < 0)
              minion.woundCur = 0;
            minion.woundCurTemp = minion.woundCur;
            minion.woundThresh = minion.minionNum * minion.woundThresh;
            minion.save(context: context);
            if(minion.showCard[minion.cardNames.indexOf("Wound")] && woundHolder.reloadFunction != null)
              woundHolder.reloadFunction!();
          },
          onUpPressed: (){
            minion.minionNum++;
            minion.woundCur += minion.woundThreshInd;
            minion.woundCurTemp = minion.woundCur;
            minion.woundThresh = minion.minionNum * minion.woundThresh;
            minion.save(context: context);
            if(minion.showCard[minion.cardNames.indexOf("Wound")] && woundHolder.reloadFunction != null)
              woundHolder.reloadFunction!();
          },
          getValue: () => minion.minionNum,
        )
      ]
    );
  }
}