import 'package:flutter/material.dart';

class IntroScreen extends StatelessWidget{

  final Widget? child;
  final Function()? nextScreenAction;
  final Function()? prevScreenAction;
  final Widget? nextScreenIcon;
  final Widget? prevScreenIcon;

  IntroScreen({this.child, this.nextScreenAction, this.nextScreenIcon, this.prevScreenAction, this.prevScreenIcon});

  @override
  Widget build(BuildContext context) =>
    Scaffold(
      floatingActionButton: (nextScreenAction != null) ? FloatingActionButton(
        child: nextScreenIcon ?? Icon(Icons.arrow_forward),
        onPressed: nextScreenAction
      ) : null,
      body: Stack(
        children: [
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 25.0),
            child: Align(
              child: child,
              alignment: Alignment.center,
            )
          ),
          if(prevScreenAction != null) Align(
            child: Padding(
              padding: EdgeInsets.all(20.0),
              child: IconButton(
                icon: prevScreenIcon ?? Icon(Icons.arrow_back),
                onPressed: prevScreenAction,
              ),
            ),
            alignment: Alignment.bottomLeft,
          )
        ],
      ),
    );
}