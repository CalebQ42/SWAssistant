import 'package:flutter/material.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/ui/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import '../../editable_common.dart';

class MinInfo extends StatefulWidget with StatefulCard{

  final EditableContentStatefulHolder holder;
  final EditableContentStatefulHolder woundHolder;

  MinInfo({required this.holder, required this.woundHolder, Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => MinInfoState();

  @override
  EditableContentStatefulHolder getHolder() => holder;
}

class MinInfoState extends State<MinInfo>{

  MinInfoState(){
    widget.holder.reloadFunction = () => setState((){});
  }

  @override
  Widget build(BuildContext context){
    var minion = Minion.of(context);
    if (minion == null) throw "MinInfo card used on non Minion";
    return Column(
      children: [
        EditingText(
          editing: widget.holder.editing,
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
          title: AppLocalizations.of(context)!.category
        ),
        Container(height: 10,),
        Text(AppLocalizations.of(context)!.minNum),
        UpDownStat(
          onDownPressed: (){
            minion.minionNum--;
            minion.woundCur -= minion.woundThreshInd;
            if(minion.woundCur < 0) minion.woundCur = 0;
            minion.woundCurTemp = minion.woundCur;
            minion.woundThresh = minion.minionNum * minion.woundThresh;
            minion.save(context: context);
            if(minion.showCard[1] && widget.woundHolder.reloadFunction != null){
              widget.woundHolder.reloadFunction!();
            }
          },
          onUpPressed: (){
            minion.minionNum++;
            minion.woundCur += minion.woundThreshInd;
            minion.woundCurTemp = minion.woundCur;
            minion.woundThresh = minion.minionNum * minion.woundThresh;
            minion.save(context: context);
            if(minion.showCard[1] && widget.woundHolder.reloadFunction != null){
              widget.woundHolder.reloadFunction!();
            }
          },
          getValue: () => minion.minionNum,
        )
      ]
    );
  }
}