import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class Description extends StatefulWidget{


  const Description({Key? key}) : super(key: key);

  @override
  State<Description> createState() => DescriptionState();
}

class DescriptionState extends State<Description> with StatefulCard {

  var edit = false;
  @override
  bool get defaultEdit => Editable.of(context).desc == "";
  @override
  set editing(bool b) => setState(() => edit = b);

  TextEditingController? descController;

  @override
  Widget build(BuildContext context) {
    if(descController == null){
      descController = TextEditingController();
      descController!.addListener(() => 
        Editable.of(context).desc = descController!.text
      );
    }
    return EditingText(
      editing: edit,
      initialText: Editable.of(context).desc,
      textCapitalization: TextCapitalization.sentences,
      textAlign: TextAlign.start,
      multiline: true,
      controller: descController,
      defaultSave: true,
    );
  }
}