import 'package:darkstorm_common/intro.dart';
import 'package:darkstorm_common/updating_switch_tile.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import 'package:swassistant/sw.dart';
import 'package:url_launcher/url_launcher_string.dart';

class Intro{
  final SW app;

  Intro(this.app);

  List<IntroPage Function(BuildContext)> get pages => [
    intro0,
    if(kIsWeb || app.isMobile) intro1,
    intro2,
  ];

  IntroPage intro0(BuildContext context) =>
    IntroPage(
      title: Text(AppLocalizations.of(context)!.introWelcome),
      subtext: Column(
        children: [
          Text(AppLocalizations.of(context)!.introPage0Line1),
          if(kIsWeb) Text.rich(
            TextSpan(
              children: [
                TextSpan(text: AppLocalizations.of(context)!.introPage0WebNoticev2),
                TextSpan(
                  text: AppLocalizations.of(context)!.introPage0WebNoticeEnd,
                  style: const TextStyle(
                    decoration: TextDecoration.underline
                  ),
                  recognizer: TapGestureRecognizer()..onTap = () =>
                    launchUrlString("https://darkstorm.tech/COOKIE-POLICY.html")
                )
              ]
            ),
          ),
        ],
      ),
    );

  IntroPage intro1(BuildContext context){
    var pagKey = GlobalKey<IntroPageState>();
    return IntroPage(
      key: pagKey,
      title: Text(AppLocalizations.of(context)!.firebase),
      subtext: Column(
        children: [
          Text(
            AppLocalizations.of(context)!.introPage1FirebaseExplaination,
          ),
          if(!kIsWeb) const SizedBox(height: 20),
          if(!kIsWeb) Text(
            AppLocalizations.of(context)!.introPage1CrashReportingExplaination,
          ),
        ],
      ),
      items: (c) => [
        if(kIsWeb || app.isMobile) UpdatingSwitchTile(
          title: Text(
            AppLocalizations.of(context)!.firebase,
          ),
          value: app.prefs.firebase,
          onChanged: (b){
            app.prefs.firebase = b;
            pagKey.currentState?.update();
          }
        ),
        if(app.isMobile) SwitchListTile(
          title: Text(
            AppLocalizations.of(context)!.crashReporting,
          ),
          value: app.prefs.crashlytics,
          onChanged: app.prefs.firebase ? (b) {
            app.prefs.crashlytics = b;
            pagKey.currentState?.update();
          } : null
        ),
      ],
    );
  }

  IntroPage intro2(BuildContext context){
    var pagKey = GlobalKey<IntroPageState>();
    return IntroPage(
      key: pagKey,
      title: Text(AppLocalizations.of(context)!.settings),
      items: (c) => [
        if(app.isMobile) UpdatingSwitchTile(
          title: Text(
            AppLocalizations.of(context)!.cloudSave
          ),
          value: app.prefs.googleDrive,
          onChanged: (b) =>
            app.prefs.googleDrive = b
        ),
        if(app.isMobile) const Divider(),
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
                value: app.prefs.subtractMode,
                onChanged: (b) {
                  app.prefs.subtractMode = b;
                  pagKey.currentState?.update();
                }
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
        const Divider(),
        SwitchListTile(
          title: Text(
            AppLocalizations.of(context)!.forceDark,
          ),
          value: app.prefs.darkTheme,
          onChanged: (b){
            app.prefs.darkTheme = b;
            pagKey.currentState?.update();
            app.topLevelUpdate();
          }
        ),
        const Divider(),
        SwitchListTile(
          title: Text(
            AppLocalizations.of(context)!.amoledTheme,
          ),
          value: app.prefs.amoledTheme,
          onChanged: (b){
            app.prefs.amoledTheme = b;
            pagKey.currentState?.update();
            print("yo");
            print("yodle: ${pagKey.currentState}");
            print(app.prefs.amoledTheme);
            app.topLevelUpdate();
          }
        ),
        const Divider(),
        SwitchListTile(
          title: Text(
            AppLocalizations.of(context)!.forceLight,
          ),
          value: app.prefs.lightTheme,
          onChanged: (b){
            app.prefs.lightTheme = b;
            pagKey.currentState?.update();
            app.topLevelUpdate();
          }
        ),
      ],
    );
  }
}