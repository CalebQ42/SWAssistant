import 'package:flutter/material.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/sw.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';
import 'package:swassistant/preferences.dart' as preferences;

class TrashList extends StatefulWidget{

  const TrashList({Key? key}) : super(key: key);

  @override
  State<TrashList> createState() => _TrashListState();
}

class _TrashListState extends State<TrashList> {
  final GlobalKey<AnimatedListState> listKey = GlobalKey();

  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
    if(app.getPreference(preferences.initialTrash, true)){
      Future(() async{
        while(!mounted){
          await Future.delayed(const Duration(milliseconds: 100));
        }
        Bottom(
          child: (c) => Text(AppLocalizations.of(context)!.trashNotice),
        ).show(context);
        app.prefs.setBool(preferences.initialTrash, false);
      });
    }
    return Scaffold(
      body: AnimatedList(
        key: listKey,
        initialItemCount: app.trashCan.length,
        itemBuilder: (context, i, anim) =>
          SlideTransition(
            position: Tween<Offset>(begin: const Offset(1.0, 0.0), end: Offset.zero).animate(anim),
            child: Dismissible(
              onDismissed: (_) {
                var messager = ScaffoldMessenger.of(context);
                var tmpEd = app.trashCan[i];
                app.trashCan.removeAt(i);
                tmpEd.deletePermanently(app);
                listKey.currentState?.removeItem(i, (context, animation) => Container());
                messager.clearSnackBars();
                messager.showSnackBar(
                  SnackBar(
                    content: Text(AppLocalizations.of(context)!.deletedPermanently(tmpEd.name)),
                    action: SnackBarAction(
                      label: AppLocalizations.of(context)!.undo,
                      onPressed: (){
                        tmpEd.save(app: app);
                        app.trashCan.insert(i, tmpEd);
                        listKey.currentState?.insertItem(i);
                      },
                    ),
                  )
                );
              },
              key: ValueKey(SW.of(context).trashCan[i].uid),
              child: Card(
                margin: const EdgeInsets.all(5),
                child: InkResponse(
                  containedInkWell: true,
                  highlightShape: BoxShape.rectangle,
                  onTap: (){
                    var messager = ScaffoldMessenger.of(context);
                    var tmp = app.trashCan[i];
                    app.trashCan.remove(tmp);
                    tmp.trashed = false;
                    tmp.trashTime = null;
                    app.add(tmp);
                    listKey.currentState?.removeItem(i, (context, animation) => Container());
                    messager.clearSnackBars();
                    messager.showSnackBar(
                      SnackBar(
                        content: Text(AppLocalizations.of(context)!.restored(tmp.name)),
                      )
                    );
                  },
                  child: Padding(
                    padding: const EdgeInsets.all(5),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        Text(
                          app.trashCan[i].name,
                          style: Theme.of(context).textTheme.headlineSmall
                        ),
                        Container(height: 10),
                        Align(
                          alignment: Alignment.bottomRight,
                          child: Text(
                            (app.trashCan[i] is Character) ?
                              AppLocalizations.of(context)!.characters
                            : (app.trashCan[i] is Minion) ?
                              AppLocalizations.of(context)!.minions
                            :
                              AppLocalizations.of(context)!.vehicles,
                            style: Theme.of(context).textTheme.caption
                          )
                        )
                      ],
                    )
                  )
                )
              ),
            )
          ),
      )
    );
  }
}