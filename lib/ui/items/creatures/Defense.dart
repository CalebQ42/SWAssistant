import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Defense extends StatelessWidget{
  final bool editing;
  final EditableContentState state;

  Defense({required this.editing, required this.state});
  
  @override
  Widget build(BuildContext context) {
    var creature = Creature.of(context);
    if (creature == null)
      throw "Defense card used on non Creature";
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: [
        Expanded(
          child: Column(
            children: [
              EditingText(
                title: "Melee",
                editing: editing,
                initialText: creature.defMelee.toString(),
                defaultSave: true,
                state: state,
                textType: TextInputType.number,
                controller: TextEditingController(text: creature.defMelee.toString()),
              )
            ],
          ),
        ),
        Expanded(
          child: Column(
            children: [
              EditingText(
                editing: editing,
                initialText: creature.defRanged.toString(),
                defaultSave: true,
                state: state,
                textType: TextInputType.number,
                controller: TextEditingController(text: creature.defRanged.toString()),
                title: "Ranged"
              )
            ],
          )
        )
      ],
    );
  }
}