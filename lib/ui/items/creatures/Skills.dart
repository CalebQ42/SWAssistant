import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/ui/dialogs/SWDiceDialog.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/ui/dialogs/SkillEditDialog.dart';

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
              child: !editing && creature is Character ? Padding(
                child:Text(creature.skills[index].value.toString()),
                padding: EdgeInsets.all(12)
              )
              : !editing ? Container() : ButtonBar(
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
                        builder: (innerContext){
                          return SkillEditDialog(
                            creature: creature,
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
                    builder: (innerContext){
                      return SkillEditDialog(
                        creature: creature,
                        onClose: (skill){
                          if(skill != null){
                            creature.skills.add(skill);
                            refresh();
                          }
                        },
                      );
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