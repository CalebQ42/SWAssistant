import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/common.dart';
import 'package:swassistant/ui/screens/editing_editable.dart';

class EditableList extends StatefulWidget{

  static const int character = 0;
  static const int minion = 1;
  static const int vehicle = 2;

  final int type;
  final void Function(Editable)? onTap;

  const EditableList(this.type, {Key? key, this.onTap}) : super(key: key);

  @override
  State<StatefulWidget> createState() => EditableListState();
}

class EditableListState extends State<EditableList>{

  late int type;
  List<Editable> list = [];
  String? cat;
  String search = "";

  late GlobalKey<AnimatedListState> listKey;

  @override
  void initState() {
    super.initState();
    type = widget.type;
    listKey = GlobalKey();
  }

  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
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
      print("Rebuild!");
    }
    print("potato");
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
    var mainList = AnimatedList(
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
    );
    return (widget.onTap == null) ?
      Scaffold(
        appBar: SWAppBar(
          context,
          backgroundColor: Theme.of(context).primaryColor,
          bottom: PreferredSize(
            child: catSelector,
            preferredSize: const Size.fromHeight(50)
          )
        ),
        drawer: const SWDrawer(),
        floatingActionButton: FloatingActionButton(
          child: const Icon(Icons.add),
          onPressed: (){
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
                Text(
                  Editable.of(context).name,
                  style: Theme.of(context).textTheme.headlineSmall
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