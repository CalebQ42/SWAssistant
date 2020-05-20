import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditingText extends StatelessWidget{

  final TextStyle style;
  final String initialText;
  final bool editing;
  final TextInputType textType;
  final EdgeInsets fieldInsets;
  final EdgeInsets textInsets;

  final TextEditingController controller;

  EditingText({@required this.editing, this.style, this.initialText = "", this.controller, this.textType, this.fieldInsets = const EdgeInsets.all(4.0), this.textInsets = const EdgeInsets.all(4.0)}){
    if(editing && this.controller == null)
      throw "text controller MUST be specified when in editing mode";
  }
  Widget build(BuildContext context) {
    if(editing)
      return Padding(
        padding: fieldInsets,
        child: Container(
          height: style.fontSize+5,
          alignment: Alignment.center,
          child:TextField(controller: controller, keyboardType: textType,)
        ),
      );
    else
      return Padding(
        padding: textInsets,
        child: Container(
          height: style.fontSize+5,
          alignment: Alignment.center,
          child: Text(initialText, style:style)
        ),
      );
  }
}

class EditableContent extends StatefulWidget{

  final Editable editable;
  final Widget Function(bool editing, Editable editable) builder;

  EditableContent({@required this.builder, @required this.editable});

  @override
  State<StatefulWidget> createState() {
    return EditableContentState(builder: builder, editable: editable);
  }
}

class EditableContentState extends State{

  Widget Function(bool editing, Editable editable) builder;
  bool editing = false;
  Editable editable;

  EditableContentState({@required this.builder, this.editable});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: <Widget>[
        builder(editing, editable),
        IconButton(
          icon: Icon(Icons.edit),
          iconSize: 20.0,
          padding: EdgeInsets.all(5.0),
          constraints: BoxConstraints.tight(Size.square(30.0)),
          color: editing ? Theme.of(context).buttonTheme.colorScheme.onSurface : Theme.of(context).buttonTheme.colorScheme.onSurface.withOpacity(.24),
          onPressed: ()=>setState(()=>editing = !editing),
        )
      ],
    );
  }

}