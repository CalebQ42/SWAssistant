import 'package:flutter/material.dart';
import 'package:swassistant/items/ForcePower.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';

class ForcePowerEditDialog{

  final ForcePower fp;
  final Function(ForcePower) onClose;

  late TextEditingController nameController;
  late TextEditingController descController;

  late Bottom bot;

  ForcePowerEditDialog({ForcePower? power, required this.onClose}) :
    fp = power == null ? ForcePower() : ForcePower.from(power){
    nameController = TextEditingController(text: fp.name)
      ..addListener(() {
        fp.name = nameController.text;
        bot.updateButtons();
      });
    descController = TextEditingController(text: fp.desc)
      ..addListener(() => fp.desc = descController.text);
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
          onPressed: fp.name != "" ? () {
            onClose(fp);
            Navigator.of(context).pop();
          } : null,
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed:() =>
            Navigator.of(context).pop(),
        )] ,
      child: (context) =>
        Wrap(
          children: [
            Container(height: 15,),
            TextField(
              controller: nameController,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.forcePower
              ),
              textCapitalization: TextCapitalization.words,
            ),
            Container(height: 10),
            TextField(
              controller: descController,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.desc
              ),
              maxLines: 3,
              minLines: 1,
              textCapitalization: TextCapitalization.sentences,
            )
          ],
        ),
    );
  }

  void show(BuildContext context) =>
    bot.show(context);
}