import 'package:flutter/material.dart';

import 'ui/EditingEditable.dart';
import 'profiles/Character.dart';
import 'profiles/Minion.dart';
import 'profiles/Vehicle.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    int index = 0;
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.red,
        primaryColor: Colors.red,
        accentColor: Colors.blueAccent,
        brightness: Brightness.dark,
      ),
      home: Scaffold(
        appBar: AppBar(title: Text("Hi Wold")),
        floatingActionButton: FloatingActionButton(onPressed: (){
          switch(index){
            case 0:
              Navigator.of(context).pushNamed("/character");
              index = 1;
              break;
            case 1:
              Navigator.of(context).pushNamed("/vehicle");
              index = 2;
              break;
            case 2:
              Navigator.of(context).pushNamed("/minion");
              index = 3;
              break;
            case 3:
              Navigator.of(context).pushNamed("/");
              index = 0;
              break;
          }
        }),
      ),
      routes: {
        "/character" : EditingEditable(Character(name:"Character 1", id: 0)).build,
        "/vehicle" : EditingEditable(Vehicle(name:"Vehicle 1", id: 0)).build,
        "/minion" : EditingEditable(Minion(name:"Minion 1", id: 0)).build,
      },
    );
  }
}
