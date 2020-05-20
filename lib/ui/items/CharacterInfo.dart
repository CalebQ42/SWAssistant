import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class CharacterInfo extends StatelessWidget{

  final bool editing;
  final Character character;
  final SW app;

  CharacterInfo({this.editing, this.character, this.app});
  @override
  Widget build(BuildContext context) {
    var species = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Text("Species:"),
        EditingText(
          editing: editing, 
          initialText: character.species,
          style: Theme.of(context).textTheme.subtitle2,
          fieldInsets: EdgeInsets.all(2.0),
          textInsets: EdgeInsets.symmetric(vertical: 2.0, horizontal: 2.0),
          defaultSave: true,
          editable: character,
          app: app,
          controller: (){
            if(editing){
              var controller = TextEditingController(text:character.species);
              controller.addListener(()=>character.species = controller.text);
              return controller;
            }else
              return null;
          }(),
        )
      ],
    );
    var age = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Text("Age:"),
        EditingText(
          editing: editing, 
          initialText: character.age.toString(),
          style: Theme.of(context).textTheme.subtitle2,
          fieldInsets: EdgeInsets.all(2.0),
          textInsets: EdgeInsets.symmetric(vertical: 2.0, horizontal: 2.0),
          defaultSave: true,
          editable: character,
          app: app,
          controller: (){
            if(editing){
              var controller = TextEditingController(text:character.age.toString());
              controller.addListener(()=>character.age = int.parse(controller.text));
              return controller;
            }else
              return null;
          }(),
          textType: TextInputType.number,
        )
      ],
    );
    var motivation = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Text("Motivation:"),
        EditingText(
          editing: editing, 
          initialText: character.motivation,
          style: Theme.of(context).textTheme.subtitle2,
          fieldInsets: EdgeInsets.all(2.0),
          textInsets: EdgeInsets.symmetric(vertical: 2.0, horizontal: 2.0),
          defaultSave: true,
          editable: character,
          app: app,
          controller: (){
            if(editing){
              var controller = TextEditingController(text:character.motivation);
              controller.addListener(()=>character.motivation = controller.text);
              return controller;
            }else
              return null;
          }(),
        )
      ],
    );
    var career = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Text("Career:"),
        EditingText(
          editing: editing, 
          initialText: character.career,
          style: Theme.of(context).textTheme.subtitle2,
          fieldInsets: EdgeInsets.all(2.0),
          textInsets: EdgeInsets.symmetric(vertical: 2.0, horizontal: 2.0),
          defaultSave: true,
          editable: character,
          app: app,
          controller: (){
            if(editing){
              var controller = TextEditingController(text:character.career);
              controller.addListener(()=>character.career = controller.text);
              return controller;
            }else
              return null;
          }(),
        )
      ],
    );
    var category = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Text("Category:"),
        EditingText(
          editing: editing, 
          initialText: character.category,
          style: Theme.of(context).textTheme.subtitle2,
          fieldInsets: EdgeInsets.all(2.0),
          textInsets: EdgeInsets.symmetric(vertical: 2.0, horizontal: 2.0),
          defaultSave: true,
          editable: character,
          app: app,
          controller: (){
            if(editing){
              var controller = TextEditingController(text:character.category);
              controller.addListener(()=>character.category = controller.text);
              return controller;
            }else
              return null;
          }(),
        )
      ],
    );
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            Expanded(
              child: Column(
                children: <Widget>[
                  species,motivation
                ],
              )
            ),
            Expanded(
              child: Column(
                children: <Widget>[
                  age,career
                ],
              )
            )
          ],
        ),
        category
      ],
    );
  }
}