import 'package:flutter/material.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/ui/dialogs/creature/skill_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/editable_common.dart';

class Skills extends StatefulWidget{

  const Skills({Key? key}) : super(key: key);

  @override
  State createState() => SkillsState();
}

class SkillsState extends State<Skills> with StatefulCard {

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Creature.of(context)?.skills.isEmpty ?? false;

  @override
  Widget build(BuildContext context){
    var creature = Creature.of(context);
    if (creature == null) throw "Characteristics card used on non Creature";
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
              child: !edit && creature is Character ? Padding(
                child:Text(creature.skills[index].value.toString()),
                padding: const EdgeInsets.all(12)
              )
              : !edit ? Container(height: 40,) : ButtonBar(
                buttonPadding: EdgeInsets.zero,
                children: [
                  IconButton(
                    iconSize: 24.0,
                    constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: const Icon(Icons.delete_forever),
                    onPressed: (){
                      var temp = Skill.from(creature.skills[index]);
                      setState(() => creature.skills.removeAt(index));
                      creature.save(context: context);
                      ScaffoldMessenger.of(context).clearSnackBars();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(AppLocalizations.of(context)!.deletedSkill),
                          action: SnackBarAction(
                            label: AppLocalizations.of(context)!.undo,
                            onPressed: (){
                              setState(() => creature.skills.insert(index, temp));
                              creature.save(context: context);
                            }
                          ),
                        )
                      );
                    }
                  ),
                  IconButton(
                    constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: const Icon(Icons.edit),
                    onPressed: () =>
                      SkillEditDialog(
                        creature: creature,
                        onClose: (skill){
                          setState(() => creature.skills[index] = skill);
                          creature.save(context: context);
                        },
                        sk: creature.skills[index]
                      ).show(context)
                  )
                ]
              ),
              duration: const Duration(milliseconds: 250),
              transitionBuilder: (child, anim){
                var offset = const Offset(1,0);
                if((!edit && child is ButtonBar) || (edit && child is Padding)){
                  offset = const Offset(-1,0);
                }
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
      padding: const EdgeInsets.symmetric(horizontal: 5.0),
      child: Column(
        children: <Widget>[
          Column(
            children: children
          ),
          AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            child: edit ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  SkillEditDialog(
                    creature: creature,
                    onClose: (skill){
                      setState(() => creature.skills.add(skill));
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