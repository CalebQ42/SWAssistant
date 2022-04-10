import 'package:flutter/material.dart';

class MiniIconButton extends StatelessWidget{

  final Color? color;
  final Widget icon;
  final void Function()? onPressed;

  const MiniIconButton({Key? key, this.color, required this.icon, this.onPressed}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    IconButton(
      iconSize: 20.0,
      splashRadius: 20,
      padding: const EdgeInsets.all(5.0),
      constraints: BoxConstraints.tight(const Size.square(30.0)),
      icon: icon,
      color: color,
      onPressed: onPressed
    );
}