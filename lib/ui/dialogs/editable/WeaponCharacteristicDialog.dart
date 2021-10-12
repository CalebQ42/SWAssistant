import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/WeaponCharacteristic.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class WeaponCharacteristicDialog extends StatefulWidget{

  final WeaponCharacteristic wc;
  final void Function(WeaponCharacteristic) onClose;

  WeaponCharacteristicDialog({WeaponCharacteristic? wc, required this.onClose}) :
      this.wc = wc == null ? WeaponCharacteristic() : WeaponCharacteristic.from(wc);

  @override
  State<StatefulWidget> createState() => _WeaponCharacteristicDialogState(wc, onClose);

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) =>
        this
    );
}

class _WeaponCharacteristicDialogState extends State{
  
  final WeaponCharacteristic wc;
  final Function(WeaponCharacteristic) onClose;

  late TextEditingController nameController;
  late TextEditingController valueController;
  late TextEditingController advantageController;

  _WeaponCharacteristicDialogState(this.wc, this.onClose){
    nameController = TextEditingController(text: wc.name)
        ..addListener(() =>
          setState(() => wc.name = nameController.text)
        );
    valueController = TextEditingController(text: wc.value != -1 ? wc.value.toString() : "")
        ..addListener(() {
          setState(() => wc.value = int.tryParse(valueController.text) ?? -1);
        });
    advantageController = TextEditingController(text: wc.advantage != -1 ? wc.advantage.toString() : "")
        ..addListener(() =>
          setState(() => wc.advantage = int.tryParse(advantageController.text) ?? -1)
        );
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15)),
      child: Column(
        mainAxisSize: MainAxisSize.min,
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
          ButtonBar(
            children: [
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
              )
            ],
          )
        ],
      ),
    );
  }
}