import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/ui/UpDownStat.dart';

class MinNum extends StatelessWidget{

  final Function woundRefresh;

  MinNum({this.woundRefresh});

  @override
  Widget build(BuildContext context){
    var minion = Minion.of(context);
    return UpDownStat(
      onDownPressed: (){
        minion.minionNum--;
        minion.woundCur -= minion.woundThreshInd;
        if(minion.woundCur > 0)
          minion.woundCur = 0;
        minion.woundThresh = minion.minionNum * minion.woundThresh;
        minion.save(context: context);
        if(woundRefresh != null && minion.showCard[minion.cardNames.indexOf("Wound")])
          woundRefresh();
      },
    );
  }
}