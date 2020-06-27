import 'package:flutter/material.dart';

class UpDownStat extends StatefulWidget{

  final Key key;
  final Function onUpPressed;
  final Function onDownPressed;
  final int Function() getValue;
  final int Function() getMax;
  final int Function() getMin;

  UpDownStat({this.key,this.onUpPressed, this.onDownPressed, this.getValue, this.getMax, this.getMin});

  @override
  State<StatefulWidget> createState()=>UpDownStatState(onUpPressed: onUpPressed,
      onDownPressed: onDownPressed, getValue: getValue, getMax: getMax, getMin: getMin);
}

class UpDownStatState extends State{

  final Function onUpPressed;
  final Function onDownPressed;
  final int Function() getValue;
  final int Function() getMax;
  final int Function() getMin;
  bool up = false;

  UpDownStatState({this.onUpPressed, this.onDownPressed, this.getValue, this.getMax, this.getMin});

  @override
  Widget build(BuildContext context) {
    return Row(
      key: ValueKey("UpDownStat"),
      children: <Widget>[
        Expanded(
          child: IconButton(
            icon:Icon(Icons.remove),
            onPressed: (){
              if(getMin == null || getValue()>getMin()){
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
              if(getMax == null || getValue()<getMax()){
                up = true;
                onUpPressed();
                setState((){});
              }
            },
          )
        )
      ],
    );
  }
}

