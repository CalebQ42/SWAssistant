import 'package:flutter/material.dart';

class IntroScreen extends StatelessWidget{

  final Widget? child;
  final Widget? nextScreen;
  final void Function()? nextScreenAction;
  final bool defPrevScreen;
  final void Function()? prevScreenAction;
  final Widget? nextScreenIcon;
  final Widget? prevScreenIcon;

  const IntroScreen({this.child, this.nextScreen, this.nextScreenAction, this.nextScreenIcon, this.defPrevScreen = true, this.prevScreenAction, this.prevScreenIcon, Key? key}) :
    super(key: key);

  @override
  Widget build(BuildContext context) =>
    Scaffold(
      floatingActionButton: (nextScreen != null || nextScreenAction != null) ? FloatingActionButton(
        heroTag: null,
        child: nextScreenIcon ?? const Icon(Icons.arrow_forward),
        onPressed: nextScreen != null ? () =>
          Navigator.of(context).push(
            PageRouteBuilder(
              pageBuilder: (context, anim, secondaryAnim) => nextScreen!,
              transitionsBuilder: (context, anim, secondary, child) =>
                SlideTransition(
                  position: Tween<Offset>(begin: const Offset(1.0, 0.0), end: Offset.zero).animate(anim),
                  child: child
                )
            )
          ) : nextScreenAction
      ) : null,
      body: Stack(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 25.0),
            child: Align(
              child: child,
              alignment: Alignment.center,
            )
          ),
          if(prevScreenAction != null || defPrevScreen) Align(
            child: Padding(
              padding: const EdgeInsets.all(20.0),
              child: IconButton(
                icon: prevScreenIcon ?? const Icon(Icons.arrow_back),
                onPressed: prevScreenAction ?? () =>
                  Navigator.of(context).pop()
              ),
            ),
            alignment: Alignment.bottomLeft,
          )
        ],
      ),
    );
}