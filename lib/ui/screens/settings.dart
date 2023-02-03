import 'dart:io';

import 'package:firebase_crashlytics/firebase_crashlytics.dart' deferred as crash;
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:in_app_purchase/in_app_purchase.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/dialogs/gplay_donate.dart';
import 'package:swassistant/ui/frame_content.dart';
import 'package:swassistant/ui/misc/updating_switch_tile.dart';
import 'package:url_launcher/url_launcher_string.dart';

class Settings extends StatefulWidget{

  const Settings({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => SettingsState();
}

class SettingsState extends State{

  @override
  Widget build(BuildContext context){
    var app = SW.of(context);
    return FrameContent(
      child: ListView(
        physics: const BouncingScrollPhysics(),
        children: [
          const SizedBox(height: 10),
          TextButton(
            onPressed: () => launchUrlString("https://github.com/CalebQ42/SWAssistant/discussions"),
            style: const ButtonStyle(
              alignment: Alignment.centerLeft
            ),
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                AppLocalizations.of(context)!.discuss,
                style: Theme.of(context).textTheme.titleMedium,
              )
            )
          ),
          const Divider(),
          TextButton(
            onPressed: () => launchUrlString("https://crwd.in/swrpg"),
            style: const ButtonStyle(
              alignment: Alignment.centerLeft
            ),
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                AppLocalizations.of(context)!.translate,
                style: Theme.of(context).textTheme.titleMedium,
              )
            )
          ),
          const Divider(),
          TextButton(
            onPressed: () async {
              if(!app.isMobile()){
                launchUrlString("https://github.com/sponsors/CalebQ42");
              }else{
                if (!await InAppPurchase.instance.isAvailable()){
                  ScaffoldMessenger.of(context).clearSnackBars();
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                    content: Text(AppLocalizations.of(context)!.gPlayUnavailable),
                  ));
                }else{
                  InAppPurchase.instance.queryProductDetails({
                    "donate1",
                    "donate5",
                    "donate10",
                    "donate20",
                  }).then((value) =>
                    GPlayDonateDialog(value.productDetails).show(context));
                }
              }
            },
            style: const ButtonStyle(
              alignment: Alignment.centerLeft
            ),
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                AppLocalizations.of(context)!.donate,
                style: Theme.of(context).textTheme.titleMedium,
              )
            )
          ),
          const Divider(),
          SwitchListTile(
            value: app.getPref(preferences.forceLight),
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
            value: app.getPref(preferences.forceDark),
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
            value: app.getPref(preferences.amoled),
            onChanged: (b){
              app.prefs.setBool(preferences.amoled, b);
              SW.of(context).topLevelUpdate();
            },
            title: Text(AppLocalizations.of(context)!.amoledTheme),
          ),
          const Divider(),
          SwitchListTile(
            value: app.getPref(preferences.colorDice),
            onChanged: (b){
              app.prefs.setBool(preferences.colorDice, b);
              SW.of(context).topLevelUpdate();
            },
            title: Text(AppLocalizations.of(context)!.colorDice),
          ),
          const Divider(),
          Padding(
            padding: const EdgeInsets.symmetric(
              horizontal: 15,
              vertical: 10
            ),
            child: DropdownButtonFormField<String>(
              items: [
                DropdownMenuItem<String>(
                  value: "",
                  child: Text(AppLocalizations.of(context)!.systemLanguage),
                ),
                const DropdownMenuItem<String>(
                  value: "en",
                  child: Text("English")
                ),
                const DropdownMenuItem<String>(
                  value: "es",
                  child: Text("Spanish")
                ),
                const DropdownMenuItem<String>(
                  value: "fr",
                  child: Text("French")
                ),
                const DropdownMenuItem<String>(
                  value: "it",
                  child: Text("Italian")
                ),
                const DropdownMenuItem<String>(
                  value: "de",
                  child: Text("German")
                )
              ],
              onChanged: (String? value) {
                value ??= "";
                app.prefs.setString(preferences.locale, value);
                app.topLevelUpdate();
              },
              value: app.getPref(preferences.locale),
            )
          ),
          const Divider(),
          Padding(
            padding: const EdgeInsets.all(15),
            child: Text(
              AppLocalizations.of(context)!.healthMode,
              style: Theme.of(context).textTheme.titleMedium,
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(15),
            child: Row(
              mainAxisSize: MainAxisSize.max,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        AppLocalizations.of(context)!.additive,
                        style: Theme.of(context).textTheme.titleSmall,
                      ),
                      Text(AppLocalizations.of(context)!.additiveExplaination)
                    ]
                  )
                ),
                UpdatingSwitch(
                  value: app.getPref(preferences.subtractMode),
                  onChanged: (b) =>
                    app.prefs.setBool(preferences.subtractMode, b),
                ),
                Expanded(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        AppLocalizations.of(context)!.subtractive,
                        style: Theme.of(context).textTheme.titleSmall
                      ),
                      Text(AppLocalizations.of(context)!.subtractiveExplaination)
                    ]
                  )
                ),
              ],
            )
          ),
          if(app.isMobile()) const Divider(),
          if(app.isMobile()) SwitchListTile(
            title: Text(AppLocalizations.of(context)!.cloudSave),
            value: app.getPref(preferences.googleDrive),
            onChanged: (b) {
              var message = ScaffoldMessenger.of(context);
              if (b) {
                app.syncRemote(context: context, nav: Navigator.of(context, rootNavigator: true)).then((value) {
                  if(value){
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
                app.driver = null;
                app.prefs.setBool(preferences.googleDrive, b);
                app.prefs.setBool(preferences.driveFirstLoad, true);
              }
              setState(() {});
            }
          ),
          const Divider(),
          SwitchListTile(
            value: app.getPref(preferences.firebase),
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
                          Navigator.of(context, rootNavigator: true).pop()
                      )
                    ],
                  )
              );
            },
            title: Text(AppLocalizations.of(context)!.firebase),
            subtitle: Text(AppLocalizations.of(context)!.firebaseSubtitle),
          ),
          if(app.isMobile()) const Divider(),
          if(app.isMobile()) SwitchListTile(
            value: app.getPref(preferences.crashlytics),
            onChanged: app.getPref(preferences.firebase) ? (b){
              crash.loadLibrary().then((_) => crash.FirebaseCrashlytics.instance.setCrashlyticsCollectionEnabled(b));
              app.prefs.setBool(preferences.crashlytics, b);
              setState((){});
            } : null,
            title: Text(AppLocalizations.of(context)!.crashReporting),
            subtitle: Text(AppLocalizations.of(context)!.crashReportingSubtitle)
          ),
          const Divider(),
          SwitchListTile(
            title: Text(AppLocalizations.of(context)!.ads),
            value: app.getPref(preferences.ads),
            onChanged: app.getPref(preferences.firebase) ? (b) {
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
                style: Theme.of(context).textTheme.titleMedium,
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
                style: Theme.of(context).textTheme.titleMedium,
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