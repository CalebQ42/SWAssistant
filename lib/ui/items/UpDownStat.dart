import 'package:flutter/material.dart';

class UpDownStat extends StatefulWidget{

  final Key key;
  final bool editing;
  final String title;
  final Function onUpPressed;
  final Function onDownPressed;
  final int Function() getValue;
  final int Function() getMax;
  final Widget editMax;

  UpDownStat({this.key,this.editing, this.title, this.onUpPressed, this.onDownPressed, this.getValue, this.getMax, this.editMax});

  @override
  State<StatefulWidget> createState(){
      print("editing?");
      print(editing);
    return UpDownStatState(editing: editing, title: title,onUpPressed: onUpPressed,
      onDownPressed: onDownPressed, getValue: getValue, getMax: getMax, editMax: editMax);

      }
}

class UpDownStatState extends State{

  final bool editing;
  final String title;
  final Function onUpPressed;
  final Function onDownPressed;
  final int Function() getValue;
  final int Function() getMax;
  final Widget editMax;
  bool up = false;

  UpDownStatState({this.editing, this.title, this.onUpPressed, this.onDownPressed, this.getValue, this.getMax, this.editMax});

  @override
  Widget build(BuildContext context) {
    Widget meat = Row(
      key: ValueKey("UpDownStat"),
      children: <Widget>[
        Expanded(
          child: IconButton(
            icon:Icon(Icons.remove),
            onPressed: (){
              if(getValue()>0){
                up = false;
                onDownPressed();
                setState((){});
              }
            },
          )
        ),
        Expanded(
          child: AnimatedSwitcher(
            duration: Duration(milliseconds: 150),
            child: Text(getValue().toString(), key: ValueKey(getValue())),
            transitionBuilder: (wid, anim){
              Tween<Offset> offset;
              if((up && wid.key != ValueKey(getValue()))||(!up && wid.key == ValueKey(getValue())))
                offset = Tween(begin: Offset(-1.0,0.0), end: Offset.zero);
              else
                offset = Tween(begin: Offset(1.0,0.0), end: Offset.zero);
              return ClipRect(
                child: SlideTransition(
                  position: offset.animate(anim),
                  child: wid,
                )
              );
            },
          )
        ),
        Expanded(
          child: IconButton(
            icon: Icon(Icons.add),
            onPressed: (){
              if(getValue()<getMax()){
                up = true;
                onUpPressed();
                setState((){});
              }
            },
          )
        )
      ],
    );
    return Column(
      children: <Widget>[
        Text(title),
        meat
      ],
    );
  }
}

