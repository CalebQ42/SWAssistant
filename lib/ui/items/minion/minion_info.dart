import 'package:flutter/material.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/sw.dart';
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

  TextEditingController? catController;

  @override
  Widget build(BuildContext context){
    var minion = Minion.of(context);
    if (minion == null) throw "MinInfo card used on non Minion";
    if(catController == null){
      catController = TextEditingController(text: minion.category);
      catController!.addListener(() => 
        SW.of(context).updateCategory(minion, catController!.text)
      );
    }
    return Column(
      children: [
        EditingText(
          editing: edit,
          initialText: minion.category,
          controller: catController,
          textCapitalization: TextCapitalization.words,
          defaultSave: true,
          textAlign: TextAlign.center,
          fieldAlign: TextAlign.center,
          title: AppLocalizations.of(context)!.category
        ),
        Container(height: 10,),
        Text(AppLocalizations.of(context)!.minNum),
        UpDownStat(
          //TODO: adjust wound so it doesn't go negative.
          onDownPressed: (){
            minion.minionNum--;
            var wound = (minion.woundThreshInd * minion.minionNum) - minion.woundDmg;
            if (wound < 0) {
              minion.woundDmg += wound;
            }
            minion.save(context: context);
            minion.woundKey.currentState?.setState(() {});
          },
          onUpPressed: (){
            minion.minionNum++;
            minion.save(context: context);
            minion.woundKey.currentState?.setState(() {});
          },
          getValue: () => minion.minionNum,
          min: 0,
        )
      ]
    );
  }
}