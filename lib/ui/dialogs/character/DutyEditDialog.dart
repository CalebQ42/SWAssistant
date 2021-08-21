import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/items/Duty.dart';

class DutyEditDialog extends StatefulWidget{

  final Duty duty;
  final Function(Duty) onClose;

  DutyEditDialog({Duty? duty, required this.onClose}) :
    this.duty = duty != null ? Duty.from(duty) : Duty();

  @override
  State<StatefulWidget> createState() => DutyEditDialogState(duty: this.duty, onClose: this.onClose);

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) => this
    );
}

class DutyEditDialogState extends State{

  Duty duty;
  Function(Duty) onClose;

  late TextEditingController nameController;
  late TextEditingController valueController;
  late TextEditingController descController;

  DutyEditDialogState({required this.duty, required this.onClose}){
    nameController = TextEditingController(text: duty.name)
      ..addListener(() =>
        setState(() =>
          duty.name = nameController.text
        )
      );
    valueController = TextEditingController(text: duty.value != -1 ? duty.value.toString() : "")
      ..addListener(() =>
        setState(() =>
          duty.value = int.tryParse(valueController.text) ?? -1
        )
      );
    descController = TextEditingController(text: duty.desc)
      ..addListener(() =>
        duty.desc = descController.text
      );
  }

  @override
  Widget build(BuildContext context) =>
    Padding(
      padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15)),
      child: Wrap(
        children: [
          Container(height: 15),
          TextField(
            controller: nameController,
            textCapitalization: TextCapitalization.words,
            decoration: InputDecoration(
              labelText: "Name"
            ),
          ),
          Container(height: 10),
          TextField(
            controller: valueController,
            inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            keyboardType: TextInputType.number,
            decoration: InputDecoration(
              labelText: "Duty"
            ),
          ),
          Container(height: 10),
          TextField(
            controller: descController,
            textCapitalization: TextCapitalization.sentences,
            maxLines: 3,
            minLines: 1,
            decoration: InputDecoration(
              labelText: "Description"
            ),
          ),
          ButtonBar(
            children: [
              TextButton(
                child: Text("Save"),
                onPressed: duty.name != "" && duty.value != -1 ? (){
                  onClose(duty);
                  Navigator.pop(context);
                } : null,
              ),
              TextButton(
                child: Text("Cancel"),
                onPressed: () => Navigator.pop(context),
              )
            ],
          )
        ],
      ),
    );
}