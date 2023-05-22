import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/sw.dart';
import 'package:darkstorm_common/bottom.dart';
import 'package:swassistant/ui/misc/up_down.dart';

class DestinyDialog extends StatelessWidget{

  final DestinyHolder dest = DestinyHolder();
  final GlobalKey<UpDownStatState> light = GlobalKey();
  final GlobalKey<UpDownStatState> dark = GlobalKey();

  DestinyDialog({Key? key}) : super(key: key);

  void reset(BuildContext context){
    SW.of(context).prefs.setInt(preferences.destinyLight, 0);
    SW.of(context).prefs.setInt(preferences.destinyDark, 0);
    dest.lightSide = 0;
    dest.darkSide = 0;
    light.currentState?.update();
    dark.currentState?.update();
  }

  @override
  Widget build(BuildContext context) {
    dest.lightSide = SW.of(context).getPref(preferences.destinyLight);
    dest.darkSide = SW.of(context).getPref(preferences.destinyDark);
    return Row(
      mainAxisSize: MainAxisSize.max,
      children: [
        Expanded(child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              AppLocalizations.of(context)!.lightSide,
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.titleLarge
            ),
            Container(height: 10),
            UpDownStat(
              key: light,
              onUpPressed: () {
                dest.lightSide++;
                SW.of(context).prefs.setInt(preferences.destinyLight, dest.lightSide);
              },
              onDownPressed: () {
                dest.lightSide--;
                SW.of(context).prefs.setInt(preferences.destinyLight, dest.lightSide);
              },
              getValue: () => dest.lightSide,
              min: 0,
            )
          ]
        )),
        Expanded(child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              AppLocalizations.of(context)!.darkSide,
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.titleLarge
            ),
            Container(height: 10),
            UpDownStat(
              key: dark,
              onUpPressed: () {
                dest.darkSide++;
                SW.of(context).prefs.setInt(preferences.destinyDark, dest.darkSide);
              },
              onDownPressed: () {
                dest.darkSide--;
                SW.of(context).prefs.setInt(preferences.destinyDark, dest.darkSide);
              },
              getValue: () => dest.darkSide,
              min: 0,
            )
          ]
        )),
      ],
    );
  }

  void show(BuildContext context) =>
    Bottom(
      child: (c) => this,
      buttons: (c) => [
        TextButton(
          child: Text(AppLocalizations.of(context)!.reset),
          onPressed: () => reset(c),
        )
      ]
    ).show(context);
}

class DestinyHolder {
  int lightSide = 0;
  int darkSide = 0;
}