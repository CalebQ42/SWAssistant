import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:path_provider/path_provider.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/intro/IntroOne.dart';
import 'package:swassistant/ui/intro/IntroScreen.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/BottomSheetTemplate.dart';

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
              onPressed: (){
                getExternalStorageDirectory()
                //TODO: Check API to see if manual import is necessary. If not, I should have access to the files directly.
                Bottom(
                  buttons: (context) => [
                    TextButton(
                      child: Text(MaterialLocalizations.of(context).continueButtonLabel),
                      onPressed: (){
                        Navigator.pop(context);
                        getSWChars(context);
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
                        Text(AppLocalizations.of(context)!.introPage0ManualImportLine2),
                        Container(height: 5),
                        Text(AppLocalizations.of(context)!.introPage0ManualImportLine3),
                        Container(height: 5),
                        Text(AppLocalizations.of(context)!.introPage0ManualImportLine4),
                      ],
                    )
                ).show(context);
              }
            )
          ]
        )
      )
    );

  void getSWChars(BuildContext context){
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