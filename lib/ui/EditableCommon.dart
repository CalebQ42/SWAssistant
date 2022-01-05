import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class EditingText extends StatelessWidget {

  final Key? key;

  final TextStyle? style;
  final String initialText;
  final bool editing;
  final TextInputType? textType;
  final EdgeInsets? fieldInsets;
  final EdgeInsets? textInsets;
  final bool multiline;
  final TextCapitalization textCapitalization;
  final TextAlign textAlign;
  final TextAlign? fieldAlign;
  final String title;
  final TextStyle? titleStyle;

  final bool collapsed;

  final TextEditingController? controller;

  final bool defaultSave;

  final Editable? editableBackup;

  EditingText({this.key, required this.editing, this.style, this.initialText = "", this.controller, this.textType, this.fieldInsets,
      this.textInsets, this.defaultSave = false, this.multiline = false, this.textCapitalization = TextCapitalization.none, this.textAlign = TextAlign.center,
      this.fieldAlign, this.collapsed = false, this.editableBackup, this.title = "", this.titleStyle}) {
    if(editing && this.controller == null)
      throw "text controller MUST be specified when in editing mode";
  }
  Widget build(BuildContext context) {
    var editable = editableBackup ?? Editable.of(context);
    if(editing){
      if(defaultSave && controller != null){
        controller!.addListener(() {
          editable.save(context: context);
        });
      }
    }
    Widget text;
    if(editing)
      text = Padding(
        key: ValueKey("textField"),
        padding: fieldInsets ?? EdgeInsets.all(4.0),
        child: TextField(
          maxLines: multiline ? null : 1,
          controller: controller,
          keyboardType: textType,
          inputFormatters: textType == TextInputType.number ? [FilteringTextInputFormatter.digitsOnly] : null,
          textCapitalization: textCapitalization,
          textAlign: fieldAlign ?? TextAlign.start,
          decoration: InputDecoration(
            isCollapsed: collapsed,
            labelText: title
          ),
        )
      );
    else
      text = Padding(
        key: ValueKey("text"),
        padding: textInsets ?? EdgeInsets.all(4.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            if(title != "") Text(
              title,
              style: titleStyle ?? Theme.of(context).textTheme.bodyText1?.apply(
                fontSizeDelta: .75,
                fontWeightDelta: 2
              ),
              textAlign: textAlign,
            ),
            if(title != "") Container(height: 5),
            Text(initialText, style: style, textAlign: textAlign,),
          ]
        )
      );
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
    return AnimatedSize(
      duration: Duration(milliseconds: 250),
      child: switcher
    );
  }
}

class EditableContent extends StatefulWidget{

  final Key? key;
  final Widget Function(bool editing, Function() refresh, EditableContentState state)? builder;
  final bool Function()? defaultEditingState;
  final bool editButton;
  final List<Widget> Function()? additonalButtons;
  final List<Widget> Function()? editingButtons;

  final StatefulCard? stateful;

  EditableContent({this.key, this.builder, this.stateful, this.defaultEditingState, this.editButton = true, this.additonalButtons, this.editingButtons});

  @override
  State<EditableContent> createState() =>
    EditableContentState(builder: builder, defaultEditingState: defaultEditingState,
        stateful: stateful, editButton: editButton, additionalButtons: additonalButtons,
        editingButtons: editingButtons);
}

class EditableContentState extends State<EditableContent> with TickerProviderStateMixin{

  Widget Function(bool editing, Function() refresh, EditableContentState state)? builder;
  bool editing;
  final bool editButton;
  List<Widget> Function()? additionalButtons;
  final List<Widget> Function()? editingButtons;

  StatefulCard? stateful;

  final bool Function()? defaultEditingState;

  EditableContentState({this.builder, this.stateful, this.defaultEditingState, this.editButton = true, this.additionalButtons, this.editingButtons}) :
      editing = defaultEditingState == null ? false : defaultEditingState(){
    if(builder == null && stateful == null)
      throw("Either a builder or stateful MUST be provided");
    if(stateful != null)
      editing = stateful!.getHolder().editing;
    additionalButtons ??= () => [];
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> addButtons;
    if (additionalButtons != null)
      addButtons = additionalButtons!();
    else
      addButtons = [];
    var bottomButtons = List<Widget>.from(addButtons);
    if(editButton)
      bottomButtons.add(
        Tooltip(
          message: AppLocalizations.of(context)!.edit,
          child: IconButton(
            iconSize: 20.0,
            splashRadius: 20,
            padding: EdgeInsets.all(5.0),
            constraints: BoxConstraints.tight(Size.square(30.0)),
            icon: Icon(Icons.edit),
            color: editing ? Theme.of(context).buttonTheme.colorScheme?.onSurface : Theme.of(context).buttonTheme.colorScheme?.onSurface.withOpacity(.24),
            onPressed: () {
              if(stateful == null)
                setState(() => editing = !editing);
              else{
                editing = !editing;
                stateful!.getHolder().editing = editing;
                if (stateful!.getHolder().reloadFunction != null)
                  stateful!.getHolder().reloadFunction!();
                setState(() {});
              }
            }
          )
        )
      );
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        builder != null ? builder!(
          editing,
          () => setState((){}),
          this
        ) : stateful!,
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            if(editingButtons != null) AnimatedSwitcher(
              duration: Duration(milliseconds: 200),
              transitionBuilder: (child, anim) =>
                ClipRect(
                  child: SlideTransition(
                    position: Tween(begin: Offset(1.0, 0), end: Offset.zero).animate(anim),
                    child: child,
                  )
                ),
              child: (!editing) ? Container() : ButtonBar(
                buttonPadding: EdgeInsets.symmetric(horizontal: 5),
                children: editingButtons!()
              )
            ),
            if(editButton || addButtons.length > 0) ButtonBar(
              buttonPadding: EdgeInsets.symmetric(horizontal: 5),
              children: bottomButtons
            )
          ]
        )
      ],
    );
  }
}

class EditableContentStatefulHolder{
  Function()? reloadFunction;
  bool editing;

  EditableContentStatefulHolder({this.reloadFunction, this.editing = false});
}

mixin StatefulCard on StatefulWidget{
  EditableContentStatefulHolder getHolder();
}