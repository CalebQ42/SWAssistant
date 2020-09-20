import 'package:flutter/material.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class XP extends StatelessWidget{

  final bool editing;

  XP({this.editing});
  @override
  Widget build(BuildContext context){
    var character = Character.of(context);
    return Column(
      children: [
        Row(
          children: [
            Expanded(
              child: Center(child: Text("Current:")),
            ),
            Expanded(
              child: Center(child: Text("Total:")),
            )
          ]
        ),
        Row(
          children: [
            Expanded(
              child: EditingText(
                editing: editing,
                initialText: character.xpCur.toString(),
                controller: (){
                  var cont = TextEditingController(text: character.xpCur.toString());
                  cont.addListener(() {
                    var val = int.tryParse(cont.text);
                    if(val == null)
                      character.xpCur = 0;
                    else
                      character.xpCur = val;
                  });
                  return cont;
                }(),
                defaultSave: true,
                textAlign: TextAlign.center,
                textType: TextInputType.number,
              ),
            ),
            Expanded(
              child: EditingText(
                editing: editing,
                initialText: character.xpTot.toString(),
              ),
            ),
          ],
        )
      ],
    );
  }
}