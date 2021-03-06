import 'package:flutter/material.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/items/ForcePower.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/dialogs/SWDiceDialog.dart';
import 'package:swassistant/ui/dialogs/character/ForcePowerEditDialog.dart';

class ForcePowers extends StatelessWidget{

  final bool editing;
  final Function() refresh;
  final EditableContentState state;

  ForcePowers({required this.editing, required this.refresh, required this.state});
  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null)
      throw "Force Powers card used on non Character";
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text("Force Rating:"),
              SizedBox(
                width: 50,
                child: EditingText(
                  editing: editing,
                  initialText: character.force.toString(),
                  collapsed: true,
                  fieldAlign: TextAlign.center,
                  fieldInsets: EdgeInsets.all(3),
                  state: state,
                  controller: (){
                    var controller = new TextEditingController(text: character.force.toString());
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
          )
        ]..addAll(List.generate(
          character.forcePowers.length,
          (index) => InkResponse(
            containedInkWell: true,
            highlightShape: BoxShape.rectangle,
            onTap: () =>
              showModalBottomSheet(
                context: context,
                builder: (context) =>
                  SWDiceDialog(
                    holder: SWDiceHolder(force: character.force),
                    context: context
                  ),
              ),
            onLongPress: () =>
              showModalBottomSheet(
                context: context,
                builder: (context) =>
                  Padding(
                    padding: EdgeInsets.only(bottom: 20, right: 10, left: 10),
                    child: Wrap(
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
                  )
              ),
            child: Row(
              children: [
                Expanded(
                  child: Text(character.forcePowers[index].name),
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
                          var temp = ForcePower.from(character.forcePowers[index]);
                          character.forcePowers.removeAt(index);
                          refresh();
                          character.save(context: context);
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(
                              content: Text("Force Power Deleted"),
                              action: SnackBarAction(
                                label: "Undo",
                                onPressed: (){
                                  character.forcePowers.insert(index, temp);
                                  refresh();
                                  character.save(context: context);
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
                          ForcePowerEditDialog(
                            onClose: (forcePower){
                              character.forcePowers[index] = forcePower;
                              refresh();
                              character.save(context: context);
                            },
                            power: character.forcePowers[index],
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
        ))..add(
          AnimatedSwitcher(
            duration: Duration(milliseconds: 300),
            child: editing ? Center(
              child: IconButton(
                icon: Icon(Icons.add),
                onPressed: () =>
                  ForcePowerEditDialog(
                    onClose: (forcePower){
                      character.forcePowers.add(forcePower);
                      refresh();
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
        ),
      ),
    );
  }
}