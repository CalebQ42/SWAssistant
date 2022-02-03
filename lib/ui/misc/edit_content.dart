import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

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
  State<StatefulWidget> createState() => EditContentState();
}

class EditContentState extends State<EditContent> with StatefulCard {

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => widget.defaultEdit != null ? widget.defaultEdit!() : widget.contentKey?.currentState?.defaultEdit ?? false;

  @override
  Widget build(BuildContext context) {
    if((widget.contentKey == null || widget.content == null) && widget.contentBuilder == null){
      throw("either contentKey and content needs to be provided, or contentBuilder");
    }
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        ClipRect(
          child: widget.content ?? widget.contentBuilder!(edit),
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          mainAxisSize: MainAxisSize.min,
          children: [
            if(widget.extraEditButtons != null) AnimatedSwitcher(
              duration: const Duration(milliseconds: 300),
              transitionBuilder: (child, anim) =>
                ClipRect(
                  child: SlideTransition(
                    position: Tween<Offset>(begin: const Offset(1.0, 0), end: Offset.zero).animate(anim),
                    child: child,
                  )
                ),
              child: (edit) ? ButtonBar(
                children: widget.extraEditButtons!(context)
              ) : Container(),
            ),
            if(widget.extraButtons != null) ...widget.extraButtons!(context, edit),
            Tooltip(
              message: AppLocalizations.of(context)!.edit,
              child: IconButton(
                iconSize: 20.0,
                splashRadius: 20,
                padding: const EdgeInsets.all(5.0),
                constraints: BoxConstraints.tight(const Size.square(30.0)),
                icon: const Icon(Icons.edit),
                color: edit ? Theme.of(context).buttonTheme.colorScheme?.onSurface : Theme.of(context).buttonTheme.colorScheme?.onSurface.withOpacity(.24),
                onPressed: () {
                  if(widget.contentKey != null){
                    widget.contentKey!.currentState?.setState(() {
                      widget.contentKey!.currentState?.editing = !edit;
                    });
                  }
                  setState(() => edit = !edit);
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
  bool get defaultEdit;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    editing = defaultEdit;
  }
}