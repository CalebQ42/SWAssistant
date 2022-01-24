import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:swassistant/ui/items/minion/minion_info.dart';
import 'package:swassistant/ui/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class MinionWound extends StatefulWidget{

  final GlobalKey<MinInfoState> numState;

  const MinionWound({required this.numState, Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => MinionWoundState();
}

class MinionWoundState extends State<MinionWound> with StatefulCard{

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);

  TextEditingController? indWoundController;

  @override
  Widget build(BuildContext context) {
    var minion = Minion.of(context);
    if (minion == null) throw "MinionWound card used on non Minion";
    indWoundController ??= TextEditingController(text: minion.woundThreshInd.toString())
        ..addListener(() {
      var tmp = int.tryParse(indWoundController!.text);
      var orig = minion.woundCur;
      if(tmp == null){
        minion.woundThreshInd = 0;
      }else{
        minion.woundThreshInd = tmp;
      }
      minion.woundThresh = minion.woundThreshInd * minion.minionNum;
      if(minion.woundCur < minion.woundCurTemp) minion.woundCur = minion.woundCurTemp;
      if(minion.woundCur > minion.woundThresh) minion.woundCur = minion.woundThresh;
      if(minion.woundCur != orig) setState((){});
    });
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(AppLocalizations.of(context)!.soak),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: edit,
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
            Text(AppLocalizations.of(context)!.minWound),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: edit,
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
            if(minion.woundCur % minion.woundThreshInd > 0){
              minNum ++;
            }
            if(minion.minionNum != minNum){
              minion.minionNum = minNum;
              if(minion.showCard[AppLocalizations.of(context)!.basicInfo] == true){
                widget.numState.currentState?.setState(() {});
              }
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