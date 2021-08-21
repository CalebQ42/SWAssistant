import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/items/Obligation.dart';

class ObligationEditDialog extends StatefulWidget{

  final Obligation obligation;
  final Function(Obligation) onClose;

  ObligationEditDialog({Obligation? obligation, required this.onClose}) :
    this.obligation = obligation != null ? Obligation.from(obligation) : Obligation();

  @override
  State<StatefulWidget> createState() => ObligationEditDialogState(obligation: this.obligation, onClose: this.onClose);

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) => this
    );
}

class ObligationEditDialogState extends State{

  Obligation obligation;
  Function(Obligation) onClose;

  late TextEditingController nameController;
  late TextEditingController valueController;
  late TextEditingController descController;

  ObligationEditDialogState({required this.obligation, required this.onClose}){
    nameController = TextEditingController(text: obligation.name)
      ..addListener(() =>
        setState(() => obligation.name = nameController.text)
      );
    valueController = TextEditingController(text: obligation.value != -1 ? obligation.value.toString() : "")
      ..addListener(() =>
        setState(() => obligation.value = int.tryParse(valueController.text) ?? -1)
      );
    descController = TextEditingController(text: obligation.desc)
      ..addListener(() =>
        obligation.desc = descController.text
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
              labelText: "Obligation"
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
                onPressed: obligation.name != "" && obligation.value != -1 ? (){
                  onClose(obligation);
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