import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/Common.dart';
import 'package:swassistant/ui/misc/Bottom.dart';
import 'package:swassistant/ui/screens/EditableList.dart';
import 'package:swassistant/ui/screens/EditingEditable.dart';

class GMMode extends StatelessWidget{

  final GMModeMessager message = GMModeMessager();

  @override
  Widget build(BuildContext context) {
    var width = MediaQuery.of(context).size.width;
    width = min(450, width / 3);
    var remain = MediaQuery.of(context).size.width - width;
    return WillPopScope(
      onWillPop: (){
        if (message.backStack.length == 1 || message.backStack.length == 0)
          return Future.value(true);
        message.backStack.removeLast();
        for(var o in message.onChange)
          o(message.backStack.last);
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
                  for(var o in message.onChange)
                    o(message.backStack.last);
                  if (ind != -1)
                    message.backStack.removeAt(ind);
                }
              )
            ),
            Expanded(
              key: ValueKey(remain),
              child: _GMModeEditor(message, remain)
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

  _GMModeBar(this.message);

  @override
  State<StatefulWidget> createState() => _BarState(message);
}

class _BarState extends State{
  
  final GMModeMessager message;
  Editable? editable;

  _BarState(this.message){
    message.onChange.add(
      (ed) => setState(() => editable = ed)
    );
  }

  @override
  Widget build(BuildContext context) =>
    SWAppBar(
      context,
      backgroundColor: Theme.of(context).primaryColor,
      additionalActions: [
        IconButton(
          icon: Icon(Icons.casino_outlined),
          onPressed: () =>
            SWDiceHolder().showDialog(context),
        ),
      ],
      additionalPopupActions: [
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
      popupFunctions: editable != null ? {
        "disableForce": () {
          setState(() => (editable as Character).disableForce = !(editable as Character).disableForce);
          message.editingState();
        },
        "disableDuty": () {
          setState(() => (editable as Character).disableDuty = !(editable as Character).disableDuty);
          message.editingState();
        },
        "disableObligation": () {
          setState(() => (editable as Character).disableObligation = !(editable as Character).disableObligation);
          message.editingState();
        },
        "disableMorality": () {
          setState(() => (editable as Character).disableMorality = !(editable as Character).disableMorality);
          message.editingState();
        },
        "clone": () => Bottom(
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
                        if (editable is Character)
                          out = Character.from(editable as Character);
                        else if (editable is Minion)
                          out = Minion.from(editable as Minion);
                        else if (editable is Vehicle) 
                          out = Vehicle.from(editable as Vehicle);
                        else
                          throw "Unsupported Editable Type";
                        out.name = nameController.text;
                        SW.of(context).add(out);
                        out.save(context: context);
                        Navigator.of(context).pop();
                        message.listKey.currentState?.setState(() {});
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
        ).show(context)
      } : null
    );
}

class _GMModeEditor extends StatefulWidget{
  final GMModeMessager message;
  final double width;

  _GMModeEditor(this.message, this.width);

  @override
  State<StatefulWidget> createState() => _GMModeState(message, width);
}

class _GMModeState extends State{
  final GMModeMessager message;
  final double width;
  Editable? curEdit;

  _GMModeState(this.message, this.width){
    message.onChange.add(
      (ed) => setState(() => curEdit = ed)
    );
    message.editingState = () => setState((){});
    if(message.backStack.isNotEmpty)
      curEdit = message.backStack[message.backStack.length-1];
  }

  @override
  Widget build(BuildContext context) =>
    AnimatedSwitcher(
      duration: Duration(milliseconds: 300),
      child: curEdit == null ? Center(
        child: Text(
          AppLocalizations.of(context)!.gmModeTap,
          textAlign: TextAlign.justify,
        )
      ) : EditingEditable(
        curEdit!,
        () => message.listKey.currentState?.setState(() {}),
        key: Key(curEdit!.uid.toString() + curEdit!.fileExtension),
        contained: true,
        w: width,
      )
    );
}