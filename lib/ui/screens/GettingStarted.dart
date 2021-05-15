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

  Widget currentBody(){
    if (screen == 0){
      return Text("HI");
    }
    return Text("UMMMS");
  }
}