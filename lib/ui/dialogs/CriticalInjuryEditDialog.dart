import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/CriticalInjury.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class CriticalInjuryEditDialog extends StatefulWidget{
  final CriticalInjury criticalInjury;
  final Function(CriticalInjury) onClose;

  CriticalInjuryEditDialog({CriticalInjury criticalInjury, this.onClose}) :
    this.criticalInjury = criticalInjury == null ? CriticalInjury.nulled() : CriticalInjury.from(criticalInjury);
  
  @override
  State<StatefulWidget> createState() => _CriticalInjuryEditState(criticalInjury: criticalInjury);
}

class _CriticalInjuryEditState extends State{
  final CriticalInjury criticalInjury;
  final Function(CriticalInjury) onClose;

  TextEditingController nameController;
  TextEditingController descController;

  _CriticalInjuryEditState({this.criticalInjury, this.onClose}){
      nameController = TextEditingController(text: criticalInjury.name)
          ..addListener(() {
            if((criticalInjury.name == null && nameController.text != null) || (criticalInjury.name != null && nameController.text == null))
              setState(() => criticalInjury.name = nameController.text);
            else
              criticalInjury.name = nameController.text;
          });
      descController = TextEditingController(text: criticalInjury.desc)
          ..addListener(() {
            if((criticalInjury.desc == null && descController.text != null) || (criticalInjury.desc != null && descController.text == null))
              setState(() => criticalInjury.desc = descController.text);
            else
              criticalInjury.desc = descController.text;
          });
    }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        TextField(
          controller: nameController,
        ),
        Container(height: 10),
        TextField(
          controller: descController,
          maxLines: 3,
          keyboardType: TextInputType.number,
          inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
        ),
        Container(height: 10),
        InputDecorator(
          decoration: InputDecoration(),
          child: DropdownButton<int>(
            isDense: true,
            isExpanded: true,
            items: [
              //TODO
              DropdownMenuItem(
                child: Text(""),
              )
            ],
            onChanged: (value){
              if(criticalInjury.severity == null)
                setState(() => criticalInjury.severity = value);
              else
                criticalInjury.severity = value;
            },
            onTap: () => FocusScope.of(context).unfocus(),
          )
        ),
        ButtonBar(
          children: [
            FlatButton(
              child: Text("Save"),
              onPressed: (){
                Navigator.of(context).pop();
                onClose(criticalInjury);
              },
            ),
            FlatButton(
              child: Text("Cancel"),
              onPressed: (){
                Navigator.of(context).pop();
              },
            )
          ],
        )
      ],
    );
  }
}