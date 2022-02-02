import 'package:flutter/material.dart';

class UpDownStat extends StatefulWidget{

  final void Function() onUpPressed;
  final void Function() onDownPressed;
  final int Function() getValue;
  final int? max;
  final int Function()? getMax;
  final int? min;
  final int Function()? getMin;
  final Color? textColor;
  final TextStyle? style;

  const UpDownStat({Key? key, required this.onUpPressed, required this.onDownPressed, required this.getValue, this.max, this.getMax, this.min,
    this.getMin, this.textColor, this.style}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _UpDownStatState();
}

class _UpDownStatState extends State<UpDownStat>{

  bool up = false;

  @override
  Widget build(BuildContext context) {
    return Row(
      key: const ValueKey("UpDownStat"),
      children: <Widget>[
        Expanded(
          child: IconButton(
            icon: Icon(Icons.remove, color: widget.textColor),
            onPressed: (){
              if((widget.min == null && widget.getMin == null) || widget.getValue() > (widget.getMin == null ? widget.min! : widget.getMin!())){
                up = false;
                widget.onDownPressed();
                setState((){});
              }
            },
          )
        ),
        Expanded(
          child: AnimatedSwitcher(
            duration: const Duration(milliseconds: 150),
            child: Text(
              widget.getValue().toString(),
              key: ValueKey(widget.getValue()),
              style: widget.style ?? Theme.of(context).textTheme.headline5?.copyWith(color: widget.textColor),
            ),
            transitionBuilder: (wid, anim){
              Tween<Offset> offset;
              if((up && wid.key != ValueKey(widget.getValue()))||(!up && wid.key == ValueKey(widget.getValue()))){
                offset = Tween(begin: const Offset(-1.0,0.0), end: Offset.zero);
              }else{
                offset = Tween(begin: const Offset(1.0,0.0), end: Offset.zero);
              }
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
            icon: Icon(Icons.add, color: widget.textColor),
            onPressed: (){
              if((widget.max == null && widget.getMax == null) || widget.getValue() < (widget.getMax == null ? widget.max! : widget.getMax!())){
                up = true;
                widget.onUpPressed();
                setState((){});
              }
            },
          )
        )
      ],
    );
  }
}