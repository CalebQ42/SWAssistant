import 'package:flutter/material.dart';
import 'package:swassistant/items/duty.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/dialogs/character/duty_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';

class Duties extends StatelessWidget{

  final Function() refresh;
  final bool editing;

  Duties({required this.refresh, required this.editing});

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null)
      throw "Duties card used on non Character";
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: List.generate(
          character.duties.length,
          (index) => InkResponse(
            containedInkWell: true,
            highlightShape: BoxShape.rectangle,
            child: Row(
              children: [
                Expanded(
                  child: Text(character.duties[index].name)
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
                          var tmp = Duty.from(character.duties[index]);
                          character.duties.removeAt(index);
                          refresh();
                          character.save(context: context);
                          ScaffoldMessenger.of(context).clearSnackBars();
                          ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                            content: Text(AppLocalizations.of(context)!.deletedDuty),
                            action: SnackBarAction(
                              label: AppLocalizations.of(context)!.undo,
                              onPressed: (){
                                character.duties.insert(index, tmp);
                                refresh();
                                character.save(context: context);
                              },
                            ),
                          ));
                        },
                      ),
                      IconButton(
                        icon: Icon(Icons.edit),
                        iconSize: 24.0,
                        constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                        onPressed: () =>
                          DutyEditDialog(
                            d: character.duties[index],
                            onClose: (duty){
                              character.duties[index] = duty;
                              refresh();
                              character.save(context: context);
                            },
                          ).show(context)
                      )
                    ],
                  ) : Padding(
                    child:Text(character.duties[index].value.toString()),
                    padding: EdgeInsets.all(12)
                  ),
                  duration: Duration(milliseconds: 250),
                  transitionBuilder: (child, anim){
                    var offset = Offset(1,0);
                    if((!editing && child is ButtonBar) || (editing && child is Padding))
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
              ]
            ),
            onTap: () =>
              Bottom(
                child: (context) =>
                  Wrap(
                    alignment: WrapAlignment.center,
                    children: [
                      Container(height: 15),
                      Text(
                        character.duties[index].name,
                        style: Theme.of(context).textTheme.headline5,
                        textAlign: TextAlign.center
                      ),
                      Container(height: 5),
                      Text(
                        character.duties[index].value.toString() + " " + AppLocalizations.of(context)!.duty,
                        style: Theme.of(context).textTheme.bodyText1,
                        textAlign: TextAlign.center,
                      ),
                      Container(height: 10),
                      if(character.duties[index].desc != "") Text(character.duties[index].desc)
                    ],
                  )
              ).show(context)
          )
        )..add(
          AnimatedSwitcher(
            duration: Duration(milliseconds: 300),
            transitionBuilder: (child, anim) =>
              SizeTransition(
                sizeFactor: anim,
                child: child,
                axisAlignment: -1.0
              ),
            child: editing ? Center(
              child: IconButton(
                icon: Icon(Icons.add),
                onPressed: () =>
                  DutyEditDialog(
                    onClose: (duty){
                      character.duties.add(duty);
                      refresh();
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