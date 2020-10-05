import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/items/Duty.dart';

class DutyEditDialog extends StatefulWidget{

  final Duty duty;
  final Function(Duty) onClose;

  DutyEditDialog({Duty duty, this.onClose}) :
    this.duty = duty != null ? Duty.from(duty) : Duty.nulled();

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

  TextEditingController nameController;
  TextEditingController valueController;
  TextEditingController descController;

  DutyEditDialogState({this.duty, this.onClose}){
    nameController = TextEditingController(text: duty.name)
      ..addListener(() {
        if(duty.name != "" && nameController.text != "")
          duty.name = nameController.text;
        else
          setState(() =>
            duty.name = nameController.text
          );
      });
    valueController = TextEditingController(text: duty.value != null ? duty.value.toString() : "")
      ..addListener(() {
        var tmp = int.tryParse(valueController.text);
        if(tmp == null || duty.value == null)
          setState(() => duty.value = tmp);
        else
          duty.value = tmp;
      });
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
              FlatButton(
                child: Text("Save"),
                onPressed: duty.name != "" && duty.value != null ? (){
                  onClose(duty);
                  Navigator.pop(context);
                } : null,
              ),
              FlatButton(
                child: Text("Cancel"),
                onPressed: () => Navigator.pop(context),
              )
            ],
          )
        ],
      ),
    );
}