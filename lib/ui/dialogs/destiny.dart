import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:darkstorm_common/bottom.dart';
import 'package:swassistant/ui/misc/up_down.dart';

class DestinyDialog extends StatelessWidget{
  final GlobalKey<UpDownStatState> light = GlobalKey();
  final GlobalKey<UpDownStatState> dark = GlobalKey();

  DestinyDialog({Key? key}) : super(key: key);

  void reset(BuildContext context){
    SW.of(context).prefs.destinyLight = 0;
    SW.of(context).prefs.destinyDark = 0;
    light.currentState?.update();
    dark.currentState?.update();
  }

  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
    return Row(
      mainAxisSize: MainAxisSize.max,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Expanded(child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              app.locale.lightSide,
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.titleLarge
            ),
            Container(height: 10),
            UpDownStat(
              key: light,
              onUpPressed: () =>
                app.prefs.destinyLight++,
              onDownPressed: () =>
                app.prefs.destinyLight--,
              getValue: () => app.prefs.destinyLight,
              min: 0,
            )
          ]
        )),
        Expanded(child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              app.locale.darkSide,
              textAlign: TextAlign.center,
              style: Theme.of(context).textTheme.titleLarge
            ),
            Container(height: 10),
            UpDownStat(
              key: dark,
              onUpPressed: () =>
                app.prefs.destinyDark++,
              onDownPressed: () =>
                app.prefs.destinyDark--,
              getValue: () => app.prefs.destinyDark,
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
          child: Text(SW.of(context).locale.reset),
          onPressed: () => reset(c),
        )
      ]
    ).show(context);
}