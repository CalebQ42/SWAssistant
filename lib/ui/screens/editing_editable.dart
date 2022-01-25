import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/common.dart';
import 'package:swassistant/ui/misc/bottom.dart';
import 'package:swassistant/ui/screens/editable_cards.dart';
import 'package:swassistant/ui/screens/editable_notes.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class EditingEditable extends StatefulWidget {

  final Editable profile;
  final bool contained;
  final double? w;

  const EditingEditable(this.profile, {Key? key, this.contained = false, this.w}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _EditingEditableState();
}

class _EditingEditableState extends State<EditingEditable>{

  int _index = 0;

  bool animating = false;

  _EditingEditableState();

  var pager = PageController();
  Widget? cards;
  GlobalKey cardKey = GlobalKey();

  @override
  Widget build(BuildContext context) {
    var bottomNav = BottomNavigationBar(
      backgroundColor: Theme.of(context).cardColor,
      items: [
        BottomNavigationBarItem(icon: const Icon(Icons.info), label: AppLocalizations.of(context)!.stats),
        BottomNavigationBarItem(icon: const Icon(Icons.note), label: AppLocalizations.of(context)!.notes)
      ],
      onTap: (value) {
        animating = true;
        pager.animateToPage(value, duration: const Duration(milliseconds: 500), curve: Curves.easeOutBack).whenComplete(() {
          animating = false;
          setState(() => _index = value);
        });
      },
      currentIndex: _index,
      elevation: 8.0,
      showSelectedLabels: true,
    );
    cards ??= EditableCards(key: cardKey);
    var main = PageView(
      key: Key(widget.profile.uid),
      restorationId: widget.profile.uid,
      physics: const BouncingScrollPhysics(),
      children: [
        cards!,
        const EditableNotes()
      ],
      controller: pager,
      onPageChanged: (i){
        if (!animating){
          setState(() => _index = i);
        }
      },
    );
    // var main = GestureDetector(
    //   onHorizontalDragEnd: (deets){
    //     if(_index == 0 && (deets.primaryVelocity ?? 0) < -1500){
    //       setState(() => _index = 1);
    //     } else if(_index == 1 && (deets.primaryVelocity ?? 0) > 1500){
    //       setState(() => _index = 0);
    //     }
    //   },
    //   child: AnimatedSwitcher(
    //     duration: const Duration(milliseconds: 300),
    //     transitionBuilder: (child, anim){
    //       Tween<Offset> twen;
    //       if(child is EditableCards) {
    //         twen = Tween(begin: const Offset(-1.0, 0), end: Offset.zero);
    //       } else {
    //         twen = Tween(begin: const Offset(1.0, 0), end: Offset.zero);
    //       }
    //       return ClipRect(
    //         child: SlideTransition(
    //           position: twen.animate(anim),
    //           child: child,
    //         )
    //       );
    //     },
    //     child: _index == 0 ? EditableCards(w: widget.w) : EditableNotes()
    //   )
    // );
    Widget body;
    if(!widget.contained) {
      body = Scaffold(
        drawer: const SWDrawer(),
        bottomNavigationBar: bottomNav,
        appBar: SWAppBar(
          context,
          additionalActions: [
            IconButton(
              icon: const Icon(Icons.casino_outlined),
              onPressed: () =>
                SWDiceHolder().showDialog(context),
            ),
          ],
          additionalPopupActions: [
            if (widget.profile is Character)
              CheckedPopupMenuItem(
                checked: (widget.profile as Character).disableForce,
                child: Text(AppLocalizations.of(context)!.disableForce),
                value: "disableForce"
              ),
            if (widget.profile is Character)
              CheckedPopupMenuItem(
                checked: (widget.profile as Character).disableMorality,
                child: Text(AppLocalizations.of(context)!.disableMorality),
                value: "disableMorality"
              ),
            if (widget.profile is Character)
              CheckedPopupMenuItem(
                checked: (widget.profile as Character).disableDuty,
                child: Text(AppLocalizations.of(context)!.disableDuty),
                value: "disableDuty"
              ),
            if (widget.profile is Character)
              CheckedPopupMenuItem(
                checked: (widget.profile as Character).disableObligation,
                child: Text(AppLocalizations.of(context)!.disableObligation),
                value: "disableObligation"
              ),
            PopupMenuItem(child: Text(AppLocalizations.of(context)!.clone), value: "clone"),
          ],
          backgroundColor: Theme.of(context).primaryColor,
          popupFunctions: {
            "disableForce": () => setState(() => cardKey.currentState?.setState(() => (widget.profile as Character)
                .disableForce = !(widget.profile as Character).disableForce)),
            "disableDuty": () => setState(() => cardKey.currentState?.setState(() => (widget.profile as Character)
                .disableDuty = !(widget.profile as Character).disableDuty)),
            "disableObligation": () => setState(() => cardKey.currentState?.setState(() =>
              (widget.profile as Character).disableObligation = !(widget.profile as Character).disableObligation)),
            "disableMorality": () => setState(() => cardKey.currentState?.setState(() => (widget.profile as Character)
                .disableMorality = !(widget.profile as Character).disableMorality)),
            "clone": () => Bottom(
              child: (context) {
                var nameController = TextEditingController(text: AppLocalizations.of(context)!.copyOf(widget.profile.name));
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
                            if (widget.profile is Character) {
                              out = Character.from(widget.profile as Character);
                            } else if (widget.profile is Minion) {
                              out = Minion.from(widget.profile as Minion);
                            } else if (widget.profile is Vehicle) {
                              out = Vehicle.from(widget.profile as Vehicle);
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
            ).show(context),
          },
        ),
        body: main
      );
    } else {
      body = Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Expanded(
            child: main
          ),
          bottomNav
        ],
      );
    }
    return InheritedEditable(
      child: body,
      editable: widget.profile
    );
  }
}

class InheritedEditable extends InheritedWidget {
  final Editable editable;

  const InheritedEditable({required Widget child, required this.editable, Key? key}) : super(key: key, child: child);

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) => false;
}
