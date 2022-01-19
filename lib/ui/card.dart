import 'package:flutter/material.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class InfoCard extends StatefulWidget {
  final InfoCardHolder holder = InfoCardHolder();
  final Function(bool b, Function() refreshList) onHideChange;

  InfoCard(
      {Key? key,
      shown = true,
      required Widget contents,
      String title = "",
      required this.onHideChange})
      : super(key: key) {
    holder.shown = shown;
    holder.contents = contents;
    holder.title = title;
  }

  @override
  State<StatefulWidget> createState() => _InfoCardState();
}

class _InfoCardState extends State<InfoCard> {
  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
      childrenPadding: const EdgeInsets.all(10),
      title: AnimatedAlign(
          curve: Curves.easeOutBack,
          duration: const Duration(milliseconds: 500),
          alignment:
              widget.holder.shown ? Alignment.center : Alignment.centerLeft,
          child: Text(
            widget.holder.title,
            style: Theme.of(context).textTheme.subtitle1,
          )),
      children: [widget.holder.contents],
      onExpansionChanged: (b) {
        setState(() {
          widget.onHideChange(b, () => setState(() {}));
          widget.holder.shown = b;
        });
      },
      initiallyExpanded: widget.holder.shown,
    );
  }
}

class InfoCardHolder {
  bool shown = false;
  late Widget contents;
  String title = "";
}

class NameCardContent extends StatefulWidget {
  const NameCardContent({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _NameCardContentState();
}

class _NameCardContentState extends State<NameCardContent> {
  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    return EditableContent(
        builder: (b, refresh, state) => Hero(
            transitionOnUserGestures: true,
            tag: () => editable.getFileLocation(SW.of(context)),
            child: EditingText(
              editing: b,
              editableBackup: editable,
              style: Theme.of(context).textTheme.headline5!,
              textAlign: TextAlign.center,
              initialText: editable.name,
              controller: () {
                var cont = TextEditingController(text: editable.name);
                cont.addListener(() {
                  editable.name = cont.text;
                });
                return cont;
              }(),
              defaultSave: true,
              textCapitalization: TextCapitalization.words,
            )),
        defaultEditingState: () {
          switch (editable.runtimeType) {
            case Character:
              return editable.name ==
                  AppLocalizations.of(context)!.newCharacter;
            case Minion:
              return editable.name == AppLocalizations.of(context)!.newMinion;
            case Vehicle:
              return editable.name == AppLocalizations.of(context)!.newVehicle;
          }
          return false;
        });
  }
}
