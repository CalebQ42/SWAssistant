import 'package:flutter/material.dart';

class AnimatedWidgetSwitcher extends StatefulWidget{

  final int widgetNum;
  final List<Widget Function(BuildContext)> widgetBuilders;
  final Widget Function(Widget, Animation<double>)? transitionBuilder;

  const AnimatedWidgetSwitcher({Key? key, required this.widgetNum, required this.widgetBuilders, this.transitionBuilder}) : super(key: key);

  @override
  State<StatefulWidget> createState() => AnimatedWidgetSwitcherState();
}

class AnimatedWidgetSwitcherState extends State<AnimatedWidgetSwitcher> {

  int curWid = 0;

  void setWidget(int n) => setState(() => curWid = n);

  @override
  void initState() {
    super.initState();
    curWid = widget.widgetNum;
  }

  @override
  Widget build(BuildContext context) =>
    AnimatedSize(
      duration: const Duration(milliseconds: 300),
      child: AnimatedSwitcher(
        duration: const Duration(milliseconds: 300),
        transitionBuilder: widget.transitionBuilder ?? AnimatedSwitcher.defaultTransitionBuilder,
        layoutBuilder: (child, oldStack){
          var newStack = <Widget>[];
          for(int i = 0; i< oldStack.length; i++){
            newStack.add(SizedOverflowBox(
              size: Size.zero,
              child: oldStack[i]
            ));
          }
          return Stack(
            alignment: AlignmentDirectional.center,
            children:[
              ...newStack,
              child!,
            ]
          );
        },
        child: widget.widgetBuilders[curWid](context)
      ),
    );
}