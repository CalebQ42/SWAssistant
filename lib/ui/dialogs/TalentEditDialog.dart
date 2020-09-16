import 'package:flutter/material.dart';
import 'package:swassistant/items/Talent.dart';

class TalentEditDialog extends StatefulWidget{

  final Talent talent;
  final Function(Talent) onClose;

  TalentEditDialog({Talent talent, this.onClose}) :
    this.talent = talent == null ? Talent.nulled() : talent;

  @override
  State<StatefulWidget> createState() => _TalentEditState(talent: talent, onClose: onClose);
}

class _TalentEditState extends State{

  final Talent talent;
  final Function(Talent) onClose;

  TextEditingController nameController;
  TextEditingController valueController;
  TextEditingController descController;

  _TalentEditState({this.talent, this.onClose}){
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
        if((value == null && talent.value != null) || (value != null && talent.value == null))
          setState(() => talent.value = value);
        else
          talent.value = value;
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
      padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 15)),
      child: Wrap(
        children: [
          TextField(
            
          )
        ],
      ),
    );
}