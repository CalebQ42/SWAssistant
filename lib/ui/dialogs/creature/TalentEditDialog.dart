import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/Talent.dart';

class TalentEditDialog extends StatefulWidget{

  final Talent talent;
  final Function(Talent) onClose;

  TalentEditDialog({Talent? talent, required this.onClose}) :
    this.talent = talent == null ? Talent() : Talent.from(talent);

  @override
  State<StatefulWidget> createState() => _TalentEditState(talent: talent, onClose: onClose);

  void show(BuildContext context) => 
    showModalBottomSheet(
      context: context,
      builder: (context) => this
    );
}

class _TalentEditState extends State{

  final Talent talent;
  final Function(Talent) onClose;

  late TextEditingController nameController;
  late TextEditingController valueController;
  late TextEditingController descController;

  _TalentEditState({required this.talent, required this.onClose}){
    nameController = TextEditingController(text: talent.name)
      ..addListener(() {
        if((talent.name == "" && nameController.text != "") || (talent.name != "" && nameController.text == ""))
          setState(() => talent.name = nameController.text);
        else
          talent.name = nameController.text;
      });
    valueController = TextEditingController(text: talent.value.toString())
      ..addListener(() {
        var value = int.tryParse(valueController.text);
        if((value == null && talent.value != -1) || (value != null && talent.value == -1))
          setState(() => talent.value = value ?? -1);
        else
          talent.value = value ?? -1;
      });
    descController = TextEditingController(text: talent.desc)
      ..addListener(() {
        if((talent.desc == "" && descController.text != "") || (talent.desc != "" && descController.text == ""))
          setState(() => talent.desc = descController.text);
        else
          talent.desc = descController.text;
      });
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
              labelText: "Talent",
            ),
          ),
          Container(height: 10),
          TextField(
            controller: valueController,
            keyboardType: TextInputType.number,
            inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            decoration: InputDecoration(
              labelText: "Rank",
            ),
          ),
          Container(height: 10),
          TextField(
            controller: descController,
            textCapitalization: TextCapitalization.sentences,
            maxLines: 3,
            minLines: 1,
            decoration: InputDecoration(
              labelText: "Description",
            ),
          ),
          ButtonBar(
            children: [
              TextButton(
                child: Text("Save"),
                onPressed: talent.name != "" && talent.value != -1 ? (){
                  onClose(talent);
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
        ],
      ),
    );
}