import 'dart:io';

import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/sw_drawer.dart';

class Settings extends StatefulWidget{

  const Settings({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => SettingsState();
}

class SettingsState extends State{

  @override
  Widget build(BuildContext context){
    var app = SW.of(context);
    return Scaffold(
      appBar: AppBar(
        title: Text(AppLocalizations.of(context)!.settings),
        backgroundColor: Theme.of(context).primaryColor
      ),
      drawer: const SWDrawer(),
      body: ListView(
        physics: const BouncingScrollPhysics(),
        children: [
          SwitchListTile(
            value: app.getPreference(preferences.forceLight, false),
            onChanged: (b){
              app.prefs.setBool(preferences.forceLight, b);
              if (b) {
                app.prefs.setBool(preferences.forceDark, false);
              }
              SW.of(context).topLevelUpdate();
            },
            title: Text(AppLocalizations.of(context)!.forceLight),
          ),
          const Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.forceDark, false),
            onChanged: (b){
              app.prefs.setBool(preferences.forceDark, b);
              if (b) {
                app.prefs.setBool(preferences.forceLight, false);
              }
              SW.of(context).topLevelUpdate();
            },
            title: Text(AppLocalizations.of(context)!.forceDark),
          ),
          const Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.amoled, false),
            onChanged: (b){
              app.prefs.setBool(preferences.amoled, b);
              SW.of(context).topLevelUpdate();
            },
            title: Text(AppLocalizations.of(context)!.amoledTheme),
          ),
          const Divider(),
          SwitchListTile(
            value: app.getPreference(preferences.colorDice, true),
            onChanged: (b){
              app.prefs.setBool(preferences.colorDice, b);
              SW.of(context).topLevelUpdate();
            },
            title: Text(AppLocalizations.of(context)!.colorDice),
          ),
          if(!kIsWeb) const Divider(),
          if(!kIsWeb) SwitchListTile(
            title: Text(AppLocalizations.of(context)!.cloudSave),
            value: app.getPreference(preferences.googleDrive, false),
            onChanged: (b) {
              var message = ScaffoldMessenger.of(context);
              if (b) {
                app.initialSync(context: context).then((value) {
                  if(value){
                    app.prefs.setBool(preferences.newDrive, true);
                    app.prefs.setBool(preferences.driveFirstLoad, false);
                    app.prefs.setBool(preferences.googleDrive, b);
                    setState(() {});
                  }else{
                    message.showSnackBar(
                      SnackBar(content: Text(AppLocalizations.of(context)!.driveEnableFail))
                    );
                  }
                });
              } else {
                if(app.driver != null) app.driver!.gsi?.disconnect();
                app.prefs.setBool(preferences.googleDrive, b);
                app.prefs.setBool(preferences.driveFirstLoad, true);
              }
              setState(() {});
            }
          ),
          const Divider(),
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
                          if(!kIsWeb && Platform.isIOS) {
                            exit(0);
                          } else {
                            SystemNavigator.pop();
                          }
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
          const Divider(),
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
          const Divider(),
          SwitchListTile(
            title: Text(AppLocalizations.of(context)!.ads),
            value: app.getPreference(preferences.ads, true),
            onChanged: app.getPreference(preferences.firebase, true) ? (b) {
              //TODO
              app.prefs.setBool(preferences.ads, b);
              setState((){});
            } : null,
          ),
          const Divider(),
          TextButton(
            onPressed: () => app.manualImport(context),
            style: const ButtonStyle(
              alignment: Alignment.centerLeft
            ),
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                AppLocalizations.of(context)!.introPage0ImportButton,
                style: Theme.of(context).textTheme.subtitle1,
              )
            )
          ),
          const Divider(),
          TextButton(
            onPressed: () => Navigator.pushNamed(context, "/intro"),
            style: const ButtonStyle(
              alignment: Alignment.centerLeft
            ),
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                AppLocalizations.of(context)!.showIntro,
                style: Theme.of(context).textTheme.subtitle1,
              )
            )
          ),
          const Divider(),
          AboutListTile(
            applicationName: "SWAssistant",
            applicationIcon: const Image(image: AssetImage("assets/SWAssistant.png"), height: 64,),
            applicationLegalese: AppLocalizations.of(context)!.aboutText,
            applicationVersion: app.package.version,
          )
        ],
      )
    );
  }
}