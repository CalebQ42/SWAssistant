//Other Settings.

import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/intro/intro.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class IntroTwo extends StatefulWidget {
  const IntroTwo({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _IntroTwoState();
}

class _IntroTwoState extends State {
  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
    return IntroScreen(
        nextScreenAction: () {
          app.prefs.setBool(preferences.firstStart, false);
          app.postInit(context).whenComplete(() =>
              Navigator.pushNamedAndRemoveUntil(
                  context,
                  app.getPreference(preferences.startingScreen, "/characters"),
                  (route) => false));
        },
        child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 500),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(AppLocalizations.of(context)!.settings,
                    style: Theme.of(context).textTheme.headline4),
                const SizedBox(height: 10),
                SwitchListTile(
                    title: const Text(
                      "Cloud Save (via Google Drive) (Not currently implemented)",
                    ),
                    value: app.getPreference(preferences.googleDrive, false),
                    onChanged: (b) {
                      //TODO: Google Drive
                      app.prefs.setBool(preferences.googleDrive, b);
                      setState(() {});
                    }),
                SwitchListTile(
                    title: Text(
                      AppLocalizations.of(context)!.forceDark,
                    ),
                    value: app.getPreference(preferences.forceDark, false),
                    onChanged: (b) {
                      app.prefs.setBool(preferences.forceDark, b);
                      if (b) {
                        app.prefs.setBool(preferences.forceLight, false);
                      }
                      setState(() {});
                      app.topLevelUpdate();
                    }),
                SwitchListTile(
                    title: Text(
                      AppLocalizations.of(context)!.amoledTheme,
                    ),
                    value: app.getPreference(preferences.amoled, false),
                    onChanged: (b) {
                      app.prefs.setBool(preferences.amoled, b);
                      setState(() {});
                      app.topLevelUpdate();
                    }),
                SwitchListTile(
                    title: Text(
                      AppLocalizations.of(context)!.forceLight,
                    ),
                    value: app.getPreference(preferences.forceLight, false),
                    onChanged: (b) {
                      app.prefs.setBool(preferences.forceLight, b);
                      if (b) {
                        app.prefs.setBool(preferences.forceDark, false);
                      }
                      setState(() {});
                      app.topLevelUpdate();
                    }),
              ],
            )));
  }
}
