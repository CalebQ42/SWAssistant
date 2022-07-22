import 'package:flutter/material.dart';
import 'package:swassistant/ui/frame_content.dart';

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
    FrameContent(
      fab: (nextScreen != null || nextScreenAction != null) ? FloatingActionButton(
        heroTag: null,
        onPressed: nextScreen != null ? () =>
          Navigator.of(context).push(
            PageRouteBuilder(
              pageBuilder: (context, anim, secondaryAnim) => nextScreen!,
              settings: const RouteSettings(name: "/intro"),
              transitionsBuilder: (context, anim, secondary, child) =>
                SlideTransition(
                  position: Tween<Offset>(begin: const Offset(1.0, 0.0), end: Offset.zero).animate(anim),
                  child: child
                )
            )
          ) : nextScreenAction,
        child: nextScreenIcon ?? const Icon(Icons.arrow_forward)
      ) : null,
      child: Stack(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 25.0),
            child: Align(
              alignment: Alignment.center,
              child: ConstrainedBox(
                constraints: const BoxConstraints(maxWidth: 500),
                child: SingleChildScrollView(
                  child: child
                ),
              ),
            )
          ),
          if(prevScreenAction != null || defPrevScreen) Align(
            alignment: Alignment.bottomLeft,
            child: Padding(
              padding: const EdgeInsets.all(20.0),
              child: IconButton(
                icon: prevScreenIcon ?? const Icon(Icons.arrow_back),
                onPressed: prevScreenAction ?? () =>
                  Navigator.of(context).pop()
              ),
            ),
          )
        ],
      ),
    );
}