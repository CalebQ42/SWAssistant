import 'package:flutter/material.dart';


class UpdatingSwitchTile extends StatefulWidget{
  final bool value;
  final Function(bool) onChanged;
  final EdgeInsetsGeometry? contentPadding;
  final Widget? title;

  UpdatingSwitchTile({required this.value, required this.onChanged, this.title, this.contentPadding});

  @override
  State<StatefulWidget> createState() => UpdatingSwitchTileState(value: value, onChanged: onChanged, title: title, contentPadding: contentPadding);
}

class UpdatingSwitchTileState extends State{

  bool value;
  final Function(bool) onChanged;
  final EdgeInsetsGeometry? contentPadding;
  final Widget? title;

  UpdatingSwitchTileState({required this.value, required this.onChanged, this.contentPadding, this.title});

  @override
  Widget build(BuildContext context) =>
    SwitchListTile(
      value: value,
      title: title,
      contentPadding: contentPadding,
      onChanged: (b){
        onChanged(b);
        setState(() => value = b);
      }
    );
}