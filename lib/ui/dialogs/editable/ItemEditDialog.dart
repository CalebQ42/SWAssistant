import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class ItemEditDialog extends StatefulWidget{

  final void Function(Item) onClose;
  final Editable editable;
  final Item item;

  ItemEditDialog({required this.onClose, Item? item, required this.editable}) :
      this.item = item == null ? Item() : Item.from(item);

  @override
  State<StatefulWidget> createState() => _ItemEditDialogState(onClose: onClose, item: item, editable: editable);
  
  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) =>
        this
    );
}

class _ItemEditDialogState extends State{

  final void Function(Item) onClose;
  final Editable editable;
  final Item item;

  late TextEditingController nameController;
  late TextEditingController countController;
  late TextEditingController encumController;
  late TextEditingController descController;

  _ItemEditDialogState({required this.onClose, required this.editable, required this.item}){
    nameController = TextEditingController(text: item.name)
      ..addListener(() =>
        setState(() => item.name = nameController.text)
      );
    countController = TextEditingController(text: item.count.toString())
      ..addListener(() =>
        item.count = int.tryParse(countController.text) ?? 1
      );
    encumController = TextEditingController(text: item.encum.toString())
      ..addListener(() =>
        item.encum = int.tryParse(encumController.text) ?? 0
      );
    descController = TextEditingController(text: item.desc)
      ..addListener(() =>
        item.desc = descController.text
      );
  }

  @override
  Widget build(BuildContext context) =>
    DropdownButtonHideUnderline(
      child: Padding(
        padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15)),
        child: Wrap(
          children: [
            Container(height: 15),
            TextField(
              textCapitalization: TextCapitalization.words,
              controller: nameController,
              decoration: InputDecoration(
                labelText: "Name"
              ),
            ),
            Container(height: 10),
            TextField(
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              keyboardType: TextInputType.number,
              controller: countController,
              decoration: InputDecoration(
                labelText: "Count"
              ),
            ),
            Container(height: 10),
            TextField(
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              keyboardType: TextInputType.number,
              controller: encumController,
              decoration: InputDecoration(
                labelText: "Encumbrance (Total)"
              ),
            ),
            Container(height: 10),
            TextField(
              textCapitalization: TextCapitalization.sentences,
              controller: descController,
              decoration: InputDecoration(
                labelText: "Description"
              ),
              maxLines: 3,
              minLines: 1,
            ),
            ButtonBar(
              children: [
                TextButton(
                  child: Text("Save"),
                  onPressed: item.name != "" ? (){
                    onClose(item);
                    Navigator.of(context).pop();
                  } : null,
                ),
                TextButton(
                  child: Text("Cancel"),
                  onPressed: () =>
                    Navigator.of(context).pop(),
                )
              ],
            )
          ]
        )
      )
    );
}