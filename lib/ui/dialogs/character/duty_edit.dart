import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/duty.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';

class DutyEditDialog {

  final Duty duty;
  final Function(Duty) onClose;

  late Bottom bot;

  DutyEditDialog({Duty? d, required this.onClose}) : duty = d != null ? Duty.from(d) : Duty(){
    var nameController = TextEditingController(text: duty.name);
    nameController.addListener(() {
      duty.name = nameController.text;
      bot.updateButtons();
    });
    var valueController = TextEditingController(text: duty.value?.toString());
    valueController.addListener(() {
      duty.value = int.tryParse(valueController.text);
      bot.updateButtons();
    });
    var descController = TextEditingController(text: duty.desc);
    descController.addListener(() =>
      duty.desc = descController.text
    );
    bot = Bottom(
      child: (context) =>
        Wrap(
          children: [
            Container(height: 15),
            TextField(
              controller: nameController,
              textCapitalization: TextCapitalization.words,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.name
              ),
            ),
            Container(height: 10),
            TextField(
              controller: valueController,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              keyboardType: TextInputType.number,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.duty
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
            )
          ],
        ),
      buttons: (context) => [
        TextButton(
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
          onPressed: duty.name != "" && duty.value != null ? (){
            onClose(duty);
            Navigator.pop(context);
          } : null,
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: () => Navigator.pop(context),
        )]
    );
  }

  void show(BuildContext context) =>
    bot.show(context);
}