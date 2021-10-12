//Other Settings.

import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/intro/IntroScreen.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class IntroTwo extends StatefulWidget{
  @override
  State<StatefulWidget> createState() =>
    _IntroTwoState();
}

class _IntroTwoState extends State{
  @override
  Widget build(BuildContext context) =>
    IntroScreen(
      nextScreenAction: () => 
      SW.of(context).postInit(context).whenComplete(() =>
        Navigator.pushNamedAndRemoveUntil(
          context, SW.of(context).getPreference(preferences.startingScreen, "/characters"), (route) => false)
      ),
      child: ConstrainedBox(
        constraints: BoxConstraints(maxWidth: 500),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              AppLocalizations.of(context)!.settings,
              style: Theme.of(context).textTheme.headline4
            ),
            SizedBox(height: 10),
            //TODO
            // SwitchListTile(
            //   title: Text(
            //     "Cloud Save (via Google Drive) (Not currently implemented)",
            //   ),
            //   value: SW.of(context).getPreference(preferences.googleDrive, true),
            //   onChanged: (b){
            //     SW.of(context).prefs.setBool(preferences.googleDrive, b);
            //     setState((){});
            //   }
            // ),
            SwitchListTile(
              title: Text(
                AppLocalizations.of(context)!.forceDark,
              ),
              value: SW.of(context).getPreference(preferences.forceDark, true),
              onChanged: (b){
                SW.of(context).prefs.setBool(preferences.forceDark, b);
                if (b)
                  SW.of(context).prefs.setBool(preferences.forceLight, false);
                setState((){});
                SW.of(context).topLevelUpdate();
              }
            ),
            SwitchListTile(
              title: Text(
                AppLocalizations.of(context)!.amoledTheme,
              ),
              value: SW.of(context).getPreference(preferences.amoled, true),
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
              value: SW.of(context).getPreference(preferences.forceLight, true),
              onChanged: (b){
                SW.of(context).prefs.setBool(preferences.forceLight, b);
                if (b)
                  SW.of(context).prefs.setBool(preferences.forceDark, false);
                setState((){});
                SW.of(context).topLevelUpdate();
              }
            ),
          ],
        )
      )
    );
}