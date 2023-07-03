import 'package:darkstorm_common/bottom.dart';
import 'package:darkstorm_common/frame_content.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/dialogs/destiny.dart';
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

  final GlobalKey<AnimatedListState> listKey =  GlobalKey();
  final GlobalKey<RefreshIndicatorState> refreshKey = GlobalKey();

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
        child: Text(app.locale.all),
      ),
      DropdownMenuItem<String>(
        value: "",
        child: Text(app.locale.uncategorized)
      ),
      ...List.generate(app.cats.length, (index) =>
        DropdownMenuItem<String>(
          value: app.cats[index],
          child: Text(app.cats[index])
        )
      )
    ];
    if(list.length != oldLen){
      listKey.currentState?.setState(() {});
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
    var localization = app.locale;
    Widget mainList = AnimatedList(
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
                tmp.trash(app);
                listKey.currentState?.removeItem(i, (context, animation) => Container());
                ScaffoldMessenger.of(context).clearSnackBars();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(
                      (tmp is Character) ?
                        app.locale.characterTrashed
                      : (tmp is Minion) ?
                        app.locale.minionTrashed
                      :
                        app.locale.vehicleTrashed
                    ),
                    action: SnackBarAction(
                      label: app.locale.undo,
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
    );
    if((app.isMobile || kIsWeb) && app.prefs.googleDrive){
      mainList = RefreshIndicator(
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
        child: mainList
      );
    }
    return FrameContent(
      fab: widget.onTap == null ? FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: (){
          if(app.prefs.googleDrive) {
            if(app.syncing){
              ScaffoldMessenger.of(context).clearSnackBars();
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(app.locale.driveSyncingNotice)
                )
              );
              return;
            }else if (app.driver == null || !app.driver!.readySync()) {
              ScaffoldMessenger.of(context).clearSnackBars();
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(app.locale.driveDisconnectNotice)
                )
              );
              return;
            }
          }
          Editable newEd;
          switch(widget.edType){
            case Character:
              newEd = Character(name: app.locale.newCharacter, saveOnCreation: true, app: app);
              break;
            case Minion:
              newEd = Minion(name: app.locale.newMinion, saveOnCreation: true, app: app);
              break;
            default:
              newEd = Vehicle(name: app.locale.newVehicle, saveOnCreation: true, app: app);
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
                  if((app.isMobile || kIsWeb) && app.prefs.googleDrive) IconButton(
                    icon: const Icon(Icons.refresh),
                    onPressed: () => refreshKey.currentState?.show()
                  ),
                  if(app.prefs.stupid) IconButton(
                    icon: const Icon(Icons.download),
                    onPressed: () async {
                      if(app.stupid?.isAvailable ?? false){
                        download(listKey);
                      }else{
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(app.locale.noConnectionStupid)
                          )
                        );
                      }
                    }
                  ),
                  if(widget.onTap != null) IconButton(
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

  void download(GlobalKey<AnimatedListState> listKey){
    Bottom? bot;
    var app = SW.of(context);
    var scaf = ScaffoldMessenger.of(context);
    var cont = TextEditingController()
        ..addListener(() => bot?.updateButtons());
    bot = Bottom(
      children: (c) => [
        Center(
          child: Text(
            app.locale.shareCode,
            style: Theme.of(context).textTheme.titleLarge
          )
        ),
        Container(height: 5,),
        TextField(
          controller: cont,
        )
      ],
      buttons: (c) => [
        TextButton(
          onPressed: cont.text.trim().isNotEmpty ? () async {
            var ed = await app.stupid?.downloadProfile(cont.text);
            if(ed == null){
              app.nav.pop();
              scaf.showSnackBar(
                SnackBar(
                  content: Text(app.locale.downloadFailed)
                )
              );
            }else{
              app.add(ed);
              app.nav.pop();
              await ed.save(app: app);
              if(widget.edType == null || widget.edType == ed.runtimeType){
                listKey.currentState?.insertItem(app.getList(type: widget.edType).length-1);
              }
            }
          } : null,
          child: Text(app.locale.download),
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: () =>
            app.nav.pop()
        )
      ],
    )..show(context);
  }
}

class EditableCard extends StatelessWidget{

  final void Function(Editable)? onTap;
  final void Function() onDismiss;

  const EditableCard({this.onTap, required this.onDismiss, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context){
    var app = SW.of(context);
    return Dismissible(
      key: Key(Editable.of(context).uid),
      confirmDismiss: (_) async {
        if(!app.prefs.googleDrive) return true;
        if(app.syncing){
          ScaffoldMessenger.of(context).clearSnackBars();
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(app.locale.driveSyncingNotice)
            )
          );
          return false;
        }else if (!app.driver!.readySync()) {
          ScaffoldMessenger.of(context).clearSnackBars();
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(app.locale.driveDisconnectNotice)
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
                      app.locale.characters
                    : (Editable.of(context) is Minion) ?
                      app.locale.minions
                    :
                      app.locale.vehicles,
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
}