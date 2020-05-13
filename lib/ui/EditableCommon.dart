import 'package:flutter/material.dart';

class EditingText extends StatelessWidget{

  final TextStyle style;
  final String initialText;
  final bool editing;

  final TextEditingController controller;

  EditingText({@required this.editing, this.style, this.initialText = "", this.controller}){
    if(editing && this.controller == null)
      throw "text controller MUST be specified when in editing mode";
  }
  Widget build(BuildContext context) {
    if(editing)
      return TextField(controller: controller, style: style);
    else
      return Padding(
        padding: EdgeInsets.all(12.0),
        child:Text(initialText, style:style)
      );
  }
}