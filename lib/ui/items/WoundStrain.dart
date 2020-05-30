
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class WoundStrain extends StatelessWidget{

  final bool editing;
  final Character character;
  final SW app;

  WoundStrain({this.editing,this.character, this.app});

  @override
  Widget build(BuildContext context) {
    var wound = Expanded(
      child:Column(

      )
    );
    return Column(
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text("Soak:"),
            EditingText(
              editing: editing,
              initialText: character.soak.toString(),
              controller: (){
                if(editing){
                  var controller = TextEditingController(text:character.soak.toString());
                  controller.addListener(()=> character.soak = int.parse(controller.text));
                  return controller;
                }else
                  return null;
              }(),
              textType: TextInputType.number,
              defaultSave: true,
              editable: character,
              app: app,
            )
          ],
        ),
        wound,
      ],
    );
  }
}

class WoundStrainSwitcher extends StatefulWidget{

  final bool editing;
  final Character character;
  final bool wound;
  final bool strain;
  final SW app;

  WoundStrainSwitcher({this.editing, this.character, this.wound = false, this.strain = false, this.app}){
    if((wound & strain) || (!wound && !strain))
      throw("Needs to be set to EITHER wound or strain");
  }

  @override
  State<StatefulWidget> createState() {

  }
}

class WoundStrainSwitcherState extends State{

  final bool editing;
  final Character character;
  final bool wound;
  final bool strain;
  final SW app;

  WoundStrainSwitcherState({this.editing,this.character, this.wound = false, this.strain = false, this.app}){
    if((wound & strain) || (!wound && !strain))
      throw("Needs to be set to EITHER wound or strain");
  }

  @override
  Widget build(BuildContext context) {
    Widget meat;
    if(editing){
      meat = EditingText(
        editing: true,
        initialText: wound ? character.woundThresh.toString() : character.strainThresh.toString(),
        controller: (){
          var controller = TextEditingController(text:character.woundThresh.toString());
          controller.addListener(() {
            if(wound){
              character.woundThresh = int.parse(controller.text);
              if(character.woundCur>character.woundThresh)
                character.woundCur = character.woundThresh;
            }else{
              character.strainThresh = int.parse(controller.text);
              if(character.strainCur>character.strainThresh)
                character.strainCur = character.strainThresh;
            }
          });
          return controller;
        }(),
        textType: TextInputType.number,
        defaultSave: true,
        editable: character,
        app: app
      );
    }else{
      meat = Row(
        children: <Widget>[
          IconButton(
            icon: Icon(Icons.add),
            onPressed: (){
              if(wound){
                if(character.woundCur<character.woundThresh){
                  setState(()=>character.woundCur++);
                  character.save(character.getFileLocation(app));
                }
              }else if(strain){
                if(character.strainCur<character.strainThresh){
                  setState(()=>character.strainCur++);
                  character.save(character.getFileLocation(app));
                }
              }
            },
          ),
          AnimatedSwitcher(
            duration: Duration(milliseconds: 200),
            child: Text(
              wound ? character.woundCur.toString() : character.strainCur.toString(),
              key: ValueKey(wound ? character.woundCur.toString() : character.strainCur.toString())
            )
          ),
          IconButton(
            icon: Icon(Icons.remove),
            onPressed: (){
              if(wound){
                if(character.woundCur>0){
                  setState(()=>character.woundCur--);
                  character.save(character.getFileLocation(app));
                }
              }else if(strain){
                if(character.strainCur<character.strainThresh){
                  setState(()=>character.strainCur++);
                  character.save(character.getFileLocation(app));
                }
              }
            },
          ),
        ],
      );
    }
    return AnimatedSwitcher(
      duration: Duration(milliseconds: 300),
      transitionBuilder: (wid, anim){
        Tween<Offset> slide;
        if(wid.key == Key("text")){
          slide = Tween(begin: Offset(-1.0,0.0),end: Offset(0.0,0.0));
        }else{
          slide = Tween(begin: Offset(1.0,0.0), end: Offset(0.0,0.0));
        }
        return ClipRect(
          child: SlideTransition(
            position: slide.animate(anim),
            child: SizeTransition(
              axisAlignment: -1.0,
              sizeFactor: anim,
              child: Center(child: wid),
            )
          )
        );
      },
      child: meat
    );
  }

}