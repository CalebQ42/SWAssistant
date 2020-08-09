import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/dice/SWDiceDialog.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/profiles/utils/Creature.dart';

class Skills extends StatelessWidget{
  final bool editing;
  final Function refresh;

  Skills({this.editing, this.refresh});

  Widget build(BuildContext context){
    var creature = Creature.of(context);
    var children = List.generate(creature.skills.length, (index){
      return InkResponse(
        containedInkWell: true,
        onTap: (){
          var ability = (creature.charVals[creature.skills[index].base] - creature.skills[index].value).abs();
          var proficiency = creature.charVals[creature.skills[index].base] < creature.skills[index].value ?
            creature.skills[index].value :
            creature.charVals[creature.skills[index].base];
          showDialog(context: context,
            child: SWDiceDialog(
              holder: SWDiceHolder(ability:ability, proficiency: proficiency),
              context: context
            )
          );
        },
        child: Row(
          children: [
            Expanded(
              child: Text(creature.skills[index].name,
                style: TextStyle(fontWeight: creature.skills[index].career ? FontWeight.bold : FontWeight.normal)
              ),
              flex: 7
            ),
            AnimatedSwitcher(
              child: !editing ? Padding(
                child:Text(creature.skills[index].value.toString()),
                padding: EdgeInsets.all(12)
              )
              : ButtonBar(
                buttonPadding: EdgeInsets.zero,
                children: [
                  IconButton(
                    iconSize: 24.0,
                    constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: Icon(Icons.delete_forever),
                    onPressed: (){
                      creature.skills.removeAt(index);
                      refresh();
                    }
                  ),
                  IconButton(
                    constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: Icon(Icons.edit),
                    onPressed: (){
                      showDialog(
                        context: context,
                        builder: (context){
                          return SkillEditDialog(
                            onClose: (skill){
                              creature.skills[index] = skill;
                              refresh();
                            },
                            skill: creature.skills[index]
                          );
                        }
                      );
                    }
                  )
                ]
              ),
              duration: Duration(milliseconds: 250),
              transitionBuilder: (child, anim){
                var offset = Offset(1,0);
                if((!editing && child is ButtonBar) || (editing && child is Padding))
                  offset = Offset(-1,0);
                return ClipRect(
                  child: SizeTransition(
                    sizeFactor: anim,
                    axis: Axis.horizontal,
                    child: SlideTransition(
                      position: Tween<Offset>(
                        begin: offset,
                        end: Offset.zero
                      ).animate(anim),
                      child: child,
                    )
                  )
                );
              },
            ),
          ]
        )
      );
    });
    return Column(
      children: <Widget>[
        Column(
          children: children
        ),
        AnimatedSwitcher(
          duration: Duration(milliseconds: 300),
          child: editing ? Center(
            child: IconButton(
              icon: Icon(Icons.add),
              onPressed: (){
                showDialog(
                  context: context,
                  builder: (context){
                    return SkillEditDialog(onClose: (skill){
                      creature.skills.add(skill);
                      refresh();
                    },skill: null);
                  }
                );
              },
            )
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

class SkillEditDialog extends StatefulWidget{
  //onClose Skill argument is the edited Skill (or new Skill). Skill argument is null if dialog is cancelled.
  //Doesn't get called when dialog is cancelled.
  final Function(Skill) onClose;
  final Skill skill;

  SkillEditDialog({this.onClose, skill}) :
      this.skill = skill == null ? Skill() : Skill.from(skill);

  @override
  State<StatefulWidget> createState() => _SkillEditDialogState(onClose: onClose, skill: skill);
}

class _SkillEditDialogState extends State{
  final Function(Skill) onClose;
  final Skill skill;

  _SkillEditDialogState({this.onClose, this.skill});
  
  Widget build(BuildContext context) {
    if(skill.name == ""){
      skill.name = Skill.skillsList.keys.first;
      skill.base = Skill.skillsList[Skill.skillsList.keys.first];
    }
    return AlertDialog(
      content: Column(
        children: [
          //TODO: Actually editing everything
          PopupMenuButton(
            initialValue: Skill.skillsList.containsValue(skill.name) ? skill.name : Skill.skillsList.keys.last,
            itemBuilder: (context){
              return List.generate(
                Skill.skillsList.length,
                (i){
                  return PopupMenuItem(
                    value: Skill.skillsList.keys.elementAt(i),
                    child: Text(Skill.skillsList.keys.elementAt(i))
                  );
                }
              );
            },
          ),
          SwitchListTile(
            title: Text("Career"),
            value: skill.career,
            onChanged: (b)=>setState(()=>skill.career = b),
          )
        ],
      ),
      actions: [
        FlatButton(
          child: Text("Save"),
          onPressed: (){
            onClose(skill);
            Navigator.of(context).pop();
          },
        ),
        FlatButton(
          child: Text("Cancel"),
          onPressed: (){
            Navigator.of(context).pop();
          },
        )
      ],
    );
  }
}