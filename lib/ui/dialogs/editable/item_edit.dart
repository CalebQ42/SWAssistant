import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/item.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';

class ItemEditDialog{

  final void Function(Item) onClose;
  final Editable editable;
  final Item item;

  late TextEditingController nameController;
  late TextEditingController countController;
  late TextEditingController encumController;
  late TextEditingController descController;

  late Bottom bot;

  ItemEditDialog({required this.onClose, Item? it, required this.editable}) :
    item = it == null ? Item() : Item.from(it){
    nameController = TextEditingController(text: item.name)
      ..addListener(() {
        item.name = nameController.text;
        bot.updateButtons();
      });
    countController = TextEditingController(text: item.count.toString())
      ..addListener(() =>
        item.count = int.tryParse(countController.text) ?? 1
      );
    encumController = TextEditingController(text: item.encum.toString())
      ..addListener(() =>
        item.encum = int.tryParse(encumController.text) ?? 0
      );
    descController = TextEditingController(text: item.desc)
      ..addListener(() =>
        item.desc = descController.text
      );
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
          onPressed: item.name != "" ? (){
            onClose(item);
            Navigator.of(context).pop();
          } : null,
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: () =>
            Navigator.of(context).pop(),
        )],
      child: (context) =>
        Wrap(
          children: [
            Container(height: 15),
            TextField(
              textCapitalization: TextCapitalization.words,
              controller: nameController,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.item
              ),
            ),
            Container(height: 10),
            TextField(
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              keyboardType: TextInputType.number,
              controller: countController,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.count
              ),
            ),
            Container(height: 10),
            TextField(
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              keyboardType: TextInputType.number,
              controller: encumController,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.encumTotal
              ),
            ),
            Container(height: 10),
            TextField(
              textCapitalization: TextCapitalization.sentences,
              controller: descController,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.desc
              ),
              maxLines: 3,
              minLines: 1,
            ),
          ]
        )
    );
  }
  
  void show(BuildContext context) => bot.show(context);
}