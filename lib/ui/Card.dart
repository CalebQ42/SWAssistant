import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class InfoCard extends StatefulWidget{

  final InfoCardHolder holder = new InfoCardHolder();
  final Function(bool b, Function() refreshList) onHideChange;

  InfoCard({shown = true, required Widget contents, String title = "", required this.onHideChange}){
    holder.shown = shown;
    holder.contents = contents;
    holder.title = title;
  }

  @override
  State<StatefulWidget> createState() {
    return _InfoCardState(holder, onHideChange);
  }
}

class _InfoCardState extends State{

  InfoCardHolder holder;
  Function(bool b, Function() refreshList) onHideChange;

  _InfoCardState(this.holder, this.onHideChange);

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
      childrenPadding: EdgeInsets.all(10),
      title: AnimatedAlign(
        curve: Curves.easeOutBack,
        duration: Duration(milliseconds: 500),
        alignment: holder.shown ? Alignment.center : Alignment.centerLeft,
        child: Text(
          holder.title,
          style: Theme.of(context).textTheme.subtitle1,
        )
      ),
      children: [holder.contents],
      onExpansionChanged: (b){
        setState((){
          onHideChange(b, () => setState((){}));
          holder.shown = b;
        });
      },
      initiallyExpanded: holder.shown,
    );
  }
}

class InfoCardHolder{
  bool shown = false;
  late Widget contents;
  String title = "";
}

class NameCardContent extends StatefulWidget{
  final Function() refreshList;

  const NameCardContent(this.refreshList);

  @override
  State<StatefulWidget> createState() =>
    _NameCardContentState(refreshList);
}

class _NameCardContentState extends State{
  Function() refreshList;

  _NameCardContentState(this.refreshList);

  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    return EditableContent(
      builder: (b, refresh, state) =>
        Hero(
          transitionOnUserGestures: true,
          tag: () => editable.getFileLocation(SW.of(context)),
          child: EditingText(
            editing: b,
            editableBackup: editable,
            style: Theme.of(context).textTheme.headline5!,
            textAlign: TextAlign.center,
            initialText: editable.name,
            controller: (){
              var cont = new TextEditingController(text: editable.name);
              cont.addListener(() {
                editable.name = cont.text;
                refreshList();
              });
              return cont;
            }(),
            defaultSave: true,
            textCapitalization: TextCapitalization.words,
          )
        )
    );
  }
}