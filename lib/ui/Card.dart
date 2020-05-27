import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class InfoCard extends StatefulWidget{

  final InfoCardHolder holder = new InfoCardHolder();
  final Function(bool b) onHideChange;

  InfoCard({shown = true, @required Widget contents, String title = "", @required this.onHideChange}){
    holder.shown = shown;
    holder.contents = contents;
    holder.title = title;
  }

  @override
  State<StatefulWidget> createState() {
    return InfoCardState(holder, onHideChange);
  }
}

class InfoCardState extends State{

  InfoCardHolder holder;
  Function(bool b) onHideChange;

  InfoCardState(this.holder, this.onHideChange);

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
                  if(onHideChange!=null) onHideChange(b);
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
  final Editable editable;
  final Function refresh;
  final SW app;

  const NameCardContent(this.editable, this.refresh, this.app);
  @override
  State<StatefulWidget> createState() {
    return NameCardContentState(editable, refresh, app);
  }
}

class NameCardContentState extends State{

  Editable editable;
  Function refresh;
  SW app;

  NameCardContentState(this.editable, this.refresh, this.app);
  @override
  Widget build(BuildContext context) {
    TextEditingController controller;
    return EditableContent(
      builder: (bool b, Editable edit){
        if(b){
          controller = new TextEditingController(text: edit.name);
          controller.addListener(() {
            editable.name = controller.text;
            refresh();
          });
        }
        return Hero(
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
          }(),child: EditingText(
            editing: b,
            style: Theme.of(context).textTheme.headline5,
            initialText: edit.name,
            controller: controller,
            defaultSave: true,
            editable: editable,
            app: app
          )
        );
      },
      editable: editable,
    );
  }
}