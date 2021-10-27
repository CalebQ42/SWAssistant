import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/WeaponCharacteristic.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';

class WeaponCharacteristicDialog{

  final WeaponCharacteristic wc;
  final void Function(WeaponCharacteristic) onClose;

  late Bottom bot;

  late TextEditingController nameController;
  late TextEditingController valueController;
  late TextEditingController advantageController;

  WeaponCharacteristicDialog({WeaponCharacteristic? characteristic, required this.onClose}) :
      this.wc = characteristic == null ? WeaponCharacteristic() : WeaponCharacteristic.from(characteristic){
    nameController = TextEditingController(text: wc.name)
        ..addListener(() {
          wc.name = nameController.text;
          bot.updateButtons();
        });
    valueController = TextEditingController(text: wc.value != -1 ? wc.value.toString() : "")
        ..addListener(() {
          wc.value = int.tryParse(valueController.text) ?? -1;
          bot.updateButtons();
        });
    advantageController = TextEditingController(text: wc.advantage != -1 ? wc.advantage.toString() : "")
        ..addListener(() {
          wc.advantage = int.tryParse(advantageController.text) ?? -1;
          bot.updateButtons();
        });
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          onPressed: wc.name != "" && wc.value != -1 && wc.advantage != -1 ? (){
            onClose(wc);
            Navigator.of(context).pop();
          } : null,
          child: Text(MaterialLocalizations.of(context).saveButtonLabel)
        ),
        TextButton(
          onPressed: () =>
            Navigator.of(context).pop(),
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel)
        )],
      child: (context) =>
        Wrap(
          children: [
            Container(height: 10),
            TextField(
              controller: nameController,
              textCapitalization: TextCapitalization.words,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.characteristic
              ),
            ),
            Container(height: 10),
            TextField(
              controller: valueController,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.rank
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            ),
            Container(height: 10),
            TextField(
              controller: advantageController,
              decoration: InputDecoration(
                labelText: AppLocalizations.of(context)!.advNeeded
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            ),
          ],
        ),
    );
  }

  void show(BuildContext context) => bot.show(context);
}