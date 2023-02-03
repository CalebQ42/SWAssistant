import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/dialogs/destiny.dart';
import 'package:swassistant/ui/frame.dart';
import 'package:swassistant/ui/frame_content.dart';
import 'package:swassistant/ui/misc/mini_icon_button.dart';
import 'package:swassistant/ui/screens/editing_editable.dart';

class EditableList extends StatefulWidget{

  final Type? edType;
  final void Function(Editable)? onTap;

  final String? uidToLoad;

  const EditableList(this.edType, {Key? key, this.onTap, this.uidToLoad}) : super(key: key);

  @override
  State<StatefulWidget> createState() => EditableListState();
}

class EditableListState extends State<EditableList>{

  List<Editable> list = [];
  String? cat;
  //TODO: search

  bool first = true;

  late GlobalKey<AnimatedListState> listKey =  GlobalKey();
  late GlobalKey<RefreshIndicatorState> refreshKey = GlobalKey();

  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
    if(widget.uidToLoad != null){
      var nav = Navigator.of(context);
      Future(() async {
        while(!mounted) {
          await Future.delayed(const Duration(milliseconds: 50));
        }
        var ed = app.getEditable(widget.uidToLoad!);
        if(ed != null) nav.pushNamed("/edit/${widget.uidToLoad!}", arguments: ed);
      });
    }
    var oldLen = list.length;
    List<DropdownMenuItem<String>> categories;
    list = app.getList(type: widget.edType, category: cat);
    categories = [
      DropdownMenuItem<String>(
        value: null,
        child: Text(AppLocalizations.of(context)!.all),
      ),
      DropdownMenuItem<String>(
        value: "",
        child: Text(AppLocalizations.of(context)!.uncategorized)
      ),
      ...List.generate(app.cats.length, (index) =>
        DropdownMenuItem<String>(
          value: app.cats[index],
          child: Text(app.cats[index])
        )
      )
    ];
    if(list.length != oldLen){
      listKey = GlobalKey();
    }
    var catSelector = Padding(
      padding: const EdgeInsets.symmetric(horizontal: 15),
      child: DropdownButtonHideUnderline(
        child: DropdownButton<String>(
          value: cat,
          items: categories,
          isExpanded: true,
          onChanged: (category) {
            cat = category;
            setState((){});
          },
        )
      )
    );
    var localization = AppLocalizations.of(context)!;
    var mainList = RefreshIndicator(
      key: refreshKey,
      onRefresh: () => Future(() async {
        if(app.syncing) return;
        var messager = ScaffoldMessenger.of(context);
        var b = await app.syncRemote();
        messager.clearSnackBars();
        if (!b) {
          messager.showSnackBar(
            SnackBar(
              content: Text(localization.syncFail)
            )
          );
        }
        setState(() {});
      }),
      child: AnimatedList(
        physics: const BouncingScrollPhysics(parent: AlwaysScrollableScrollPhysics()),
        key: listKey,
        initialItemCount: list.length,
        padding: const EdgeInsets.only(bottom: 80),
        itemBuilder: (context, i, anim) =>
          SlideTransition(
            position: Tween<Offset>(begin: const Offset(1.0, 0), end: Offset.zero).animate(anim),
            child: InheritedEditable(
              editable: list[i],
              child: EditableCard(
                onTap: widget.onTap,
                onDismiss: () {
                  var tmp = list[i];
                  list[i].trash(app);
                  list.removeAt(i);
                  listKey.currentState?.removeItem(i, (context, animation) => Container());
                  ScaffoldMessenger.of(context).clearSnackBars();
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text(
                        (tmp is Character) ?
                          AppLocalizations.of(context)!.characterTrashed
                        : (tmp is Minion) ?
                          AppLocalizations.of(context)!.minionTrashed
                        :
                          AppLocalizations.of(context)!.vehicleTrashed
                      ),
                      action: SnackBarAction(
                        label: AppLocalizations.of(context)!.undo,
                        onPressed: (){
                          app.add(tmp);
                          tmp.save(app: app);
                          list.insert(i, tmp);
                          listKey.currentState?.insertItem(i);
                        },
                      )
                    )
                  );
                },
              )
            )
          ),
      )
    );
    return FrameContent(
      fab: widget.onTap == null ? FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: (){
          if(SW.of(context).getPref(preferences.googleDrive)) {
            if(SW.of(context).syncing){
              ScaffoldMessenger.of(context).clearSnackBars();
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(AppLocalizations.of(context)!.driveSyncingNotice)
                )
              );
              return;
            }else if (SW.of(context).driver == null || !SW.of(context).driver!.readySync()) {
              ScaffoldMessenger.of(context).clearSnackBars();
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(AppLocalizations.of(context)!.driveDisconnectNotice)
                )
              );
              return;
            }
          }
          Editable newEd;
          switch(widget.edType){
            case Character:
              newEd = Character(name: AppLocalizations.of(context)!.newCharacter, saveOnCreation: true, app: app);
              break;
            case Minion:
              newEd = Minion(name: AppLocalizations.of(context)!.newMinion, saveOnCreation: true, app: app);
              break;
            default:
              newEd = Vehicle(name: AppLocalizations.of(context)!.newVehicle, saveOnCreation: true, app: app);
          }
          app.add(newEd);
          Navigator.pushNamed(
            context,
            "/edit/${newEd.uid}",
            arguments: newEd
          );
        },
      ) : null,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Container(
            color: Theme.of(context).primaryColorDark,
            child: Padding(
              padding: const EdgeInsets.only(left: 15),
              child: Row(
                children: [
                  Expanded(child: catSelector),
                  IconButton(
                    icon: const Icon(Icons.refresh),
                    onPressed: () => refreshKey.currentState?.show()
                  ),
                  if(widget.onTap != null && !Frame.of(context).thin)
                  IconButton(
                    icon: const Icon(Icons.tonality),
                    onPressed: () => DestinyDialog().show(context)
                  ),
                ],
              )
            )
          ),
          Expanded(
            child: ClipRect(
              child: mainList
            ),
          ),
        ]
      ),
    );
    // return (widget.onTap == null) ?
    //   Scaffold(
    //     // appBar: AppBar(
    //     //   backgroundColor: Theme.of(context).primaryColor,
    //     //   bottom: PreferredSize(
    //     //     child: catSelect or,
    //     //     preferredSize: const Size.fromHeight(50)
    //     //   ),
    //     //   actions: [
    //     //     IconButton(
    //     //       icon: const Icon(Icons.sync),
    //     //       onPressed: () =>
    //     //         refreshKey.currentState?.show(),
    //     //     )
    //     //   ],
    //     // ),
    //     body: mainList
    //   )
    // :
  }
}

