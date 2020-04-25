import 'package:flutter/material.dart';

class InfoCard extends StatefulWidget{

  final InfoCardHolder holder = new InfoCardHolder();

  InfoCard({hidden = true, @required Widget contents}){
    holder.hidden = hidden;
    holder.contents = contents;
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
    if(holder.hidden)
      content = Column();
    else
      content = holder.contents;
    return Card(
      child: Padding(
        padding: EdgeInsets.all(10.0),
        child: Column(
          children: <Widget>[
            SwitchListTile(
              title: Text("Wolololo"),
              value: !holder.hidden,
              onChanged: (bool b){
                setState((){
                  holder.hidden = !b;
                });
              },
            ),
            new AnimatedSwitcher(
              duration: Duration(milliseconds:150),
              transitionBuilder: (Widget wid,Animation<double> anim){
                return SizeTransition(
                  axis:Axis.vertical,
                  sizeFactor: anim,
                  child: wid,
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
  bool hidden;
  Widget contents;
}