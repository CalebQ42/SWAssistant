import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/intro/IntroScreen.dart';

class IntroZero extends StatelessWidget{
  @override
  Widget build(BuildContext context) =>
    IntroScreen(
      nextScreenAction: () =>
        Navigator.of(context).pushNamed("/intro1"),
      prevScreenAction: () =>
        SW.of(context).postInit(context).whenComplete(() =>
          Navigator.of(context).pushNamedAndRemoveUntil("/characters", (route) => false)
        ),
      prevScreenIcon: Icon(Icons.exit_to_app),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            "Welcome!",
            style: Theme.of(context).textTheme.headline4
          ),
          Text(
            "Thank you for trying SWAssistant. Before we get started, you first have to set up some preferences.",
            textAlign: TextAlign.justify,
          ),
          Text(
            "Additionally, if you've used this app before, we will need to import your old profiles first.",
            textAlign: TextAlign.justify,
          ),
          ElevatedButton(
            child: Text("Import Profiles"),
            onPressed: (){
              //TODO: import
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text("Importing... (Doesn't work yet)"))
              );
            },
          )
        ],
      ),
    );
}