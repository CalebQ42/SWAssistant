import 'package:flutter/material.dart';
import 'package:swassistant/items/ForcePower.dart';

class ForcePowerEditDialog extends StatefulWidget{

  final ForcePower fp;
  final Function(ForcePower) onClose;

  ForcePowerEditDialog({ForcePower? power, required this.onClose}) :
    fp = power == null ? ForcePower() : ForcePower.from(power);

  @override
  State<StatefulWidget> createState() => _FPState(fp: fp, onClose: onClose);

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) => this
    );
}

class _FPState extends State{

  final ForcePower fp;
  final Function(ForcePower) onClose;

  late TextEditingController nameController;
  late TextEditingController descController;

  _FPState({required this.fp, required this.onClose}){
    nameController = TextEditingController(text: fp.name)
      ..addListener(() =>
        setState(() => fp.name = nameController.text)
      );
    descController = TextEditingController(text: fp.desc)
      ..addListener(() => fp.desc = descController.text);
  }

  @override
  Widget build(BuildContext context) =>
    Padding(
      padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15)),
      child: Wrap(
        children: [
          Container(height: 15,),
          TextField(
            controller: nameController,
            decoration: InputDecoration(
              labelText: "Force Power"
            ),
            textCapitalization: TextCapitalization.words,
          ),
          Container(height: 10),
          TextField(
            controller: descController,
            decoration: InputDecoration(
              labelText: "Description"
            ),
            maxLines: 3,
            minLines: 1,
            textCapitalization: TextCapitalization.sentences,
          ),
          ButtonBar(
            children: [
              TextButton(
                child: Text("Save"),
                onPressed: fp.name != "" ? () {
                  onClose(fp);
                  Navigator.of(context).pop();
                } : null,
              ),
              TextButton(
                child: Text("Cancel"),
                onPressed:() =>
                  Navigator.of(context).pop(),
              )
            ],
          )
        ],
      )
    );
}