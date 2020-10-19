import 'package:firebase_analytics/firebase_analytics.dart';
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
      navigatorObservers: [
        // if(SW.of(context).getPreference(preferences.analytics, true) && SW.of(context).getPreference(preferences.firebase, true) &&
        //     SW.of(context).firebaseAvailable)
        //   FirebaseAnalyticsObserver(analytics: FirebaseAnalytics()),
        // Observatory()
      ],
      initialRoute: "/characters",
      routes: {
        "/characters" : (context) => EditableList(EditableList.character),
        "/vehicles" : (context) => EditableList(EditableList.vehicle),
        "/minions" : (context) => EditableList(EditableList.minion),
        "/settings" : (context) => Settings(updateTopLevel: () => setState((){}),)
      },
    );
  }
}

// class Observatory extends NavigatorObserver{

//   List<Route> routeHistory = List();

//   @override
//   void didPush(Route route, Route previousRoute) {
//     var oldIndex = routeHistory.indexWhere(
//       (element) =>
//         element.settings.name == route.settings.name &&
//         element.settings.arguments == route.settings.name
//     );
//     if(oldIndex != -1){
//       print("Old Route Found");
//       navigator.removeRoute(routeHistory[oldIndex]);
//       routeHistory.removeAt(oldIndex);
//     }
//     routeHistory.add(route);
//     super.didPush(route, previousRoute);
//   }

//   @override
//   void didPop(Route route, Route previousRoute) {
//     routeHistory.removeLast();
//     super.didPop(route, previousRoute);
//   }

//   @override
//   void didRemove(Route route, Route previousRoute) {
//     routeHistory.remove(route);
//     super.didRemove(route, previousRoute);
//   }

//   @override
//   void didReplace({Route newRoute, Route oldRoute}) {
//     if(routeHistory.contains(oldRoute))
//       routeHistory[routeHistory.indexOf(oldRoute)] = newRoute;
//     super.didReplace(newRoute: newRoute, oldRoute: oldRoute);
//   }
// }