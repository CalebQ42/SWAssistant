import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/ui/EditableCommon.dart';

class VehicleInfo extends StatelessWidget{

  final bool editing;
  final EditableContentState state;
  final Function() updateList;

  VehicleInfo({required this.editing, required this.state, required this.updateList});

  @override
  Widget build(BuildContext context) {
    var vehicle = Vehicle.of(context);
    if (vehicle == null)
      throw "VehicleInfo card called on non Vehicle";
    
    var silhouette = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: vehicle.silhouette.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text: vehicle.silhouette.toString());
            controller.addListener((){
              var parse = int.tryParse(controller.text);
              vehicle.silhouette = parse ?? 0;
            });
            return controller;
          }(),
          title: "Silhouette",
        )
      ],
    );
    var speed = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: vehicle.speed.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text: vehicle.speed.toString());
            controller.addListener((){
              var parse = int.tryParse(controller.text);
              vehicle.speed = parse ?? 0;
            });
            return controller;
          }(),
          title: "Speed",
        )
      ],
    );
    var armor = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: vehicle.armor.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text: vehicle.armor.toString());
            controller.addListener((){
              var parse = int.tryParse(controller.text);
              vehicle.armor = parse ?? 0;
            });
            return controller;
          }(),
          title: "Armor",
        )
      ],
    );
    var handling = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: vehicle.handling.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text: vehicle.handling.toString());
            controller.addListener((){
              var parse = int.tryParse(controller.text);
              vehicle.handling = parse ?? 0;
            });
            return controller;
          }(),
          title: "Handling",
        )
      ],
    );
    //TODO: Possibly move to dedicated modifications card
    var hp = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: vehicle.hp.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text: vehicle.hp.toString());
            controller.addListener((){
              var parse = int.tryParse(controller.text);
              vehicle.hp = parse ?? 0;
            });
            return controller;
          }(),
          title: "Hard Points",
        )
      ],
    );
    var capPas = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: vehicle.passengerCapacity.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text: vehicle.passengerCapacity.toString());
            controller.addListener((){
              var parse = int.tryParse(controller.text);
              vehicle.passengerCapacity = parse ?? 0;
            });
            return controller;
          }(),
          title: "Passengers",
        )
      ],
    );
    var category = new Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: editing, 
          initialText: vehicle.category,
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: (){
            var controller = TextEditingController(text:vehicle.category);
            controller.addListener((){
              vehicle.category = controller.text;
              SW.of(context).updateCharacterCategories();
              updateList();
            });
            return controller;
          }(),
          title: "Category"
        )
      ],
    );
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        Row(
          children: <Widget>[
            Expanded(child: silhouette),
            Expanded(child: speed),
          ],
        ),
        Row(
          children: <Widget>[
            Expanded(child: armor),
            Expanded(child: handling),
          ],
        ),
        Row(
          children: <Widget>[
            Expanded(child: hp),
            Expanded(child: capPas),
          ],
        ),
        category,
      ],
    );
  }
}