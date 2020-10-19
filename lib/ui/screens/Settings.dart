import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/Preferences.dart' as preferences;

class Settings extends StatefulWidget{

  final Function updateTopLevel;

  Settings({this.updateTopLevel});

  @override
  State<StatefulWidget> createState() => SettingsState(updateTopLevel: updateTopLevel);
}

class SettingsState extends State{

  final Function updateTopLevel;

  SettingsState({this.updateTopLevel});

  @override
  Widget build(BuildContext context){
    var app = SW.of(context);
    return Column(
      children: [
        SwitchListTile(
          value: app.getPreference(preferences.light, false),
          onChanged: (b){
            app.prefs.setBool(preferences.light, b);
            updateTopLevel();
          },
          title: Text("Light Side Theme"),
        ),
        Divider(),
        SwitchListTile(
          value: app.getPreference(preferences.firebase, true),
          onChanged: (b){
            //TODO: show dialog first about restarting app
          },
          title: Text("Firebase"),
          subtitle: Text("WARNING: Needed for crash reporting and downloads"),
        ),
        Divider(),
        SwitchListTile(
          value: app.getPreference(preferences.crashlytics, true),
          onChanged: app.getPreference(preferences.firebase, true) ? (b){
            FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(b);
            setState(() => app.prefs.setBool(preferences.firebase, b));
          } : null,
          title: Text("Crash Reporting"),
          subtitle: Text("Anonymous crash reporting provided by Firebase Crashlytics")
        ),
        Divider(),
        SwitchListTile(
          value: app.getPreference(preferences.analytics, true),
          onChanged: app.getPreference(preferences.firebase, true) ? (b){
            //TODO: implement analytics
          } : null,
          title: Text("Anonymous Usage Data"),
          subtitle: Text("Analytics provided by Firebase (Google)"),
        )
      ],
    );
  }
}