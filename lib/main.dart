import 'package:flutter/material.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/screens/EditableList.dart';

void main() async{
  var app = await SW.initialize(SWApp());
  runApp(app);
}

class SWApp extends StatelessWidget {
  SWApp();
  @override
  Widget build(BuildContext context) {
    ThemeData theme;
    if(SW.of(context).prefs.getBool(preferences.light)!= null && SW.of(context).prefs.getBool(preferences.light))
      theme = ThemeData.light().copyWith(
        primaryColor: Colors.blue,
        accentColor: Colors.redAccent,
        bottomSheetTheme: BottomSheetThemeData(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20.0),
              topRight: Radius.circular(20.0)
            )
          )
        )
      );
    else
      theme = ThemeData.dark().copyWith(
        primaryColor: Colors.red,
        accentColor: Colors.blueAccent,
        bottomSheetTheme: BottomSheetThemeData(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20.0),
              topRight: Radius.circular(20.0)
            )
          )
        )
      );
    return MaterialApp(
      title: 'SWAssistant',
      theme: theme,
      home: EditableList(EditableList.character),
      routes: {
        "/characters" : (context) => EditableList(EditableList.character),
        "/vehicles" : (context) => EditableList(EditableList.vehicle),
        "/minions" : (context) => EditableList(EditableList.minion),
      },
    );
  }
}