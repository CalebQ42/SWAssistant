import 'package:flutter/material.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class VehicleDefense extends StatefulWidget{

  const VehicleDefense({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => VehicleDefenseState();

}

class VehicleDefenseState extends State<VehicleDefense> with StatefulCard{

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => false;

  TextEditingController? totalDefenseCont;
  TextEditingController? foreDefenseCont;
  TextEditingController? aftDefenseCont;
  TextEditingController? portDefenseCont;
  TextEditingController? starboardDefenseCont;

  @override
  Widget build(BuildContext context) {
    var vehicle = Vehicle.of(context)!;
    int angleTotal = 0;
    for (int i in vehicle.defense){
      angleTotal += i;
    }
    totalDefenseCont ??= TextEditingController(text: vehicle.totalDefense.toString())
      ..addListener(() =>
        setState(() => vehicle.totalDefense = int.tryParse(totalDefenseCont!.text) ?? 0)
      );
    var app = SW.of(context);
    var total = EditingText(
      editing: edit,
      initialText: vehicle.totalDefense.toString(),
      controller: totalDefenseCont,
      textType: TextInputType.number,
      defaultSave: true,
      title: app.locale.totalDefense
    );
    foreDefenseCont ??= TextEditingController(text: vehicle.defense[0].toString())
      ..addListener(() =>
        setState(() => vehicle.defense[0] = int.tryParse(foreDefenseCont!.text) ?? 0)
      );
    var fore = EditingText(
      editing: edit,
      initialText: vehicle.defense[0].toString(),
      controller: foreDefenseCont,
      textType: TextInputType.number,
      defaultSave: true,
      title: app.locale.fore
    );
    portDefenseCont ??= TextEditingController(text: vehicle.defense[1].toString())
      ..addListener(() =>
        setState(() => vehicle.defense[1] = int.tryParse(portDefenseCont!.text) ?? 0)
      );
    var port = EditingText(
      editing: edit,
      initialText: vehicle.defense[1].toString(),
      controller: portDefenseCont,
      textType: TextInputType.number,
      defaultSave: true,
      title: app.locale.port
    );
    starboardDefenseCont ??= TextEditingController(text: vehicle.defense[2].toString())
      ..addListener(() =>
        setState(() => vehicle.defense[2] = int.tryParse(starboardDefenseCont!.text) ?? 0)
      );
    var starboard = EditingText(
      editing: edit,
      initialText: vehicle.defense[2].toString(),
      controller: starboardDefenseCont,
      textType: TextInputType.number,
      defaultSave: true,
      title: app.locale.starboard
    );
    aftDefenseCont ??= TextEditingController(text: vehicle.defense[3].toString())
      ..addListener(() =>
        setState(() => vehicle.defense[3] = int.tryParse(aftDefenseCont!.text) ?? 0)
      );
    var aft = EditingText(
      editing: edit,
      initialText: vehicle.defense[3].toString(),
      controller: aftDefenseCont,
      textType: TextInputType.number,
      defaultSave: true,
      title: app.locale.aft
    );
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        total,
        const Divider(
          indent: 10,
          endIndent: 10,
        ),
        fore,
        Row(
          children: [
            Expanded(child: port),
            Expanded(child: starboard)
          ],
        ),
        aft,
        if (angleTotal != vehicle.totalDefense) Container(height: 5,),
        if (angleTotal != vehicle.totalDefense) Text(
          app.locale.anglingWarning,
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyLarge?.copyWith(color: Colors.red),
        )
      ],
    );
  }
}