import 'package:firebase_analytics/observer.dart';
import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/screens/EditableList.dart';
import 'package:swassistant/ui/screens/Settings.dart';

void main() =>
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
        snackBarTheme: SnackBarThemeData(
          behavior: SnackBarBehavior.floating,
          shape: RoundedRectangleBorder(),
        ),
        inputDecorationTheme: InputDecorationTheme(
          border: OutlineInputBorder(),
          contentPadding: EdgeInsets.symmetric(vertical: 5, horizontal: 15)
        ),
        brightness: Brightness.light
      );
    else{
      if(SW.of(context).getPreference(preferences.amoled, false))
        theme = ThemeData(
          backgroundColor: Colors.black,
          canvasColor: Colors.black,
          shadowColor: Colors.grey.shade800,
          scaffoldBackgroundColor: Colors.black,
          cardColor: Color.fromARGB(255, 15, 15, 15),
          snackBarTheme: SnackBarThemeData(
            backgroundColor: Color.fromARGB(255, 15, 15, 15),
            actionTextColor: Colors.amberAccent,
            contentTextStyle: TextStyle(
              color: Colors.white
            ),
            shape: RoundedRectangleBorder(),
            behavior: SnackBarBehavior.floating
          ),
          primaryColor: Colors.red,
          accentColor: Colors.lightBlue,
          bottomSheetTheme: BottomSheetThemeData(
            backgroundColor: Colors.black,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(10.0),
                topRight: Radius.circular(10.0)
              )
            )
          ),
          inputDecorationTheme: InputDecorationTheme(
            border: OutlineInputBorder(),
            contentPadding: EdgeInsets.symmetric(vertical: 5, horizontal: 15)
          ),
          brightness: Brightness.dark
        );
      else
        theme = ThemeData(
          primaryColor: Colors.red,
          accentColor: Colors.lightBlue,
          snackBarTheme: SnackBarThemeData(
            shape: RoundedRectangleBorder(),
            behavior: SnackBarBehavior.floating
          ),
          bottomSheetTheme: BottomSheetThemeData(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(10.0),
                topRight: Radius.circular(10.0)
              )
            )
          ),
          inputDecorationTheme: InputDecorationTheme(
            border: OutlineInputBorder(),
            contentPadding: EdgeInsets.symmetric(vertical: 5, horizontal: 15)
          ),
          brightness: Brightness.dark
        );
    }
    return MaterialApp(
      title: 'SWAssistant',
      theme: theme,
      navigatorObservers: [
        if(SW.of(context).getPreference(preferences.analytics, true) && SW.of(context).firebaseAvailable)
          FirebaseAnalyticsObserver(analytics: SW.of(context).analytics),
        SW.of(context).observatory
      ],
      initialRoute: "/characters",
      routes: {
        // "/gm" : (context) => GMMode(),
        "/characters" : (context) => EditableList(EditableList.character),
        "/minions" : (context) => EditableList(EditableList.minion),
        "/vehicles" : (context) => EditableList(EditableList.vehicle),
        // "/download" : (context) => Downloads(),
        // "/dice" : (context) => Dice(),
        // "/guide" : (context) => Guide(),
        "/settings" : (context) => Settings(updateTopLevel: () => setState((){}),),
      },
    );
  }
}


class Observatory extends NavigatorObserver{

  List<Route> routeHistory = [];

  @override
  void didPush(Route route, Route previousRoute) {
    routeHistory.add(route);
    super.didPush(route, previousRoute);
  }

  @override
  void didPop(Route route, Route previousRoute) {
    routeHistory.removeLast();
    super.didPop(route, previousRoute);
  }

  @override
  void didRemove(Route route, Route previousRoute) {
    var beginIndex = routeHistory.indexOf(route);
    var endIndex = routeHistory.indexOf(previousRoute);
    if((endIndex == -1 && beginIndex != -1) || beginIndex -1 == endIndex)
      routeHistory.remove(route);
    else if(endIndex != -1 && beginIndex != -1)
      routeHistory.removeRange(beginIndex,endIndex);
    super.didRemove(route, previousRoute);
  }

  @override
  void didReplace({Route newRoute, Route oldRoute}) {
    if(routeHistory.contains(oldRoute))
      routeHistory[routeHistory.indexOf(oldRoute)] = newRoute;
    super.didReplace(newRoute: newRoute, oldRoute: oldRoute);
  }

  //Returns the route if it is in history (it), otherwise it return null.
  //It preferes the route give, then settings, then the name
  Route containsRoute({Route route, RouteSettings settings, String name}){
    if(route != null)
      return routeHistory.contains(route) ? route : null;
    if(settings != null)
      return routeHistory.firstWhere(
        (element) => element.settings == settings,
        orElse: () => null
      );
    if(name != null)
      return routeHistory.firstWhere(
        (element) => element.settings.name == name,
        orElse: () => null
      );
    return null; 
  }
}