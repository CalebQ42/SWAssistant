import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/ui/dialogs/creature/SkillEditDialog.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Skills extends StatelessWidget{
  final bool editing;
  final Function() refresh;

  Skills({required this.editing, required this.refresh});

  Widget build(BuildContext context){
    var creature = Creature.of(context);
    if (creature == null)
      throw "Characteristics card used on non Creature";
    var children = List.generate(creature.skills.length, (index){
      return InkResponse(
        containedInkWell: true,
        highlightShape: BoxShape.rectangle,
        onTap: () =>
          creature.skills[index].getDice(creature).showDialog(context),
        child: Row(
          children: [
            Expanded(
              child: Text(creature.skills[index].name!,
                style: TextStyle(fontWeight: creature.skills[index].career ? FontWeight.bold : FontWeight.normal)
              ),
              flex: 7
            ),
            AnimatedSwitcher(
              child: !editing && creature is Character ? Padding(
                child:Text(creature.skills[index].value.toString()),
                padding: EdgeInsets.all(12)
              )
              : !editing ? Container(height: 40,) : ButtonBar(
                buttonPadding: EdgeInsets.zero,
                children: [
                  IconButton(
                    iconSize: 24.0,
                    constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: Icon(Icons.delete_forever),
                    onPressed: (){
                      var temp = Skill.from(creature.skills[index]);
                      creature.skills.removeAt(index);
                      refresh();
                      creature.save(context: context);
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(AppLocalizations.of(context)!.deletedSkill),
                          action: SnackBarAction(
                            label: AppLocalizations.of(context)!.undo,
                            onPressed: (){
                              creature.skills.insert(index, temp);
                              refresh();
                              creature.save(context: context);
                            }
                          ),
                        )
                      );
                    }
                  ),
                  IconButton(
                    constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: Icon(Icons.edit),
                    onPressed: () =>
                      SkillEditDialog(
                        creature: creature,
                        onClose: (skill){
                          creature.skills[index] = skill;
                          refresh();
                          creature.save(context: context);
                        },
                        sk: creature.skills[index]
                      ).show(context)
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
                onPressed: () =>
                  SkillEditDialog(
                    creature: creature,
                    onClose: (skill){
                      creature.skills.add(skill);
                      refresh();
                      creature.save(context: context);
                    },
                  ).show(context)
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