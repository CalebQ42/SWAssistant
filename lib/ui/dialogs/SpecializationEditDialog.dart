import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/Character.dart';

class SpecializationEditDialog extends StatefulWidget{

  final Function(String) onClose;
  final String specialization;

  SpecializationEditDialog({this.onClose, this.specialization});

  @override
  State<StatefulWidget> createState() => _SpecializationState(onClose: onClose, specialization: specialization);

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      isScrollControlled: false,
      builder: (context) =>
        this
    );
}

class _SpecializationState extends State{

  final Function(String) onClose;
  String specialization;

  TextEditingController specCont;

  _SpecializationState({this.onClose, this.specialization}){
    specCont = TextEditingController(text: specialization)
      ..addListener(() {
        if((specialization == "" && specCont.text != "") || (specialization != "" && specCont.text == ""))
          setState(() => specialization = specCont.text);
        else
          specialization = specCont.text;
      });
  }

  @override
  Widget build(BuildContext context) =>
    Padding(
      padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 15)),
      child: Wrap(
        children: [
          TextField(
            controller: specCont,
            textCapitalization: TextCapitalization.words,
            decoration: InputDecoration(
              prefixText: "Specialization: ",
              labelText: "Specialization: ",
              floatingLabelBehavior: FloatingLabelBehavior.never
            ),
          ),
          ButtonBar(
            children: [
              FlatButton(
                child: Text("Save"),
                onPressed: specialization != "" ? (){
                  onClose(specialization);
                  Navigator.of(context).pop();
                } : null,
              ),
              FlatButton(
                child: Text("Cancel"),
                onPressed: () =>
                  Navigator.of(context).pop(),
              )
            ],
          )
        ],
      )
    );
}