import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/Common.dart';
import 'package:swassistant/ui/misc/Bottom.dart';
import 'package:swassistant/ui/screens/EditableCards.dart';
import 'package:swassistant/ui/screens/EditableNotes.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class EditingEditable extends StatefulWidget {
  final Key? key;
  final Editable profile;
  final void Function() refreshList;
  final bool contained;
  final double? w;

  EditingEditable(this.profile, this.refreshList, {this.key, this.contained = false, this.w});

  @override
  State<StatefulWidget> createState() =>
      _EditingEditableState(profile, refreshList, contained, w);
}

class _EditingEditableState extends State {
  final Editable profile;
  final void Function() refreshList;
  int _index = 0;
  final bool contained;
  final double? w;

  _EditingEditableState(this.profile, this.refreshList, this.contained, this.w);

  Widget build(BuildContext context) {
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
    if(!contained)
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
                            if (profile is Character)
                              out = Character.from(profile as Character);
                            else if (profile is Minion)
                              out = Minion.from(profile as Minion);
                            else if (profile is Vehicle)
                              out = Vehicle.from(profile as Vehicle);
                            else
                              throw "Unsupported Editable Type";
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
        body: AnimatedSwitcher(
          duration: Duration(milliseconds: 300),
          transitionBuilder: (child, anim){
            Tween<Offset> twen;
            if(child is EditableCards)
              twen = Tween(begin: Offset(-1.0, 0), end: Offset.zero);
            else
              twen = Tween(begin: Offset(1.0, 0), end: Offset.zero);
            return ClipRect(
              child: SlideTransition(
                position: twen.animate(anim),
                child: child,
              )
            );
          },
          child: _index == 0 ? EditableCards(refreshList: refreshList, w: w) : EditableNotes()
        )
      );
    else
      body = Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Expanded(
            child: AnimatedSwitcher(
              duration: Duration(milliseconds: 300),
              transitionBuilder: (child, anim){
                Tween<Offset> twen;
                if(child is EditableCards)
                  twen = Tween(begin: Offset(-1.0, 0), end: Offset.zero);
                else
                  twen = Tween(begin: Offset(1.0, 0), end: Offset.zero);
                return SlideTransition(
                  position: twen.animate(anim),
                  child: child,
                );
              },
              child: _index == 0 ? EditableCards(refreshList: refreshList, w: w) : EditableNotes()
            )
          ),
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
