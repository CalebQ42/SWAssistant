
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/items/UpDownStat.dart';

class WoundStrain extends StatelessWidget{

  final bool editing;

  WoundStrain({this.editing});

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    return Column(
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text("Soak:"),
            SizedBox(
              width: 50,
              child: EditingText(
                editing: editing,
                initialText: character.soak.toString(),
                controller: (){
                  var controller = new TextEditingController(text: character.soak.toString());
                  controller.addListener(() {
                    if(controller.text =="")
                      character.soak = 0;
                    else
                      character.soak = int.parse(controller.text);
                  });
                  return controller;
                }(),
                textType: TextInputType.number,
                defaultSave: true,
              )
            )
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            Expanded(
              child: Center(child: editing ? Text("Max Wound:") : Text("Wound:"))
            ),
            Expanded(
              child: Center(child: editing ? Text("Max Strain:") : Text("Strain:"))
            )
          ],
        ),
        Row(
          children: <Widget>[
            Expanded(
              child: AnimatedSwitcher(
                duration: Duration(milliseconds: 300),
                transitionBuilder: (wid, anim){
                  Tween<Offset> offset;
                  if(wid.key == ValueKey("UpDownWound"))
                    offset = Tween(begin: Offset(0.0,1.0), end: Offset.zero);
                  else
                    offset = Tween(begin: Offset(0.0,-1.0), end: Offset.zero);
                  return ClipRect(
                    child: SlideTransition(
                      position: offset.animate(anim),
                      child: SizeTransition(
                        sizeFactor: anim,
                        child: Center(child: wid)
                      ),
                    )
                  );
                },
                child: !editing ? UpDownStat(
                  key: ValueKey("UpDownWound"),
                  onUpPressed: () => character.woundCur++,
                  onDownPressed: ()=>character.woundCur--,
                  getValue: ()=>character.woundCur,
                  getMax: ()=>character.woundThresh,
                  getMin: ()=>0,
                ) : (){
                  var controll = TextEditingController(text: character.woundThresh.toString());
                  controll.addListener(() {
                    character.woundThresh = int.parse(controll.text);
                    character.save(character.getFileLocation(SW.of(context)));
                  });
                  return Padding(
                    child:TextField(
                      controller: controll,
                      keyboardType: TextInputType.number,
                      inputFormatters: [WhitelistingTextInputFormatter.digitsOnly]
                    ),
                    padding: EdgeInsets.symmetric(horizontal: 2.0)
                  ); 
                }()
              )
            ),
            Expanded(
              child: AnimatedSwitcher(
                duration: Duration(milliseconds: 300),
                transitionBuilder: (wid, anim){
                  Tween<Offset> offset;
                  if(wid.key == ValueKey("UpDownStrain"))
                    offset = Tween(begin: Offset(0.0,1.0), end: Offset.zero);
                  else
                    offset = Tween(begin: Offset(0.0,-1.0), end: Offset.zero);
                  return ClipRect(
                    child: SlideTransition(
                      position: offset.animate(anim),
                      child: SizeTransition(
                        sizeFactor: anim,
                        child: Center(child: wid)
                      ),
                    )
                  );
                },
                child: !editing ? UpDownStat(
                  key: ValueKey("UpDownStrain"),
                  onUpPressed: () => character.strainCur++,
                  onDownPressed: ()=>character.strainCur--,
                  getValue: ()=>character.strainCur,
                  getMax: ()=>character.strainThresh,
                  getMin: ()=>0,
                ) : (){
                  var controll = TextEditingController(text: character.strainThresh.toString());
                  controll.addListener(() {
                    character.strainThresh = int.parse(controll.text);
                    character.save(character.getFileLocation(SW.of(context)));
                  });
                  return Padding(
                    child: TextField(
                      controller: controll,
                      keyboardType: TextInputType.number,
                      inputFormatters: [WhitelistingTextInputFormatter.digitsOnly]
                    ),
                    padding: EdgeInsets.symmetric(horizontal: 2.0)
                  ); 
                }()
              )
            )
          ],
        )
      ],
    );
  }
}