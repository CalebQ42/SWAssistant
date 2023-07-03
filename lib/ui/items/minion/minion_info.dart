import 'package:flutter/material.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';
import 'package:swassistant/ui/misc/up_down.dart';

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
    var app = SW.of(context);
    if(catController == null){
      catController = TextEditingController(text: minion.category);
      catController!.addListener(() => 
        app.updateCategory(minion, catController!.text)
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
          title: app.locale.category
        ),
        Container(height: 10,),
        Text(app.locale.minNum),
        UpDownStat(
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