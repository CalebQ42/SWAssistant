//firebase options.

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/intro/intro.dart';
import 'package:swassistant/ui/intro/intro_two.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class IntroOne extends StatefulWidget{

  const IntroOne({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() =>
    _IntroOneState();

}

class _IntroOneState extends State{
  @override
  Widget build(BuildContext context) =>
    IntroScreen(
      nextScreen: const IntroTwo(),
      child: ConstrainedBox(
        constraints: const BoxConstraints(maxWidth: 500),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              AppLocalizations.of(context)!.firebase,
              style: Theme.of(context).textTheme.headline4
            ),
            const SizedBox(height: 5),
            Text(
              AppLocalizations.of(context)!.introPage1FirebaseExplaination,
              textAlign: TextAlign.justify,
            ),
            if(!kIsWeb) const SizedBox(height: 20),
            if(!kIsWeb) Text(
              AppLocalizations.of(context)!.introPage1CrashReportingExplaination,
              textAlign: TextAlign.justify,
            ),
            const SizedBox(height: 25),
            SwitchListTile(
              title: Text(
                AppLocalizations.of(context)!.firebase,
              ),
              value: SW.of(context).getPreference(preferences.firebase, true),
              onChanged: (b){
                SW.of(context).prefs.setBool(preferences.firebase, b);
                setState((){});
              }
            ),
            SwitchListTile(
              title: Text(
                AppLocalizations.of(context)!.crashReporting,
              ),
              value: SW.of(context).getPreference(preferences.crashlytics, true),
              onChanged: SW.of(context).getPreference(preferences.firebase, true) ? (b) {
                SW.of(context).prefs.setBool(preferences.crashlytics, b);
                setState((){});
              } : null
            ),
            // TODO:Ads
            // SwitchListTile(
            //   title: const Text(
            //     "Ads (Not currently implemented)",
            //   ),
            //   value: SW.of(context).getPreference(preferences.ads, true),
            //   onChanged: SW.of(context).getPreference(preferences.firebase, true) ? (b) {
            //     SW.of(context).prefs.setBool(preferences.ads, b);
            //     setState((){});
            //   } : null
            // ),
          ],
        )
      )
    );
}