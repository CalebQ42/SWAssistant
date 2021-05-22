import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';

class GettingStarted extends StatefulWidget{
  final SW app;
  final Function() onEnd;

  GettingStarted({required this.app, required this.onEnd});
  
  @override
  State<StatefulWidget> createState() =>
    StartingState(app: app, onEnd: onEnd);
}

class StartingState extends State<GettingStarted>{

  int screen = 0;
  final SW app;
  final Function() onEnd;

  StartingState({required this.app, required this.onEnd});

  @override
  Widget build(BuildContext context) =>
    Scaffold(
      backgroundColor: Theme.of(context).primaryColor,
      body: currentBody(),
    );

  Widget currentBody(){
    switch (screen){
      case 0:
        return screenOne(context);
      case 1:
        return screenTwo(context);
    }
    return Text("UMMM");
  }

  Widget screenOne(BuildContext context) =>
    Stack(
      children: [
        Center(
          child: Padding(
            padding: EdgeInsets.symmetric(
              horizontal: 20.0
            ),
            child: Column(
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
                    //TODO: ask for files permission and then import
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text("Importing..."))
                    );
                  },
                )
              ],
            )
          )
        ),
        Align(
          alignment: Alignment.bottomRight,
          child:  Padding(
            padding: EdgeInsets.all(10.0),
            child: FloatingActionButton(
              child: Icon(Icons.arrow_forward),
              onPressed: () =>
                setState((){
                  screen++;
                }),
            )
          ),
        ),
        if(app.devMode) Align(
          alignment: Alignment.bottomLeft,
          child:  Padding(
            padding: EdgeInsets.all(10.0),
            child: FloatingActionButton(
              child: Icon(Icons.exit_to_app),
              onPressed: () =>
                onEnd()
            )
          ),
        ),
      ],
    );

  Widget screenTwo(BuildContext context) =>
    Stack(
      children: [
        Center(
          child: Padding(
            padding: EdgeInsets.symmetric(
              horizontal: 20.0
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  "We have to set up some preferences before we start.",
                ),
                Text(
                  "Firebase is a framework by Google that allows for quick and easy development. Disabling it will disable all other Firebase plugins (Crashlytics) and will remove the ability to download pre-made profiles."
                ),
                Text(
                  "Crashlytics is a Firebase plugin that provides automatic, anonymous crash reporting. Highly recommeded to keep enabled to allow for quick bug fixes. If disabled, don't expect any bugs you run into to get fixed."
                ),
              ]
            )
          )
        )
      ]
    );
}