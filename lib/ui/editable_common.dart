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
  final String? heroTag;

  final bool collapsed;

  final TextEditingController? controller;

  final bool defaultSave;

  final Editable? editableBackup;

  final void Function()? onTap;

  EditingText({Key? key, required this.editing, this.style, this.initialText = "", this.controller, this.textType, this.fieldInsets,
      this.textInsets, this.defaultSave = false, this.multiline = false, this.textCapitalization = TextCapitalization.none, this.textAlign = TextAlign.center,
      this.fieldAlign, this.collapsed = false, this.editableBackup, this.title = "", this.titleStyle, this.onTap, this.heroTag}) : super(key: key) {
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
          textAlign: fieldAlign ?? textAlign,
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
              style: titleStyle ?? Theme.of(context).textTheme.titleSmall,
              textAlign: textAlign,
            ),
            if(title != "") Container(height: 5),
            (heroTag == null) ? Text(initialText, style: style, textAlign: textAlign,) :
              Hero(tag: heroTag!, transitionOnUserGestures: true, child: Text(initialText, style: style, textAlign: textAlign,)),
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

class EditContent extends StatefulWidget{

  final GlobalKey<StatefulCard>? contentKey;
  final Widget? content;
  final Widget Function(bool b)? contentBuilder;
  final bool Function()? defaultEdit;

  final List<Widget> Function(BuildContext, bool)? extraButtons;
  final List<Widget> Function(BuildContext)? extraEditButtons;

  const EditContent({Key? key,
    this.contentKey,
    this.content,
    this.contentBuilder,
    this.defaultEdit,
    this.extraButtons, 
    this.extraEditButtons}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _EditContentState();
}

class _EditContentState extends State<EditContent>{

  late bool editing;

  @override
  void initState() {
    super.initState();
    editing = widget.defaultEdit != null ? widget.defaultEdit!() : false;
    if(widget.contentKey != null) widget.contentKey!.currentState?.editing = editing;
  }

  @override
  Widget build(BuildContext context) {
    if((widget.contentKey == null || widget.content == null) && widget.contentBuilder == null){
      throw("either contentKey and content needs to be provided, or contentBuilder");
    }
    return Column(
      children: [
        Expanded(child: widget.content ?? widget.contentBuilder!(editing)),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          mainAxisSize: MainAxisSize.min,
          children: [
            if(widget.extraEditButtons != null) AnimatedSwitcher(
              duration: const Duration(milliseconds: 300),
              transitionBuilder: (child, anim) =>
                SlideTransition(
                  position: Tween<Offset>(begin: const Offset(1.0, 0), end: Offset.zero).animate(anim),
                  child: child,
                ),
              child: (editing) ? ButtonBar(
                children: widget.extraEditButtons!(context)
              ) : Container(),
            ),
            if(widget.extraButtons != null) ...widget.extraButtons!(context, editing),
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
                  if(widget.contentKey != null){
                    widget.contentKey!.currentState?.setState(() {
                      widget.contentKey!.currentState?.editing = !editing;
                    });
                  }
                  setState(() => editing = !editing);
                }
              )
            )
          ]
        )
      ],
    );
  }
}

mixin StatefulCard<T extends StatefulWidget> on State<T> {
  set editing(bool b);
}