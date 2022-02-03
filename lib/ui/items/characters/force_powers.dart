import 'package:flutter/material.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/items/force_power.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/dialogs/character/fp_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class ForcePowers extends StatefulWidget{

  const ForcePowers({Key? key}) : super(key: key);
  
  @override
  State<StatefulWidget> createState() => ForcePowerState();
}

class ForcePowerState extends State<ForcePowers> with StatefulCard{

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Character.of(context)?.forcePowers.isEmpty ?? false;

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null) throw "Force Powers card used on non Character";
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(AppLocalizations.of(context)!.forceRating),
              SizedBox(
                width: 50,
                child: EditingText(
                  editing: edit,
                  initialText: character.force.toString(),
                  collapsed: true,
                  fieldAlign: TextAlign.center,
                  fieldInsets: const EdgeInsets.all(3),
                  controller: (){
                    var controller = TextEditingController(text: character.force.toString());
                    controller.addListener(() =>
                      character.force = int.tryParse(controller.text) ?? 0
                    );
                    return controller;
                  }(),
                  textType: TextInputType.number,
                  defaultSave: true,
                )
              )
            ],
          ), ...List.generate(
          character.forcePowers.length,
          (index) => InkResponse(
            containedInkWell: true,
            highlightShape: BoxShape.rectangle,
            onTap: () =>
              SWDiceHolder(force: character.force).showDialog(context),
            onLongPress: () =>
              Bottom(
                child: (context) =>
                  Wrap(
                    alignment: WrapAlignment.center,
                    children: [
                      Container(height: 15),
                      Center(
                        child: Text(
                          character.forcePowers[index].name,
                          style: Theme.of(context).textTheme.headline5,
                          textAlign: TextAlign.justify,
                        )
                      ),
                      Container(height: 10),
                      Text(character.forcePowers[index].desc)
                    ],
                  )
              ).show(context),
            child: Row(
              children: [
                Expanded(
                  child: Text(character.forcePowers[index].name),
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
                          var temp = ForcePower.from(character.forcePowers[index]);
                          setState(() => character.forcePowers.removeAt(index));
                          character.save(context: context);
                          ScaffoldMessenger.of(context).clearSnackBars();
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(
                              content: Text(AppLocalizations.of(context)!.deletedFP),
                              action: SnackBarAction(
                                label: AppLocalizations.of(context)!.undo,
                                onPressed: (){
                                  setState(() => character.forcePowers.insert(index, temp));
                                  character.save(context: context);
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
                          ForcePowerEditDialog(
                            onClose: (forcePower){
                              setState(() => character.forcePowers[index] = forcePower);
                              character.save(context: context);
                            },
                            power: character.forcePowers[index],
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
        ), AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            child: edit ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  ForcePowerEditDialog(
                    onClose: (forcePower){
                      setState(() => character.forcePowers.add(forcePower));
                      character.save(context: context);
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
      ),
    );
  }
}