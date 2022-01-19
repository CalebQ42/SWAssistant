import 'package:flutter/material.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class VehicleDefense extends StatefulWidget implements StatefulCard {
  final EditableContentStatefulHolder holder = EditableContentStatefulHolder();

  VehicleDefense({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _VehicleDefenseState();

  @override
  EditableContentStatefulHolder getHolder() => holder;
}

class _VehicleDefenseState extends State<VehicleDefense> {
  TextEditingController? totalDefenseCont;
  TextEditingController? foreDefenseCont;
  TextEditingController? aftDefenseCont;
  TextEditingController? portDefenseCont;
  TextEditingController? starboardDefenseCont;

  @override
  void initState() {
    super.initState();
    widget.holder.reloadFunction = () => setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    var vehicle = Vehicle.of(context)!;
    int angleTotal = 0;
    for (int i in vehicle.defense) {
      angleTotal += i;
    }
    totalDefenseCont ??= TextEditingController(
        text: vehicle.totalDefense.toString())
      ..addListener(() => setState(() =>
          vehicle.totalDefense = int.tryParse(totalDefenseCont!.text) ?? 0));
    var total = EditingText(
        editing: widget.holder.editing,
        initialText: vehicle.totalDefense.toString(),
        controller: totalDefenseCont,
        textType: TextInputType.number,
        defaultSave: true,
        title: AppLocalizations.of(context)!.totalDefense);
    foreDefenseCont ??= TextEditingController(
        text: vehicle.defense[0].toString())
      ..addListener(() => setState(
          () => vehicle.defense[0] = int.tryParse(foreDefenseCont!.text) ?? 0));
    var fore = EditingText(
        editing: widget.holder.editing,
        initialText: vehicle.defense[0].toString(),
        controller: foreDefenseCont,
        textType: TextInputType.number,
        defaultSave: true,
        title: AppLocalizations.of(context)!.fore);
    portDefenseCont ??= TextEditingController(
        text: vehicle.defense[1].toString())
      ..addListener(() => setState(
          () => vehicle.defense[1] = int.tryParse(portDefenseCont!.text) ?? 0));
    var port = EditingText(
        editing: widget.holder.editing,
        initialText: vehicle.defense[1].toString(),
        controller: portDefenseCont,
        textType: TextInputType.number,
        defaultSave: true,
        title: AppLocalizations.of(context)!.port);
    starboardDefenseCont ??= TextEditingController(
        text: vehicle.defense[2].toString())
      ..addListener(() => setState(() =>
          vehicle.defense[2] = int.tryParse(starboardDefenseCont!.text) ?? 0));
    var starboard = EditingText(
        editing: widget.holder.editing,
        initialText: vehicle.defense[2].toString(),
        controller: starboardDefenseCont,
        textType: TextInputType.number,
        defaultSave: true,
        title: AppLocalizations.of(context)!.starboard);
    aftDefenseCont ??= TextEditingController(
        text: vehicle.defense[3].toString())
      ..addListener(() => setState(
          () => vehicle.defense[3] = int.tryParse(aftDefenseCont!.text) ?? 0));
    var aft = EditingText(
        editing: widget.holder.editing,
        initialText: vehicle.defense[3].toString(),
        controller: aftDefenseCont,
        textType: TextInputType.number,
        defaultSave: true,
        title: AppLocalizations.of(context)!.aft);
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
          children: [Expanded(child: port), Expanded(child: starboard)],
        ),
        aft,
        if (angleTotal != vehicle.totalDefense)
          Container(
            height: 5,
          ),
        if (angleTotal != vehicle.totalDefense)
          Text(
            AppLocalizations.of(context)!.anglingWarning,
            textAlign: TextAlign.center,
            style: Theme.of(context)
                .textTheme
                .bodyText1
                ?.copyWith(color: Colors.red),
          )
      ],
    );
  }
}
