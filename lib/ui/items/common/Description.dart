import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Description extends StatelessWidget{

  final bool editing;

  Description({this.editing});

  @override
  Widget build(BuildContext context) {
    return EditingText(
      editing: editing,
      initialText: Editable.of(context).desc,
      textCapitalization: TextCapitalization.sentences,
      multiline: true,
      controller: (){
        var controller = TextEditingController(text: Editable.of(context).desc);
        controller.addListener(() => 
          Editable.of(context).desc = controller.text
        );
        return controller;
      }(),
      defaultSave: true,
    );
  }
}