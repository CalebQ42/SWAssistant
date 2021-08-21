import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/intro/IntroZero.dart';
import 'package:swassistant/ui/screens/DiceRoller.dart';
import 'package:swassistant/ui/screens/EditableList.dart';
import 'package:swassistant/ui/screens/Settings.dart';

Future<void> main() async =>
  SW.baseInit().then(
    (app) =>
      runApp(SWWidget(
        child: SWApp(
          init: (app.devMode || app.getPreference(preferences.firstStart, true)) ? "/intro" : null
        ),
        app: app
      ))
  );

class SWApp extends StatefulWidget{

  final String? init;

  SWApp({this.init});

  @override
  State<StatefulWidget> createState() => SWAppState(init: init);
}

class SWAppState extends State {

  String? init;

  SWAppState({this.init});

  @override
  Widget build(BuildContext context) {
    SW.of(context).topLevelUpdate = () => setState(() {});
    var snackTheme = SnackBarThemeData(
      behavior: SnackBarBehavior.floating,
    );
    var inputTheme = InputDecorationTheme(
      border: OutlineInputBorder(),
    );
    var bottomSheetTheme = BottomSheetThemeData(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(25)
      )
    );
    return MaterialApp(
      title: 'SWAssistant',
      themeMode: SW.of(context).getPreference(preferences.forceLight, false) ?
        ThemeMode.light : SW.of(context).getPreference(preferences.forceDark, false) ?
        ThemeMode.dark : ThemeMode.system,
      theme: ThemeData(
        primaryColor: Colors.blue,
        // accentColor: Colors.redAccent,
        bottomSheetTheme: bottomSheetTheme,
        inputDecorationTheme: inputTheme,
        snackBarTheme: snackTheme
      ),
      darkTheme: SW.of(context).getPreference(preferences.amoled, false) ? 
        ThemeData( //Amoled Theme
          backgroundColor: Colors.black,
          canvasColor: Colors.black,
          shadowColor: Colors.grey.shade800,
          scaffoldBackgroundColor: Colors.black,
          cardColor: Color.fromARGB(255, 15, 15, 15),
          snackBarTheme: snackTheme.copyWith(
            backgroundColor: Color.fromARGB(255, 15, 15, 15),
            actionTextColor: Colors.amberAccent,
            contentTextStyle: TextStyle(
              color: Colors.white
            ),
          ),
          primaryColor: Colors.red,
          // accentColor: Colors.lightBlueAccent,
          bottomSheetTheme: bottomSheetTheme.copyWith(
            backgroundColor: Colors.black,
          ),
          dividerColor: Colors.grey.shade800,
          brightness: Brightness.dark,
          inputDecorationTheme: inputTheme
        ) : ThemeData( //Dark Theme
          primaryColor: Colors.red,
          // accentColor: Colors.lightBlueAccent,
          brightness: Brightness.dark,
          bottomSheetTheme: bottomSheetTheme,
          inputDecorationTheme: inputTheme,
          snackBarTheme: snackTheme
        ),
      navigatorObservers: [
        SW.of(context).observatory
      ],
      initialRoute: init ?? SW.of(context).getPreference(preferences.startingScreen, "/characters"),
      routes: {
        // "/gm" : (context) => GMMode(),.
        "/characters" : (context) => EditableList(EditableList.character),
        "/minions" : (context) => EditableList(EditableList.minion),
        "/vehicles" : (context) => EditableList(EditableList.vehicle),
        // "/download" : (context) => Downloads(),
        "/dice" : (context) => DiceRoller(),
        // "/guide" : (context) => Guide(),
        "/settings" : (context) => Settings(),
        // Initial setup pages
        "/intro" : (context) => IntroZero(),
      },
    );
  }
}


class Observatory extends NavigatorObserver{

  List<Route> routeHistory = [];

  @override
  void didPush(Route route, Route? previousRoute) {
    routeHistory.add(route);
    super.didPush(route, previousRoute);
  }

  @override
  void didPop(Route route, Route? previousRoute) {
    routeHistory.removeLast();
    super.didPop(route, previousRoute);
  }

  @override
  void didRemove(Route route, Route? previousRoute) {
    var beginIndex = routeHistory.indexOf(route);
    var endIndex = -1;
    if (previousRoute != null)
      endIndex = routeHistory.indexOf(previousRoute);
    if((endIndex == -1 && beginIndex != -1) || beginIndex -1 == endIndex)
      routeHistory.remove(route);
    else if(endIndex != -1 && beginIndex != -1)
      routeHistory.removeRange(beginIndex,endIndex);
    super.didRemove(route, previousRoute);
  }

  @override
  void didReplace({Route? newRoute, Route? oldRoute}) {
    if (oldRoute != null && newRoute != null && routeHistory.contains(oldRoute))
      routeHistory[routeHistory.indexOf(oldRoute)] = newRoute;
    super.didReplace(newRoute: newRoute, oldRoute: oldRoute);
  }

  //Returns the route if it is in history, otherwise it return null.
  //It preferes the route give, then settings, then the name
  Route? containsRoute({Route? route, RouteSettings? settings, String? name}){
    if(route != null)
      return routeHistory.contains(route) ? route : null;
    if(settings != null)
      try{
        return routeHistory.firstWhere(
          (element) => element.settings == settings,
        );
      } catch (e){
        if(e is StateError)
          return null;
      }
    if(name != null)
      try{
        return routeHistory.firstWhere(
          (element) => element.settings.name == name,
        );
      } catch (e){
        if(e is StateError)
          return null;
      }
    return null;
  }
}