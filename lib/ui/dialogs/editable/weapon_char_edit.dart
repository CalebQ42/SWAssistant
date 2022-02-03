import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/weapon_characteristic.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';

class WeaponCharacteristicDialog{

  final WeaponCharacteristic wc;
  final void Function(WeaponCharacteristic) onClose;

  late Bottom bot;

  //TODO: passive characteristics

  WeaponCharacteristicDialog({WeaponCharacteristic? characteristic, required this.onClose}) :
    wc = characteristic == null ? WeaponCharacteristic() : WeaponCharacteristic.from(characteristic){
    var nameController = TextEditingController(text: wc.name);
    nameController.addListener(() {
      wc.name = nameController.text;
      bot.updateButtons();
    });
    var valueController = TextEditingController(text: wc.value?.toString() ?? "");
    valueController.addListener(() {
      wc.value = int.tryParse(valueController.text);
      bot.updateButtons();
    });
    var advantageController = TextEditingController(text: wc.advantage?.toString() ?? "");
    advantageController.addListener(() {
      wc.advantage = int.tryParse(advantageController.text);
      bot.updateButtons();
    });
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          onPressed: wc.name != "" ? (){
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