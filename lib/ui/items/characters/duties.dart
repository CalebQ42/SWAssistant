import 'package:flutter/material.dart';
import 'package:swassistant/items/duty.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/dialogs/character/duty_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:darkstorm_common/bottom.dart';

class Duties extends StatefulWidget{

  const Duties({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => DutiesState();

}

class DutiesState extends State<Duties> with StatefulCard {

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Character.of(context)?.duties.isEmpty ?? false;

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null) throw "Duties card used on non Character";
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: List.generate(
          character.duties.length,
          (index) => Row(
            children: [
              Expanded(
                child: Text(character.duties[index].name)
              ),
              ButtonBar(
                buttonPadding: EdgeInsets.zero,
                children: [
                  IconButton(
                    constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: const Icon(Icons.info_outline),
                    splashRadius: 20,
                    onPressed: () =>
                      Bottom(
                        child: (context) =>
                          Wrap(
                            alignment: WrapAlignment.center,
                            children: [
                              Container(height: 15),
                              Text(
                                character.duties[index].name,
                                style: Theme.of(context).textTheme.headlineSmall,
                                textAlign: TextAlign.center
                              ),
                              Container(height: 5),
                              Text(
                                "${character.duties[index].value} ${AppLocalizations.of(context)!.duty}",
                                style: Theme.of(context).textTheme.bodyLarge,
                                textAlign: TextAlign.center,
                              ),
                              Container(height: 10),
                              if(character.duties[index].desc != "") Text(character.duties[index].desc)
                            ],
                          )
                      ).show(context)
                  )
                ]
              ),
              AnimatedSwitcher(
                duration: const Duration(milliseconds: 250),
                transitionBuilder: (child, anim){
                  var offset = const Offset(1,0);
                  if(child is Padding){
                    offset = const Offset(-1,0);
                  }
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
                child: edit ? ButtonBar(
                  buttonPadding: EdgeInsets.zero,
                  children: [
                    IconButton(
                      icon: const Icon(Icons.delete_forever),
                      splashRadius: 20,
                      constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                      onPressed: (){
                        var tmp = Duty.from(character.duties[index]);
                        setState(() => character.duties.removeAt(index));
                        character.save(context: context);
                        ScaffoldMessenger.of(context).clearSnackBars();
                        ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                          content: Text(AppLocalizations.of(context)!.deletedDuty),
                          action: SnackBarAction(
                            label: AppLocalizations.of(context)!.undo,
                            onPressed: (){
                              setState(() => character.duties.insert(index, tmp));
                              character.save(context: context);
                            },
                          ),
                        ));
                      },
                    ),
                    IconButton(
                      icon: const Icon(Icons.edit),
                      splashRadius: 20,
                      constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                      onPressed: () =>
                        DutyEditDialog(
                          d: character.duties[index],
                          onClose: (duty){
                            setState(() => character.duties[index] = duty);
                            character.save(context: context);
                          },
                        ).show(context)
                    )
                  ],
                ) : Padding(
                  padding: const EdgeInsets.all(12),
                  child: Text(character.duties[index].value.toString())
                ),
              )
            ]
          ),
        )..add(
          AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            transitionBuilder: (child, anim) =>
              SizeTransition(
                sizeFactor: anim,
                axisAlignment: -1.0,
                child: child
              ),
            child: edit ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  DutyEditDialog(
                    onClose: (duty){
                      setState(() => character.duties.add(duty));
                      character.save(context: context);
                    }
                  ).show(context),
              )
            ) : Container(),
          )
        )
      )
    );
  }
}