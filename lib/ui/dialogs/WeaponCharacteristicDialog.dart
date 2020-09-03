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
    valueController = TextEditingController(text: wc.value.toString())
        ..addListener(() {
          var val = int.tryParse(valueController.text);
          if((wc.value == null && val != null) || (wc.value != null && val == null))
            setState(() => wc.value = val);
          else
            wc.value = val;
        });
    advantageController = TextEditingController(text: wc.advantage.toString())
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
    return AlertDialog(
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          TextField(
            controller: nameController,
            decoration: InputDecoration(
              prefixIcon: Text("Name: ", style: TextStyle(color:Theme.of(context).hintColor),),
              prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
            ),
          ),
          TextField(
            controller: valueController,
            decoration: InputDecoration(
              prefixIcon: Text("Value: ", style: TextStyle(color:Theme.of(context).hintColor),),
              prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
            ),
            keyboardType: TextInputType.number,
            inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
          ),
          TextField(
            controller: nameController,
            decoration: InputDecoration(
              prefixIcon: Text("Advantage Needed: ", style: TextStyle(color:Theme.of(context).hintColor),),
              prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
            ),
            keyboardType: TextInputType.number,
            inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
          ),
        ],
      ),
      actions: [
        FlatButton(
          onPressed: (){
            onClose(wc);
            Navigator.of(context).pop();
          },
          child: Text("Save")
        ),
        FlatButton(
          onPressed: (){
            Navigator.of(context).pop();
          },
          child: Text("Cancel")
        )
      ],
    );
  }
}