import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/UpDownStat.dart';

class MinionWound extends StatefulWidget with StatefulCard{

  final EditableContentStatefulHolder holder;
  final Function numRefresh;

  MinionWound({this.holder, this.numRefresh});

  @override
  State<StatefulWidget> createState() => MinionWoundState(holder: holder);

  @override
  EditableContentStatefulHolder getHolder() => holder;
}

class MinionWoundState extends State with TickerProviderStateMixin{

  final EditableContentStatefulHolder holder;
  final Function numRefresh;

  TextEditingController indWoundController;

  MinionWoundState({this.holder, this.numRefresh}){
    holder.reloadFunction = () => setState((){});
  }

  @override
  Widget build(BuildContext context) {
    var minion = Minion.of(context);
    var woundCurTemp = minion.woundCur;
    if(indWoundController == null)
      indWoundController = TextEditingController(text: minion.woundThreshInd.toString())
          ..addListener(() {
        var tmp = int.tryParse(indWoundController.text);
        if(tmp == null)
          minion.woundThreshInd = 0;
        else
          minion.woundThreshInd = tmp;
        minion.woundThresh = minion.woundThreshInd * minion.minionNum;
        if(minion.woundCur < woundCurTemp)
          minion.woundCur = woundCurTemp;
        if(minion.woundCur > minion.woundThresh)
          minion.woundCur = minion.woundThresh;
        return tmp;
      });
    return Column(
      children: [
        Row(
          children: [
            Text("Soak:"),
            SizedBox(
              width: 50,
              child: EditingText(
                editing: holder.editing,
                state: this,
                initialText: minion.soak.toString(),
                controller: (){
                  var controller = TextEditingController(text: minion.soak.toString());
                  controller.addListener(() {
                    var tmp = int.tryParse(controller.text);
                    if(tmp == null)
                      minion.soak = 0;
                    else
                      minion.soak = tmp;
                    return tmp;
                  });
                }(),
                collapsed: true,
                defaultSave: true,
              ),
            )
          ]
        ),
        Row(
          children: [
            Text("Wound per Minion:"),
            SizedBox(
              width: 50,
              child: EditingText(
                editing: holder.editing,
                state: this,
                initialText: minion.woundThreshInd.toString(),
                controller: indWoundController,
                collapsed: true,
                defaultSave: true,
              ),
            )
          ],
        ),
        UpDownStat(
          onUpPressed: (){
            minion.woundCur++;
            woundCurTemp = minion.woundCur;
            minion.save(context: context);
          },
          onDownPressed: (){
            minion.woundCur--;
            woundCurTemp = minion.woundCur;
            minion.save(context: context);
          },
          getValue: () => minion.woundCur,
          getMax: () => minion.woundThresh,
        )
      ],
    );
  }
}