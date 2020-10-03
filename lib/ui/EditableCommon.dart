import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditingText extends StatelessWidget {

  final Key key;

  final TickerProviderStateMixin state;

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

  final bool collapsed;

  final TextEditingController controller;

  final bool defaultSave;

  final Editable editableBackup;

  EditingText({this.key, @required this.editing, this.style, this.initialText = "", this.controller, this.textType,
      this.fieldInsets = const EdgeInsets.symmetric(horizontal:2.0), this.textInsets = const EdgeInsets.all(4.0),
      this.defaultSave = false, this.multiline = false, this.textCapitalization = TextCapitalization.none, this.textAlign = TextAlign.start,
      this.fieldAlign = TextAlign.start, this.collapsed = false, this.state, this.editableBackup}) {
    if(editing && this.controller == null)
      throw "text controller MUST be specified when in editing mode";
  }
  Widget build(BuildContext context) {
    var editable = editableBackup ?? Editable.of(context);
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
          inputFormatters: textType == TextInputType.number ? [FilteringTextInputFormatter.digitsOnly] : null,
          textCapitalization: textCapitalization,
          textAlign: fieldAlign,
          decoration: InputDecoration(isCollapsed: collapsed),
        )
      );
    }else{
      text = Padding(
        key: ValueKey("text"),
        padding: textInsets,
        child: Text(initialText, style: style, textAlign: textAlign,)
      );
    }
    var switcher = AnimatedSwitcher(
      duration: Duration(milliseconds: 300),
      child: text,
      transitionBuilder: (wid, anim){
        Tween<Offset> slide;
        if((editing && wid.key == ValueKey("text")) || (!editing && wid.key == ValueKey("textField")))
          slide = Tween(begin: Offset(0.0,-1.0),end: Offset.zero);
        else
          slide = Tween(begin: Offset(0.0,1.0), end: Offset.zero);
        return ClipRect(
          child: SlideTransition(
            position: slide.animate(anim),
            child: Center(child: wid)
          )
        );
      },
    );
    return state != null ? AnimatedSize(
      duration: Duration(milliseconds: 250),
      vsync: state,
      child: switcher
    ) : switcher;
  }
}

class EditableContent extends StatefulWidget{

  final Widget Function(bool editing, Function refresh, EditableContentState state) builder;
  final bool Function() defaultEditingState;

  EditableContent({@required this.builder, this.defaultEditingState});

  @override
  State<EditableContent> createState() {
    return EditableContentState(builder: builder, defaultEditingState: defaultEditingState);
  }
}

class EditableContentState extends State<EditableContent> with TickerProviderStateMixin{

  Widget Function(bool editing, Function refresh, EditableContentState state) builder;
  bool editing;
  final bool Function() defaultEditingState;

  EditableContentState({@required this.builder, this.defaultEditingState}) :
    editing = defaultEditingState == null ? false : defaultEditingState();

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        builder(
          editing,
          () => setState((){}),
          this
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