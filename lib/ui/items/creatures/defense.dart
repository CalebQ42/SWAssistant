import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class Defense extends StatefulWidget{

  const Defense({Key? key}) : super(key: key);

  @override
  State<Defense> createState() => DefenseState();
}

class DefenseState extends State<Defense> with StatefulCard {

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Creature.of(context)!.defMelee == 0 && Creature.of(context)!.defRanged == 0;

  TextEditingController? melee;
  TextEditingController? ranged;

  @override
  Widget build(BuildContext context) {
    var creature = Creature.of(context);
    if (creature == null) throw "Defense card used on non Creature";
    if(melee == null){
      melee = TextEditingController(text: creature.defMelee.toString());
      melee!.addListener(() {
        creature.defMelee = int.tryParse(melee!.text) ?? 0;
      });
      ranged = TextEditingController(text: creature.defRanged.toString());
      ranged!.addListener(() {
        creature.defRanged = int.tryParse(ranged!.text) ?? 0;
      });
    }
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: [
        Expanded(
          child: Column(
            children: [
              EditingText(
                title: AppLocalizations.of(context)!.melee,
                editing: edit,
                initialText: creature.defMelee.toString(),
                defaultSave: true,
                textType: TextInputType.number,
                controller: melee,
              )
            ],
          ),
        ),
        Expanded(
          child: Column(
            children: [
              EditingText(
                title: AppLocalizations.of(context)!.ranged,
                editing: edit,
                initialText: creature.defRanged.toString(),
                defaultSave: true,
                textType: TextInputType.number,
                controller: ranged,
              )
            ],
          )
        )
      ],
    );
  }
}