import 'package:flutter/material.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/screens/EditableList.dart';

import 'ui/screens/EditingEditable.dart';
import 'ui/Common.dart';

void main(){
  var app = SW();
  app.initialize().whenComplete(() => runApp(SWApp(app)));
}

class SWApp extends StatelessWidget {
  final SW app;
  SWApp(this.app);
  @override
  Widget build(BuildContext context) {
    ThemeData theme;
    if(app.prefs.getBool(preferences.light)!= null && app.prefs.getBool(preferences.light))
      theme = ThemeData.light().copyWith(
        primaryColor: Colors.blue,
        accentColor: Colors.redAccent,
      );
    else
      theme = ThemeData.dark().copyWith(
          primaryColor: Colors.red,
          accentColor: Colors.blueAccent,
      );
    return MaterialApp(
      title: 'SWAssistant',
      theme: theme,
      home: EditableList(app,EditableList.character),
      routes: {
        "/characters" : (context)=>EditableList(app, EditableList.character),
        "/vehicles" : (context)=>EditableList(app,EditableList.vehicle),
        "/minion" : (context)=>EditableList(app, EditableList.minion),
      },
    );
  }
}

class Home extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    int index = 0;
    return Scaffold(
      appBar: AppBar(title: Text("Hi World")),
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
            index = 0;
            break;
        }
      })
    );
  }
}
