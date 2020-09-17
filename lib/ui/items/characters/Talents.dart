import 'package:flutter/material.dart';
import 'package:swassistant/items/Talent.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/dialogs/character/TalentEditDialog.dart';

class Talents extends StatelessWidget{

  final bool editing;
  final Function refresh;

  Talents({this.editing, this.refresh});

  @override
  Widget build(BuildContext context) {
    var character = Editable.of(context) as Character;
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: List.generate(
          character.talents.length,
          (index) => InkResponse(
            containedInkWell: true,
            highlightShape: BoxShape.rectangle,
            onTap: () =>
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
                            character.talents[index].name,
                            style: Theme.of(context).textTheme.headline5,
                            textAlign: TextAlign.justify,
                          )
                        ),
                        Container(height: 5),
                        Center(
                          child: Text(
                            "Rank: " + character.talents[index].value.toString(),
                            style: Theme.of(context).textTheme.bodyText1,
                          ),
                        ),
                        Container(height: 10),
                        Text(character.talents[index].desc)
                      ],
                    )
                  )
              ),
            child: Row(
              children: [
                Expanded(
                  child: Text(character.talents[index].name + (character.talents[index].value > 1 ? " " + character.talents[index].value.toString() : "")),
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
                          var temp = Talent.from(character.talents[index]);
                          character.talents.removeAt(index);
                          Scaffold.of(context).showSnackBar(
                            SnackBar(
                              content: Text("Talent Deleted"),
                              action: SnackBarAction(
                                label: "Undo",
                                onPressed: (){
                                  character.talents.insert(index, temp);
                                  refresh();
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
                              character.talents[index] = talent;
                              refresh();
                            },
                            talent: character.talents[index],
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
                      character.talents.add(talent);
                      refresh();
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