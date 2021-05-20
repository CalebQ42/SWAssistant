import 'package:flutter/material.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/dialogs/character/SpecializationEditDialog.dart';

class Specializations extends StatelessWidget{

  final bool editing;
  final Function() refresh;

  Specializations({this.editing, this.refresh});

  @override
  Widget build(BuildContext context) {
    var character = Editable.of(context) as Character;
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: List.generate(
          character.specializations.length,
          (index) => Row(
            children: [
              Expanded(
                child: Text(character.specializations[index])
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
                        var temp = character.specializations[index];
                        character.specializations.removeAt(index);
                        refresh();
                        character.save(context: context);
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text("Deleted Specialization"),
                            action: SnackBarAction(
                              label: "Undo",
                              onPressed: (){
                                character.specializations.insert(index,temp);
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
                        SpecializationEditDialog(
                          onClose: (specialization){
                            character.specializations[index] = specialization;
                            refresh();
                            character.save(context: context);
                          },
                          specialization: character.specializations[index]
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
        )..add(
          AnimatedSwitcher(
            duration: Duration(milliseconds: 300),
            child: editing ? Center(
              child: IconButton(
                icon: Icon(Icons.add),
                onPressed: () =>
                  SpecializationEditDialog(
                    onClose: (specialization){
                      character.specializations.add(specialization);
                      refresh();
                      character.save(context: context);
                    },
                    specialization: ""
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
      )
    );
  }
}