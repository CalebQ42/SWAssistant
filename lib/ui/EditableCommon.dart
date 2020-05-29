import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditingText extends StatelessWidget{

  final TextStyle style;
  final String initialText;
  final bool editing;
  final TextInputType textType;
  final EdgeInsets fieldInsets;
  final EdgeInsets textInsets;

  final TextEditingController controller;

  final bool defaultSave;
  final Editable editable;
  final SW app;

  EditingText({@required this.editing, this.style, this.initialText = "", this.controller, this.textType,
      this.fieldInsets = const EdgeInsets.symmetric(horizontal:2.0), this.textInsets = const EdgeInsets.all(4.0),
      this.defaultSave = false, this.editable, this.app}){
    if(editing && this.controller == null)
      throw "text controller MUST be specified when in editing mode";
    if(editing && defaultSave && (editable == null || app == null))
      throw "default save needs an editable and SW app";
  }
  Widget build(BuildContext context) {
    if(editing){
      if(defaultSave){
        controller.addListener(() {
          editable.save(editable.getFileLocation(app));
        });
      }
    }
    Widget text;
    if(editing){
      text = Padding(
        key: Key("textField"),
        padding: fieldInsets,
        child: TextField(controller: controller, keyboardType: textType)
      );
    }else{
      text = Padding(
        key: Key("text"),
        padding: textInsets,
        child: Text(initialText, style: style)
      );
    }
    return AnimatedSwitcher(
      duration: Duration(milliseconds: 300),
      child: text,
      transitionBuilder: (wid, anim){
        Tween<Offset> slide;
        if(wid.key == Key("text")){
          slide = Tween(begin: Offset(-1.0,0.0),end: Offset(0.0,0.0));
        }else{
          slide = Tween(begin: Offset(1.0,0.0), end: Offset(0.0,0.0));
        }
        return ClipRect(
          child: SlideTransition(
            position: slide.animate(anim),
            child: SizeTransition(
              sizeFactor: anim,
              child: Center(child: wid),
            )
          )
        );
      },
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
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        builder(editing, editable),
        Align(
          alignment: Alignment.centerRight,
          child: IconButton(
            icon: Icon(Icons.edit),
            iconSize: 20.0,
            padding: EdgeInsets.all(5.0),
            constraints: BoxConstraints.tight(Size.square(30.0)),
            color: editing ? Theme.of(context).buttonTheme.colorScheme.onSurface : Theme.of(context).buttonTheme.colorScheme.onSurface.withOpacity(.24),
            onPressed: ()=>setState(()=>editing = !editing),
          )
        )
      ],
    );
  }

}