import 'package:flutter/material.dart';
import 'package:swassistant/items/talent.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/ui/dialogs/creature/talent_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/editable_common.dart';
import 'package:swassistant/ui/misc/bottom.dart';

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
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5),
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
                  child: Text(creature.talents[index].name + (creature.talents[index].value! > 1 ? " " + creature.talents[index].value.toString() : "")),
                ),
                AnimatedSwitcher(
                  child: edit ? ButtonBar(
                    buttonPadding: EdgeInsets.zero,
                    children: [
                      IconButton(
                        icon: const Icon(Icons.delete_forever),
                        iconSize: 24.0,
                        constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                        onPressed: (){
                          var temp = Talent.from(creature.talents[index]);
                          setState(() => creature.talents.removeAt(index));
                          creature.save(context: context);
                          ScaffoldMessenger.of(context).clearSnackBars();
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(
                              content: Text(AppLocalizations.of(context)!.deletedTalent),
                              action: SnackBarAction(
                                label: AppLocalizations.of(context)!.undo,
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
                        iconSize: 24.0,
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
                  ) : Container(height: 40),
                  duration: const Duration(milliseconds: 250),
                  transitionBuilder: (child, anim){
                    var offset = const Offset(1,0);
                    if((!edit && child is ButtonBar) || (edit && child is Container)){
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
                )
              ],
            )
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