
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/UpDownStat.dart';

class WoundStrain extends StatelessWidget{

  final bool editing;
  final EditableContentState state;

  WoundStrain({this.editing, this.state});

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
                collapsed: true,
                fieldAlign: TextAlign.center,
                fieldInsets: EdgeInsets.all(3),
                state: state,
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
        if(editing) Container(height: 10), 
        Row(
          children: <Widget>[
            Expanded(
              child: AnimatedSize(
                duration: Duration(milliseconds: 250),
                vsync: state,
                child: AnimatedSwitcher(
                  duration: Duration(milliseconds: 300),
                  transitionBuilder: (wid, anim){
                    Tween<Offset> offset;
                    if(wid.key == ValueKey("UpDownWound"))
                      offset = Tween(begin: Offset(0.0,-1.0), end: Offset.zero);
                    else
                      offset = Tween(begin: Offset(0.0,1.0), end: Offset.zero);
                    return ClipRect(
                      child: SlideTransition(
                        position: offset.animate(anim),
                        child: Center(child: wid),
                      )
                    );
                  },
                  child: !editing ? Column(
                    children: [
                      Center(child: Text("Wound")),
                      UpDownStat(
                        key: ValueKey("UpDownWound"),
                        onUpPressed: () {
                          character.woundCur++;
                          character.save(context: context);
                        },
                        onDownPressed: (){
                          character.woundCur--;
                          character.save(context: context);
                        },
                        getValue: ()=>character.woundCur,
                        getMax: ()=>character.woundThresh,
                        getMin: ()=>0,
                      )
                    ]
                  ) : (){
                    var controll = TextEditingController(text: character.woundThresh.toString());
                    controll.addListener(() {
                      character.woundThresh = int.parse(controll.text);
                      character.save(context: context);
                    });
                    return Padding(
                      child: TextField(
                        controller: controll,
                        keyboardType: TextInputType.number,
                        inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
                        decoration: InputDecoration(labelText: "Max Wound",),
                      ),
                      padding: EdgeInsets.only(right: 3.0, left: 3.0, top: 3.0)
                    ); 
                  }()
                )
              )
            ),
            Expanded(
              child: AnimatedSize(
                duration: Duration(milliseconds: 250),
                vsync: state,
                child: AnimatedSwitcher(
                  duration: Duration(milliseconds: 300),
                  transitionBuilder: (wid, anim){
                    Tween<Offset> offset;
                    if(wid.key == ValueKey("UpDownStrain"))
                      offset = Tween(begin: Offset(0.0,-1.0), end: Offset.zero);
                    else
                      offset = Tween(begin: Offset(0.0,1.0), end: Offset.zero);
                    return ClipRect(
                      child: SlideTransition(
                        position: offset.animate(anim),
                        child: Center(child: wid)
                      )
                    );
                  },
                  child: !editing ? Column(
                    children: [
                      Center(child: Text("Strain"),),
                      UpDownStat(
                        key: ValueKey("UpDownStrain"),
                        onUpPressed: (){
                          character.strainCur++;
                          character.save(context: context);
                        },
                        onDownPressed: (){
                          character.strainCur--;
                          character.save(context: context);
                        },
                        getValue: ()=>character.strainCur,
                        getMax: ()=>character.strainThresh,
                        getMin: ()=>0,
                      )
                    ]
                    ) : (){
                    var controll = TextEditingController(text: character.strainThresh.toString());
                    controll.addListener(() {
                      character.strainThresh = int.parse(controll.text);
                      character.save(context: context);
                    });
                    return Padding(
                      child: TextField(
                        controller: controll,
                        keyboardType: TextInputType.number,
                        inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
                        decoration: InputDecoration(labelText: "Max Strain"),
                      ),
                      padding: EdgeInsets.only(right: 3.0, left: 3.0, top: 3.0)
                    ); 
                  }()
                )
              )
            )
          ],
        )
      ],
    );
  }
}