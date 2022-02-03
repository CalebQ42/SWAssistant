import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class Description extends StatelessWidget{

  final bool editing;

  const Description({required this.editing, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return EditingText(
      editing: editing,
      initialText: Editable.of(context).desc,
      textCapitalization: TextCapitalization.sentences,
      textAlign: TextAlign.start,
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