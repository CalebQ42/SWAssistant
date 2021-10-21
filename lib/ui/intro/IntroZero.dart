import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
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
                //TODO: Check API to see if manual import is necessary.
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
                    Text(
                      AppLocalizations.of(context)!.introPage0ImportMessage,
                      textAlign: TextAlign.justify,
                    )
                ).show(context);
              }
            )
          ]
        )
      )
    );

  void getSWChars(BuildContext context){
    //possibly show loading dialog.
    FilePicker.platform.pickFiles(allowMultiple: true).then((value) {
      if(value == null){
        print("ret");
        return;
      }
      if (value.files.isEmpty) {
        //TODO: Dialog about you failing.
      }
      for(var f in value.files){
        //TODO: import
      }
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(AppLocalizations.of(context)!.introPage0ImportSuccess(value.files.length))
        )
      );
    });
  }
}