import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/sw_drawer.dart';
import 'package:swassistant/ui/screens/editing_editable.dart';

class EditableList extends StatefulWidget{

  static const int character = 0;
  static const int minion = 1;
  static const int vehicle = 2;

  final int type;
  final void Function(Editable)? onTap;

  final String? uidToLoad;

  const EditableList(this.type, {Key? key, this.onTap, this.uidToLoad}) : super(key: key);

  @override
  State<StatefulWidget> createState() => EditableListState();
}

class EditableListState extends State<EditableList>{

  late int type;
  List<Editable> list = [];
  String? cat;
  String search = "";

  bool first = true;

  late GlobalKey<AnimatedListState> listKey =  GlobalKey();
  late GlobalKey<RefreshIndicatorState> refreshKey = GlobalKey();

  @override
  void initState() {
    super.initState();
    type = widget.type;
  }

  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
    if(!kIsWeb && !app.getPreference(preferences.googleDrive, false) && app.getPreference(preferences.driveFirstLoad, true)){
      Future(() async {
        while(!mounted){
          await Future.delayed(const Duration(milliseconds: 100));
        }
        app.prefs.setBool(preferences.driveFirstLoad, false);
        showDialog(
          context: context,
          builder: (c) => AlertDialog(
            content: Text(
              AppLocalizations.of(context)!.driveFirstLaunch
            ),
            actions: [
              TextButton(
                child: Text(MaterialLocalizations.of(context).continueButtonLabel),
                onPressed: () => Navigator.popAndPushNamed(context, "/settings"),
              ),
              TextButton(
                child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
                onPressed: () => Navigator.pop(context)
              )
            ],
          )
        );
      });
    }
    if(widget.uidToLoad != null){
      Future(() async {
        while(!mounted) {
          await Future.delayed(const Duration(milliseconds: 50));
        }
        showDialog(
          barrierDismissible: false,
          context: context,
          builder: (context) =>
            AlertDialog(
              content: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const CircularProgressIndicator(),
                  Container(height: 10),
                  Text(
                    AppLocalizations.of(context)!.loadingDialog,
                    textAlign: TextAlign.center,
                  )
                ]
              )
            )
        );
        var syncSuccess = false;
        if(app.getPreference(preferences.driveFirstLoad, true)){
          syncSuccess = await app.initialSync();
        }else{
          syncSuccess = await app.syncCloud();
        }
        if(syncSuccess){
          app.prefs.setBool(preferences.driveFirstLoad, false);
          Navigator.pop(context);
          var ed = app.findEditable(widget.uidToLoad!);
          if(ed != null) Navigator.pushNamed(context, "/edit/" + widget.uidToLoad!, arguments: ed);
        }else{
          Navigator.pop(context);
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(AppLocalizations.of(context)!.syncFail)
            )
          );
        }
        await app.syncCloud(context).then((value) {
        });
      });
    }
    var oldLen = list.length;
    List<DropdownMenuItem<String>> categories;
    switch(type){
      case -1:
        if(cat == null){
          list = [
            ...app.characters(),
            ...app.minions(),
            ...app.vehicles()
          ];
        }else if(cat == "char"){
          list = app.characters();
        }else if(cat == "min"){
          list = app.minions();
        }else{
          list = app.vehicles();
        }
        categories = [
          DropdownMenuItem<String>(
            child: Text(AppLocalizations.of(context)!.all),
            value: null,
          ),
          DropdownMenuItem<String>(
            child: Text(AppLocalizations.of(context)!.characters),
            value: "char",
          ),
          DropdownMenuItem<String>(
            child: Text(AppLocalizations.of(context)!.minions),
            value: "min",
          ),
          DropdownMenuItem<String>(
            child: Text(AppLocalizations.of(context)!.vehicles),
            value: "veh",
          )
        ];
        break;
      case 0:
        list = app.characters(search: search, category: cat);
        categories = List.generate(
          app.charCats.length + 2,
          (index) =>
            (index == 0) ?
              DropdownMenuItem<String>(
                child: Text(AppLocalizations.of(context)!.all),
                value: null,
              )
            : (index == 1) ?
              DropdownMenuItem<String>(
                child: Text(AppLocalizations.of(context)!.uncategorized),
                value: ""
              )
            :
              DropdownMenuItem<String>(
                child: Text(app.charCats[index-2]),
                value: app.charCats[index-2]
              )
        );
        break;
      case 1:
        list = app.minions(search: search, category: cat);
        categories = List.generate(
          app.minCats.length + 2,
          (index) =>
            (index == 0) ?
              DropdownMenuItem<String>(
                child: Text(AppLocalizations.of(context)!.all),
                value: null,
              )
            : (index == 1) ?
              DropdownMenuItem<String>(
                child: Text(AppLocalizations.of(context)!.uncategorized),
                value: ""
              )
            :
              DropdownMenuItem<String>(
                child: Text(app.minCats[index-2]),
                value: app.minCats[index-2]
              )
        );
        break;
      default:
        list = app.vehicles(search: search, category: cat);
        categories = List.generate(
          app.vehCats.length + 2,
          (index) =>
            (index == 0) ?
              DropdownMenuItem<String>(
                child: Text(AppLocalizations.of(context)!.all),
                value: null,
              )
            : (index == 1) ?
              DropdownMenuItem<String>(
                child: Text(AppLocalizations.of(context)!.uncategorized),
                value: ""
              )
            :
              DropdownMenuItem<String>(
                child: Text(app.vehCats[index-2]),
                value: app.vehCats[index-2]
              )
        );
    }
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
    var mainList = RefreshIndicator(
      key: refreshKey,
      onRefresh: () => Future(() async {
        if(app.syncing) return;
        var messager = ScaffoldMessenger.of(context);
        if (app.getPreference(preferences.googleDrive, false)){
          if(app.getPreference(preferences.driveFirstLoad, true)){
            if(await app.initialSync()){
              app.prefs.setBool(preferences.driveFirstLoad, false);
              messager.clearSnackBars();
            }else{
              messager.clearSnackBars();
              messager.showSnackBar(
                SnackBar(
                  content: Text(AppLocalizations.of(context)!.syncFail)
                )
              );
            }
          }else{
            if(!await app.syncCloud()){
              messager.clearSnackBars();
              messager.showSnackBar(
                SnackBar(
                  content: Text(AppLocalizations.of(context)!.syncFail)
                )
              );
            }
          }
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
              child: EditableCard(
                onTap: widget.onTap,
                refreshList: () => setState((){}),
                onDismiss: (){
                  var tmp = list[i];
                  list[i].delete(app);
                  list.removeAt(i);
                  app.remove(tmp, context);
                  listKey.currentState?.removeItem(
                    i,
                    (context, animation) => Container()
                  );
                  ScaffoldMessenger.of(context).clearSnackBars();
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                      content: Text(
                        (tmp is Character) ?
                          AppLocalizations.of(context)!.deletedCharacter
                        : (tmp is Minion) ?
                          AppLocalizations.of(context)!.deletedMinion
                        :
                          AppLocalizations.of(context)!.deletedVehicle
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
              ),
              editable: list[i]
            )
          ),
      )
    );
    return (widget.onTap == null) ?
      Scaffold(
        appBar: AppBar(
          backgroundColor: Theme.of(context).primaryColor,
          bottom: PreferredSize(
            child: catSelector,
            preferredSize: const Size.fromHeight(50)
          ),
          actions: [
            IconButton(
              icon: const Icon(Icons.sync),
              onPressed: () =>
                refreshKey.currentState?.show(),
            )
          ],
        ),
        drawer: const SWDrawer(),
        floatingActionButton: FloatingActionButton(
          child: const Icon(Icons.add),
          onPressed: (){
            if(SW.of(context).getPreference(preferences.googleDrive, false)) {
              if(SW.of(context).syncing){
                ScaffoldMessenger.of(context).clearSnackBars();
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text(AppLocalizations.of(context)!.driveSyncingNotice)
                  )
                );
                return;
              }else if (!SW.of(context).driver!.readySync()) {
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
            switch(type){
              case 0:
                newEd = Character(name: AppLocalizations.of(context)!.newCharacter, saveOnCreation: true, app: app);
                break;
              case 1:
                newEd = Minion(name: AppLocalizations.of(context)!.newMinion, saveOnCreation: true, app: app);
                break;
              default:
                newEd = Vehicle(name: AppLocalizations.of(context)!.newVehicle, saveOnCreation: true, app: app);
            }
            app.add(newEd);
            Navigator.pushNamed(
              context,
              "/edit/" + newEd.uid,
              arguments: newEd
            );
          },
        ),
        body: mainList
      )
    :
      Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Container(
            color: Theme.of(context).primaryColor,
            child: catSelector,
          ),
          Expanded(child:
            ClipRect(
              child: mainList
            )
          )
        ]
      );
  }
}

class EditableCard extends StatelessWidget{

  final void Function(Editable)? onTap;
  final void Function() refreshList;
  final void Function() onDismiss;

  const EditableCard({this.onTap, required this.refreshList, required this.onDismiss, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    Dismissible(
      key: Key(Editable.of(context).uid),
      confirmDismiss: (_) async {
        if(!SW.of(context).getPreference(preferences.googleDrive, false)) return true;
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
                "/edit/" + ed.uid,
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
                    style: Theme.of(context).textTheme.caption
                  )
                )
              ],
            )
          )
        )
      )
    );
}