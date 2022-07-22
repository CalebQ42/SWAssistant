import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/frame_content.dart';
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
      controller: pager,
      onPageChanged: (i){
        if (!animating){
          setState(() => _index = i);
        }
      },
      children: [
        cards!,
        EditableNotes(key: widget.profile.notesKey)
      ],
    );
    Widget body;
    if(!widget.contained) {
      body = FrameContent(
        bottom: bottomNav,
        child: main
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
      editable: widget.profile,
      child: body
    );
  }
}

class InheritedEditable extends InheritedWidget {
  final Editable editable;

  const InheritedEditable({required Widget child, required this.editable, Key? key}) : super(key: key, child: child);

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) => false;
}
