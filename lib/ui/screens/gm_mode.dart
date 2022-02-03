import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
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
      child: Scaffold(
        appBar: PreferredSize(
          child: _GMModeBar(message),
          preferredSize: Size.fromHeight(Theme.of(context).appBarTheme.toolbarHeight ?? 55),
        ),
        // drawer: SWDrawer(),
        body: Row(
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

class _GMModeBar extends StatefulWidget{
  final GMModeMessager message;

  const _GMModeBar(this.message);

  @override
  State<StatefulWidget> createState() => _BarState();
}

class _BarState extends State<_GMModeBar>{
  
  Editable? editable;

  @override
  void initState(){
    super.initState();
    widget.message.onChange.add(
      (ed) => setState(() => editable = ed)
    );
  }

  @override
  Widget build(BuildContext context) =>
    AppBar(
      backgroundColor: Theme.of(context).primaryColor,
      actions: [
        IconButton(
          icon: const Icon(Icons.casino_outlined),
          onPressed: () =>
            SWDiceHolder().showDialog(context),
        ),
        PopupMenuButton(
          itemBuilder: (c) => [
            if (editable is Character)
              CheckedPopupMenuItem(
                checked: (editable as Character).disableForce,
                child: Text(AppLocalizations.of(context)!.disableForce),
                value: "disableForce"
              ),
            if (editable is Character)
              CheckedPopupMenuItem(
                checked: (editable as Character).disableMorality,
                child: Text(AppLocalizations.of(context)!.disableMorality),
                value: "disableMorality"
              ),
            if (editable is Character)
              CheckedPopupMenuItem(
                checked: (editable as Character).disableDuty,
                child: Text(AppLocalizations.of(context)!.disableDuty),
                value: "disableDuty"
              ),
            if (editable is Character)
              CheckedPopupMenuItem(
                checked: (editable as Character).disableObligation,
                child: Text(AppLocalizations.of(context)!.disableObligation),
                value: "disableObligation"
              ),
            if (editable != null)
              PopupMenuItem(child: Text(AppLocalizations.of(context)!.clone), value: "clone"),
          ],
          onSelected: (value) {
            switch(value){
              case "disableForce":
                setState(() => (editable as Character).disableForce = !(editable as Character).disableForce);
                widget.message.editingState();
                break;
              case "disableDuty":
                setState(() => (editable as Character).disableDuty = !(editable as Character).disableDuty);
                widget.message.editingState();
                break;
              case "disableObligation":
                setState(() => (editable as Character).disableObligation = !(editable as Character).disableObligation);
                widget.message.editingState();
                break;
              case "disableMorality":
                setState(() => (editable as Character).disableMorality = !(editable as Character).disableMorality);
                widget.message.editingState();
                break;
              case "clone":
                Bottom(
                  child: (context) {
                    var nameController = TextEditingController(text: AppLocalizations.of(context)!.copyOf(editable!.name));
                    return Wrap(
                      children: [
                        Container(height: 10),
                        TextField(
                          controller: nameController,
                          decoration: InputDecoration(labelText: AppLocalizations.of(context)!.name),
                        ),
                        ButtonBar(
                          children: [
                            TextButton(
                              child: Text(MaterialLocalizations.of(context).saveButtonLabel),
                              onPressed: () {
                                Editable out;
                                if (editable is Character) {
                                  out = Character.from(editable as Character);
                                } else if (editable is Minion) {
                                  out = Minion.from(editable as Minion);
                                } else if (editable is Vehicle) {
                                  out = Vehicle.from(editable as Vehicle);
                                } else {
                                  throw "Unsupported Editable Type";
                                }
                                out.name = nameController.text;
                                SW.of(context).add(out);
                                out.save(context: context);
                                Navigator.of(context).pop();
                              },
                            ),
                            TextButton(
                              child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
                              onPressed: () =>
                                Navigator.of(context).pop()
                            )
                          ],
                        )
                      ],
                    );
                  }
                ).show(context);
            }
          },
        )
      ],
    );
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