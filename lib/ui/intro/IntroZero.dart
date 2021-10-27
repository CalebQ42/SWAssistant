import 'dart:io';

import 'package:file_picker/file_picker.dart';
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

class IntroZero extends StatelessWidget{
  @override
  Widget build(BuildContext context) =>
    IntroScreen(
      nextScreen: IntroOne(),
      prevScreenAction: () =>
        SW.of(context).postInit(context).whenComplete(() =>
          Navigator.of(context).pushNamedAndRemoveUntil("/characters", (route) => false)
        ),
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
              onPressed: () {
                tryOldImport(context).then((value) {
                  if (value != 0)
                    return;
                  Bottom(
                    buttons: (context) => [
                      TextButton(
                        child: Text(MaterialLocalizations.of(context).continueButtonLabel),
                        onPressed: (){
                          Navigator.pop(context);
                          manualImport(context);
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
                });
              }
            )
          ]
        )
      )
    );

  Future<int> tryOldImport(BuildContext context) async{
    //First try to access the files directly. This won't work on newer versions of Android, but on older version SHOULD work fine.
    var extStorage = Directory("/sdcard");
    if (extStorage.existsSync()){
      var charsFold = Directory(extStorage.path + "/SWChars");
      if (charsFold.existsSync()){
        print("I'm Super Alive!");
        var fils = charsFold.listSync();
        print(fils.length);
        if (fils.length > 0){
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
            ed.loc = "";
            ed.findNexID(SW.of(context));
            ed.save(app: SW.of(context));
            SW.of(context).add(ed);
          }
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(AppLocalizations.of(context)!.introPage0ImportSuccess(fils.length))
            )
          );
          return fils.length;
        }
      }
    }
    return 0;
  }

  void manualImport(BuildContext context){
    var app = SW.of(context);
    var message = ScaffoldMessenger.of(context);
    var locs = AppLocalizations.of(context)!;
    //possibly show loading dialog.
    FilePicker.platform.pickFiles(allowMultiple: true).then((value) {
      if(value == null)
        return;
      if (value.files.isEmpty){
        message.showSnackBar(
          SnackBar(content: Text(locs.introPage0ImportNone))
        );
        return;
      }
      for(var f in value.files){
        var fil = File(f.path!);
        Editable ed;
        switch (f.extension){
          case "swminion":
            ed = Minion.load(fil, app);
            break;
          case "swcharacter":
            ed = Character.load(fil, app);
            break;
          case "swvehicle":
            ed = Vehicle.load(fil, app);
            break;
          default:
            continue;
        }
        ed.loc = "";
        ed.findNexID(app);
        ed.save(app: app);
        app.add(ed);
      }
      message.showSnackBar(
        SnackBar(
          content: Text(locs.introPage0ImportSuccess(value.files.length))
        )
      );
    });
  }
}