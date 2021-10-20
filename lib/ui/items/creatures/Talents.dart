import 'package:flutter/material.dart';
import 'package:swassistant/items/Talent.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/ui/dialogs/creature/TalentEditDialog.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/BottomSheetTemplate.dart';

class Talents extends StatelessWidget{

  final bool editing;
  final Function() refresh;

  Talents({required this.editing, required this.refresh});

  @override
  Widget build(BuildContext context) {
    var creature = Creature.of(context);
    if (creature == null)
      throw "Talents card used on non Creature";
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: List.generate(
          creature.talents.length,
          (index) => InkResponse(
            containedInkWell: true,
            highlightShape: BoxShape.rectangle,
            onTap: () =>
              Bottom(
                child: (context) =>
                  Wrap(
                    alignment: WrapAlignment.center,
                    children: [
                      Container(height: 15),
                      Center(
                        child: Text(
                          creature.talents[index].name,
                          style: Theme.of(context).textTheme.headline5,
                          textAlign: TextAlign.justify,
                        )
                      ),
                      Container(height: 5),
                      Center(
                        child: Text(
                          AppLocalizations.of(context)!.rank + ": " + creature.talents[index].value.toString(),
                          style: Theme.of(context).textTheme.bodyText1,
                        ),
                      ),
                      Container(height: 10),
                      Text(creature.talents[index].desc)
                    ],
                  )
              ).show(context),
            child: Row(
              children: [
                Expanded(
                  child: Text(creature.talents[index].name + (creature.talents[index].value > 1 ? " " + creature.talents[index].value.toString() : "")),
                ),
                AnimatedSwitcher(
                  child: editing ? ButtonBar(
                    buttonPadding: EdgeInsets.zero,
                    children: [
                      IconButton(
                        icon: Icon(Icons.delete_forever),
                        iconSize: 24.0,
                        constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                        onPressed: (){
                          var temp = Talent.from(creature.talents[index]);
                          creature.talents.removeAt(index);
                          refresh();
                          creature.save(context: context);
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(
                              content: Text(AppLocalizations.of(context)!.deletedTalent),
                              action: SnackBarAction(
                                label: AppLocalizations.of(context)!.undo,
                                onPressed: (){
                                  creature.talents.insert(index, temp);
                                  refresh();
                                  creature.save(context: context);
                                },
                              ),
                            )
                          );
                        },
                      ),
                      IconButton(
                        icon: Icon(Icons.edit),
                        iconSize: 24.0,
                        constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                        onPressed: () =>
                          TalentEditDialog(
                            onClose: (talent){
                              creature.talents[index] = talent;
                              refresh();
                              creature.save(context: context);
                            },
                            tal: creature.talents[index],
                          ).show(context)
                      )
                    ],
                  ) : Container(height: 40),
                  duration: Duration(milliseconds: 250),
                  transitionBuilder: (child, anim){
                    var offset = Offset(1,0);
                    if((!editing && child is ButtonBar) || (editing && child is Container))
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
                )
              ],
            )
          )
        )..add(
          AnimatedSwitcher(
            duration: Duration(milliseconds: 300),
            child: editing ? Center(
              child: IconButton(
                icon: Icon(Icons.add),
                onPressed: () =>
                  TalentEditDialog(
                    onClose: (talent){
                      creature.talents.add(talent);
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
        ),
      ),
    );
  }
}