import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Defense extends StatelessWidget{
  final bool editing;

  Defense({this.editing});
  
  @override
  Widget build(BuildContext context) {
    var creature = Editable.of(context) as Creature;
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: [
        Expanded(
          child: Column(
            children: [
              Text("Melee:"),
              if(editing) Container(height: 5,),
              EditingText(
                editing: editing,
                initialText: creature.defMelee.toString(),
                defaultSave: true,
                textType: TextInputType.number,
                controller: TextEditingController(text: creature.defMelee.toString()),
              )
            ],
          ),
        ),
        Expanded(
          child: Column(
            children: [
              Text("Ranged:"),
              if(editing) Container(height: 5,),
              EditingText(
                editing: editing,
                initialText: creature.defRanged.toString(),
                defaultSave: true,
                textType: TextInputType.number,
                controller: TextEditingController(text: creature.defRanged.toString()),
              )
            ],
          )
        )
      ],
    );
  }
}