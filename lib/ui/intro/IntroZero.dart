import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/intro/IntroOne.dart';
import 'package:swassistant/ui/intro/IntroScreen.dart';

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
              "Welcome!",
              style: Theme.of(context).textTheme.headline4
            ),
            SizedBox(height: 5),
            Text(
              "Thank you for trying SWAssistant. Before we get started, you first have to set up some preferences.",
              textAlign: TextAlign.justify,
            ),
            SizedBox(height:10),
            Text(
              "Additionally, if you used a previous version of this app, we will need to import your old profiles first.",
              textAlign: TextAlign.justify,
            ),
            SizedBox(height:5),
            ElevatedButton(
              child: Text("Import Profiles"),
              onPressed: (){
                //TODO: import
                ScaffoldMessenger.of(context).showSnackBar(
                  SnackBar(content: Text("Importing... (Doesn't work yet)"))
                );
              }
            )
          ]
        )
      )
    );
}