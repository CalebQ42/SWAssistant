import 'package:flutter/material.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/dialogs/creature/ItemEditDialog.dart';

class Inventory extends StatelessWidget{

  final bool editing;
  final Function refresh;
  final EditableContentState state;

  Inventory({this.editing, this.refresh, this.state});

  @override
  Widget build(BuildContext context) {
    var creature = Creature.of(context);
    var overEncumbered = false;
    var encumTot = 0;
    if(creature is Character && creature.inventory.length > 0){
      for(Item item in creature.inventory){
        encumTot += item.encum;
        if(encumTot > creature.encumCap){
          overEncumbered = true;
          break;
        }
      }
    }
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          if(creature is Character) Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text("Credits:"),
              SizedBox(
                width: 75,
                child: EditingText(
                  editing: editing,
                  initialText: creature.credits.toString(),
                  collapsed: true,
                  fieldAlign: TextAlign.center,
                  fieldInsets: EdgeInsets.all(3),
                  state: state,
                  controller: (){
                    var controller = new TextEditingController(text: creature.credits.toString());
                    controller.addListener(() {
                      if(controller.text =="")
                        creature.credits = 0;
                      else
                        creature.credits = int.parse(controller.text);
                    });
                    return controller;
                  }(),
                  textType: TextInputType.number,
                  defaultSave: true,
                )
              )
            ],
          ),
          if(creature is Character) Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text("Encumbrance Capacity:"),
              SizedBox(
                width: 50,
                child: EditingText(
                  editing: editing,
                  initialText: creature.encumCap.toString(),
                  collapsed: true,
                  fieldAlign: TextAlign.center,
                  fieldInsets: EdgeInsets.all(3),
                  state: state,
                  controller: (){
                    var controller = new TextEditingController(text: creature.encumCap.toString());
                    controller.addListener(() {
                      if(controller.text =="")
                        creature.encumCap = 0;
                      else
                        creature.encumCap = int.parse(controller.text);
                    });
                    return controller;
                  }(),
                  textType: TextInputType.number,
                  defaultSave: true,
                )
              )
            ],
          ),
          if(overEncumbered) Text(
            "You are overencumbered! Add a setback die to all Agility and Brawn checks.",
            style: TextStyle(color: Theme.of(context).errorColor),
            textAlign: TextAlign.center,
          )
        ]..addAll(List.generate(
          creature.inventory.length,
          (index) => InkResponse(
            containedInkWell: true,
            highlightShape: BoxShape.rectangle,
            onTap: () =>
              showModalBottomSheet(
                context: context,
                builder: (context) =>
                  Padding(
                    padding: EdgeInsets.only(bottom: 20, right: 10, left: 10),
                    child: Wrap(
                      alignment: WrapAlignment.center,
                      children: [
                        Container(height: 15),
                        Center(
                          child: Text(
                            creature.inventory[index].name,
                            style: Theme.of(context).textTheme.headline5,
                            textAlign: TextAlign.center,
                          )
                        ),
                        Container(height: 5,),
                        Center(
                          child: Text(
                            "Count: " + creature.inventory[index].count.toString(),
                            style: Theme.of(context).textTheme.bodyText1,
                          ),
                        ),
                        if(creature is Character) Container(height: 5,),
                        if(creature is Character) Center(
                          child: Text(
                            "Encumbrance: " + creature.inventory[index].encum.toString(),
                            style: Theme.of(context).textTheme.bodyText1,
                          ),
                        ),
                        Container(height: 10),
                        Text(creature.inventory[index].desc)
                      ],
                    )
                  )
              ),
            child: Row(
              children: [
                Expanded(
                  child: Text(creature.inventory[index].name),
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
                          var temp = Item.from(creature.inventory[index]);
                          creature.inventory.removeAt(index);
                          refresh();
                          (creature as Editable).save(context: context);
                          Scaffold.of(context).showSnackBar(
                            SnackBar(
                              content: Text("Item Deleted"),
                              action: SnackBarAction(
                                label: "Undo",
                                onPressed: (){
                                  creature.inventory.insert(index, temp);
                                  refresh();
                                  (creature as Editable).save(context: context);
                                }
                              ),
                            )
                          );
                        },
                      ),
                      IconButton(
                        icon: Icon(Icons.edit),
                        iconSize: 24.0,
                        constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                        onPressed: () =>
                          ItemEditDialog(
                            item: creature.inventory[index],
                            creature: creature,
                            onClose: (item){
                              creature.inventory[index] = item;
                              refresh();
                              (creature as Editable).save(context: context);
                            },
                          ).show(context)
                      )
                    ],
                  ) : Container(height: 40),
                  duration: Duration(milliseconds: 250),
                  transitionBuilder: (child, anim){
                    var offset = Offset(1,0);
                    if((!editing && child is ButtonBar) || (editing && child is Container))
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
              ],
            )
          )
        ))..add(
          AnimatedSwitcher(
            duration: Duration(milliseconds: 300),
            child: editing ? Center(
              child: IconButton(
                icon: Icon(Icons.add),
                onPressed: () =>
                  ItemEditDialog(
                    creature: creature,
                    onClose: (item){
                      creature.inventory.add(item);
                      refresh();
                      (creature as Editable).save(context: context);
                    },
                  ).show(context)
              )
            ) : Container(),
            transitionBuilder: (wid,anim){
              return SizeTransition(
                sizeFactor: anim,
                child: wid,
                axisAlignment: -1.0,
              );
            },
          )
        ),
      ),
    );
  }
}