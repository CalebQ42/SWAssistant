import 'dart:io';

import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/ui/Common.dart';

class Settings extends StatefulWidget{

  Settings();

  @override
  State<StatefulWidget> createState() => SettingsState();
}

class SettingsState extends State{

  SettingsState();

  @override
  Widget build(BuildContext context){
    var app = SW.of(context);
    return Scaffold(
      appBar: SWAppBar(),
      drawer: SWDrawer(),
      body: Column(
        children: [
          SwitchListTile(
            value: app.getPreference(preferences.forceLight, false),
            onChanged: (b){
              app.prefs.setBool(preferences.forceLight, b);
              if (b){
                app.prefs.setBool(preferences.forceDark, false);
              }
              SW.of(context).topLevelUpdate();
            },
            title: Text("Force Light Side"),
          ),
          Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.forceDark, false),
            onChanged: (b){
              app.prefs.setBool(preferences.forceDark, b);
              if (b){
                app.prefs.setBool(preferences.forceLight, false);
              }
              SW.of(context).topLevelUpdate();
            },
            title: Text("Force Dark Side"),
          ),
          Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.amoled, false),
            onChanged: (b){
              app.prefs.setBool(preferences.amoled, b);
              SW.of(context).topLevelUpdate();
            },
            title: Text("Amoled (Pitch Black) Dark Theme"),
          ),
          Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.colorDice, true),
            onChanged: (b){
              app.prefs.setBool(preferences.colorDice, b);
              SW.of(context).topLevelUpdate();
            },
            title: Text("Color Dice Roller"),
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
                      content: Text("Turning off firebase will remove the ability to download profiles. Additionally the app needs to be restarted for this to take effect. Continue?"),
                      actions: [
                        TextButton(
                          onPressed: (){
                            app.prefs.setBool(preferences.firebase, b);
                            if(Platform.isIOS)
                              exit(0);
                            else
                              SystemNavigator.pop();
                          },
                          child: Text("Continue")
                        ),
                        TextButton(
                          child: Text("Cancel"),
                          onPressed: () =>
                            Navigator.pop(context)
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
                        TextButton(
                          onPressed: (){
                            app.prefs.setBool(preferences.firebase, b);
                            if(Platform.isIOS)
                              exit(0);
                            else
                              SystemNavigator.pop();
                          },
                          child: Text("Continue")
                        ),
                        TextButton(
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
        ],
      )
    );
  }
}