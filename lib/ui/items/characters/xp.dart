import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

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
  Widget build(BuildContext context){
    var character = Character.of(context);
    if (character == null) throw "XP card used on non Character";
    var xpAddController = TextEditingController();
    return Column(
      children: [
        Row(
          children: [
            Expanded(
              child: EditingText(
                editing: edit,
                initialText: character.xpCur.toString(),
                controller: (){
                  var cont = TextEditingController(text: character.xpCur.toString());
                  cont.addListener(() =>
                    character.xpCur = int.tryParse(cont.text) ?? 0
                  );
                  return cont;
                }(),
                defaultSave: true,
                textAlign: TextAlign.center,
                textType: TextInputType.number,
                title: AppLocalizations.of(context)!.current,
              ),
            ),
            Expanded(
              child: EditingText(
                editing: edit,
                initialText: character.xpTot.toString(),
                controller: (){
                  var cont = TextEditingController(text: character.xpTot.toString());
                  cont.addListener(() =>
                    character.xpTot = int.tryParse(cont.text) ?? 0
                  );
                  return cont;
                }(),
                defaultSave: true,
                textAlign: TextAlign.center,
                textType: TextInputType.number,
                title: AppLocalizations.of(context)!.total,
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
                    xpAddController.text = "";
                    setState(() {});
                  }
                },
              ),
            ),
            Container(width: 10,),
            TextButton.icon(
              onPressed: (){
                if(xpAddController.text != ""){
                  var adding = int.tryParse(xpAddController.text);
                  if (adding != null){
                    character.xpTot += adding;
                    character.xpCur += adding;
                  }
                  xpAddController.text = "";
                  setState(() {});
                }
              },
              icon: const Icon(Icons.add),
              label: Text(AppLocalizations.of(context)!.addXP)
            )
          ]
        )
      ],
    );
  }
}