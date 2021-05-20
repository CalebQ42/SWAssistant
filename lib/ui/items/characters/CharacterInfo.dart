import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class CharacterInfo extends StatelessWidget{

  final bool editing;
  final EditableContentState state;
  final Function() updateList;

  CharacterInfo({this.editing, this.state, this.updateList});
  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    var species = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: character.species,
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          state: state,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text:character.species);
            controller.addListener(()=>character.species = controller.text);
            return controller;
          }(),
          title: "Species",
        )
      ],
    );
    var age = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: character.age.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          state: state,
          controller: (){
            var controller = TextEditingController(text: character.age.toString());
            controller.addListener((){
              if(controller.text == "")
                character.age = 0;
              else
                character.age = int.parse(controller.text);
            });
            return controller;
          }(),
          textType: TextInputType.number,
          title: "Age"
        )
      ],
    );
    var motivation = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: character.motivation,
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          state: state,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text: character.motivation);
            controller.addListener(() => character.motivation = controller.text);
            return controller;
          }(),
          title: "Motivation"
        )
      ],
    );
    var career = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: character.career,
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          state: state,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text:character.career);
            controller.addListener(()=>character.career = controller.text);
            return controller;
          }(),
          title: "Career"
        )
      ],
    );
    var category = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: character.category,
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          state: state,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text:character.category);
            controller.addListener((){
              character.category = controller.text;
              SW.of(context).updateCharacterCategories();
              updateList();
            });
            return controller;
          }(),
          title: "Category"
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