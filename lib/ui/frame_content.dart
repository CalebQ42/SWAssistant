import 'package:flutter/material.dart';

class FrameContent extends StatelessWidget{

  final Widget? child;
  final FloatingActionButton? fab;

  const FrameContent({Key? key, this.child, this.fab}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    WillPopScope(
      onWillPop: () async {
        var rootNav = Navigator.of(context, rootNavigator: true);
        if(rootNav.canPop()){
          rootNav.pop();
          return false;
        }
        return true;
      },
      child: Container(
        color: Theme.of(context).canvasColor,
        child: Stack(
          children: [
            if(child != null) child!,
            if(fab != null) Positioned(
              right: 16,
              bottom: 16,
              child: fab!
            )
          ]
        )
      )
    );
}