import 'package:flutter/material.dart';
import 'package:swassistant/items/Talent.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

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
          (index) => Row(
            children: [
              Expanded(
                child: Text(character.talents[index].name + " " + character.talents[index].value.toString()),
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
                        //TODO: delete
                      },
                    ),
                    IconButton(
                      icon: Icon(Icons.edit),
                      iconSize: 24.0,
                      constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                      onPressed: () {}
                        //TODO: edit
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
                onPressed: () {}
                  //TODO: add
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