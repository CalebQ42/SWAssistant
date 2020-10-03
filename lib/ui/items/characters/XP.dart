import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class XP extends StatelessWidget{

  final bool editing;
  final Function refresh;
  final EditableContentState state;

  XP({this.editing, this.refresh, this.state});
  @override
  Widget build(BuildContext context){
    var character = Character.of(context);
    var xpAddController = TextEditingController();
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
                state: state,
                defaultSave: true,
                textAlign: TextAlign.center,
                textType: TextInputType.number,
              ),
            ),
            Expanded(
              child: EditingText(
                editing: editing,
                initialText: character.xpTot.toString(),
                controller: (){
                  var cont = TextEditingController(text: character.xpTot.toString());
                  cont.addListener(() {
                    var val = int.tryParse(cont.text);
                    if(val == null)
                      character.xpTot = 0;
                    else
                      character.xpTot = val;
                  });
                  return cont;
                }(),
                state: state,
                defaultSave: true,
                textAlign: TextAlign.center,
                textType: TextInputType.number,
              ),
            ),
          ],
        ),
        Container(height: 10),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(
              width: 75,
              child: TextField(
                controller: xpAddController,
                keyboardType: TextInputType.number,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                decoration: InputDecoration(contentPadding: EdgeInsets.symmetric(horizontal: 5)),
                onSubmitted: (text){
                  if(text != ""){
                    var adding = int.tryParse(text);
                    character.xpTot += adding;
                    character.xpCur += adding;
                    xpAddController.text = "";
                    refresh();
                  }
                },
              ),
            ),
            Container(width: 10,),
            FlatButton.icon(
              onPressed: (){
                if(xpAddController.text != ""){
                  var adding = int.tryParse(xpAddController.text);
                  character.xpTot += adding;
                  character.xpCur += adding;
                  xpAddController.text = "";
                  refresh();
                }
              },
              icon: Icon(Icons.add),
              label: Text("Add XP")
            )
          ]
        )
      ],
    );
  }
}