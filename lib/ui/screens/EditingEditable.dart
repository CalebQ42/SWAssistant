import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/Common.dart';
import 'package:swassistant/ui/misc/Bottom.dart';
import 'package:swassistant/ui/screens/EditableCards.dart';
import 'package:swassistant/ui/screens/EditableNotes.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class EditingEditable extends StatefulWidget {
  final Editable profile;
  final Function() refreshList;
  final bool contained;

  EditingEditable(this.profile, this.refreshList, {this.contained = false});

  @override
  State<StatefulWidget> createState() =>
      _EditingEditableState(profile, refreshList, contained);
}

class _EditingEditableState extends State {
  final Editable profile;
  final Function() refreshList;
  int _index = 0;
  final bool contained;

  _EditingEditableState(this.profile, this.refreshList, this.contained);

  Widget build(BuildContext context) {
    var main = _index == 0 ? EditableCards(refreshList: refreshList) : EditableNotes();
    var bottomNav = BottomNavigationBar(
      backgroundColor: Theme.of(context).cardColor,
      items: [
        BottomNavigationBarItem(icon: Icon(Icons.info), label: AppLocalizations.of(context)!.stats),
        BottomNavigationBarItem(icon: Icon(Icons.note), label: AppLocalizations.of(context)!.notes)
      ],
      onTap: (value) => setState(() => _index = value),
      currentIndex: _index,
      elevation: 8.0,
      showSelectedLabels: true,
    );
    Widget body;
    if(contained)
      body = Scaffold(
        drawer: SWDrawer(),
        bottomNavigationBar: bottomNav,
        appBar: SWAppBar(
          context,
          additionalActions: [
            IconButton(
              icon: Icon(Icons.casino_outlined),
              onPressed: () =>
                SWDiceHolder().showDialog(context),
            ),
          ],
          additionalPopupActions: [
            if (profile is Character)
              CheckedPopupMenuItem(
                checked: (profile as Character).disableForce,
                child: Text(AppLocalizations.of(context)!.disableForce),
                value: "disableForce"
              ),
            if (profile is Character)
              CheckedPopupMenuItem(
                checked: (profile as Character).disableMorality,
                child: Text(AppLocalizations.of(context)!.disableMorality),
                value: "disableMorality"
              ),
            if (profile is Character)
              CheckedPopupMenuItem(
                checked: (profile as Character).disableDuty,
                child: Text(AppLocalizations.of(context)!.disableDuty),
                value: "disableDuty"
              ),
            if (profile is Character)
              CheckedPopupMenuItem(
                checked: (profile as Character).disableObligation,
                child: Text(AppLocalizations.of(context)!.disableObligation),
                value: "disableObligation"
              ),
            PopupMenuItem(child: Text(AppLocalizations.of(context)!.clone), value: "clone"),
          ],
          backgroundColor: Theme.of(context).primaryColor,
          popupFunctions: {
            "disableForce": () => setState(() => (profile as Character)
                .disableForce = !(profile as Character).disableForce),
            "disableDuty": () => setState(() => (profile as Character)
                .disableDuty = !(profile as Character).disableDuty),
            "disableObligation": () => setState(() =>
              (profile as Character).disableObligation = !(profile as Character).disableObligation),
            "disableMorality": () => setState(() => (profile as Character)
                .disableMorality = !(profile as Character).disableMorality),
            "clone": () => Bottom(
              child: (context) {
                var nameController = TextEditingController(text: AppLocalizations.of(context)!.copyOf(profile.name));
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
                            if (profile is Character) {
                              var id = 0;
                              while (SW.of(context).characters().any((element) => element.id == id))
                                id++;
                              out = Character.from(profile as Character, id: id);
                            } else if (profile is Minion) {
                              var id = 0;
                              while (SW.of(context).minions().any((element) => element.id == id))
                                id++;
                              out = Minion.from(profile as Minion, id: id);
                            } else if (profile is Vehicle) {
                              var id = 0;
                              while (SW.of(context).vehicles().any((element) => element.id == id))
                                id++;
                              out = Vehicle.from(profile as Vehicle, id: id);
                            }else{
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
            ).show(context),
          },
        ),
        body: main
      );
    else
      body = Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Expanded(child: main,),
          bottomNav
        ],
      );
    return InheritedEditable(
      child: body,
      editable: profile
    );
  }
}

class InheritedEditable extends InheritedWidget {
  final Editable editable;

  InheritedEditable({required Widget child, required this.editable}) : super(child: child);

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) => false;
}
