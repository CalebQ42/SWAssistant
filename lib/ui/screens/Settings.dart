import 'dart:io';

import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/ui/Common.dart';

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
    return Scaffold(
      appBar: SWAppBar(),
      drawer: SWDrawer(),
      body: Column(
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
              if(!b)
                showDialog(
                  context: context,
                  builder: (context) => 
                    AlertDialog(
                      content: Text("Turning off firebase will remove the ability to download profiles and any bugs you encounter CANNOT be fixed. Additionally the app needs to be restarted for this to take effect. Continue?"),
                      actions: [
                        FlatButton(
                          onPressed: (){
                            app.prefs.setBool(preferences.firebase, b);
                            if(Platform.isIOS)
                              exit(0);
                            else
                              SystemNavigator.pop();
                          },
                          child: Text("Continue")
                        ),
                        FlatButton(
                          child: Text("Cancel"),
                          onPressed: (){
                            Navigator.pop(context);
                          }
                        )
                      ],
                    )
                );
              else
                showDialog(
                  context: context,
                  builder: (context) => 
                    AlertDialog(
                      content: Text("Turning on Firebase requires an app restart. Continue?"),
                      actions: [
                        FlatButton(
                          onPressed: (){
                            app.prefs.setBool(preferences.firebase, b);
                            if(Platform.isIOS)
                              exit(0);
                            else
                              SystemNavigator.pop();
                          },
                          child: Text("Continue")
                        ),
                        FlatButton(
                          child: Text("Cancel"),
                          onPressed: (){
                            Navigator.pop(context);
                          }
                        )
                      ],
                    )
                );
            },
            title: Text("Firebase"),
            subtitle: Text("WARNING: Needed for crash reporting and downloads"),
          ),
          Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.crashlytics, true),
            onChanged: app.getPreference(preferences.firebase, true) ? (b){
              FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(b);
              app.prefs.setBool(preferences.crashlytics, b);
              setState((){});
            } : null,
            title: Text("Crash Reporting"),
            subtitle: Text("Anonymous crash reporting provided by Firebase Crashlytics")
          ),
          Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.analytics, true),
            onChanged: app.getPreference(preferences.firebase, true) ? (b){
              app.analytics.setAnalyticsCollectionEnabled(b);
              app.prefs.setBool(preferences.analytics, b);
              setState((){});
            } : null,
            title: Text("Anonymous Usage Data"),
            subtitle: Text("Analytics provided by Firebase (Google)"),
          )
        ],
      )
    );
  }
}