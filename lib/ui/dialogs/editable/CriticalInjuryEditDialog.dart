import 'package:flutter/material.dart';
import 'package:swassistant/items/CriticalInjury.dart';

class CriticalInjuryEditDialog extends StatefulWidget{
  final CriticalInjury criticalInjury;
  final Function(CriticalInjury) onClose;

  CriticalInjuryEditDialog({CriticalInjury criticalInjury, this.onClose}) :
    this.criticalInjury = criticalInjury == null ? CriticalInjury.nulled() : CriticalInjury.from(criticalInjury);
  
  @override
  State<StatefulWidget> createState() => _CriticalInjuryEditState(criticalInjury: criticalInjury);
  
  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) =>
        this
    );
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
    return DropdownButtonHideUnderline(
      child: Padding(
        padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15)),
        child: Wrap(
          children: [
            Container(height: 15),
            TextField(
              textCapitalization: TextCapitalization.words,
              controller: nameController,
              decoration: InputDecoration(
                    labelText: "Injury",
              ),
            ),
            Container(height: 10),
            TextField(
              textCapitalization: TextCapitalization.sentences,
              controller: descController,
              maxLines: 3,
              minLines: 1,
              decoration: InputDecoration(
                    labelText: "Description",
              ),
            ),
            Container(height: 10),
            InputDecorator(
              decoration: InputDecoration(
                    labelText: "Severity",
              ),
              child: DropdownButton<int>(
                isDense: true,
                isExpanded: true,
                items: [
                  DropdownMenuItem(
                    child: Text("Easy"),
                    value: 0,
                  ),
                  DropdownMenuItem(
                    child: Text("Average"),
                    value: 1,
                  ),
                  DropdownMenuItem(
                    child: Text("Hard"),
                    value: 2,
                  ),
                  DropdownMenuItem(
                    child: Text("Daunting"),
                    value: 3,
                  ),
                ],
                onChanged: (value) =>
                  setState(() => criticalInjury.severity = value),
                value: criticalInjury.severity,
                onTap: () => FocusScope.of(context).unfocus(),
              )
            ),
            ButtonBar(
              children: [
                FlatButton(
                  child: Text("Save"),
                  onPressed: criticalInjury.name != null && criticalInjury.desc != null && criticalInjury.severity != null ? (){
                    Navigator.of(context).pop();
                    onClose(criticalInjury);
                  } : null,
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
        )
      )
    );
  }
}