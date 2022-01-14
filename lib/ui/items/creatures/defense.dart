import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Defense extends StatelessWidget{
  final bool editing;
  final EditableContentState state;

  const Defense({required this.editing, required this.state, Key? key}) : super(key: key);
  
  @override
  Widget build(BuildContext context) {
    var creature = Creature.of(context);
    if (creature == null) throw "Defense card used on non Creature";
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: [
        Expanded(
          child: Column(
            children: [
              EditingText(
                title: AppLocalizations.of(context)!.melee,
                editing: editing,
                initialText: creature.defMelee.toString(),
                defaultSave: true,
                textType: TextInputType.number,
                controller: (){
                  var cont = TextEditingController(text: creature.defMelee.toString());
                  cont.addListener(() =>
                    creature.defMelee = int.tryParse(cont.text) ?? 0
                  );
                  return cont;
                }(),
                // controller: TextEditingController(text: creature.defMelee.toString()),
              )
            ],
          ),
        ),
        Expanded(
          child: Column(
            children: [
              EditingText(
                title: AppLocalizations.of(context)!.ranged,
                editing: editing,
                initialText: creature.defRanged.toString(),
                defaultSave: true,
                textType: TextInputType.number,
                controller: (){
                  var cont = TextEditingController(text: creature.defRanged.toString());
                  cont.addListener(() =>
                    creature.defRanged = int.tryParse(cont.text) ?? 0
                  );
                  return cont;
                }(),
              )
            ],
          )
        )
      ],
    );
  }
}