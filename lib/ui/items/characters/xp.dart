import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class XP extends StatefulWidget{

  const XP({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => XPState();
}

class XPState extends State<XP> with StatefulCard {

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => false;

  TextEditingController? xpCurController;
  TextEditingController? xpTotalController;
  TextEditingController? xpAddController;

  @override
  Widget build(BuildContext context){
    var character = Character.of(context);
    if (character == null) throw "XP card used on non Character";
    if(xpCurController == null){
      xpCurController = TextEditingController();
      xpCurController!.addListener(() => 
        character.xpCur = int.tryParse(xpCurController!.text) ?? 0
      );
    }
    if(xpTotalController == null){
      xpTotalController = TextEditingController();
      xpTotalController!.addListener(() => 
        character.xpTot = int.tryParse(xpTotalController!.text) ?? 0
      );
    }
    var app = SW.of(context);
    xpAddController ??= TextEditingController();
    return Column(
      children: [
        Row(
          children: [
            Expanded(
              child: EditingText(
                editing: edit,
                initialText: character.xpCur.toString(),
                controller: xpCurController,
                defaultSave: true,
                textAlign: TextAlign.center,
                textType: TextInputType.number,
                title: app.locale.current,
              ),
            ),
            Expanded(
              child: EditingText(
                editing: edit,
                initialText: character.xpTot.toString(),
                controller: xpTotalController,
                defaultSave: true,
                textAlign: TextAlign.center,
                textType: TextInputType.number,
                title: app.locale.total,
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
                decoration: const InputDecoration(contentPadding: EdgeInsets.symmetric(horizontal: 5)),
                onSubmitted: (text){
                  if(text != ""){
                    var adding = int.tryParse(text);
                    if (adding != null){
                      character.xpTot += adding;
                      character.xpCur += adding;
                    }
                    xpAddController!.text = "";
                    setState(() {});
                  }
                },
              ),
            ),
            Container(width: 10,),
            TextButton.icon(
              onPressed: (){
                if(xpAddController!.text != ""){
                  var adding = int.tryParse(xpAddController!.text);
                  if (adding != null){
                    character.xpTot += adding;
                    character.xpCur += adding;
                  }
                  xpAddController!.text = "";
                  setState(() {});
                }
              },
              icon: const Icon(Icons.add),
              label: Text(app.locale.addXP)
            )
          ]
        )
      ],
    );
  }
}