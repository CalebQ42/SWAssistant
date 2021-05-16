import 'package:flutter/material.dart';

class GettingStarted extends StatefulWidget{
  @override
  State<StatefulWidget> createState() =>
    StartingState();
}

class StartingState extends State<GettingStarted>{
  int screen = 0;
  @override
  Widget build(BuildContext context) =>
    Scaffold(
      // backgroundColor: Theme.of(context).primaryColor,
      body: currentBody(),
    );

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
                  style: Theme.of(context).textTheme.headline3,
                  textAlign: TextAlign.center,
                ),
                Text(
                  "Welcome to SWAssistant. Thanks for giving it a try. If you've used this app before, you MUST import your old profiles to have them work. Otherwise, continue on.",
                  textAlign: TextAlign.justify,
                ),
                Container(
                  height: 10,
                ),
                ElevatedButton(
                  onPressed: (){},
                  child: Text("Import Profiles"),
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
              shape: BeveledRectangleBorder(
                borderRadius: BorderRadius.all(Radius.circular(30.0))
              ),
              onPressed: (){},
            )
          ),
        ),
        Align(
          alignment: Alignment.bottomLeft,
          child: Padding(
            padding: EdgeInsets.all(10.0),
            child: FloatingActionButton(
              child: Icon(Icons.arrow_back),
              shape: BeveledRectangleBorder(
                borderRadius: BorderRadius.all(Radius.circular(30.0))
              ),
              onPressed: (){},
            )
          ),
        )
      ],
    );

  Widget currentBody(){
    if (screen == 0){
      return screenOne(context);
    }
    return Text("UMMMS");
  }
}