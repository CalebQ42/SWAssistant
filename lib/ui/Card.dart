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
    var content = holder.shown? holder.contents : SizedBox();
    return Card(
      child: Padding(
        padding: EdgeInsets.all(10.0),
        child: Column(
          children: <Widget>[
            SwitchListTile(
              title: Text(holder.title),
              value: holder.shown,
              onChanged: (bool b){
                setState((){
                  if(onHideChange!=null) onHideChange(b,()=>setState((){}));
                  holder.shown = b;
                });
              },
            ),
            new AnimatedSwitcher(
              duration: Duration(milliseconds:250),
              transitionBuilder: (Widget wid,Animation<double> anim){
                return SizeTransition(
                  axis:Axis.vertical,
                  sizeFactor: anim,
                  child: wid,
                  axisAlignment: -1.0
                );
              },
              child: content
            )
          ],
        )
      )
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
      builder: (bool b, refresh){
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