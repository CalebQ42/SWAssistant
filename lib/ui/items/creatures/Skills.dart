import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class Skills extends StatefulWidget{
  final Editable editable;
  final bool editing;
  final SW app;

  Skills({this.editable, this.editing, this.app});

  @override
  State<StatefulWidget> createState() {
    return _SkillsState();
  }
}

class _SkillsState extends State{
  Editable editable;
  bool editing;
  SW app;

  _SkillsState({this.editable, this.editing, this.app});
  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Column(
          children: List.generate(
            (editable as Creature).skills.length,
            (index) => _SkillDisplay(creature: editable as Creature,skill: index, editing: editing))
        ),
        AnimatedSwitcher(
          duration: Duration(milliseconds: 300),
          child: editing ? IconButton(
            icon: Icon(Icons.add),
            onPressed: (){
              //TODO: add skill
            },
          ) : Container(),
          transitionBuilder: (wid,anim){
            return SizeTransition(
              sizeFactor: anim,
              child: wid,
              axisAlignment: -1.0,
            );
          },
        )
      ],
    );
  }
}

class _SkillDisplay extends StatelessWidget{
  final bool editing;
  final int skill;
  final Creature creature;

  _SkillDisplay({this.editing, this.skill, this.creature});

  Widget build(BuildContext context){

  }
}