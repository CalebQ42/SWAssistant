import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/talent.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';

class TalentEditDialog{

  final Talent talent;
  final Function(Talent) onClose;

  late Bottom bot;

  TalentEditDialog({Talent? tal, required this.onClose}) :
    talent = tal == null ? Talent() : Talent.from(tal){
    var nameController = TextEditingController(text: talent.name);
    nameController.addListener(() {
      talent.name = nameController.text;
      bot.updateButtons();
    });
    var valueController = TextEditingController(text: talent.value?.toString());
    valueController.addListener(() {
      talent.value = int.tryParse(valueController.text);
      bot.updateButtons();
    });
    var descController = TextEditingController(text: talent.desc);
    descController.addListener(() =>
      talent.desc = descController.text
    );
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          onPressed: talent.name != "" && talent.value != null ? (){
            onClose(talent);
            Navigator.of(context).pop();
          } : null,
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
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