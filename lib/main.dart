import 'package:flutter/material.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/screens/EditableList.dart';

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