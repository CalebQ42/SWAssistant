import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class VehicleInfo extends StatefulWidget{

  const VehicleInfo({Key? key}) : super(key: key);

  @override
  State<VehicleInfo> createState() => VehicleInfoState();
}

class VehicleInfoState extends State<VehicleInfo> with StatefulCard{

  var edit = false;
  @override
  bool get defaultEdit => Vehicle.of(context)!.silhouette == 0 && Vehicle.of(context)!.speed == 0 && Vehicle.of(context)!.armor == 0 &&
      Vehicle.of(context)!.handling == 0 && Vehicle.of(context)!.hp == 0 && Vehicle.of(context)!.passengerCapacity == 0 && Vehicle.of(context)!.category == "";
  @override
  set editing(bool b) => setState(() => edit = b);

  TextEditingController? silhouetteController;
  TextEditingController? speedController;
  TextEditingController? armorController;
  TextEditingController? handlingController;
  TextEditingController? hpController;
  TextEditingController? passengerCapacityController;
  TextEditingController? categoryController;

  @override
  Widget build(BuildContext context) {
    var vehicle = Vehicle.of(context);
    if (vehicle == null) throw "VehicleInfo card called on non Vehicle";
    if (silhouetteController == null) {
      silhouetteController = TextEditingController(text: vehicle.silhouette.toString());
      silhouetteController!.addListener(() => vehicle.silhouette = int.tryParse(silhouetteController!.text) ?? 0);
      speedController = TextEditingController(text: vehicle.speed.toString());
      speedController!.addListener(() => vehicle.speed = int.tryParse(speedController!.text) ?? 0);
      armorController = TextEditingController(text: vehicle.armor.toString());
      armorController!.addListener(() => vehicle.armor = int.tryParse(armorController!.text) ?? 0);
      handlingController = TextEditingController(text: vehicle.handling.toString());
      handlingController!.addListener(() => vehicle.handling = int.tryParse(handlingController!.text) ?? 0);
      hpController = TextEditingController(text: vehicle.hp.toString());
      hpController!.addListener(() => vehicle.hp = int.tryParse(hpController!.text) ?? 0);
      passengerCapacityController = TextEditingController(text: vehicle.passengerCapacity.toString());
      passengerCapacityController!.addListener(() => vehicle.passengerCapacity = int.tryParse(passengerCapacityController!.text) ?? 0);
      categoryController = TextEditingController(text: vehicle.category);
      categoryController!.addListener(() => SW.of(context).updateCategory(vehicle, categoryController!.text));
    }
    var silhouette = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: edit, 
          initialText: vehicle.silhouette.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: silhouetteController,
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.silhouette,
        )
      ],
    );
    var speed = Column( 
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: edit, 
          initialText: vehicle.speed.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: speedController,
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.speed,
        )
      ],
    );
    var armor = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: edit, 
          initialText: vehicle.armor.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: armorController,
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.armor,
        )
      ],
    );
    var handling = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: edit, 
          initialText: vehicle.handling.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: handlingController,
          textType: const TextInputType.numberWithOptions(signed: true),
          title: AppLocalizations.of(context)!.handling,
        )
      ],
    );
    //TODO: Move to dedicated modifications card
    var hp = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: edit, 
          initialText: vehicle.hp.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: hpController,
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.hardPoints,
        )
      ],
    );
    var capPas = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: edit, 
          initialText: vehicle.passengerCapacity.toString(),
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: passengerCapacityController,
          textType: TextInputType.number,
          title: AppLocalizations.of(context)!.passengers,
        )
      ],
    );
    var category = Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: <Widget>[
        EditingText(
          editing: edit, 
          initialText: vehicle.category,
          style: Theme.of(context).textTheme.subtitle1,
          defaultSave: true,
          fieldAlign: TextAlign.center,
          textCapitalization: TextCapitalization.words,
          controller: categoryController,
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