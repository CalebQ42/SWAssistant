import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditableList extends StatefulWidget{

  final int type;
  final Key? key;
  final bool contained;
  final void Function(Editable)? onTap;

  EditableList(this.type, {this.key, this.contained = false, this.onTap})

  @override
  State<StatefulWidget> createState() =>
    EditableListState()
}

class EditableListState extends State{

  //This determines which list is shown
  //character = 0
  //minion = 1
  //vehicle = 2
  //TODO
  int type;
  final Key? key;
  final bool contained;
  final void Function(Editable)? onTap;
  List<Editable> list;

  EditableListState(int type, {this.key, this.contained = false, this.onTap}){
    switch(type){
      case 0:

    }
  }

  @override
  Widget build(BuildContext context) {
  }
}

class EditableCard extends StatelessWidget{

  final bool showType;
  final void Function(Editable)? onTap;

  @override
  Widget build(BuildContext context) =>
    Card(
      margin: EdgeInsets.all(5),
      child: InkResponse(
        containedInkWell: true,
        highlightShape: BoxShape.rectangle,
        onTap: (){

        }
        child: Padding(
          padding: EdgeInsets.all(5),
        )
      )
    );
}