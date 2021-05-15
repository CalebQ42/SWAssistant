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
      backgroundColor: Theme.of(context).primaryColor,
      body: currentBody(),
    );

  Widget screenOne(BuildContext context) =>
    Stack(
      children: [
        Align(
          alignment: Alignment.bottomRight,
          child: FloatingActionButton(
            onPressed: ()=>
              setState((){
                screen++;
              }),
            child: Icon(Icons.arrow_forward),
          ),
        ),
        Align(
          alignment: Alignment.bottomLeft,
          child: FloatingActionButton(
            onPressed: (){},
            child: Icon(Icons.arrow_back),
          ),
        )
      ],
    );

  Widget currentBody(){
    if (screen == 0){
      return Text("HI");
    }
    return Text("UMMMS");
  }
}