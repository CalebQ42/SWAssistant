import 'package:flutter/material.dart';
import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/dialogs/editable/ItemEditDialog.dart';

class Inventory extends StatelessWidget{

  final bool editing;
  final Function refresh;
  final EditableContentState state;

  Inventory({this.editing, this.refresh, this.state});

  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    var overEncumbered = false;
    var encumTot = 0;
    if(editable is !Minion && editable.inventory.length > 0){
      if(editable is Character)
        for(Item item in editable.inventory){
          encumTot += item.encum;
          if(encumTot > editable.encumCap){
            overEncumbered = true;
            break;
          }
        }
      else if(editable is Vehicle)
        for(Item item in editable.inventory){
          encumTot += item.encum;
          if(encumTot > editable.encumCapacity){
            overEncumbered = true;
            break;
          }
        }
    }
    if(editable is !Minion && editable.inventory.length > 0){
      if(editable is Character)
        for(Weapon weap in editable.weapons){
          encumTot += weap.encumbrance;
          if(encumTot > editable.encumCap){
            overEncumbered = true;
            break;
          }
        }
      else if(editable is Vehicle)
        for(Weapon weap in editable.weapons){
          encumTot += weap.encumbrance;
          if(encumTot > editable.encumCapacity){
            overEncumbered = true;
            break;
          }
        }
    }
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          if(editable is Character) Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Center(child: Text("Credits:")),
              SizedBox(
                width: 75,
                height: 25,
                child: EditingText(
                  editing: editing,
                  initialText: editable.credits.toString(),
                  collapsed: true,
                  fieldAlign: TextAlign.center,
                  fieldInsets: EdgeInsets.all(3),
                  state: state,
                  controller: (){
                    var controller = new TextEditingController(text: editable.credits.toString());
                    controller.addListener(() {
                      if(controller.text =="")
                        editable.credits = 0;
                      else
                        editable.credits = int.parse(controller.text);
                    });
                    return controller;
                  }(),
                  textType: TextInputType.number,
                  defaultSave: true,
                )
              )
            ],
          ),
          if(editable is Character) Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Center(child: Text("Encumbrance Capacity:")),
              SizedBox(
                width: 50,
                height: 25,
                child: EditingText(
                  editing: editing,
                  initialText: editable.encumCap.toString(),
                  collapsed: true,
                  fieldAlign: TextAlign.center,
                  fieldInsets: EdgeInsets.all(3),
                  state: state,
                  controller: (){
                    var controller = new TextEditingController(text: editable.encumCap.toString());
                    controller.addListener(() {
                      if(controller.text =="")
                        editable.encumCap = 0;
                      else
                        editable.encumCap = int.parse(controller.text);
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
            "You are over-encumbered! Add a setback die to all Agility and Brawn checks.",
            style: TextStyle(color: Theme.of(context).errorColor),
            textAlign: TextAlign.center,
          )
        ]..addAll(List.generate(
          editable.inventory.length,
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
                            editable.inventory[index].name,
                            style: Theme.of(context).textTheme.headline5,
                            textAlign: TextAlign.center,
                          )
                        ),
                        Container(height: 5,),
                        Center(
                          child: Text(
                            "Count: " + editable.inventory[index].count.toString(),
                            style: Theme.of(context).textTheme.bodyText1,
                          ),
                        ),
                        Container(height: 5),
                        Center(
                          child: Text(
                            "Encumbrance: " + editable.inventory[index].encum.toString(),
                            style: Theme.of(context).textTheme.bodyText1,
                          ),
                        ),
                        Container(height: 10),
                        if(editable.inventory[index].desc != "") Text(editable.inventory[index].desc)
                      ],
                    )
                  )
              ),
            child: Row(
              children: [
                Expanded(
                  child: Text(editable.inventory[index].name),
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
                          var temp = Item.from(editable.inventory[index]);
                          editable.inventory.removeAt(index);
                          refresh();
                          editable.save(context: context);
                          Scaffold.of(context).showSnackBar(
                            SnackBar(
                              content: Text("Item Deleted"),
                              action: SnackBarAction(
                                label: "Undo",
                                onPressed: (){
                                  editable.inventory.insert(index, temp);
                                  refresh();
                                  editable.save(context: context);
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
                            item: editable.inventory[index],
                            editable: editable,
                            onClose: (item){
                              editable.inventory[index] = item;
                              refresh();
                              editable.save(context: context);
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
                    editable: editable,
                    onClose: (item){
                      editable.inventory.add(item);
                      refresh();
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
        ),
      ),
    );
  }
}