import 'package:flutter/material.dart';

class UpDownStat extends StatefulWidget{

  final Key key;
  final Function onUpPressed;
  final Function onDownPressed;
  final int Function() getValue;
  final int max;
  final Function getMax;
  final int min;
  final bool allowNegative;

  UpDownStat({this.key,this.onUpPressed, this.onDownPressed, this.getValue, this.max, this.getMax, this.min, this.allowNegative = false});

  @override
  State<StatefulWidget> createState()=>_UpDownStatState(onUpPressed: onUpPressed,
      onDownPressed: onDownPressed, getValue: getValue, max: max, min: min, allowNegative: allowNegative, getMax: getMax);
}

class _UpDownStatState extends State{

  final Function onUpPressed;
  final Function onDownPressed;
  final int Function() getValue;
  final int max;
  final Function getMax;
  final int min;
  final bool allowNegative;
  bool up = false;

  _UpDownStatState({this.onUpPressed, this.onDownPressed, this.getValue, this.max, int min, this.allowNegative = false, this.getMax}) :
    this.min = min ?? (allowNegative ? null : 0);

  @override
  Widget build(BuildContext context) {
    return Row(
      key: ValueKey("UpDownStat"),
      children: <Widget>[
        Expanded(
          child: IconButton(
            icon:Icon(Icons.remove),
            onPressed: (){
              if(min == null || getValue() > min){
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
              if((max == null && getMax == null) || getValue() < (getMax == null ? max : getMax())){
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