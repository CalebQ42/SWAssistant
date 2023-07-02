import 'package:flutter/material.dart';
import 'package:swassistant/items/talent.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/dialogs/creature/talent_edit.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:darkstorm_common/bottom.dart';

class Talents extends StatefulWidget{

  const Talents({Key? key}) : super(key: key);

  @override
  State createState() => TalentsState();
}

class TalentsState extends State<Talents> with StatefulCard {

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Creature.of(context)?.talents.isEmpty ?? false;

  @override
  Widget build(BuildContext context) {
    var creature = Creature.of(context);
    if (creature == null) throw "Talents card used on non Creature";
    var app = SW.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: List.generate(
          creature.talents.length,
          (index) => Row(
            children: [
              Expanded(
                child: Text(creature.talents[index].name + (creature.talents[index].value! > 1 ? " ${creature.talents[index].value}" : "")),
              ),
              ButtonBar(
                buttonPadding: EdgeInsets.zero,
                children: [
                  IconButton(
                    constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: const Icon(Icons.info_outline),
                    splashRadius: 20,
                    onPressed: () =>
                      Bottom(
                        child: (context) =>
                          Wrap(
                            alignment: WrapAlignment.center,
                            children: [
                              Container(height: 15),
                              Center(
                                child: Text(
                                  creature.talents[index].name,
                                  style: Theme.of(context).textTheme.headlineSmall,
                                  textAlign: TextAlign.justify,
                                )
                              ),
                              Container(height: 5),
                              Center(
                                child: Text(
                                  "${app.locale.rank}: ${creature.talents[index].value}",
                                  style: Theme.of(context).textTheme.bodyLarge,
                                ),
                              ),
                              Container(height: 10),
                              Text(creature.talents[index].desc)
                            ],
                          )
                      ).show(context),
                  )
                ]
              ),
              AnimatedSwitcher(
                duration: const Duration(milliseconds: 300),
                transitionBuilder: (child, anim){
                  var offset = const Offset(1,0);
                  if(child is Container){
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
                child: edit ? ButtonBar(
                  buttonPadding: EdgeInsets.zero,
                  children: [
                    IconButton(
                      icon: const Icon(Icons.delete_forever),
                      splashRadius: 20,
                      constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                      onPressed: (){
                        var temp = Talent.from(creature.talents[index]);
                        setState(() => creature.talents.removeAt(index));
                        creature.save(context: context);
                        ScaffoldMessenger.of(context).clearSnackBars();
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(app.locale.deletedTalent),
                            action: SnackBarAction(
                              label: app.locale.undo,
                              onPressed: (){
                                setState(() => creature.talents.insert(index, temp));
                                creature.save(context: context);
                              },
                            ),
                          )
                        );
                      },
                    ),
                    IconButton(
                      icon: const Icon(Icons.edit),
                      splashRadius: 20,
                      constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                      onPressed: () =>
                        TalentEditDialog(
                          onClose: (talent){
                            setState(() => creature.talents[index] = talent);
                            creature.save(context: context);
                          },
                          tal: creature.talents[index],
                        ).show(context)
                    )
                  ],
                ) : Container(),
              )
            ],
          )
        )..add(
          AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            child: edit ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  TalentEditDialog(
                    onClose: (talent){
                      setState(() => creature.talents.add(talent));
                      creature.save(context: context);
                    },
                  ).show(context)
              )
            ) : Container(),
            transitionBuilder: (wid,anim){
              return SizeTransition(
                sizeFactor: anim,
                axisAlignment: -1.0,
                child: wid,
              );
            },
          )
        ),
      ),
    );
  }
}