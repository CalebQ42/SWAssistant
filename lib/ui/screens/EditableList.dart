import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/common.dart';
import 'package:swassistant/ui/screens/EditingEditable.dart';

class EditableList extends StatefulWidget{

  static final int character = 0;
  static final int minion = 1;
  static final int vehicle = 2;

  final int type;
  final Key? key;
  final void Function(Editable)? onTap;

  EditableList(this.type, {this.key, this.onTap});

  @override
  State<StatefulWidget> createState() =>
    EditableListState(type, key: key, onTap: onTap);
}

class EditableListState extends State{

  //This determines which list is shown
  //all = -1
  //character = 0
  //minion = 1
  //vehicle = 2
  //TODO
  int type;
  final Key? key;
  final void Function(Editable)? onTap;
  List<Editable> list = [];
  String? cat;
  String search = "";

  GlobalKey<AnimatedListState> listKey = GlobalKey();

  EditableListState(this.type, {this.key, this.onTap});

  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
    List<DropdownMenuItem<String>> categories;
    switch(type){
      case -1:
        if(cat == null)
          list = app.characters().cast<Editable>()..addAll(app.minions())..addAll(app.vehicles());
        else if(cat == "char")
          list = app.characters();
        else if(cat == "min")
          list = app.minions();
        else
          list = app.vehicles();
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
    var catSelector = DropdownButton<String>(
      items: categories,
      onChanged: (category) => cat = category,
    );
    var mainList = AnimatedList(
      key: listKey,
      initialItemCount: list.length,
      itemBuilder: (context, i, anim) =>
        SlideTransition(
          position: Tween<Offset>(begin: Offset(1.0, 0), end: Offset.zero).animate(anim),
          child: InheritedEditable(
            child: EditableCard(
              onTap: onTap,
              refreshList: () => setState((){}),
              onDismiss: (){
                var tmp = list[i];
                list.removeAt(i);
                list[i].delete(app);
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
    return (onTap == null) ?
      Scaffold(
        appBar: SWAppBar(
          context,
          backgroundColor: Theme.of(context).primaryColor,
          bottom: PreferredSize(
            child: catSelector,
            preferredSize: Size.fromHeight(50)
          )
        ),
        drawer: SWDrawer(),
        floatingActionButton: FloatingActionButton(
          child: Icon(Icons.add),
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
            list.add(newEd);
            listKey.currentState?.insertItem(list.length-1);
          },
        ),
      )
    :
      Column(
        children: [
          catSelector,
          mainList
        ]
      );
  }
}

class EditableCard extends StatelessWidget{

  final void Function(Editable)? onTap;
  final void Function() refreshList;
  final void Function() onDismiss;

  EditableCard({this.onTap, required this.refreshList, required this.onDismiss});

  @override
  Widget build(BuildContext context) =>
    Dismissible(
      key: Key(Editable.of(context).uid),
      onDismissed: (_) => onDismiss(),
      child: Card(
        child: InkResponse(
          containedInkWell: true,
          highlightShape: BoxShape.rectangle,
          onTap: (){
            var ed = Editable.of(context);
            if(onTap != null)
              onTap!(ed);
            else
              Navigator.of(context).push(
                MaterialPageRoute(
                  builder: (c) =>
                    EditingEditable(
                      ed,
                      refreshList
                    )
                )
              );
          },
          child: Padding(
            padding: EdgeInsets.all(5),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Text(
                  Editable.of(context).name,
                  style: Theme.of(context).textTheme.headlineMedium
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