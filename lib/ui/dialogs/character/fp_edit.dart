import 'package:flutter/material.dart';
import 'package:swassistant/items/force_power.dart';
import 'package:darkstorm_common/bottom.dart';
import 'package:swassistant/sw.dart';

class ForcePowerEditDialog{

  final ForcePower fp;
  final Function(ForcePower) onClose;

  late Bottom bot;

  ForcePowerEditDialog({ForcePower? power, required this.onClose}) :
    fp = power == null ? ForcePower() : ForcePower.from(power){
    var nameController = TextEditingController(text: fp.name);
    nameController.addListener(() {
      fp.name = nameController.text;
      bot.updateButtons();
    });
    var descController = TextEditingController(text: fp.desc);
    descController.addListener(() => fp.desc = descController.text);
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          onPressed: fp.name != "" ? () {
            onClose(fp);
            Navigator.of(context).pop();
          } : null,
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
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
                labelText: SW.of(context).locale.forcePower
              ),
              textCapitalization: TextCapitalization.words,
            ),
            Container(height: 10),
            TextField(
              controller: descController,
              decoration: InputDecoration(
                labelText: SW.of(context).locale.desc
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