import 'package:flutter/material.dart';
import 'package:swassistant/Preferences.dart' as preferences;
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/intro/IntroZero.dart';
import 'package:swassistant/ui/screens/DiceRoller.dart';
import 'package:swassistant/ui/screens/EditableList.dart';
import 'package:swassistant/ui/screens/GMMode.dart';
import 'package:swassistant/ui/screens/Settings.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

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

//TODO: Before Release
//* Google Play donate
//* Google Drive saving
//  * Create a generic "Driver" to also use for CDR.
//* Move all old translations to arb files.
//  * Possibly ajust UI to require less translation.

//TODO: Post-release
//* Character creator
//* Real-time Google Drive syncing
//* Add index value for custom order.
//* Game Room???
//  * Share characters and vehicles with a GM
//  * Possibly share between all players.
//  * Would have to utilize private server for functionality.
//  * Possibly lock behind paymment to pay for server.
//    * Don't really want to do this. Maybe have a limited number of free rooms?

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
        borderRadius: BorderRadius.vertical(top: Radius.circular(15))
      ),
      clipBehavior: Clip.antiAlias
    );
    return MaterialApp(
      title: 'SWAssistant',
      localizationsDelegates: [
        AppLocalizations.delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate
      ],
      supportedLocales: [
        Locale("en", ""),
        Locale("de",""),
        Locale("es",""),
        Locale("fr",""),
        Locale("it","")
      ],
      themeMode: SW.of(context).getPreference(preferences.forceLight, false) ?
        ThemeMode.light : SW.of(context).getPreference(preferences.forceDark, false) ?
        ThemeMode.dark : ThemeMode.system,
      theme: ThemeData.light().copyWith(
        primaryColor: Colors.lightBlue,
        colorScheme: ColorScheme.fromSwatch(
          primarySwatch: Colors.lightBlue,
          accentColor: Colors.redAccent
        ),
        snackBarTheme: snackTheme,
        inputDecorationTheme: inputTheme,
        bottomSheetTheme: bottomSheetTheme
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
          colorScheme: ColorScheme.fromSwatch(
            primarySwatch: Colors.red,
            primaryColorDark: Colors.red,
            accentColor: Colors.lightBlueAccent,
            brightness: Brightness.dark
          ),
          bottomSheetTheme: bottomSheetTheme.copyWith(
            backgroundColor: Colors.black,
          ),
          dividerColor: Colors.grey.shade800,
          inputDecorationTheme: inputTheme
        ) : ThemeData( //Dark Theme
          primaryColor: Colors.red,
          colorScheme: ColorScheme.fromSwatch(
            primarySwatch: Colors.red,
            accentColor: Colors.lightBlueAccent,
            brightness: Brightness.dark
          ),
          bottomSheetTheme: bottomSheetTheme,
          inputDecorationTheme: inputTheme,
          snackBarTheme: snackTheme
        ),
      navigatorObservers: [
        SW.of(context).observatory
      ],
      initialRoute: init ?? SW.of(context).getPreference(preferences.startingScreen, "/characters"),
      routes: {
        "/gm" : (context) => GMMode(),
        "/characters" : (context) => EditableList(EditableList.character),
        "/minions" : (context) => EditableList(EditableList.minion),
        "/vehicles" : (context) => EditableList(EditableList.vehicle),
        // "/download" : (context) => Downloads(),
        "/dice" : (context) => DiceRoller(),
        "/settings" : (context) => Settings(),
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