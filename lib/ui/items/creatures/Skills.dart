import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
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
          var proficiency = min(creature.charVals[creature.skills[index].base],creature.skills[index].value);
          showDialog(
            context: context,
            builder: (b) => SWDiceDialog(
              holder: SWDiceHolder(ability: ability, proficiency: proficiency),
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
                      showModalBottomSheet(
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
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5.0),
      child: Column(
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
                  showModalBottomSheet(
                    context: context,
                    builder: (context){
                      return SkillEditDialog(onClose: (skill){
                        if(skill != null){
                          creature.skills.add(skill);
                          refresh();
                        }
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
      )
    );
  }
}

class SkillEditDialog extends StatefulWidget{
  //onClose Skill argument is the edited Skill (or new Skill). Skill argument is null if dialog is cancelled.
  //Doesn't get called when dialog is cancelled.
  final void Function(Skill) onClose;
  final Skill skill;

  SkillEditDialog({this.onClose, Skill skill}) :
      this.skill = skill == null ? Skill.nulled() : Skill.from(skill);

  @override
  State<StatefulWidget> createState() => _SkillEditDialogState(onClose: onClose, skill: skill);
}

class _SkillEditDialogState extends State{
  final Function(Skill) onClose;
  final Skill skill;
  bool manual = false;
  TextSelection prevSelection;

  TextEditingController valueController;

  _SkillEditDialogState({this.onClose, this.skill}){
    if(skill.name != null && !Skill.skillsList.containsKey(skill.name))
      manual = true;
    valueController = new TextEditingController(text: skill.value != null ? skill.value.toString() : "")
        ..addListener(() {
          var val = int.tryParse(valueController.text);
          if((skill.value == null && val != null) || (skill.value != null && val == null))
            setState(() => skill.value = val);
          else
            skill.value = val;
        });
  }
  
  Widget build(BuildContext context) {
    return Padding(
      padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 5)),
      child: Wrap(
      children: [
        Column(
          children: [
            DropdownButton<String>(
              underline: null,
              isExpanded: true,
              onChanged: (value) {
                setState((){
                  if(value != "Other..."){
                    manual = false;
                    skill.name = value;
                    skill.base = Skill.skillsList[value];
                  }else{
                    manual = true;
                    skill.name = "";
                  }
                });
              },
              value: (Skill.skillsList.containsKey(skill.name) || skill.name == null) ? skill.name : Skill.skillsList.keys.last,
              hint: Text("Skill"),
              items: List.generate(
                Skill.skillsList.length,
                (i){
                  return DropdownMenuItem<String>(
                    value: Skill.skillsList.keys.elementAt(i),
                    child: Text(Skill.skillsList.keys.elementAt(i))
                  );
                }
              ),
            ),
            AnimatedSwitcher(
              child: !manual ? Container() :
                TextField(
                  onChanged: (value) => skill.name = value,
                  autofillHints: Skill.skillsList.keys,
                  controller: TextEditingController(text: skill.name),
                ),
              duration: Duration(milliseconds: 150),
              transitionBuilder: (child, anim) {
                return SizeTransition(
                  sizeFactor: anim,
                  child: child,
                );
              }, 
            ),
            DropdownButton<int>(
              isExpanded: true,
              hint: Text("Characteristic"),
              items: List.generate(
                Creature.characteristics.length,
                (i) => DropdownMenuItem(
                  child: Text(Creature.characteristics[i]),
                  value: i
                )
              ),
              value: skill.base,
              onChanged: (value) => setState(() => skill.base = value),
            ),
            TextField(
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
              controller: valueController,
              decoration: InputDecoration(
                prefixIcon: Text("Skill Value: ", style: TextStyle(color:Theme.of(context).hintColor),),
                prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
              ),
            ),
            SwitchListTile(
              contentPadding: EdgeInsets.zero,
              title: Text("Career"),
              value: skill.career,
              onChanged: (b)=>setState(()=>skill.career = b),
            ),
            ButtonBar(
              children: [
                FlatButton(
                  child: Text("Save"),
                  onPressed: (skill.name != null && skill.base != null && skill.value != null) ? (){
                    onClose(skill);
                    Navigator.of(context).pop();
                  } : null,
                ),
                FlatButton(
                  child: Text("Cancel"),
                  onPressed: (){
                    Navigator.of(context).pop();
                  },
                )
              ]
            )
          ],
        ),
      ]
    ));
  }
}