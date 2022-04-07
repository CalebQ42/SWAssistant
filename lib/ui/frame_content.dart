import 'package:flutter/material.dart';
import 'package:swassistant/ui/frame.dart';

class FrameContent extends StatelessWidget{

  final Widget? child;
  final FloatingActionButton? fab;
  final Widget? bottom;
  final bool allowPop;

  const FrameContent({Key? key, this.child, this.fab, this.bottom, this.allowPop = true}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    WillPopScope(
      onWillPop: () async {
        if(!allowPop) return false;
        var rootNav = Navigator.of(context, rootNavigator: true);
        if(Frame.of(context).expanded){
          Frame.of(context).expand();
          return false;
        }
        if(rootNav.canPop()){
          rootNav.pop();
          return false;
        }
        return true;
      },
      child: Container(
        color: Theme.of(context).canvasColor,
        child: Column(
          mainAxisSize: MainAxisSize.max,
          children: [
            Expanded(
              child: Stack(
                children: [
                  if(child != null) child!,
                  if(fab != null) Positioned(
                    right: 16,
                    bottom: 16,
                    child: AnimatedSwitcher(
                      duration: const Duration(milliseconds: 200),
                      child: fab,
                    ),
                  )
                ]
              )
            ),
            if(bottom != null) bottom!
          ]
        )
      )
    );
}