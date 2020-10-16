import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/ui/UpDownStat.dart';

import '../../EditableCommon.dart';

class MinNum extends StatefulWidget with StatefulCard{

  final EditableContentStatefulHolder holder;
  final EditableContentStatefulHolder woundHolder;

  MinNum({this.holder, this.woundHolder});

  @override
  State<StatefulWidget> createState() => MinNumState(holder: holder, woundHolder: woundHolder);

  @override
  EditableContentStatefulHolder getHolder() => holder;
}

class MinNumState extends State{

  final EditableContentStatefulHolder holder;
  final EditableContentStatefulHolder woundHolder;

  MinNumState({this.woundHolder, this.holder}){
    holder.reloadFunction = () => setState((){});
  }

  @override
  Widget build(BuildContext context){
    var minion = Minion.of(context);
    return UpDownStat(
      onDownPressed: (){
        minion.minionNum--;
        minion.woundCur -= minion.woundThreshInd;
        if(minion.woundCur < 0)
          minion.woundCur = 0;
        minion.woundCurTemp = minion.woundCur;
        minion.woundThresh = minion.minionNum * minion.woundThresh;
        minion.save(context: context);
        if(minion.showCard[minion.cardNames.indexOf("Wound")])
          woundHolder.reloadFunction();
      },
      onUpPressed: (){
        minion.minionNum++;
        minion.woundCur += minion.woundThreshInd;
        minion.woundCurTemp = minion.woundCur;
        minion.woundThresh = minion.minionNum * minion.woundThresh;
        minion.save(context: context);
        if(minion.showCard[minion.cardNames.indexOf("Wound")])
          woundHolder.reloadFunction();
      },
      getValue: () => minion.minionNum,
    );
  }
}