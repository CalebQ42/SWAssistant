import 'package:flutter/material.dart';

class InfoCard extends StatefulWidget{

  final InfoCardHolder holder = new InfoCardHolder();

  InfoCard({shown = true, @required Widget contents, String title = ""}){
    holder.shown = shown;
    holder.contents = contents;
    holder.title = title;
  }

  @override
  State<StatefulWidget> createState() {
    return InfoCardState(holder);
  }
}

class InfoCardState extends State{

  InfoCardHolder holder;

  InfoCardState(this.holder);

  @override
  Widget build(BuildContext context) {
    var content;
    if(holder.shown)
      content = holder.contents;
    else
      content = Container();
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