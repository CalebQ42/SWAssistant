import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class CharacterInfo extends StatelessWidget {
  final bool editing;
  final EditableContentState state;

  const CharacterInfo({required this.editing, required this.state, Key? key})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null) throw "CharacterInfo card on non Character";
    var species = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing,
          initialText: character.species,
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: () {
            var controller = TextEditingController(text: character.species);
            controller.addListener(() => character.species = controller.text);
            return controller;
          }(),
          title: AppLocalizations.of(context)!.species,
        )
      ],
    );
    var age = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
            editing: editing,
            initialText: character.age.toString(),
            style: Theme.of(context).textTheme.subtitle1,
            defaultSave: true,
            fieldAlign: TextAlign.center,
            controller: () {
              var controller =
                  TextEditingController(text: character.age.toString());
              controller.addListener(
                  () => character.age = int.tryParse(controller.text) ?? 0);
              return controller;
            }(),
            textType: TextInputType.number,
            title: AppLocalizations.of(context)!.age)
      ],
    );
    var motivation = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
            editing: editing,
            initialText: character.motivation,
            style: Theme.of(context).textTheme.subtitle1,
            defaultSave: true,
            fieldAlign: TextAlign.center,
            textCapitalization: TextCapitalization.words,
            controller: () {
              var controller =
                  TextEditingController(text: character.motivation);
              controller
                  .addListener(() => character.motivation = controller.text);
              return controller;
            }(),
            title: AppLocalizations.of(context)!.motivation)
      ],
    );
    var career = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
            editing: editing,
            initialText: character.career,
            style: Theme.of(context).textTheme.subtitle1,
            defaultSave: true,
            fieldAlign: TextAlign.center,
            textCapitalization: TextCapitalization.words,
            controller: () {
              var controller = TextEditingController(text: character.career);
              controller.addListener(() => character.career = controller.text);
              return controller;
            }(),
            title: AppLocalizations.of(context)!.career)
      ],
    );
    var category = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
            editing: editing,
            initialText: character.category,
            style: Theme.of(context).textTheme.subtitle1,
            defaultSave: true,
            fieldAlign: TextAlign.center,
            textCapitalization: TextCapitalization.words,
            controller: () {
              var controller = TextEditingController(text: character.category);
              controller.addListener(() {
                character.category = controller.text;
                SW.of(context).updateCharacterCategories();
              });
              return controller;
            }(),
            title: AppLocalizations.of(context)!.category)
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
              children: <Widget>[species, motivation],
            )),
            Expanded(
                child: Column(
              children: <Widget>[age, career],
            ))
          ],
        ),
        category
      ],
    );
  }
}
