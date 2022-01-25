import 'package:flutter/material.dart';
import 'package:swassistant/items/item.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:swassistant/ui/dialogs/editable/item_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';

class Inventory extends StatefulWidget {

  const Inventory({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => InventoryState();
}
class InventoryState extends State<Inventory> with StatefulCard{

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Editable.of(context).inventory.isEmpty;

  void update(void Function() updateFunc) => setState(updateFunc);

  TextEditingController? creditController;
  TextEditingController? encumController;

  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    var overEncumbered = false;
    var encumTot = 0;
    if(editable is Character && creditController == null){
      creditController = TextEditingController(text: editable.credits.toString());
      creditController?.addListener(() =>
        editable.credits = int.tryParse(creditController!.text) ?? 0
      );
    }
    if(editable is Character && encumController == null){
      encumController = TextEditingController(text: editable.encumCap.toString());
      encumController?.addListener(() {
        editable.encumCap = int.tryParse(encumController!.text) ?? 0;
        setState((){});
      });
    }
    if(editable is Vehicle && encumController == null){
      encumController = TextEditingController(text: editable.encumCap.toString());
      encumController?.addListener(() {
        editable.encumCap = int.tryParse(encumController!.text) ?? 0;
        setState((){});
      });
    }
    if(editable is !Minion && editable.inventory.isNotEmpty){
      if(editable is Character){
        for(Item item in editable.inventory){
          encumTot += item.encum;
          if(encumTot > editable.encumCap){
            overEncumbered = true;
            break;
          }
        }
      }else if(editable is Vehicle){
        for(Item item in editable.inventory){
          encumTot += item.encum;
          if(encumTot > editable.encumCap){
            overEncumbered = true;
            break;
          }
        }
      }
    }
    if(editable is !Minion && editable.inventory.isNotEmpty){
      if(editable is Character){
        for(Weapon weap in editable.weapons){
          encumTot += weap.encumbrance;
          if(encumTot > editable.encumCap){
            overEncumbered = true;
            break;
          }
        }
      }else if(editable is Vehicle){
        for(Weapon weap in editable.weapons){
          encumTot += weap.encumbrance;
          if(encumTot > editable.encumCap){
            overEncumbered = true;
            break;
          }
        }
      }
    }
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          if(editable is Character) Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Center(child: Text(AppLocalizations.of(context)!.credits)),
              SizedBox(
                width: 75,
                height: 25,
                child: EditingText(
                  editing: edit,
                  initialText: editable.credits.toString(),
                  collapsed: true,
                  fieldAlign: TextAlign.center,
                  fieldInsets: const EdgeInsets.all(3),
                  controller: creditController,
                  textType: TextInputType.number,
                  defaultSave: true,
                )
              )
            ],
          ),
          if(editable is Character) Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(AppLocalizations.of(context)!.encum + ":"),
              Container(width: 10),
              SizedBox(
                width: 50,
                height: 25,
                child: Center(child: Text(encumTot.toString()))
              ),
              const Text("/"),
              SizedBox(
                width: 50,
                height: 25,
                child: EditingText(
                  editing: edit,
                  initialText: editable.encumCap.toString(),
                  collapsed: true,
                  fieldInsets: const EdgeInsets.all(3),
                  controller: encumController,
                  textType: TextInputType.number,
                  defaultSave: true,
                )
              )
            ],
          ),
          if(editable is Vehicle) Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(AppLocalizations.of(context)!.encum + ":"),
              Container(width: 10),
              SizedBox(
                width: 50,
                height: 25,
                child: Center(child: Text(encumTot.toString()))
              ),
              const Text("/"),
              SizedBox(
                width: 50,
                height: 25,
                child: EditingText(
                  editing: edit,
                  initialText: editable.encumCap.toString(),
                  collapsed: true,
                  fieldInsets: const EdgeInsets.all(3),
                  controller: encumController,
                  textType: TextInputType.number,
                  defaultSave: true,
                )
              )
            ],
          ),
          if(overEncumbered) Text(
            AppLocalizations.of(context)!.overEncumNotice,
            style: TextStyle(color: Theme.of(context).errorColor),
            textAlign: TextAlign.center,
          ), ...List.generate(
          editable.inventory.length,
          (index) => InkResponse(
            containedInkWell: true,
            highlightShape: BoxShape.rectangle,
            onTap: () =>
              Bottom(
                child: (context) =>
                  Wrap(
                    alignment: WrapAlignment.center,
                    children: [
                      Container(height: 15),
                      Center(
                        child: Text(
                          editable.inventory[index].name,
                          style: Theme.of(context).textTheme.headline5,
                          textAlign: TextAlign.center,
                        )
                      ),
                      Container(height: 5,),
                      Center(
                        child: Text(
                          AppLocalizations.of(context)!.count + ": " + editable.inventory[index].count.toString(),
                          style: Theme.of(context).textTheme.bodyText1,
                        ),
                      ),
                      Container(height: 5),
                      Center(
                        child: Text(
                          AppLocalizations.of(context)!.encum + ": " + editable.inventory[index].encum.toString(),
                          style: Theme.of(context).textTheme.bodyText1,
                        ),
                      ),
                      Container(height: 10),
                      if(editable.inventory[index].desc != "") Text(editable.inventory[index].desc)
                    ],
                  )
              ).show(context),
            child: Row(
              children: [
                Expanded(
                  child: Text((editable.inventory[index].count > 1 ? editable.inventory[index].count.toString() + " " : "" )
                    + editable.inventory[index].name),
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
                          var temp = Item.from(editable.inventory[index]);
                          editable.inventory.removeAt(index);
                          setState((){});
                          editable.save(context: context);
                          ScaffoldMessenger.of(context).clearSnackBars();
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(
                              content: Text(AppLocalizations.of(context)!.deletedItem),
                              action: SnackBarAction(
                                label: AppLocalizations.of(context)!.undo,
                                onPressed: (){
                                  editable.inventory.insert(index, temp);
                                  setState((){});
                                  editable.save(context: context);
                                }
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
                          ItemEditDialog(
                            it: editable.inventory[index],
                            editable: editable,
                            onClose: (item){
                              editable.inventory[index] = item;
                              setState((){});
                              editable.save(context: context);
                            },
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
          )
        ), AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            child: edit ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  ItemEditDialog(
                    editable: editable,
                    onClose: (item){
                      editable.inventory.add(item);
                      setState((){});
                      editable.save(context: context);
                    },
                  ).show(context)
              )
            ) : Container(),
            transitionBuilder: (wid,anim) =>
              SizeTransition(
                sizeFactor: anim,
                child: wid,
                axisAlignment: -1.0,
              ),
          )
        ],
      ),
    );
  }
}