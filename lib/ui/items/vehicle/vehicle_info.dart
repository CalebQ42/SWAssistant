import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class VehicleInfo extends StatelessWidget{

  final bool editing;

  const VehicleInfo({required this.editing, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var vehicle = Vehicle.of(context);
    if (vehicle == null) throw "VehicleInfo card called on non Vehicle";
    var silhouette = Column(
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
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.silhouette,
        )
      ],
    );
    var speed = Column( 
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
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.speed,
        )
      ],
    );
    var armor = Column(
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
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.armor,
        )
      ],
    );
    var handling = Column(
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
          textType: const TextInputType.numberWithOptions(signed: true),
          title: AppLocalizations.of(context)!.handling,
        )
      ],
    );
    //TODO: Possibly move to dedicated modifications card
    var hp = Column(
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
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.hardPoints,
        )
      ],
    );
    var capPas = Column(
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
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.passengers,
        )
      ],
    );
    var category = Column(
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
            });
            return controller;
          }(),
          title: AppLocalizations.of(context)!.category
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