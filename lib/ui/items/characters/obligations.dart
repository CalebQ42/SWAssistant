import 'package:flutter/material.dart';
import 'package:swassistant/items/obligation.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:swassistant/ui/dialogs/character/obli_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';

class Obligations extends StatefulWidget{

  const Obligations({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => ObligationsState();

}

class ObligationsState extends State<Obligations> with StatefulCard {

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null) throw "Obligations card used on non Character";
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: List.generate(
          character.obligations.length,
          (index) => InkResponse(
            containedInkWell: true,
            highlightShape: BoxShape.rectangle,
            child: Row(
              children: [
                Expanded(
                  child: Text(character.obligations[index].name)
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
                          var tmp = Obligation.from(character.obligations[index]);
                          setState(() => character.obligations.removeAt(index));
                          character.save(context: context);
                          ScaffoldMessenger.of(context).clearSnackBars();
                          ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                            content: Text(AppLocalizations.of(context)!.deletedObli),
                            action: SnackBarAction(
                              label: AppLocalizations.of(context)!.undo,
                              onPressed: (){
                                setState(() => character.obligations.insert(index, tmp));
                                character.save(context: context);
                              },
                            ),
                          ));
                        },
                      ),
                      IconButton(
                        icon: const Icon(Icons.edit),
                        iconSize: 24.0,
                        constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                        onPressed: () =>
                          ObligationEditDialog(
                            obli: character.obligations[index],
                            onClose: (obligation){
                              setState(() => character.obligations[index] = obligation);
                              character.save(context: context);
                            },
                          ).show(context)
                      )
                    ],
                  ) : Padding(
                    child:Text(character.obligations[index].value.toString()),
                    padding: const EdgeInsets.all(12)
                  ),
                  duration: const Duration(milliseconds: 250),
                  transitionBuilder: (child, anim){
                    var offset = const Offset(1,0);
                    if((!edit && child is ButtonBar) || (edit && child is Padding)){
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
                        character.obligations[index].name,
                        style: Theme.of(context).textTheme.headline5,
                        textAlign: TextAlign.center
                      ),
                      Container(height: 5),
                      Text(
                        character.obligations[index].value.toString() + " " + AppLocalizations.of(context)!.obligation,
                        style: Theme.of(context).textTheme.bodyText1,
                        textAlign: TextAlign.center,
                      ),
                      Container(height: 10),
                      if(character.obligations[index].desc != "") Text(character.obligations[index].desc)
                    ],
                  )
              ).show(context),
          )
        )..add(
          AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            transitionBuilder: (child, anim) =>
              SizeTransition(
                sizeFactor: anim,
                child: child,
                axisAlignment: -1.0
              ),
            child: edit ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  ObligationEditDialog(
                    onClose: (obligation){
                      setState(() => character.obligations.add(obligation));
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