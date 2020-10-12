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
          decoration: InputDecoration(isCollapsed: collapsed, contentPadding: EdgeInsets.symmetric(horizontal: 5)),
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
  final bool editButton;
  final List<Widget> additonalButtons;

  final StatefulCard stateful;

  EditableContent({this.builder, this.stateful, this.defaultEditingState, this.editButton = true, this.additonalButtons});

  @override
  State<EditableContent> createState() =>
    EditableContentState(builder: builder, defaultEditingState: defaultEditingState,
        stateful: stateful, editButton: editButton, additionalButtons: additonalButtons);
}

class EditableContentState extends State<EditableContent> with TickerProviderStateMixin{

  Widget Function(bool editing, Function refresh, EditableContentState state) builder;
  bool editing;
  final bool editButton;
  List<Widget> additionalButtons;

  StatefulCard stateful;

  final bool Function() defaultEditingState;

  EditableContentState({this.builder, this.stateful, this.defaultEditingState, this.editButton, this.additionalButtons}) :
      editing = defaultEditingState == null ? false : defaultEditingState(){
    if(builder == null && stateful == null)
      throw("Either a builder or stateful MUST be provided");
    if(stateful != null)
      editing = stateful.getHolder().editing;
    additionalButtons ??= List();
  }

  @override
  Widget build(BuildContext context) {
    var bottomButtons = List.from(additionalButtons);
    if(editButton)
      bottomButtons.add(
        Tooltip(
          message: "Edit",
          child: IconButton(
            iconSize: 20.0,
            padding: EdgeInsets.all(5.0),
            constraints: BoxConstraints.tight(Size.square(30.0)),
            icon: Icon(Icons.edit),
            color: editing ? Theme.of(context).buttonTheme.colorScheme.onSurface : Theme.of(context).buttonTheme.colorScheme.onSurface.withOpacity(.24),
            onPressed: () {
              if(stateful == null)
                setState(() => editing = !editing);
              else{
                editing = !editing;
                stateful.getHolder().editing = editing;
                stateful.getHolder().reloadFunction();
                setState(() {});
              }
            }
          )
        )
      );
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        builder != null ? builder(
          editing,
          () => setState((){}),
          this
        ) : stateful,
        if(editButton || additionalButtons.length > 0) ButtonBar(
          buttonPadding: EdgeInsets.only(left: 5, right: 5),
          children: bottomButtons
        )
      ],
    );
  }
}

class EditableContentStatefulHolder{
  Function reloadFunction = () =>
    throw("THIS NEEDS TO BE OVERWRITTEN");
  bool editing = false;

  EditableContentStatefulHolder({this.reloadFunction, this.editing});
}

mixin StatefulCard on StatefulWidget{
  EditableContentStatefulHolder getHolder();
}