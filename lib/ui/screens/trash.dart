import 'package:darkstorm_common/bottom.dart';
import 'package:darkstorm_common/frame_content.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/sw.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/ui/misc/mini_icon_button.dart';

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
    if(app.getPref(preferences.initialTrash)){
      show(){
        Bottom(
          child: (c) => Text(
            AppLocalizations.of(context)!.trashNotice,
            style: Theme.of(context).textTheme.headlineSmall,
          ),
        ).show(context);
      }
      Future(() async{
        while(!mounted){
          await Future.delayed(const Duration(milliseconds: 100));
        }
        show();
        app.prefs.setBool(preferences.initialTrash, false);
      });
    }
    return FrameContent(
      child: AnimatedList(
        key: listKey,
        initialItemCount: app.trash.length,
        itemBuilder: (context, i, anim) =>
          SlideTransition(
            position: Tween<Offset>(begin: const Offset(1.0, 0.0), end: Offset.zero).animate(anim),
            child: Dismissible(
              onDismissed: (_) {
                var messager = ScaffoldMessenger.of(context);
                var tmpEd = app.trash[i];
                app.trash.removeAt(i);
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
                        app.trash.insert(i, tmpEd);
                        listKey.currentState?.insertItem(i);
                      },
                    ),
                  )
                );
              },
              key: ValueKey(SW.of(context).trash[i].uid),
              child: Card(
                shape: i == 0 ? app.frame.topItemShape : null,
                clipBehavior: i == 0 ? Clip.hardEdge : null,
                margin: const EdgeInsets.all(5),
                child: InkResponse(
                  containedInkWell: true,
                  highlightShape: BoxShape.rectangle,
                  onTap: kIsWeb ? null : (){
                    var messager = ScaffoldMessenger.of(context);
                    var tmp = app.trash[i];
                    app.trash.remove(tmp);
                    tmp.trashed = false;
                    tmp.trashTime = null;
                    app.add(tmp);
                    tmp.save(context: context);
                    listKey.currentState?.removeItem(i, (context, animation) => Container());
                    messager.clearSnackBars();
                    messager.showSnackBar(
                      SnackBar(
                        content: Text(AppLocalizations.of(context)!.restored(tmp.name)),
                      )
                    );
                  },
                  child: Padding(
                    padding: i == 0 ? app.frame.topItemMargin.add(const EdgeInsets.all(5)) : const EdgeInsets.all(5),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        Text(
                          app.trash[i].name,
                          style: Theme.of(context).textTheme.headlineSmall
                        ),
                        Container(height: 10),
                        Align(
                          alignment: Alignment.bottomRight,
                          child: Text(
                            (app.trash[i] is Character) ?
                              AppLocalizations.of(context)!.characters
                            : (app.trash[i] is Minion) ?
                              AppLocalizations.of(context)!.minions
                            :
                              AppLocalizations.of(context)!.vehicles,
                            style: Theme.of(context).textTheme.bodySmall
                          )
                        ),
                        if(kIsWeb) Container(height: 5),
                        if(kIsWeb) ButtonBar(
                          alignment: MainAxisAlignment.end,
                          children: [
                            MiniIconButton(
                              icon: const Icon(Icons.restore),
                              onPressed: (){
                                var messager = ScaffoldMessenger.of(context);
                                var tmp = app.trash[i];
                                app.trash.remove(tmp);
                                tmp.trashed = false;
                                tmp.trashTime = null;
                                app.add(tmp);
                                tmp.save(context: context);
                                listKey.currentState?.removeItem(i, (context, animation) => Container());
                                messager.clearSnackBars();
                                messager.showSnackBar(
                                  SnackBar(
                                    content: Text(AppLocalizations.of(context)!.restored(tmp.name)),
                                  )
                                );
                              }
                            ),
                            MiniIconButton(
                              icon: const Icon(Icons.delete),
                              onPressed: (){
                                var messager = ScaffoldMessenger.of(context);
                                var tmpEd = app.trash[i];
                                app.trash.removeAt(i);
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
                                        app.trash.insert(i, tmpEd);
                                        listKey.currentState?.insertItem(i);
                                      },
                                    ),
                                  )
                                );
                              }
                            ),
                          ],
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