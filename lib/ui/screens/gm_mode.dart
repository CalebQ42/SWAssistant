import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/dialogs/destiny.dart';
import 'package:swassistant/ui/frame_content.dart';
import 'package:swassistant/ui/misc/bottom.dart';
import 'package:swassistant/ui/screens/editable_list.dart';
import 'package:swassistant/ui/screens/editing_editable.dart';

class GMModeSize extends InheritedWidget{

  final double width; 
  const GMModeSize({Key? key, required Widget child, required this.width}) : super(key: key, child: child);

  @override
  bool updateShouldNotify(covariant InheritedWidget oldWidget) => false;

  static GMModeSize? of(BuildContext context) =>
    context.dependOnInheritedWidgetOfExactType<GMModeSize>();
}

class GMMode extends StatelessWidget{

  final GMModeMessager message = GMModeMessager();

  GMMode({Key? key}) : super(key: key);

  //TODO: find place for destiny points

  @override
  Widget build(BuildContext context) {
    var width = MediaQuery.of(context).size.width;
    width = min(450, width / 3);
    var remain = MediaQuery.of(context).size.width - width;
    return WillPopScope(
      onWillPop: (){
        if (message.backStack.length == 1 || message.backStack.isEmpty) {
          return Future.value(true);
        }
        message.backStack.removeLast();
        for(var o in message.onChange) {
          o(message.backStack.last);
        }
        return Future.value(false);
      },
      child: FrameContent(
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          mainAxisSize: MainAxisSize.max,
          children: [
            ConstrainedBox(
              constraints: BoxConstraints(
                maxWidth: width,
              ),
              child: EditableList(
                -1,
                key: message.listKey,
                onTap: (ed) {
                  var ind = message.backStack.indexWhere((element) => element.fileExtension == ed.fileExtension && element.uid == ed.uid);
                  message.backStack.add(ed);
                  for(var o in message.onChange) {
                    o(message.backStack.last);
                  }
                  if (ind != -1) {
                    message.backStack.removeAt(ind);
                  }
                }
              )
            ),
            Container(
              width: 1,
              color: Theme.of(context).dividerColor
            ),
            Expanded(
              child: GMModeSize(
                key: ValueKey(remain),
                width: remain,
                child: _GMModeEditor(message)
              )
            )
          ],
        ),
      )
    );
  }
}

class GMModeMessager{
  final List<void Function(Editable)> onChange = [];
  late void Function() editingState;
  final GlobalKey<EditableListState> listKey = GlobalKey();

  final List<Editable> backStack = [];
}

class _GMModeEditor extends StatefulWidget{

  final GMModeMessager message;

  const _GMModeEditor(this.message);

  @override
  State<StatefulWidget> createState() => _GMModeState();
}

class _GMModeState extends State<_GMModeEditor>{

  Editable? curEdit;
  GlobalKey stuff = GlobalKey();

  @override
  void initState(){
    super.initState();
    widget.message.onChange.add(
      (ed) => setState(() => curEdit = ed)
    );
    widget.message.editingState = () => setState((){});
    if(widget.message.backStack.isNotEmpty) {
      curEdit = widget.message.backStack[widget.message.backStack.length-1];
    }
  }

  @override
  Widget build(BuildContext context) =>
    AnimatedSwitcher(
      duration: const Duration(milliseconds: 300),
      child: curEdit == null ? Center(
        child: Text(
          AppLocalizations.of(context)!.gmModeTap,
          textAlign: TextAlign.justify,
        )
      ) : EditingEditable(
        curEdit!,
        key: stuff,
        contained: true,
      )
    );
}