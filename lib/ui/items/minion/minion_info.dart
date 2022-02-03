import 'package:flutter/material.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/ui/misc/editing_text.dart';
import 'package:swassistant/ui/misc/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import '../../misc/edit_content.dart';

class MinInfo extends StatefulWidget{

  const MinInfo({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => MinInfoState();
}

class MinInfoState extends State<MinInfo> with StatefulCard{

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => false;

  @override
  Widget build(BuildContext context){
    var minion = Minion.of(context);
    if (minion == null) throw "MinInfo card used on non Minion";
    return Column(
      children: [
        EditingText(
          editing: edit,
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
            minion.woundKey.currentState?.setState(() {});
          },
          onUpPressed: (){
            minion.minionNum++;
            minion.woundCur += minion.woundThreshInd;
            minion.woundCurTemp = minion.woundCur;
            minion.woundThresh = minion.minionNum * minion.woundThresh;
            minion.save(context: context);
            minion.woundKey.currentState?.setState(() {});
          },
          getValue: () => minion.minionNum,
        )
      ]
    );
  }
}