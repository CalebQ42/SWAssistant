
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
    return Column(
      children: <Widget>[
        SizedBox.fromSize(
          size: Size.fromWidth(100),
          child: Row(
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
        ),
        
      ],
    );
  }
}