import 'package:flutter/material.dart';

class UpDownStat extends StatefulWidget{

  final Function() onUpPressed;
  final Function() onDownPressed;
  final int Function() getValue;
  final int? max;
  final Function()? getMax;
  final int? min;
  final bool allowNegative;
  final Color? textColor;
  final TextStyle? style;

  const UpDownStat({Key? key, required this.onUpPressed, required this.onDownPressed, required this.getValue, this.max, this.getMax, int? min,
    this.allowNegative = false, this.textColor, this.style}) : min = min ?? (allowNegative ? null : 0), super(key: key);

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
              if(widget.min == null || widget.getValue() > widget.min!){
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
            child: ConstrainedBox(
              key: ValueKey(widget.getValue()),
              child: Text(
                widget.getValue().toString(),
                style: widget.style ?? Theme.of(context).textTheme.bodyText2?.copyWith(color: widget.textColor),
              ),
              constraints: const BoxConstraints(maxWidth: 100),
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
              if((widget.max == null && widget.getMax == null) || widget.getValue() < (widget.getMax == null ? widget.max : widget.getMax!())){
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