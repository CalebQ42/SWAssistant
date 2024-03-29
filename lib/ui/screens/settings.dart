import 'package:darkstorm_common/frame_content.dart';
import 'package:darkstorm_common/updating_switch_tile.dart';
import 'package:flutter/material.dart';
import 'package:in_app_purchase/in_app_purchase.dart';
import 'package:stupid/stupid.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/dialogs/gplay_donate.dart';
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
    var scaf = ScaffoldMessenger.of(context);
    var loc = app.locale;
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
                app.locale.discuss,
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
                app.locale.translate,
                style: Theme.of(context).textTheme.titleMedium,
              )
            )
          ),
          const Divider(),
          TextButton(
            onPressed: () async {
              if(!app.isMobile){
                launchUrlString("https://github.com/sponsors/CalebQ42");
              }else{
                if (!await InAppPurchase.instance.isAvailable()){
                  scaf.clearSnackBars();
                  scaf.showSnackBar(SnackBar(
                    content: Text(loc.gPlayUnavailable),
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
                app.locale.donate,
                style: Theme.of(context).textTheme.titleMedium,
              )
            )
          ),
          const Divider(),
          SwitchListTile(
            value: app.prefs.lightTheme,
            onChanged: (b){
              setState(() => app.prefs.lightTheme = b);
              app.topLevelUpdate();
            },
            title: Text(app.locale.forceLight),
          ),
          const Divider(),
          SwitchListTile(
            value: app.prefs.darkTheme,
            onChanged: (b){
              setState(() => app.prefs.darkTheme = b);
              app.topLevelUpdate();
            },
            title: Text(app.locale.forceDark),
          ),
          const Divider(),
          SwitchListTile(
            value: app.prefs.amoledTheme,
            onChanged: (b){
              app.prefs.amoledTheme = b;
              app.topLevelUpdate();
            },
            title: Text(app.locale.amoledTheme),
          ),
          const Divider(),
          SwitchListTile(
            value: app.prefs.colorDice,
            onChanged: (b){
              app.prefs.colorDice = b;
              app.topLevelUpdate();
            },
            title: Text(app.locale.colorDice),
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
                  child: Text(app.locale.systemLanguage),
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
                app.prefs.locale = value ?? "";
                app.topLevelUpdate();
              },
              value: app.prefs.locale,
            )
          ),
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
                    ]
                  )
                ),
                UpdatingSwitch(
                  value: app.prefs.subtractMode,
                  onChanged: (b) =>
                    app.prefs.subtractMode = b,
                ),
                Expanded(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        app.locale.subtractive,
                        style: Theme.of(context).textTheme.titleSmall
                      ),
                      Text(app.locale.subtractiveExplaination)
                    ]
                  )
                ),
              ],
            )
          ),
          if(app.isMobile) const Divider(),
          if(app.isMobile) SwitchListTile(
            title: Text(app.locale.cloudSave),
            value: app.prefs.googleDrive,
            onChanged: (b) {
              var message = ScaffoldMessenger.of(context);
              if (b) {
                app.syncRemote(
                  context: context,
                  nav: Navigator.of(context, rootNavigator: true),
                  onFull: (){
                    if(app.showFullError){
                      message.showSnackBar(
                        SnackBar(
                          content: Text(app.locale.driveFull),
                        )
                      );
                      app.showFullError = false;
                      Future.delayed(const Duration(minutes: 5), () => app.showFullError = true);
                    }
                  }
                ).then((value) {
                  if(value){
                    app.prefs.googleDrive = b;
                    setState(() {});
                  }else{
                    message.showSnackBar(
                      SnackBar(content: Text(app.locale.driveEnableFail))
                    );
                  }
                });
              } else {
                if(app.driver != null) app.driver!.gsi?.disconnect();
                app.driver = null;
                app.prefs.googleDrive = b;
                app.prefs.driveFirstLoad = true;
              }
              setState(() {});
            }
          ),
          const Divider(),
          SwitchListTile(
            value: app.prefs.stupid,
            onChanged: (b) {
              setState(() => app.prefs.stupid = b);
              if(b){
                app.initStupid();
              }else{
                app.stupid = null;
              }
            },
            title: Text(app.locale.stupid),
            subtitle: Text(app.locale.stupidSub)
          ),
          const Divider(),
          UpdatingSwitchTile(
            value: app.prefs.stupidLog,
            onChanged: app.prefs.stupid ? (b){
              app.prefs.stupidLog = b;
            } : null,
            title: Text(app.locale.stupidLog),
          ),
          const Divider(),
          UpdatingSwitchTile(
            value: app.prefs.stupidCrash,
            onChanged: app.prefs.stupid ? (b){
              app.prefs.stupidCrash = b;
              if(b){
                FlutterError.onError = (err) {
                  app.stupid!.crash(Crash(
                    error: err.exceptionAsString(),
                    stack: err.stack?.toString() ?? "Not given",
                    version: app.package.version
                  ));
                  FlutterError.presentError(err);
                };
              }else{
                FlutterError.onError = FlutterError.presentError;
              }
            } : null,
            title: Text(app.locale.stupidCrash),
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
            onPressed: () => Navigator.pushNamed(context, "/intro"),
            style: const ButtonStyle(
              alignment: Alignment.centerLeft
            ),
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Text(
                app.locale.showIntro,
                style: Theme.of(context).textTheme.titleMedium,
              )
            )
          ),
          const Divider(),
          AboutListTile(
            applicationName: "SWAssistant",
            applicationIcon: const Image(image: AssetImage("assets/SWAssistant.png"), height: 64,),
            applicationLegalese: app.locale.aboutText,
            applicationVersion: app.package.version,
          )
        ],
      )
    );
  }
}