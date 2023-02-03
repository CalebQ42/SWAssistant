import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/sw.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/intro/intro_one.dart';
import 'package:swassistant/ui/intro/intro.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:uuid/uuid.dart';

class IntroZero extends StatelessWidget{

  const IntroZero({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    IntroScreen(
      nextScreen: const IntroOne(),
      prevScreenAction: SW.of(context).devMode ? () {      
        var app = SW.of(context);
        app.prefs.setBool(preferences.driveFirstLoad, true);
        app.prefs.setBool(preferences.firstStart, false);
        if(kIsWeb) app.prefs.setBool(preferences.googleDrive, true);
        Navigator.pushNamedAndRemoveUntil(
          context, SW.of(context).getPref(preferences.startingScreen), (route) => false);
      } : null,
      defPrevScreen: false,
      prevScreenIcon: const Icon(Icons.exit_to_app),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            AppLocalizations.of(context)!.introWelcome,
            style: Theme.of(context).textTheme.headlineMedium
          ),
          const SizedBox(height: 5),
          Text(
            AppLocalizations.of(context)!.introPage0Line1,
            textAlign: TextAlign.justify,
          ),
          const SizedBox(height:10),
          if(kIsWeb) Text(
            AppLocalizations.of(context)!.introPage0WebNotice,
            textAlign: TextAlign.justify,
          ),
        ]
      )
    );

  int oldImport(BuildContext context){
    var extStorage = Directory("/sdcard");
    if (extStorage.existsSync()){
      var charsFold = Directory("${extStorage.path}/SWChars");
      if (charsFold.existsSync()){
        var fils = charsFold.listSync();
        if (fils.isNotEmpty){
          var uuid = const Uuid();
          for (var f in fils){
            Editable ed;
            if (f.path.endsWith(".swminion")){
              ed = Minion.load(f, SW.of(context));
            }else if (f.path.endsWith(".swcharacter")){
              ed = Character.load(f, SW.of(context));
            }else if (f.path.endsWith(".swvehicle")){
              ed = Vehicle.load(f, SW.of(context));
            }else{
              continue;
            }
            ed.uid = uuid.v4();
            ed.loc = "";
            ed.save(app: SW.of(context));
            SW.of(context).add(ed);
          }
          ScaffoldMessenger.of(context).clearSnackBars();
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