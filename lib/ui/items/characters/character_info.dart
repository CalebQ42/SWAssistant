import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class CharacterInfo extends StatefulWidget{

  const CharacterInfo({Key? key}) : super(key: key);

  @override
  State<CharacterInfo> createState() => CharacterInfoState();
}

class CharacterInfoState extends State<CharacterInfo> with StatefulCard {

  bool edit = false;

  @override
  set editing(bool b) => setState(() => edit = b);

  @override
  bool get defaultEdit {
    var character = Character.of(context)!;
    return character.species == "" && character.age == 0 && character.motivation == "" &&
      character.career == "" && character.category == "";
  }

  TextEditingController? speciesController;
  TextEditingController? ageController;
  TextEditingController? motivationController;
  TextEditingController? careerController;
  TextEditingController? categoryController;
  
  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null) throw "CharacterInfo card on non Character";
    if(speciesController == null){
      speciesController = TextEditingController(text: character.species);
      speciesController!.addListener(() => character.species = speciesController!.text);
      ageController = TextEditingController(text: character.age.toString());
      ageController!.addListener(() => character.age = int.tryParse(ageController!.text) ?? 0);
      motivationController = TextEditingController(text: character.motivation);
      motivationController!.addListener(() => character.motivation = motivationController!.text);
      careerController = TextEditingController(text: character.career);
      careerController!.addListener(() => character.career = careerController!.text);
      categoryController = TextEditingController(text: character.category);
      categoryController!.addListener(() => SW.of(context).updateCategory(character, categoryController!.text));
    }
    var species = EditingText(
      editing: edit, 
      initialText: character.species,
      style: Theme.of(context).textTheme.titleMedium,
      defaultSave: true,
      fieldAlign: TextAlign.center,
      textCapitalization: TextCapitalization.words,
      controller: speciesController,
      title: AppLocalizations.of(context)!.species,
    );
    var age = EditingText(
      editing: edit,
      initialText: character.age.toString(),
      style: Theme.of(context).textTheme.titleMedium,
      defaultSave: true,
      fieldAlign: TextAlign.center,
      controller: ageController,
      textType: TextInputType.number,
      title: AppLocalizations.of(context)!.age
    );
    var motivation = EditingText(
      editing: edit, 
      initialText: character.motivation,
      style: Theme.of(context).textTheme.titleMedium,
      defaultSave: true,
      fieldAlign: TextAlign.center,
      textCapitalization: TextCapitalization.words,
      controller: motivationController,
      title: AppLocalizations.of(context)!.motivation
    );
    var career = EditingText(
      editing: edit, 
      initialText: character.career,
      style: Theme.of(context).textTheme.titleMedium,
      defaultSave: true,
      fieldAlign: TextAlign.center,
      textCapitalization: TextCapitalization.words,
      controller: careerController,
      title: AppLocalizations.of(context)!.career
    );
    var category = EditingText(
      editing: edit, 
      initialText: character.category,
      style: Theme.of(context).textTheme.titleMedium,
      defaultSave: true,
      fieldAlign: TextAlign.center,
      textCapitalization: TextCapitalization.words,
      controller: categoryController,
      title: AppLocalizations.of(context)!.category
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