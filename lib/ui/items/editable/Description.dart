import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Description extends StatelessWidget{

  final bool editing;
  final EditableContentState state;

  Description({this.editing, this.state});

  @override
  Widget build(BuildContext context) {
    return EditingText(
      editing: editing,
      initialText: Editable.of(context).desc,
      textCapitalization: TextCapitalization.sentences,
      textAlign: TextAlign.start,
      multiline: true,
      state: state,
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