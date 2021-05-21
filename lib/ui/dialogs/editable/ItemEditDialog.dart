import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class ItemEditDialog extends StatefulWidget{

  final void Function(Item?) onClose;
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

  final void Function(Item?) onClose;
  final Editable editable;
  final Item item;

  late TextEditingController nameController;
  late TextEditingController countController;
  late TextEditingController encumController;
  late TextEditingController descController;

  _ItemEditDialogState({required this.onClose, required this.editable, required this.item}){
    nameController = TextEditingController(text: item.name)
      ..addListener(() {
        if((nameController.text == "" && item.name != "") || (nameController.text != "" && item.name == ""))
          setState(() => item.name = nameController.text);
        else
          item.name = nameController.text;
      });
    countController = TextEditingController(text: item.count.toString())
      ..addListener(() {
        var tmp = int.tryParse(countController.text);
        if((tmp == null && item.count != -1) || (tmp != null && item.count == -1))
          setState(() => item.count = tmp ?? -1);
        else
          item.count = tmp ?? -1;
      });
    encumController = TextEditingController(text: item.encum.toString())
      ..addListener(() {
        var tmp = int.tryParse(encumController.text);
        if((tmp == null && item.encum != -1) || (tmp != null && item.encum == -1))
          setState(() => item.encum = tmp ?? -1);
        else
          item.encum = tmp ?? -1;
      });
    descController = TextEditingController(text: item.desc)
      ..addListener(() {
        if((descController.text == "" && item.desc != "") || (descController.text != "" && item.desc == ""))
          setState(() => item.desc = descController.text);
        else
          item.desc = descController.text;
      });
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
                  onPressed: (){
                    onClose(item);
                    Navigator.of(context).pop();
                  },
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