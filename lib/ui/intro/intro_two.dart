//Other Settings.

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/intro/intro.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/screens/loading.dart';

class IntroTwo extends StatefulWidget{

  const IntroTwo({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() =>
    _IntroTwoState();
}

class _IntroTwoState extends State{
  @override
  Widget build(BuildContext context) =>
    IntroScreen(
      nextScreenAction: () {
        var app = SW.of(context);
        app.prefs.setBool(preferences.driveFirstLoad, false);
        app.prefs.setBool(preferences.firstStart, false);
        app.prefs.setBool(preferences.newDrive, true);
        for(var ask in preferences.newPrefs){
          app.prefs.setBool(ask, false);
        }
        Navigator.pushNamedAndRemoveUntil(
          context, SW.of(context).getPref(preferences.startingScreen), (route) => false);
      },
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(
            AppLocalizations.of(context)!.settings,
            style: Theme.of(context).textTheme.headlineMedium
          ),
          const SizedBox(height: 10),
          if(!kIsWeb) SwitchListTile(
            title: Text(
              AppLocalizations.of(context)!.cloudSave
            ),
            value: SW.of(context).getPref(preferences.googleDrive),
            onChanged: (b){
              SW.of(context).prefs.setBool(preferences.googleDrive, b);
              setState((){});
            }
          ),
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
                        style: Theme.of(context).textTheme.titleLarge,
                      ),
                      Text(AppLocalizations.of(context)!.additiveExplaination)
                    ]
                  )
                ),
                Switch(
                  value: SW.of(context).getPref(preferences.subtractMode),
                  onChanged: (b) => setState(() {
                    SW.of(context).prefs.setBool(preferences.subtractMode, b);
                  }),
                ),
                Expanded(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        AppLocalizations.of(context)!.subtractive,
                        style: Theme.of(context).textTheme.titleLarge
                      ),
                      Text(AppLocalizations.of(context)!.subtractiveExplaination)
                    ]
                  )
                ),
              ],
            )
          ),
          SwitchListTile(
            title: Text(
              AppLocalizations.of(context)!.forceDark,
            ),
            value: SW.of(context).getPref(preferences.forceDark),
            onChanged: (b){
              SW.of(context).prefs.setBool(preferences.forceDark, b);
              if (b) SW.of(context).prefs.setBool(preferences.forceLight, false);
              setState((){});
              SW.of(context).topLevelUpdate();
            }
          ),
          SwitchListTile(
            title: Text(
              AppLocalizations.of(context)!.amoledTheme,
            ),
            value: SW.of(context).getPref(preferences.amoled),
            onChanged: (b){
              SW.of(context).prefs.setBool(preferences.amoled, b);
              setState((){});
              SW.of(context).topLevelUpdate();
            }
          ),
          SwitchListTile(
            title: Text(
              AppLocalizations.of(context)!.forceLight,
            ),
            value: SW.of(context).getPref(preferences.forceLight),
            onChanged: (b){
              SW.of(context).prefs.setBool(preferences.forceLight, b);
              if (b) SW.of(context).prefs.setBool(preferences.forceDark, false);
              setState((){});
              SW.of(context).topLevelUpdate();
            }
          ),
        ],
      )
    );
}