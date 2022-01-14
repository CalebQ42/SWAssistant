import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Morality extends StatefulWidget with StatefulCard{

  final EditableContentStatefulHolder holder = EditableContentStatefulHolder();

  @override
  State<StatefulWidget> createState() => MoralityState(holder: holder);

  @override
  EditableContentStatefulHolder getHolder() => holder;
}

class MoralityState extends State with TickerProviderStateMixin{

  bool editing = false;

  EditableContentStatefulHolder holder;

  TextEditingController? moralityController;
  TextEditingController? conflictController;
  TextEditingController? strengthController;
  TextEditingController? weaknessController;

  MoralityState({required this.holder}){
    editing = holder.editing;
    holder.reloadFunction = () => setState(() =>
      editing = holder.editing
    );
  }

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null)
      throw "Morality card used on non Character";
    if(moralityController == null){
      moralityController = new TextEditingController(text: character.morality.toString());
      moralityController?.addListener(() =>
        character.morality = int.tryParse(moralityController!.text) ?? 0
      );
    }
    if(conflictController == null){
      conflictController = new TextEditingController(text: character.conflict.toString());
      conflictController?.addListener(() {
        character.conflict = int.tryParse(conflictController!.text) ?? 0;
        character.save(context: context);
      });
    }
    if(strengthController == null){
      strengthController = new TextEditingController(text: character.emotionalStr);
      strengthController?.addListener(() {
        character.emotionalStr = strengthController!.text;
      });
    }
    if(weaknessController == null){
      weaknessController = new TextEditingController(text: character.emotionalWeak);
      weaknessController?.addListener(() {
        character.emotionalWeak = weaknessController!.text;
      });
    }
    return Column(
      children: [
        Row(
          children: [
            Expanded(
              child: Text(AppLocalizations.of(context)!.morality, textAlign: TextAlign.center,),
            ),
            Container(width: 10),
            Expanded(
              child: Text(AppLocalizations.of(context)!.conflict, textAlign: TextAlign.center),
            )
          ],
        ),
        Container(height: 5,),
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
                  style: Theme.of(context).textTheme.headline6,
                )
              )
            ),
            Expanded(
              child: TextField(
                controller: conflictController,
                keyboardType: TextInputType.number,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                // decoration: InputDecoration(contentPadding: EdgeInsets.symmetric(horizontal: 5)),
                textAlign: TextAlign.center,
              )
            )
          ],
        ),
        Container(height: 5,),
        Center(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(AppLocalizations.of(context)!.darkSide),
              Switch(
                value: character.darkSide,
                onChanged: (b) => setState((){
                  character.darkSide = b;
                  character.save(context: context);
                }),
              ),
              ElevatedButton(
                child: Text(AppLocalizations.of(context)!.resolveConflict),
                onPressed: (){
                  var conflict = int.tryParse(conflictController!.text);
                  if(conflict == null)
                    conflict = 0;
                  conflictController!.text = "0";
                  character.conflict = 0;
                  var resolution = Random().nextInt(9) + 1;
                  ScaffoldMessenger.of(context).clearSnackBars();
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                    content: Text(AppLocalizations.of(context)!.conflictResult(resolution)),
                  ));
                  resolution = resolution - conflict;
                  setState((){
                    character.morality += character.darkSide ? -resolution : resolution;
                    if(character.morality > 100)
                      character.morality = 100;
                    else if (character.morality < 0)
                      character.morality = 0;
                    character.save(context: context);
                  });
                },
              ),
            ],
          )
        ),
        Container(height: 5,),
        Row(
          children: [
            Expanded(
              child: Center(child: Text(AppLocalizations.of(context)!.emoStr, style: Theme.of(context).textTheme.subtitle1,)),
            ),
            Expanded(
              child: Center(child: Text(AppLocalizations.of(context)!.emoWeak, style: Theme.of(context).textTheme.subtitle1,))
            )
          ]
        ),
        Container(height: 5),
        Row(
          children: [
            Expanded(
              child: EditingText(
                editing: editing,
                initialText: character.emotionalStr,
                controller: strengthController,
                defaultSave: true,
                textCapitalization: TextCapitalization.words,
                textAlign: TextAlign.center,
                fieldAlign: TextAlign.center,
              )
            ),
            Expanded(
              child: EditingText(
                editing: editing,
                initialText: character.emotionalWeak,
                controller: weaknessController,
                defaultSave: true,
                textCapitalization: TextCapitalization.words,
                textAlign: TextAlign.center,
                fieldAlign: TextAlign.center,
              )
            )
          ]
        )
      ],
    );
  }
}