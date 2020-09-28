import 'package:flutter/material.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class Inventory extends StatelessWidget{

  final bool editing;
  final Function refresh;
  final EditableContentState state;

  Inventory({this.editing, this.refresh, this.state});

  @override
  Widget build(BuildContext context) {
    var creature = Creature.of(context);
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
                          //TODO: delete
                        },
                      ),
                      IconButton(
                        icon: Icon(Icons.edit),
                        iconSize: 24.0,
                        constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                        onPressed: () {}
                          //TODO: edit
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
                onPressed: () {}
                  //TODO: add
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