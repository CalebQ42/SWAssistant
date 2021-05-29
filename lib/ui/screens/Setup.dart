import 'dart:io';

import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:swassistant/SW.dart';

class Setup extends StatelessWidget{

  final int screen;

  Setup({required this.screen});

  @override
  Widget build(BuildContext context) =>
    Scaffold(
      backgroundColor: Theme.of(context).primaryColor,
      body: Stack(
        children: [
          Center(
            child: Padding(
              padding: EdgeInsets.symmetric(
                horizontal: 20.0
              ),
              child: currentBody(context),
            )
          ),
          if(SW.of(context).devMode || screen > 0) Align(
            alignment: Alignment.bottomLeft,
            child:  Padding(
              padding: EdgeInsets.all(10.0),
              child: IconButton(
                icon: screen > 0 ? Icon(Icons.arrow_back) : Icon(Icons.exit_to_app),
                onPressed: () {
                  if(screen == 0)
                    //TODO: Add a loading indicator here
                    SW.of(context).postInit().then(
                      (value) =>
                        Navigator.pushNamedAndRemoveUntil(context, "/characters", (route) => false) //Should remove all setup screens
                    );
                  else
                    Navigator.pop(context);
                }
              )
            ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.arrow_forward),
        onPressed: (){
          if(screen < 2)
            Navigator.pushNamed(context, "/setup" + (screen + 1).toString());
          else //TODO: Add a loading indicator here
            SW.of(context).postInit().then(
              (_) =>  
                Navigator.pushNamedAndRemoveUntil(context, "/characters", (route) => false) //Should remove all setup screens
            );
        },
      ),
    );

  Widget currentBody(BuildContext context){
    switch (screen){
      case 0:
        return screenZero(context);
      case 1:
        return screenOne(context);
    }
    return Text("UMMM");
  }

  Widget screenZero(BuildContext context) =>
    Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(
          "Welcome!",
          style: Theme.of(context).textTheme.headline4?.copyWith(color: Colors.black),
          textAlign: TextAlign.center,
        ),
        Text(
          "Welcome to SWAssistant. Thanks for giving it a try. If you've used this app before, you MUST import your old profiles to have them work. Otherwise, continue on.",
          textAlign: TextAlign.justify,
          style: Theme.of(context).textTheme.bodyText2?.copyWith(color: Colors.black)
        ),
        Container(
          height: 5,
        ),
        ElevatedButton(
          child: Text("Import Profiles"),
          onPressed: (){
            Permission.manageExternalStorage.status.then((value) {
              if (value.isRestricted || value.isDenied)
                Permission.manageExternalStorage.request().then((value) {
                  if (value.isGranted){
                    var dir = Directory("/sdcard/SWChars");
                    if (!dir.existsSync())
                      dir.createSync();
                    print(dir.path);
                  }
                  return null;
                });
              else if (value.isGranted){
                var dir = Directory("/sdcard/SWChars");
                if (!dir.existsSync())
                  dir.createSync();
                print(dir.path);
              }else
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(
                    content: Text("Failed: " + value.toString())
                  )
                );
            });
            //TODO: ask for files permission and then import
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(content: Text("Importing..."))
            );
          },
        )
      ]
    );

  Widget screenOne(BuildContext context) => //TODO: Move to a stateful to show pref switch changes
    Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(
          "Preferences",
          style: Theme.of(context).textTheme.headline4?.copyWith(color: Colors.black),
          textAlign: TextAlign.center,
        ),
        Center(
          child: SwitchListTile(
            title: Text("Firebase",
              style: Theme.of(context).textTheme.headline6?.copyWith(color: Colors.black)
            ),
            value: true,
            onChanged: (b) =>
              b = false
          ),
        ),
        Text(
          "Firebase is a framework by Google that allows for quick and easy development. Disabling it will disable all other Firebase plugins (Crashlytics) and will remove the ability to download pre-made profiles.",
          textAlign: TextAlign.justify,
          style: Theme.of(context).textTheme.bodyText2?.copyWith(color: Colors.black)
        ),
        Center(
          child: SwitchListTile(
            title: Text("Crashlytics",
              style: Theme.of(context).textTheme.headline6?.copyWith(color: Colors.black)
            ),
            dense: true,
            value: true,
            onChanged: (b) =>
              b = false
          ),
        ),
        Text(
          "Crashlytics is a Firebase plugin that provides automatic, anonymous crash reporting. Highly recommeded to keep enabled to allow for quick bug fixes. If disabled, don't expect any bugs you run into to get fixed.",
          textAlign: TextAlign.justify,
          style: Theme.of(context).textTheme.bodyText2?.copyWith(color: Colors.black)
        ),
      ]
    );

  Widget screenTwo(BuildContext context) =>
    Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(
          "Get READY",
          style: Theme.of(context).textTheme.headline4?.copyWith(color: Colors.black),
          textAlign: TextAlign.center,
        ),
      ]
    );
}