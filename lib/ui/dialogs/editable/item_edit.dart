import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/item.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:darkstorm_common/bottom.dart';
import 'package:swassistant/sw.dart';

class ItemEditDialog{

  final void Function(Item) onClose;
  final Editable editable;
  final Item item;

  late Bottom bot;

  ItemEditDialog({required this.onClose, Item? it, required this.editable}) :
    item = it == null ? Item() : Item.from(it){
    var nameController = TextEditingController(text: item.name);
    nameController.addListener(() {
      item.name = nameController.text;
      bot.updateButtons();
    });
    var countController = TextEditingController(text: item.count.toString());
    countController.addListener(() =>
      item.count = int.tryParse(countController.text) ?? 1
    );
    var encumController = TextEditingController(text: item.encum.toString());
    encumController.addListener(() =>
      item.encum = int.tryParse(encumController.text) ?? 0
    );
    var descController = TextEditingController(text: item.desc);
    descController.addListener(() =>
      item.desc = descController.text
    );
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          onPressed: item.name != "" ? (){
            onClose(item);
            Navigator.of(context).pop();
          } : null,
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
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
                labelText: SW.of(context).locale.item
              ),
            ),
            Container(height: 10),
            TextField(
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              keyboardType: TextInputType.number,
              controller: countController,
              decoration: InputDecoration(
                labelText: SW.of(context).locale.count
              ),
            ),
            Container(height: 10),
            TextField(
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              keyboardType: TextInputType.number,
              controller: encumController,
              decoration: InputDecoration(
                labelText: SW.of(context).locale.encumTotal
              ),
            ),
            Container(height: 10),
            TextField(
              textCapitalization: TextCapitalization.sentences,
              controller: descController,
              decoration: InputDecoration(
                labelText: SW.of(context).locale.desc
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