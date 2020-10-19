import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/screens/EditableList.dart';
import 'package:swassistant/ui/screens/Settings.dart';

void main() =>
  //TODO: On first start, show an onboarding UI with options for analytics & restore profiles from old versions.
  SW.initialize().then(
    (value) {
      if(value.firebaseAvailable && value.getPreference(preferences.crashlytics, true) == true &&
          value.getPreference(preferences.firebase, true))
        FlutterError.onError = FirebaseCrashlytics.instance.recordFlutterError;
      runApp(SWWidget(child: SWApp(), app: value));
    }
  );

class SWApp extends StatefulWidget{
  @override
  State<StatefulWidget> createState() => SWAppState();
}

class SWAppState extends State {
  @override
  Widget build(BuildContext context) {
    ThemeData theme;
    if(SW.of(context).getPreference(preferences.light, false))
      theme = ThemeData(
        primaryColor: Colors.blue,
        accentColor: Colors.redAccent,
        bottomSheetTheme: BottomSheetThemeData(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(10.0),
              topRight: Radius.circular(10.0)
            )
          )
        ),
        inputDecorationTheme: InputDecorationTheme(
          border: OutlineInputBorder()
        ),
        brightness: Brightness.light
      );
    else
      theme = ThemeData(
        primaryColor: Colors.red,
        accentColor: Colors.lightBlue,
        bottomSheetTheme: BottomSheetThemeData(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(10.0),
              topRight: Radius.circular(10.0)
            )
          )
        ),
        inputDecorationTheme: InputDecorationTheme(
          border: OutlineInputBorder()
        ),
        brightness: Brightness.dark
      );
    return MaterialApp(
      title: 'SWAssistant',
      theme: theme,
      home: EditableList(EditableList.character),
      routes: {
        "/characters" : (context) => EditableList(EditableList.character),
        "/vehicles" : (context) => EditableList(EditableList.vehicle),
        "/minions" : (context) => EditableList(EditableList.minion),
        "/settings" : (context) => Settings(updateTopLevel: () => setState((){}),)
      },
    );
  }
}