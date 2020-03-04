import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';

import 'ui/Screens/EditingEditable.dart';
import 'profiles/Character.dart';
import 'profiles/Minion.dart';
import 'profiles/Vehicle.dart';
import 'ui/Common.dart';

void main(){
  var app = SW();
  app.initialize().whenComplete(() => runApp(SWApp(app)));
}

class SWApp extends StatelessWidget {
  SW app;
  SWApp(this.app);
  @override
  Widget build(BuildContext context) {
    ThemeData theme;
    //if(darkMode){
      theme = ThemeData.dark().copyWith(
          primaryColor: Colors.red,
          accentColor: Colors.blueAccent,
      );
    //}else{
    //theme = ThemeData.light().copyWith(
    //    primaryColor: Colors.red,
    //    accentColor: Colors.blueAccent,
    //);
    return MaterialApp(
      title: 'Flutter Demo',
      theme: theme,
      home: Home(),
      routes: {
        "/characters" : EditingEditable(Character(name:"Character 1", id: 0)).build,
        "/vehicles" : EditingEditable(Vehicle(name:"Vehicle 1", id: 0)).build,
        "/minions" : EditingEditable(Minion(name:"Minion 1", id: 0)).build,
      },
    );
  }
}

class Home extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    int index = 0;
    return Scaffold(
      appBar: AppBar(title: Text("Hi Wold")),
      drawer: SWDrawer(),
      floatingActionButton: FloatingActionButton(onPressed: (){
        switch(index){
          case 0:
            Navigator.pushNamed(context,"/characters");
            index = 1;
            break;
          case 1:
            Navigator.pushNamed(context,"/vehicles");
            index = 2;
            break;
          case 2:
            Navigator.pushNamed(context,"/minion");
            index = 3;
            break;
          case 3:
            Navigator.pushNamed(context,"/");
            index = 0;
            break;
        }
      }),
    );
  }

}
