import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

//TODO: Convert to EditContent compatible. VS Code is having a stroke.

class Morality extends StatefulWidget{

  const Morality({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => MoralityState();
}

class MoralityState extends State<Morality> with StatefulCard {

  bool edit = false;

  set editing(bool b) => editing = b;

  TextEditingController? moralityController;
  TextEditingController? conflictController;
  TextEditingController? strengthController;
  TextEditingController? weaknessController;

  @override
  void initState() {
    super.initState();
    editing = widget.holder.editing;
    widget.holder.reloadFunction = () => setState(() =>
      editing = widget.holder.editing
    );
  }

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null) throw "Morality card used on non Character";
    if(moralityController == null){
      moralityController = TextEditingController(text: character.morality.toString());
      moralityController?.addListener(() =>
        character.morality = int.tryParse(moralityController!.text) ?? 0
      );
    }
    if(conflictController == null){
      conflictController = TextEditingController(text: character.conflict.toString());
      conflictController?.addListener(() {
        character.conflict = int.tryParse(conflictController!.text) ?? 0;
        character.save(context: context);
      });
    }
    if(strengthController == null){
      strengthController = TextEditingController(text: character.emotionalStr);
      strengthController?.addListener(() {
        character.emotionalStr = strengthController!.text;
      });
    }
    if(weaknessController == null){
      weaknessController = TextEditingController(text: character.emotionalWeak);
      weaknessController?.addListener(() {
        character.emotionalWeak = weaknessController!.text;
      });
    }
    return Column(
      children: [
        Row(
          mainAxisSize: MainAxisSize.max,
          children: [
            Expanded(
              child: EditingText(
                editing: editing,
                initialText: character.morality.toString(),
                controller: moralityController,
                textType: TextInputType.number,
                defaultSave: true,
                textAlign: TextAlign.center,
                fieldAlign: TextAlign.center,
                style: Theme.of(context).textTheme.headline6,
                title: AppLocalizations.of(context)!.morality,
              )
            ),
            Expanded(
              child: TextField(
                controller: conflictController,
                keyboardType: TextInputType.number,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.conflict,
                ),
                // decoration: InputDecoration(contentPadding: EdgeInsets.symmetric(horizontal: 5)),
                textAlign: TextAlign.center,
              )
            )
          ],
        ),
        Container(height: 15),
        Row(
          children: [
            Expanded(
              child: SwitchListTile(
                title: Text(AppLocalizations.of(context)!.darkSide),
                value: character.darkSide,
                onChanged: (b) => setState((){
                  character.darkSide = b;
                  character.save(context: context);
                }),
              )
            ),
            Expanded(
              child: ElevatedButton(
                child: Text(AppLocalizations.of(context)!.resolveConflict),
                onPressed: (){
                  var conflict = int.tryParse(conflictController!.text);
                  conflict ??= 0;
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
                    if(character.morality > 100){
                      character.morality = 100;
                    }else if (character.morality < 0){
                      character.morality = 0;
                    }
                    character.save(context: context);
                  });
                },
              ),
            )
          ],
        ),
        Container(height: 15),
        Row(
          mainAxisSize: MainAxisSize.max,
          children: [
            Expanded(
              child: EditingText(
                editing: editing,
                initialText: character.emotionalStr,
                controller: strengthController,
                defaultSave: true,
                textCapitalization: TextCapitalization.words,
                title: AppLocalizations.of(context)!.emoStr
              )
            ),
            Expanded(
              child: EditingText(
                editing: editing,
                initialText: character.emotionalWeak,
                controller: weaknessController,
                defaultSave: true,
                textCapitalization: TextCapitalization.words,
                title: AppLocalizations.of(context)!.emoWeak
              )
            )
          ]
        )
      ],
    );
  }
}