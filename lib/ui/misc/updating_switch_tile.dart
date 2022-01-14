import 'package:flutter/material.dart';


class UpdatingSwitchTile extends StatefulWidget{
  final bool value;
  final Function(bool) onChanged;
  final EdgeInsetsGeometry? contentPadding;
  final Widget? title;

  const UpdatingSwitchTile({required this.value, required this.onChanged, this.title, this.contentPadding, Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => UpdatingSwitchTileState();
}

class UpdatingSwitchTileState extends State<UpdatingSwitchTile>{

  late bool value;

  UpdatingSwitchTileState(){
    value = widget.value;
  }

  @override
  Widget build(BuildContext context) =>
    SwitchListTile(
      value: value,
      title: widget.title,
      contentPadding: widget.contentPadding,
      onChanged: (b){
        widget.onChanged(b);
        setState(() => value = b);
      }
    );
}