import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class InfoCard extends StatefulWidget{

  final InfoCardHolder holder = new InfoCardHolder();
  final Function(bool b, Function refreshList) onHideChange;

  InfoCard({shown = true, @required Widget contents, String title = "", @required this.onHideChange}){
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
  Function(bool b, Function refreshList) onHideChange;

  _InfoCardState(this.holder, this.onHideChange);

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
      childrenPadding: EdgeInsets.all(10),
      title: AnimatedAlign(
        curve: Curves.easeOutBack,
        duration: Duration(milliseconds: 500),
        alignment: holder.shown ? Alignment.center : Alignment.centerLeft,
        child: Text(holder.title, style: TextStyle(color: Theme.of(context).primaryTextTheme.bodyText1.color),),
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
  bool shown;
  Widget contents;
  String title;
}

class NameCardContent extends StatefulWidget{
  final Function refreshList;

  const NameCardContent(this.refreshList);
  @override
  State<StatefulWidget> createState() {
    return _NameCardContentState(refreshList);
  }
}

class _NameCardContentState extends State{
  Function refreshList;

  _NameCardContentState(this.refreshList);
  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    return EditableContent(
      builder: (bool b, refresh, state){
        //TODO: fix hero!
        return /*Hero(
          transitionOnUserGestures: true,
          tag: (){
            String out = "";
            if(editable is Character)
              out = "character/";
            else if (editable is Minion)
              out = "minion/";
            else if (editable is Vehicle)
              out = "vehicle/";
            return out + editable.id.toString();
          }(),child: */EditingText(
            editing: b,
            style: Theme.of(context).textTheme.headline5,
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
          //)
        );
      },
    );
  }
}