import 'dart:io';

import 'package:device_info_plus/device_info_plus.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/intro/IntroOne.dart';
import 'package:swassistant/ui/intro/IntroScreen.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';
import 'package:uuid/uuid.dart';

class IntroZero extends StatelessWidget{
  @override
  Widget build(BuildContext context) =>
    IntroScreen(
      nextScreen: IntroOne(),
      prevScreenAction: SW.of(context).devMode ? () =>
        SW.of(context).postInit(context).whenComplete(() =>
          Navigator.of(context).pushNamedAndRemoveUntil("/characters", (route) => false)
        ) : null,
      defPrevScreen: false,
      prevScreenIcon: Icon(Icons.exit_to_app),
      child: ConstrainedBox(
        constraints: BoxConstraints(maxWidth: 500),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              AppLocalizations.of(context)!.introWelcome,
              style: Theme.of(context).textTheme.headline4
            ),
            SizedBox(height: 5),
            Text(
              AppLocalizations.of(context)!.introPage0Line1,
              textAlign: TextAlign.justify,
            ),
            SizedBox(height:10),
            Text(
              AppLocalizations.of(context)!.introPage0Line2,
              textAlign: TextAlign.justify,
            ),
            SizedBox(height:5),
            ElevatedButton(
              child: Text(AppLocalizations.of(context)!.introPage0ImportButton),
              onPressed: () async {
                //Android 10+ prevents full access to the filesystem. If older, we can access old profiles directly.
                //Otherwise, we need to have the user manually select the profiles.
                if (Platform.isAndroid && ((await DeviceInfoPlugin().androidInfo).version.sdkInt ?? 29) >= 29)
                  Bottom(
                    buttons: (context) => [
                      TextButton(
                        child: Text(MaterialLocalizations.of(context).continueButtonLabel),
                        onPressed: (){
                          Navigator.pop(context);
                          SW.of(context).manualImport(context);
                        },
                      ),
                      TextButton(
                        child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
                        onPressed: () =>
                          Navigator.pop(context),
                      )
                    ],
                    child: (context) =>
                      Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          Text(
                            AppLocalizations.of(context)!.introPage0ManualImport,
                            textAlign: TextAlign.center,
                            style: Theme.of(context).textTheme.headline5,
                          ),
                          Container(height: 10),
                          Text(
                            AppLocalizations.of(context)!.introPage0ManualImportLine0,
                            textAlign: TextAlign.justify,
                          ),
                          Container(height: 10),
                          Text(AppLocalizations.of(context)!.introPage0ManualImportLine1),
                          Container(height: 5),
                          Text(AppLocalizations.of(context)!.introPage0ManualImportLine1a),
                          Container(height: 5),
                          Text(AppLocalizations.of(context)!.introPage0ManualImportLine2),
                          Container(height: 5),
                          Text(AppLocalizations.of(context)!.introPage0ManualImportLine3),
                          Container(height: 5),
                          Text(AppLocalizations.of(context)!.introPage0ManualImportLine4),
                        ],
                      )
                  ).show(context);
                else{
                  var num = oldImport(context);
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                    content: Text(AppLocalizations.of(context)!.importSuccess(num)),
                  ));
                }
              }
            )
          ]
        )
      )
    );

  int oldImport(BuildContext context){
    var extStorage = Directory("/sdcard");
    if (extStorage.existsSync()){
      var charsFold = Directory(extStorage.path + "/SWChars");
      if (charsFold.existsSync()){
        var fils = charsFold.listSync();
        if (fils.length > 0){
          var uuid = Uuid();
          for (var f in fils){
            Editable ed;
            if (f.path.endsWith(".swminion"))
                ed = Minion.load(f, SW.of(context));
            else if (f.path.endsWith(".swcharacter"))
                ed = Character.load(f, SW.of(context));
            else if (f.path.endsWith(".swvehicle"))
                ed = Vehicle.load(f, SW.of(context));
            else
              continue;
            ed.uid = uuid.v4();
            ed.loc = "";
            ed.save(app: SW.of(context));
            SW.of(context).add(ed);
          }
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(AppLocalizations.of(context)!.importSuccess(fils.length))
            )
          );
          return fils.length;
        }
      }
    }
    return 0;
  }
}