import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/obligation.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';

class ObligationEditDialog{

  final Obligation obligation;
  final Function(Obligation) onClose;

  late Bottom bot;

  late TextEditingController nameController;
  late TextEditingController valueController;
  late TextEditingController descController;

  ObligationEditDialog({Obligation? obli, required this.onClose}) :
    this.obligation = obli != null ? Obligation.from(obli) : Obligation(){
    nameController = TextEditingController(text: obligation.name)
      ..addListener(() {
        obligation.name = nameController.text;
        bot.updateButtons();
      });
    valueController = TextEditingController(text: obligation.value != -1 ? obligation.value.toString() : "")
      ..addListener(() {
        obligation.value = int.tryParse(valueController.text) ?? -1;
        bot.updateButtons();
      });
    descController = TextEditingController(text: obligation.desc)
      ..addListener(() =>
        obligation.desc = descController.text
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
                labelText: AppLocalizations.of(context)!.obligation
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
      buttons: (context) =>[
        TextButton(
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
          onPressed: obligation.name != "" && obligation.value != -1 ? (){
            onClose(obligation);
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