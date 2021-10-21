import 'dart:io';

import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/ui/Common.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

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
      appBar: SWAppBar(
        context,
        title: Text(AppLocalizations.of(context)!.settings),
        backgroundColor: Theme.of(context).primaryColor
      ),
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
            title: Text(AppLocalizations.of(context)!.forceLight),
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
            title: Text(AppLocalizations.of(context)!.forceDark),
          ),
          Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.amoled, false),
            onChanged: (b){
              app.prefs.setBool(preferences.amoled, b);
              SW.of(context).topLevelUpdate();
            },
            title: Text(AppLocalizations.of(context)!.amoledTheme),
          ),
          Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.colorDice, true),
            onChanged: (b){
              app.prefs.setBool(preferences.colorDice, b);
              SW.of(context).topLevelUpdate();
            },
            title: Text(AppLocalizations.of(context)!.colorDice),
          ),
          Divider(),
          SwitchListTile(
            title: Text(AppLocalizations.of(context)!.cloudSave),
            value: app.getPreference(preferences.googleDrive, false),
            onChanged: (b){
              //TODO
              app.prefs.setBool(preferences.googleDrive, b);
              setState(() {});
            }
          ),
          Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.firebase, true),
            onChanged: (b){
              showDialog(
                context: context,
                builder: (context) => 
                  AlertDialog(
                    content: Text(!b ? AppLocalizations.of(context)!.firebaseOffNotice : AppLocalizations.of(context)!.firebaseOnNotice),
                    actions: [
                      TextButton(
                        onPressed: (){
                          app.prefs.setBool(preferences.firebase, b);
                          if(Platform.isIOS)
                            exit(0);
                          else
                            SystemNavigator.pop();
                        },
                        child: Text(MaterialLocalizations.of(context).continueButtonLabel)
                      ),
                      TextButton(
                        child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
                        onPressed: () =>
                          Navigator.pop(context)
                      )
                    ],
                  )
              );
            },
            title: Text(AppLocalizations.of(context)!.firebase),
            subtitle: Text(AppLocalizations.of(context)!.firebaseSubtitle),
          ),
          Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.crashlytics, true),
            onChanged: app.getPreference(preferences.firebase, true) ? (b){
              FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(b);
              app.prefs.setBool(preferences.crashlytics, b);
              setState((){});
            } : null,
            title: Text(AppLocalizations.of(context)!.crashReporting),
            subtitle: Text(AppLocalizations.of(context)!.crashReportingSubtitle)
          ),
          Divider(),
          SwitchListTile(
            title: Text(AppLocalizations.of(context)!.ads),
            value: app.getPreference(preferences.ads, true),
            onChanged: app.getPreference(preferences.firebase, true) ? (b) {
              //TOOD
              app.prefs.setBool(preferences.ads, b);
              setState((){});
            } : null,
          ),
          Divider(),
          AboutListTile(
            applicationName: "SWAssistant",
            applicationIcon: Image(image: AssetImage("assets/SWAssistant.png"), height: 64,),
            applicationLegalese: AppLocalizations.of(context)!.aboutText,
            applicationVersion: "3.0.0.0",
          )
        ],
      )
    );
  }
}