//firebase options.

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/intro/IntroScreen.dart';
import 'package:swassistant/ui/intro/IntroTwo.dart';
import 'package:swassistant/Preferences.dart' as preferences;

class IntroOne extends StatefulWidget{
  @override
  State<StatefulWidget> createState() =>
    _IntroOneState();

}

class _IntroOneState extends State{
  @override
  Widget build(BuildContext context) =>
    IntroScreen(
      nextScreen: IntroTwo(),
      child: ConstrainedBox(
        constraints: BoxConstraints(maxWidth: 500),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              "Firebase",
              style: Theme.of(context).textTheme.headline4
            ),
            SizedBox(height: 5),
            Text(
              "SWAssistant makes use of the Firebase by Google. Firebase is used in the project to provide crash reporting, ads, and to download pre-made profiles. If you want to, you can disable Firebase completely, or disable just crash reporting or ads. You can change this later in settings.",
              textAlign: TextAlign.justify,
            ),
            SizedBox(height: 20),
            Text(
              "If you disable crash reporting, any issues you encounter WILL NOT be fixed as I will have no ability to know what's causing the issues.",
              textAlign: TextAlign.justify,
            ),
            SizedBox(height: 25),
            SwitchListTile(
              title: Text(
                "Firebase",
              ),
              value: SW.of(context).getPreference(preferences.firebase, true),
              onChanged: (b){
                SW.of(context).prefs.setBool(preferences.firebase, b);
                setState((){});
              }
            ),
            SwitchListTile(
              title: Text(
                "Crashlytics (Crash Reporting)",
              ),
              value: SW.of(context).getPreference(preferences.crashlytics, true),
              onChanged: SW.of(context).getPreference(preferences.firebase, true) ? (b) {
                SW.of(context).prefs.setBool(preferences.crashlytics, b);
                setState((){});
              } : null
            ),
            //TODO
            // SwitchListTile(
            //   title: Text(
            //     "Ads (Not currently implemented)",
            //   ),
            //   value: SW.of(context).getPreference(preferences.crashlytics, true),
            //   onChanged: SW.of(context).getPreference(preferences.firebase, true) ? (b) {
            //     SW.of(context).prefs.setBool(preferences.crashlytics, b);
            //     setState((){});
            //   } : null
            // )
          ],
        )
      )
    );
}