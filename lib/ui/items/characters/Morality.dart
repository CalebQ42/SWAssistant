import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Morality extends StatefulWidget with StatefulCard{

  final EditableContentStatefulHolder holder = EditableContentStatefulHolder();

  Morality(){
    holder.editing = true;
  }

  @override
  State<StatefulWidget> createState() => MoralityState(holder: holder);

  @override
  EditableContentStatefulHolder getHolder() => holder;
}

class MoralityState extends State with TickerProviderStateMixin{

  bool editing = false;

  EditableContentStatefulHolder holder;

  MoralityState({this.holder}){
    editing = holder.editing;
  }

  @override
  Widget build(BuildContext context) {
    holder.reloadFunction = () => setState(() =>
      editing = holder.editing
    );
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
              child: Text("Morality", textAlign: TextAlign.center,),
            ),
            Expanded(
              child: Text("Conflict", textAlign: TextAlign.center),
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
                  state: this
                )
              )
            ),
            Expanded(
              child: TextField(
                controller: conflictController,
                keyboardType: TextInputType.number,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                decoration: InputDecoration(contentPadding: EdgeInsets.symmetric(horizontal: 5)),
                textAlign: TextAlign.center,
              )
            )
          ],
        ),
        FlatButton(
          child: Text("Resolve Conflict"),
          onPressed: (){
            //TODO
          },
        )
        //TODO: emotional strength and weakness
      ],
    );
  }
}