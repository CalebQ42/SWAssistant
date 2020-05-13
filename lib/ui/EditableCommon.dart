import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditingText extends StatelessWidget{

  final TextStyle style;
  final String initialText;
  final bool editing;

  final TextEditingController controller;

  EditingText({@required this.editing, this.style, this.initialText = "", this.controller}){
    if(editing && this.controller == null)
      throw "text controller MUST be specified when in editing mode";
  }
  Widget build(BuildContext context) {
    if(editing)
      return TextField(controller: controller, style: style);
    else
      return Padding(
        padding: EdgeInsets.all(12.0),
        child:Text(initialText, style:style)
      );
  }
}

class EditableContent extends StatefulWidget{

  final Widget Function(bool editing) builder;

  EditableContent({@required this.builder});

  @override
  State<StatefulWidget> createState() {
    return EditableContentState(builder: builder);
  }

}

class EditableContentState extends State{

  Widget Function(bool editing) builder;
  bool editing = false;

  EditableContentState({@required this.builder});

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        builder(editing),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: <Widget>[
            IconButton(
              icon: Icon(Icons.edit),
              iconSize: 20.0,
              padding: EdgeInsets.all(5.0),
              constraints: BoxConstraints.tight(Size.square(30.0)),
              color: editing ? Colors.white : Colors.white30,
              onPressed: (){
                setState(()=>editing = !editing);
              },
            )
          ],
        )
      ],
    );
  }

}