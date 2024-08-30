import 'package:flutter/material.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/dialogs/creature/skill_edit.dart';
import 'package:swassistant/ui/misc/edit_content.dart';

class Skills extends StatefulWidget {
  const Skills({super.key});

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
  Widget build(BuildContext context) {
    var creature = Creature.of(context);
    if (creature == null) throw "Characteristics card used on non Creature";
    var app = SW.of(context);
    var children = List.generate(
      creature.skills.length,
      (index) {
        return InkResponse(
          containedInkWell: true,
          highlightShape: BoxShape.rectangle,
          onTap: () =>
              creature.skills[index].getDice(creature).showDialog(context),
          child: Row(
            children: [
              Expanded(
                flex: 7,
                child: Text(
                  (creature.skills[index].career ? "* " : "") +
                      creature.skills[index].name!,
                  style: TextStyle(
                    fontWeight: creature.skills[index].career
                        ? FontWeight.bold
                        : FontWeight.normal,
                  ),
                ),
              ),
              AnimatedSize(
                duration: app.globalDuration,
                child: AnimatedSwitcher(
                  duration: app.globalDuration,
                  transitionBuilder: (child, anim) {
                    return ClipRect(
                      child: SlideTransition(
                        position: Tween<Offset>(
                                begin: const Offset(1, 0), end: Offset.zero)
                            .animate(anim),
                        child: child,
                      ),
                    );
                  },
                  layoutBuilder: (child, oldStack) {
                    List<Widget> newStack = [];
                    for (var chil in oldStack) {
                      newStack.add(
                        SizedOverflowBox(
                          size: Size.zero,
                          child: chil,
                        ),
                      );
                    }
                    return Stack(
                      alignment: AlignmentDirectional.center,
                      children: [
                        ...newStack,
                        child!,
                      ],
                    );
                  },
                  child: !edit && creature is Character
                      ? Padding(
                          padding: const EdgeInsets.all(12),
                          child: Text(creature.skills[index].value.toString()),
                        )
                      : !edit
                          ? Container(
                              height: 40,
                            )
                          : OverflowBar(
                              children: [
                                IconButton(
                                  splashRadius: 20,
                                  constraints: const BoxConstraints(
                                      maxHeight: 40.0, maxWidth: 40.0),
                                  icon: const Icon(Icons.delete_forever),
                                  onPressed: () {
                                    var temp =
                                        Skill.from(creature.skills[index]);
                                    setState(
                                      () => creature.skills.removeAt(index),
                                    );
                                    creature.save(context: context);
                                    ScaffoldMessenger.of(context)
                                        .clearSnackBars();
                                    ScaffoldMessenger.of(context).showSnackBar(
                                      SnackBar(
                                        content: Text(app.locale.deletedSkill),
                                        action: SnackBarAction(
                                          label: app.locale.undo,
                                          onPressed: () {
                                            setState(
                                              () => creature.skills
                                                  .insert(index, temp),
                                            );
                                            creature.save(context: context);
                                          },
                                        ),
                                      ),
                                    );
                                  },
                                ),
                                IconButton(
                                  splashRadius: 20,
                                  constraints: const BoxConstraints(
                                      maxHeight: 40.0, maxWidth: 40.0),
                                  icon: const Icon(Icons.edit),
                                  onPressed: () => SkillEditDialog(
                                    creature: creature,
                                    onClose: (skill) {
                                      setState(
                                          () => creature.skills[index] = skill);
                                      creature.save(context: context);
                                    },
                                    sk: creature.skills[index],
                                  ).show(context),
                                )
                              ],
                            ),
                ),
              ),
            ],
          ),
        );
      },
    );
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5.0),
      child: Column(
        children: <Widget>[
          Column(children: children),
          AnimatedSwitcher(
            duration: app.globalDuration,
            child: edit
                ? Center(
                    child: IconButton(
                      icon: const Icon(Icons.add),
                      onPressed: () => SkillEditDialog(
                        creature: creature,
                        onClose: (skill) {
                          setState(
                            () => creature.skills.add(skill),
                          );
                          creature.save(context: context);
                        },
                      ).show(context),
                    ),
                  )
                : Container(),
            transitionBuilder: (wid, anim) {
              return SizeTransition(
                sizeFactor: anim,
                axisAlignment: -1.0,
                child: wid,
              );
            },
          )
        ],
      ),
    );
  }
}
