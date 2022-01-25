import 'package:flutter/material.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/dialogs/character/specialization_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/editable_common.dart';

class Specializations extends StatefulWidget{

  const Specializations({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => SpecializationsState();

}

class SpecializationsState extends State<Specializations> with StatefulCard {

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Character.of(context)?.specializations.isEmpty ?? false;

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null) throw "Specializations card used on non Character";
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: List.generate(
          character.specializations.length,
          (index) => Row(
            children: [
              Expanded(
                child: Text(character.specializations[index])
              ),
              AnimatedSwitcher(
                child: edit ? ButtonBar(
                  buttonPadding: EdgeInsets.zero,
                  children: [
                    IconButton(
                      icon: const Icon(Icons.delete_forever),
                      iconSize: 24.0,
                      constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                      onPressed: (){
                        var temp = character.specializations[index];
                        setState(() => character.specializations.removeAt(index));
                        character.save(context: context);
                        ScaffoldMessenger.of(context).clearSnackBars();
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(AppLocalizations.of(context)!.deletedSpecialization),
                            action: SnackBarAction(
                              label: AppLocalizations.of(context)!.undo,
                              onPressed: (){
                                setState(() => character.specializations.insert(index,temp));
                                character.save(context: context);
                              },
                            ),
                          )
                        );
                      },
                    ),
                    IconButton(
                      icon: const Icon(Icons.edit),
                      iconSize: 24.0,
                      constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                      onPressed: () =>
                        SpecializationEditDialog(
                          onClose: (specialization){
                            setState(() => character.specializations[index] = specialization);
                            character.save(context: context);
                          },
                          specialization: character.specializations[index]
                        ).show(context)
                    )
                  ],
                ) : Container(height: 40),
                duration: const Duration(milliseconds: 250),
                transitionBuilder: (child, anim){
                  var offset = const Offset(1,0);
                  if((!edit && child is ButtonBar) || (edit && child is Container)){
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
              )
            ],
          )
        )..add(
          AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            child: edit ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  SpecializationEditDialog(
                    onClose: (specialization){
                      setState(() => character.specializations.add(specialization));
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