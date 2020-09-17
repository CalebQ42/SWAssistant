import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditingText extends StatelessWidget {

  final Key key;

  final TextStyle style;
  final String initialText;
  final bool editing;
  final TextInputType textType;
  final EdgeInsets fieldInsets;
  final EdgeInsets textInsets;
  final bool multiline;
  final TextCapitalization textCapitalization;
  final TextAlign textAlign;
  final TextAlign fieldAlign;

  final TextEditingController controller;

  final bool defaultSave;

  EditingText({this.key, @required this.editing, this.style, this.initialText = "", this.controller, this.textType,
      this.fieldInsets = const EdgeInsets.symmetric(horizontal:2.0), this.textInsets = const EdgeInsets.all(4.0),
      this.defaultSave = false, this.multiline = false, this.textCapitalization = TextCapitalization.none, this.textAlign = TextAlign.start,
      this.fieldAlign = TextAlign.start}){
    if(editing && this.controller == null)
      throw "text controller MUST be specified when in editing mode";
  }
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    if(editing){
      if(defaultSave){
        controller.addListener(() {
          editable.save(context: context);
        });
      }
    }
    Widget text;
    if(editing){
      text = Padding(
        key: ValueKey("textField"),
        padding: fieldInsets,
        child: TextField(
          maxLines: multiline ? null : 1,
          controller: controller,
          keyboardType: textType,
          inputFormatters: textType == TextInputType.number ? [WhitelistingTextInputFormatter.digitsOnly] : null,
          textCapitalization: textCapitalization,
          textAlign: fieldAlign,
        )
      );
    }else{
      text = Padding(
        key: ValueKey("text"),
        padding: textInsets,
        child: Text(initialText, style: style, textAlign: textAlign,)
      );
    }
    return AnimatedSwitcher(
      duration: Duration(milliseconds: 300),
      child: text,
      transitionBuilder: (wid, anim){
        Tween<Offset> slide;
        if(wid.key == ValueKey("text")){
          slide = Tween(begin: Offset(0.0,-1.0),end: Offset(0.0,0.0));
        }else{
          slide = Tween(begin: Offset(0.0,1.0), end: Offset(0.0,0.0));
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

  final Widget Function(bool editing, Function refresh) builder;
  final bool Function() defaultEditingState;

  EditableContent({@required this.builder, this.defaultEditingState});

  @override
  State<StatefulWidget> createState() {
    return _EditableContentState(builder: builder, defaultEditingState: defaultEditingState);
  }
}

class _EditableContentState extends State{

  Widget Function(bool editing, Function refresh) builder;
  bool editing;
  final bool Function() defaultEditingState;

  _EditableContentState({@required this.builder, this.defaultEditingState}) :
    editing = defaultEditingState == null ? false : defaultEditingState();

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        builder(
          editing,
          () => setState((){}),
        ),
        Align(
          alignment: Alignment.centerRight,
          child: IconButton(
            icon: Icon(Icons.edit),
            iconSize: 20.0,
            padding: EdgeInsets.all(5.0),
            constraints: BoxConstraints.tight(Size.square(30.0)),
            color: editing ? Theme.of(context).buttonTheme.colorScheme.onSurface : Theme.of(context).buttonTheme.colorScheme.onSurface.withOpacity(.24),
            onPressed: () => setState(() => editing = !editing),
          )
        )
      ],
    );
  }

}