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
    return _SkillsState(editable: editable, editing: editing, app: app);
  }
}

class _SkillsState extends State{
  Editable editable;
  bool editing;
  SW app;

  _SkillsState({this.editable, this.editing, this.app});
  @override
  Widget build(BuildContext context) {
    var skillDisplays = List.generate(
      (editable as Creature).skills.length,
      (index) => _SkillDisplay(creature: editable as Creature,skill: index, editing: editing)
    );
    return Column(
      children: <Widget>[
        Column(
          key: ValueKey(editing.toString() + skillDisplays.toString()),
          children: skillDisplays
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
    return Row(
      children: [
        Expanded(
          child: Text(creature.skills[skill].name),
          flex: 7
        ),
        Expanded(
          child: AnimatedSwitcher(
            child: !editing ? Text(creature.skills[skill].value.toString()):
            IconButton(
              icon: Icon(Icons.delete_forever),
              onPressed: (){
                //TODO: delete skill
              }
            ),
            duration: Duration(milliseconds: 300),
          ),
          flex: 1
        ),
        Expanded(
          child: IconButton(
            icon: Icon(Icons.edit),
            onPressed: (){
              //TODO: edit skill
            }
          ),
          flex: 1
        )
      ]
    );
  }
}