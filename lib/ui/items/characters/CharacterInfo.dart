import 'package:flutter/material.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class CharacterInfo extends StatelessWidget{

  final bool editing;
  final EditableContentState state;

  CharacterInfo({this.editing, this.state});
  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    var species = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        Text("Species:"),
        EditingText(
          editing: editing, 
          initialText: character.species,
          style: Theme.of(context).textTheme.subtitle2,
          defaultSave: true,
          collapsed: true,
          fieldAlign: TextAlign.center,
          fieldInsets: EdgeInsets.all(3),
          state: state,
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
          defaultSave: true,
          collapsed: true,
          fieldAlign: TextAlign.center,
          fieldInsets: EdgeInsets.all(3),
          state: state,
          controller: (){
            if(editing){
              var controller = TextEditingController(text:character.age.toString());
              controller.addListener((){
                if(controller.text == "")
                  character.age = 0;
                else
                  character.age = int.parse(controller.text);
              });
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
          defaultSave: true,
          collapsed: true,
          fieldAlign: TextAlign.center,
          fieldInsets: EdgeInsets.all(3),
          state: state,
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
          defaultSave: true,
          collapsed: true,
          fieldAlign: TextAlign.center,
          fieldInsets: EdgeInsets.all(3),
          state: state,
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
          defaultSave: true,
          collapsed: true,
          fieldAlign: TextAlign.center,
          fieldInsets: EdgeInsets.all(3),
          state: state,
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