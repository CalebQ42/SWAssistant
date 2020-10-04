import 'package:flutter/material.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Morality extends StatelessWidget{

  final Function refresh;
  final bool editing;
  final EditableContentState state;

  Morality({this.refresh, this.editing, this.state});

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    var moralityController = new TextEditingController(text: character.morality.toString());
    moralityController.addListener(() =>
      character.morality = int.tryParse(moralityController.text) ?? 0
    );
    var conflictController = new TextEditingController(text: character.conflict.toString());
    conflictController.addListener(() {
      character.conflict = int.tryParse(conflictController.text) ?? 0;
      character.save(context: context);
    });
    return Column(
      children: [
        Row(
          children: [
            Expanded(
              child: Text("Morality"),
            ),
            Expanded(
              child: Text("Conflict"),
            )
          ],
        ),
        Row(
          children: [
            Expanded(
              child: Center(
                child: EditingText(
                  editing: editing,
                  initialText: character.morality.toString(),
                  controller: moralityController,
                  textType: TextInputType.number,
                  defaultSave: true,
                  textAlign: TextAlign.center,
                  fieldAlign: TextAlign.center,
                  state: state
                )
              )
            ),
            Expanded(
              child: TextField(

              )
            )
          ],
        )
      ],
    );
  }
}