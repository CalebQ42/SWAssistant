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

  bool hidden;

  InfoCardState(this.holder){
    hidden = holder.hidden;
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: EdgeInsets.all(10.0),
        child: Column(
          children: <Widget>[
            SwitchListTile(
              title: Text("Wolololo"),
              value: !hidden,
              onChanged: (bool b){
                setState((){
                  holder.hidden = !b;
                  hidden = !b;
                  print("Yo");
                });
              },
            ),
            holder.contents
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