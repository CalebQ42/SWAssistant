import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/dice/SWDiceDialog.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/profiles/utils/Creature.dart';

class Skills extends StatefulWidget{
  final bool editing;
  final Function refresh;

  Skills({this.editing, this.refresh});

  @override
  State<StatefulWidget> createState() => _SkillsState(editing: editing, refresh: refresh);
}

class _SkillsState extends State{
  bool editing;
  Function refresh;
  bool editingPrev = false;

  _SkillsState({this.editing, this.refresh});

  Widget build(BuildContext context){
    print(editing);
    if(editing != editingPrev){
      refresh();
      editingPrev = editing;
      print("refreshing!");
    }
    var creature = Creature.of(context);
    var skillList = List.generate(creature.skills.length, (index){
      return InkResponse(
        containedInkWell: true,
        onTap: (){
          var ability = (creature.charVals[creature.skills[index].base] - creature.skills[index].value).abs();
          var proficiency = creature.charVals[creature.skills[index].base] < creature.skills[index].value ?
            creature.skills[index].value :
            creature.charVals[creature.skills[index].base];
          showDialog(context: context,
            child: SWDiceDialog(
              holder: SWDiceHolder(ability:ability, proficiency: proficiency),
              context: context
            )
          );
        },
        child: Row(
          children: [
            Expanded(
              child: Text(creature.skills[index].name),
              flex: 7
            ),
            AnimatedSwitcher(
              child: !editing ? Text(creature.skills[index].value.toString())
                : IconButton(
                  icon: Icon(Icons.delete_forever),
                  onPressed: (){
                    //TODO: delete skill
                  }
                ),
              duration: Duration(milliseconds: 150),
              transitionBuilder: (child, anim){
                return ClipRect(
                  child: SlideTransition(
                    position: Tween<Offset>(
                      begin: (editing && child is IconButton) || (!editing && child is Text) ? Offset(-1.0,0) : Offset(1.0,0.0),
                      end: Offset.zero
                    ).animate(anim),
                    child: child,
                  )
                );
              },
            ),
            AnimatedSwitcher(
              duration: Duration(milliseconds: 150),
              child: editing ? IconButton(
                icon: Icon(Icons.edit),
                onPressed: (){
                  //TODO: edit skill
                }
              ) : Container(height: 48),
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