import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/Talent.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';

class TalentEditDialog{

  final Talent talent;
  final Function(Talent) onClose;

  late Bottom bot;

  late TextEditingController nameController;
  late TextEditingController valueController;
  late TextEditingController descController;

  TalentEditDialog({Talent? tal, required this.onClose}) :
    this.talent = tal == null ? Talent() : Talent.from(tal){
    nameController = TextEditingController(text: talent.name)
      ..addListener(() {
        talent.name = nameController.text;
        bot.updateButtons();
      });
    valueController = TextEditingController(text: talent.value == -1 ? "" : talent.value.toString())
      ..addListener(() {
        talent.value = int.tryParse(valueController.text) ?? -1;
        bot.updateButtons();
      });
    descController = TextEditingController(text: talent.desc)
      ..addListener(() =>
        talent.desc = descController.text
      );
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
          onPressed: talent.name != "" && talent.value != -1 ? (){
            onClose(talent);
            Navigator.of(context).pop();
          } : null,
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: () =>
            Navigator.of(context).pop(),
        )
      ],
      child: (context) =>
        Wrap(
          children: [
            Container(height: 15),
            TextField(
              controller: nameController,
              textCapitalization: TextCapitalization.words,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.talent,
              ),
            ),
            Container(height: 10),
            TextField(
              controller: valueController,
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.rank
              ),
            ),
            Container(height: 10),
            TextField(
              controller: descController,
              textCapitalization: TextCapitalization.sentences,
              maxLines: 3,
              minLines: 1,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.desc
              ),
            ),
          ]
        )
    );
  }

  void show(BuildContext context) => bot.show(context);
}