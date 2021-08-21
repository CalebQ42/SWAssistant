import 'package:flutter/material.dart';

class UpDownStat extends StatefulWidget{

  final Key? key;
  final Function() onUpPressed;
  final Function() onDownPressed;
  final int Function() getValue;
  final int? max;
  final Function()? getMax;
  final int? min;
  final bool allowNegative;
  final Color? textColor;
  final TextStyle? style;

  UpDownStat({this.key, required this.onUpPressed, required this.onDownPressed, required this.getValue, this.max, this.getMax, this.min, this.allowNegative = false, this.textColor, this.style});

  @override
  State<StatefulWidget> createState()=>_UpDownStatState(onUpPressed: onUpPressed,
      onDownPressed: onDownPressed, getValue: getValue, max: max, min: min, allowNegative: allowNegative, getMax: getMax, textColor: textColor, style: style);
}

class _UpDownStatState extends State{

  final Function() onUpPressed;
  final Function() onDownPressed;
  final int Function() getValue;
  final int? max;
  final Function()? getMax;
  final int? min;
  final bool allowNegative;
  final Color? textColor;
  final TextStyle? style;
  bool up = false;

  _UpDownStatState({required this.onUpPressed, required this.onDownPressed, required this.getValue, this.max = 0, int? min, this.allowNegative = false, this.getMax, this.textColor, this.style}) :
    this.min = min ?? (allowNegative ? null : 0);

  @override
  Widget build(BuildContext context) {
    return Row(
      key: ValueKey("UpDownStat"),
      children: <Widget>[
        Expanded(
          child: IconButton(
            icon: Icon(Icons.remove, color: textColor),
            onPressed: (){
              if(min == null || getValue() > min!){
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
            child: ConstrainedBox(
              key: ValueKey(getValue()),
              child: Text(
                getValue().toString(),
                style: style != null ? style : Theme.of(context).textTheme.bodyText2?.copyWith(color: textColor),
              ),
              constraints: BoxConstraints(maxWidth: 100),
            ),
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
            icon: Icon(Icons.add, color: textColor),
            onPressed: (){
              if((max == null && getMax == null) || getValue() < (getMax == null ? max : getMax!())){
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