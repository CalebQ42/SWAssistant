import 'package:darkstorm_common/backend/backend.dart';
import 'package:darkstorm_common/ui/frame_content.dart';
import 'package:darkstorm_common/ui/updating_switch_tile.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:url_launcher/url_launcher_string.dart';

class Settings extends StatefulWidget {
  const Settings({super.key});

  @override
  State<StatefulWidget> createState() => SettingsState();
}

class SettingsState extends State {
  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
    return FrameContent(
      child: ListView(
        physics: const BouncingScrollPhysics(),
        children: [
          const SizedBox(height: 10),
          TextButton(
            onPressed: () => launchUrlString(
                "https://github.com/CalebQ42/SWAssistant/discussions"),
            style: const ButtonStyle(alignment: Alignment.centerLeft),
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                app.locale.discuss,
                style: Theme.of(context).textTheme.titleMedium,
              ),
            ),
          ),
          const Divider(),
          TextButton(
            onPressed: () => launchUrlString("https://crwd.in/swrpg"),
            style: const ButtonStyle(alignment: Alignment.centerLeft),
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                app.locale.translate,
                style: Theme.of(context).textTheme.titleMedium,
              ),
            ),
          ),
          const Divider(),
          SwitchListTile(
            value: app.prefs.lightTheme,
            onChanged: (b) {
              setState(() => app.prefs.lightTheme = b);
              app.topLevelUpdate();
            },
            title: Text(app.locale.forceLight),
          ),
          const Divider(),
          SwitchListTile(
            value: app.prefs.darkTheme,
            onChanged: (b) {
              setState(() => app.prefs.darkTheme = b);
              app.topLevelUpdate();
            },
            title: Text(app.locale.forceDark),
          ),
          const Divider(),
          SwitchListTile(
            value: app.prefs.amoledTheme,
            onChanged: (b) {
              app.prefs.amoledTheme = b;
              app.topLevelUpdate();
            },
            title: Text(app.locale.amoledTheme),
          ),
          const Divider(),
          UpdatingSwitchTile(
            value: app.prefs.colorDice,
            onChanged: (b) => app.prefs.colorDice = b,
            title: Text(app.locale.colorDice),
          ),
          const Divider(),
          Padding(
              padding: const EdgeInsets.symmetric(horizontal: 15, vertical: 10),
              child: DropdownButtonFormField<String>(
                items: [
                  DropdownMenuItem<String>(
                    value: "",
                    child: Text(app.locale.systemLanguage),
                  ),
                  const DropdownMenuItem<String>(
                    value: "en",
                    child: Text("English"),
                  ),
                  const DropdownMenuItem<String>(
                    value: "es",
                    child: Text("Spanish"),
                  ),
                  const DropdownMenuItem<String>(
                    value: "fr",
                    child: Text("French"),
                  ),
                  const DropdownMenuItem<String>(
                    value: "it",
                    child: Text("Italian"),
                  ),
                  const DropdownMenuItem<String>(
                    value: "de",
                    child: Text("German"),
                  )
                ],
                onChanged: (String? value) {
                  app.prefs.locale = value ?? "";
                  app.topLevelUpdate();
                },
                value: app.prefs.locale,
              )),
          const Divider(),
          Padding(
            padding: const EdgeInsets.all(15),
            child: Text(
              app.locale.healthMode,
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
                        app.locale.additive,
                        style: Theme.of(context).textTheme.titleSmall,
                      ),
                      Text(app.locale.additiveExplaination)
                    ],
                  ),
                ),
                UpdatingSwitch(
                  value: app.prefs.subtractMode,
                  onChanged: (b) => app.prefs.subtractMode = b,
                ),
                Expanded(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(app.locale.subtractive,
                          style: Theme.of(context).textTheme.titleSmall),
                      Text(app.locale.subtractiveExplaination)
                    ],
                  ),
                ),
              ],
            ),
          ),
          if (app.isMobile) const Divider(),
          if (app.isMobile)
            SwitchListTile(
              title: Text(app.locale.cloudSave),
              value: app.prefs.googleDrive,
              onChanged: (b) {
                var message = ScaffoldMessenger.of(context);
                if (b) {
                  app
                      .syncRemote(
                    context: context,
                    nav: Navigator.of(context, rootNavigator: true),
                    onFull: () {
                      if (app.showFullError) {
                        message.showSnackBar(
                          SnackBar(
                            content: Text(app.locale.driveFull),
                          ),
                        );
                        app.showFullError = false;
                        Future.delayed(const Duration(minutes: 5),
                            () => app.showFullError = true);
                      }
                    },
                  )
                      .then(
                    (value) {
                      if (value) {
                        app.prefs.googleDrive = b;
                        setState(() {});
                      } else {
                        message.showSnackBar(
                          SnackBar(
                            content: Text(app.locale.driveEnableFail),
                          ),
                        );
                      }
                    },
                  );
                } else {
                  if (app.driver != null) app.driver!.gsi?.disconnect();
                  app.driver = null;
                  app.prefs.googleDrive = b;
                  app.prefs.driveFirstLoad = true;
                }
                setState(() {});
              },
            ),
          const Divider(),
          SwitchListTile(
            value: app.prefs.darkstormBackend,
            onChanged: (b) {
              setState(() => app.prefs.darkstormBackend = b);
              if (b) {
                app.initStupid();
              } else {
                app.backend = null;
              }
            },
            title: Text(app.locale.darkstorm),
            subtitle: Text(app.locale.darkstormSub),
          ),
          const Divider(),
          UpdatingSwitchTile(
            value: app.prefs.darkstormCount,
            onChanged: app.prefs.darkstormBackend
                ? (b) {
                    app.prefs.darkstormCount = b;
                  }
                : null,
            title: Text(app.locale.darkstormCount),
          ),
          const Divider(),
          UpdatingSwitchTile(
            value: app.prefs.darkstormCrash,
            onChanged: app.prefs.darkstormBackend
                ? (b) {
                    app.prefs.darkstormCrash = b;
                    if (b) {
                      FlutterError.onError = (err) {
                        app.backend!.crash(
                          Crash(
                            error: err.exceptionAsString(),
                            stack: err.stack?.toString() ?? "Not given",
                            version: app.package.version,
                          ),
                        );
                        FlutterError.presentError(err);
                      };
                    } else {
                      FlutterError.onError = FlutterError.presentError;
                    }
                  }
                : null,
            title: Text(app.locale.darkstormCrash),
          ),
          const Divider(),
          UpdatingSwitchTile(
            value: app.prefs.noAnimations,
            onChanged: (b) {
              app.prefs.noAnimations = b;
              if (b) {
                app.globalDuration = Duration.zero;
                app.transitionDuration = Duration.zero;
              } else {
                app.globalDuration = const Duration(milliseconds: 300);
                app.transitionDuration = const Duration(milliseconds: 100);
              }
            },
            title: Text(app.locale.disableAnimations),
          ),
          const Divider(),
          // TextButton(
          //   onPressed: () => app.manualImport(context),
          //   style: const ButtonStyle(
          //     alignment: Alignment.centerLeft
          //   ),
          //   child: Padding(
          //     padding: const EdgeInsets.all(10),
          //     child: Text(
          //       app.locale.introPage0ImportButton,
          //       style: Theme.of(context).textTheme.titleMedium,
          //     )
          //   )
          // ),
          // const Divider(),
          TextButton(
            onPressed: () {
              app.prefs.showIntro = true;
              Navigator.pushNamed(context, "/intro");
            },
            style: const ButtonStyle(alignment: Alignment.centerLeft),
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                app.locale.showIntro,
                style: Theme.of(context).textTheme.titleMedium,
              ),
            ),
          ),
          const Divider(),
          AboutListTile(
            applicationName: "SWAssistant",
            applicationIcon: const Image(
              image: AssetImage("assets/SWAssistant.png"),
              height: 64,
            ),
            applicationLegalese: app.locale.aboutText,
            applicationVersion: app.package.version,
          )
        ],
      ),
    );
  }
}
