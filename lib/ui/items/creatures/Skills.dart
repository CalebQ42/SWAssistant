import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class Skills extends StatelessWidget{
  final Editable editable;
  final bool editing;
  final SW app;

  Skills({this.editable, this.editing, this.app});

  Widget build(BuildContext context){
    var skillList = List.generate((editable as Creature).skills.length, (index){
      return InkResponse(
        containedInkWell: true,
        onTap: (){
          //TODO: skill tap
        },
        child: Row(
          children: [
            Expanded(
              child: Text((editable as Creature).skills[index].name),
              flex: 7
            ),
            AnimatedSwitcher(
              child: !editing ? Text((editable as Creature).skills[index].value.toString())
                : IconButton(
                  icon: Icon(Icons.delete_forever),
                  onPressed: (){
                    //TODO: delete skill
                  }
                ),
              duration: Duration(milliseconds: 300),
              transitionBuilder: (child, anim){
                return ClipRect(
                  child:SlideTransition(
                    position: Tween<Offset>(
                      begin: (editing && child is IconButton) || (!editing && child is Text) ? Offset(-1.0,0):Offset(1.0,0.0),
                      end: Offset.zero
                    ).animate(anim),
                    child: child,
                  )
                );
              },
            ),
            AnimatedSwitcher(
              duration: Duration(milliseconds: 300),
              child:editing ? IconButton(
                icon: Icon(Icons.edit),
                onPressed: (){
                  //TODO: edit skill
                }
              ) : Padding(padding: EdgeInsets.symmetric(vertical: 24.0),),
              transitionBuilder: (child, animation) {
                return SizeTransition(
                  axis: Axis.horizontal,
                  axisAlignment: 1.0,
                  sizeFactor: animation,
                  child: child,
                );
              },
            )
          ]
        )
      );
    });
    return Column(
      children: <Widget>[
        Column(
          children: skillList
        ),
        AnimatedSwitcher(
          duration: Duration(milliseconds: 300),
          child: editing ? Center(
            child: IconButton(
              icon: Icon(Icons.add),
              onPressed: (){
                //TODO: add skill
              },
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
    );
  }
}