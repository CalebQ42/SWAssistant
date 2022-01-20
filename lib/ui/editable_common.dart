import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class EditingText extends StatelessWidget {

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

  final void Function()? onTap;

  EditingText({Key? key, required this.editing, this.style, this.initialText = "", this.controller, this.textType, this.fieldInsets,
      this.textInsets, this.defaultSave = false, this.multiline = false, this.textCapitalization = TextCapitalization.none, this.textAlign = TextAlign.center,
      this.fieldAlign, this.collapsed = false, this.editableBackup, this.title = "", this.titleStyle, this.onTap}) : super(key: key) {
    if(editing && controller == null) throw "text controller MUST be specified when in editing mode";
  }

  @override
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
    if(editing){
      text = Padding(
        key: const ValueKey("textField"),
        padding: fieldInsets ?? const EdgeInsets.all(4.0),
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
    }else{
      text = Padding(
        key: const ValueKey("text"),
        padding: textInsets ?? const EdgeInsets.all(4.0),
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
      if(onTap != null){
        text = InkResponse(
          key: const ValueKey("text"),
          child: text,
          onTap: onTap,
          containedInkWell: true,
          highlightShape: BoxShape.rectangle,
        );
      }
    }
    var switcher = AnimatedSwitcher(
      duration: const Duration(milliseconds: 300),
      child: text,
      transitionBuilder: (wid, anim){
        Tween<Offset> slide;
        if(wid.key == const ValueKey("text")){
          slide = Tween(begin: const Offset(0.0,-1.0),end: Offset.zero);
        }else{
          slide = Tween(begin: const Offset(0.0,1.0), end: Offset.zero);
        }
        return ClipRect(
          child: SlideTransition(
            position: slide.animate(anim),
            child: Center(child: wid)
          )
        );
      },
      layoutBuilder: (child, oldStack){
        List<Widget> newStack = [];
        for(var chil in oldStack){
          newStack.add(
            SizedOverflowBox(
              size: Size.zero,
              child: chil
            )
          );
        }
        return Stack(
          alignment: AlignmentDirectional.center,
          children:[
            ...newStack,
            child!,
          ]
        );
      },
    );
    return AnimatedSize(
      duration: const Duration(milliseconds: 300),
      child: switcher
    );
  }
}

class EditableContent extends StatefulWidget{

  final Widget Function(bool editing, Function() refresh, EditableContentState state)? builder;
  final bool Function()? defaultEditingState;
  final bool editButton;
  final List<Widget> Function()? additionalButtons;
  final List<Widget> Function()? editingButtons;

  final StatefulCard? stateful;

  const EditableContent({Key? key, this.builder, this.stateful, this.defaultEditingState, this.editButton = true, this.additionalButtons, this.editingButtons}) : super(key: key);

  @override
  State<EditableContent> createState() => EditableContentState();
}

class EditableContentState extends State<EditableContent>{
  
  late bool editing;

  @override
  void initState() {
    super.initState();
    editing = widget.defaultEditingState == null ? false : widget.defaultEditingState!();
    if(widget.builder == null && widget.stateful == null) throw("Either a builder or stateful MUST be provided");
    if(widget.stateful != null) editing = widget.stateful!.getHolder().editing;
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> addButtons;
    if(widget.additionalButtons != null){
      addButtons = widget.additionalButtons!();
    }else{
      addButtons = [];
    }
    var bottomButtons = List<Widget>.from(addButtons);
    if(widget.editButton){
      bottomButtons.add(
        Tooltip(
          message: AppLocalizations.of(context)!.edit,
          child: IconButton(
            iconSize: 20.0,
            splashRadius: 20,
            padding: const EdgeInsets.all(5.0),
            constraints: BoxConstraints.tight(const Size.square(30.0)),
            icon: const Icon(Icons.edit),
            color: editing ? Theme.of(context).buttonTheme.colorScheme?.onSurface : Theme.of(context).buttonTheme.colorScheme?.onSurface.withOpacity(.24),
            onPressed: () {
              if(widget.stateful == null){
                setState(() => editing = !editing);
              }else{
                editing = !editing;
                widget.stateful!.getHolder().editing = editing;
                if (widget.stateful!.getHolder().reloadFunction != null){
                  widget.stateful!.getHolder().reloadFunction!();
                }
                setState(() {});
              }
            }
          )
        )
      );
    }
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        widget.builder != null ? widget.builder!(
          editing,
          () => setState((){}),
          this
        ) : widget.stateful!,
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            if(widget.editingButtons != null) AnimatedSwitcher(
              duration: const Duration(milliseconds: 200),
              transitionBuilder: (child, anim) =>
                ClipRect(
                  child: SlideTransition(
                    position: Tween(begin: const Offset(1.0, 0), end: Offset.zero).animate(anim),
                    child: child,
                  )
                ),
              child: (!editing) ? Container() : ButtonBar(
                buttonPadding: const EdgeInsets.symmetric(horizontal: 5),
                children: widget.editingButtons!()
              )
            ),
            if(widget.editButton || addButtons.isNotEmpty) ButtonBar(
              buttonPadding: const EdgeInsets.symmetric(horizontal: 5),
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