class EditableCard extends StatelessWidget{

  final void Function(Editable)? onTap;
  final void Function() onDismiss;

  const EditableCard({this.onTap, required this.onDismiss, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    Dismissible(
      key: Key(Editable.of(context).uid),
      confirmDismiss: (_) async {
        if(!SW.of(context).getPref(preferences.googleDrive)) return true;
        if(SW.of(context).syncing){
          ScaffoldMessenger.of(context).clearSnackBars();
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(AppLocalizations.of(context)!.driveSyncingNotice)
            )
          );
          return false;
        }else if (!SW.of(context).driver!.readySync()) {
          ScaffoldMessenger.of(context).clearSnackBars();
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(AppLocalizations.of(context)!.driveDisconnectNotice)
            )
          );
          return false;
        }
        return true;
      },
      onDismissed: (_) {
        onDismiss();
      },
      child: Card(
        margin: const EdgeInsets.all(5),
        child: InkResponse(
          containedInkWell: true,
          highlightShape: BoxShape.rectangle,
          onTap: (){
            var ed = Editable.of(context);
            if(onTap != null){
              onTap!(ed);
            }else{
              Navigator.of(context).pushNamed(
                "/edit/${ed.uid}",
                arguments: ed
              );
            }
          },
          child: Padding(
            padding: const EdgeInsets.all(5),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Hero(
                  transitionOnUserGestures: true,
                  tag: onTap != null ? UniqueKey() : Editable.of(context).uid,
                  child: Text(
                    Editable.of(context).name,
                    style: Theme.of(context).textTheme.headlineSmall
                  )
                ),
                if(onTap != null) Container(height: 10),
                if(onTap != null) Align(
                  alignment: Alignment.bottomRight,
                  child: Text(
                    (Editable.of(context) is Character) ?
                      AppLocalizations.of(context)!.characters
                    : (Editable.of(context) is Minion) ?
                      AppLocalizations.of(context)!.minions
                    :
                      AppLocalizations.of(context)!.vehicles,
                    style: Theme.of(context).textTheme.bodySmall
                  )
                ),
                if(kIsWeb) Container(height: 10),
                if(kIsWeb) Align(
                  alignment: Alignment.centerRight,
                  child: MiniIconButton(
                    icon: const Icon(Icons.delete),
                    onPressed: onDismiss
                  )
                )
              ],
            )
          )
        )
      )
    );
}