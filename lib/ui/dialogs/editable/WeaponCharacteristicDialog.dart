import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/WeaponCharacteristic.dart';

class WeaponCharacteristicDialog extends StatefulWidget{

  final WeaponCharacteristic wc;
  final void Function(WeaponCharacteristic) onClose;

  WeaponCharacteristicDialog({WeaponCharacteristic wc, this.onClose}) :
      this.wc = wc == null ? WeaponCharacteristic.nulled() : WeaponCharacteristic.from(wc);

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

  TextEditingController nameController;
  TextEditingController valueController;
  TextEditingController advantageController;

  _WeaponCharacteristicDialogState(this.wc, this.onClose){
    nameController = TextEditingController(text: wc.name)
        ..addListener((){
          if((wc.name == "" && nameController.text != "") || (wc.name != "" && nameController.text == ""))
            setState(() => wc.name = nameController.text);
          else
            wc.name = nameController.text;
        });
    valueController = TextEditingController(text: wc.value != null ? wc.value.toString() : "")
        ..addListener(() {
          var val = int.tryParse(valueController.text);
          if((wc.value == null && val != null) || (wc.value != null && val == null))
            setState(() => wc.value = val);
          else
            wc.value = val;
        });
    advantageController = TextEditingController(text: wc.advantage != null ? wc.advantage.toString() : "")
        ..addListener(() {
          var val = int.tryParse(advantageController.text);
          if((wc.advantage == null && val != null) || (wc.advantage != null && val == null))
            setState(() => wc.advantage = val);
          else
            wc.advantage = val;
        });
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
              labelText: "Name",
            ),
          ),
          Container(height: 10),
          TextField(
            controller: valueController,
            decoration: InputDecoration(
              labelText: "Rank",
            ),
            keyboardType: TextInputType.number,
            inputFormatters: [FilteringTextInputFormatter.digitsOnly],
          ),
          Container(height: 10),
          TextField(
            controller: advantageController,
            decoration: InputDecoration(
              labelText: "Advantage Needed",
            ),
            keyboardType: TextInputType.number,
            inputFormatters: [FilteringTextInputFormatter.digitsOnly],
          ),
          ButtonBar(
            children: [
              FlatButton(
                onPressed: wc.name != "" && wc.value != null && wc.advantage != null ? (){
                  onClose(wc);
                  Navigator.of(context).pop();
                } : null,
                child: Text("Save")
              ),
              FlatButton(
                onPressed: () =>
                  Navigator.of(context).pop(),
                child: Text("Cancel")
              )
            ],
          )
        ],
      ),
    );
  }
}