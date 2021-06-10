import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/UpDownStat.dart';

class MinionWound extends StatefulWidget with StatefulCard{

  final EditableContentStatefulHolder holder;
  final EditableContentStatefulHolder numHolder;

  MinionWound({required this.holder, required this.numHolder});

  @override
  State<StatefulWidget> createState() => MinionWoundState(holder: holder, numHolder: numHolder);

  @override
  EditableContentStatefulHolder getHolder() => holder;
}

class MinionWoundState extends State with TickerProviderStateMixin{

  final EditableContentStatefulHolder holder;
  final EditableContentStatefulHolder numHolder;

  TextEditingController? indWoundController;

  MinionWoundState({required this.holder, required this.numHolder}){
    holder.reloadFunction = () => setState((){});
  }

  @override
  Widget build(BuildContext context) {
    var minion = Minion.of(context);
    if (minion == null)
      throw "MinionWound card used on non Minion";
    if(indWoundController == null)
      indWoundController = TextEditingController(text: minion.woundThreshInd.toString())
          ..addListener(() {
        var tmp = int.tryParse(indWoundController!.text);
        var orig = minion.woundCur;
        if(tmp == null)
          minion.woundThreshInd = 0;
        else
          minion.woundThreshInd = tmp;
        minion.woundThresh = minion.woundThreshInd * minion.minionNum;
        if(minion.woundCur < minion.woundCurTemp)
          minion.woundCur = minion.woundCurTemp;
        if(minion.woundCur > minion.woundThresh)
          minion.woundCur = minion.woundThresh;
        if(minion.woundCur != orig)
          setState((){});
      });
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text("Soak:"),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: holder.editing,
                state: this,
                initialText: minion.soak.toString(),
                controller: (){
                  var controller = TextEditingController(text: minion.soak.toString());
                  controller.addListener(() =>
                    minion.soak = int.tryParse(controller.text) ?? 0
                  );
                  return controller;
                }(),
                textAlign: TextAlign.center,
                fieldAlign: TextAlign.center,
                textType: TextInputType.number,
                collapsed: true,
                defaultSave: true,
              ),
            )
          ]
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text("Wound per Minion:"),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: holder.editing,
                state: this,
                initialText: minion.woundThreshInd.toString(),
                controller: indWoundController,
                textType: TextInputType.number,
                collapsed: true,
                defaultSave: true,
                textAlign: TextAlign.center,
                fieldAlign: TextAlign.center,
              ),
            )
          ],
        ),
        UpDownStat(
          onUpPressed: (){
            minion.woundCur++;
            minion.woundCurTemp = minion.woundCur;
            minion.save(context: context);
          },
          onDownPressed: (){
            minion.woundCur--;
            minion.woundCurTemp = minion.woundCur;
            int minNum = minion.woundCur ~/ minion.woundThreshInd;
            if(minion.woundCur % minion.woundThreshInd > 0)
              minNum ++;
            if(minion.minionNum != minNum){
              minion.minionNum = minNum;
              if(minion.showCard[minion.cardNames.indexOf("Number of Minions")] && numHolder.reloadFunction != null)
                numHolder.reloadFunction!();
            }
            minion.save(context: context);
          },
          getValue: () => minion.woundCur,
          getMax: () => minion.woundThreshInd * minion.minionNum,
        )
      ],
    );
  }
}