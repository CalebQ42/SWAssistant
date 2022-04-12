import 'dart:async';

import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/frame.dart';
import 'package:swassistant/ui/intro/intro_zero.dart';
import 'package:swassistant/ui/screens/dice_roller.dart';
import 'package:swassistant/ui/screens/editable_list.dart';
import 'package:swassistant/ui/screens/editing_editable.dart';
import 'package:swassistant/ui/screens/gm_mode.dart';
import 'package:swassistant/ui/screens/loading.dart';
import 'package:swassistant/ui/screens/settings.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/screens/trash.dart';

Future<void> main() async {
  runZonedGuarded<Future<void>>(() async =>
    SW.baseInit().then(
      (app) =>
        runApp(SWWidget(
          child: const SWApp(),
          app: app
        ))
    ), (error, stack) {
      if(kDebugMode) {
        print("$error\n$stack");
      }else{
        FirebaseCrashlytics.instance.recordError(error, stack);
      }
    }
  );
}

class SWApp extends StatefulWidget{

  final String? init;

  const SWApp({Key? key, this.init}) : super(key: key);

  @override
  State<StatefulWidget> createState() => SWAppState();
}

class SWAppState extends State<SWApp> {

  @override
  Widget build(BuildContext context) {
    SW.of(context).topLevelUpdate = () => setState(() {});
    const snackTheme = SnackBarThemeData(
      behavior: SnackBarBehavior.floating,
    );
    const inputTheme = InputDecorationTheme(
      border: OutlineInputBorder(),
    );
    var bottomSheetTheme = BottomSheetThemeData(
      shape: const BeveledRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(25))
      ),
      clipBehavior: Clip.antiAlias,
      constraints: BoxConstraints.loose(const Size.fromWidth(600)),
    );
    var framKey = GlobalKey<FrameState>();
    SW.of(context).observatory = Observatory(SW.of(context), framKey);
    return MaterialApp(
      builder: (c, child) =>
        Navigator(
          onGenerateRoute: (rs) {
            return MaterialPageRoute(
              builder: (c) =>
                Frame(key: framKey, child: child),
            );
          },
        ),
      navigatorKey: SW.of(context).navy,
      title: 'SWAssistant',
      navigatorObservers: [
        SW.of(context).observatory!
      ],
      onGenerateRoute: (settings) {
        bool initSwitch = true;
        Widget? widy;
        if(settings.name == "/dice") {
          widy = DiceRoller();
        }else if(settings.name == "/intro" || SW.of(context).getPreference(preferences.firstStart, true)) {
          widy = const IntroZero();
          settings = settings.copyWith(name: "/intro");
        }else if(!SW.of(context).initialized){
          return PageRouteBuilder(
            pageBuilder: (context, anim, secondaryAnim) {
              if(initSwitch){
                Frame.of(context).selected = "/loading";
                initSwitch = false;
              }
              return Loading(afterLoad: settings);
            },
            settings: const RouteSettings(name: "/loading"),
            maintainState: false,
            transitionsBuilder: (context, anim, secondary, child) => FadeTransition(opacity: anim, child: child),
          );
        }else if(settings.name?.startsWith("/edit/") == true){
          Editable? ed;
          if(settings.arguments != null) {
            ed = settings.arguments as Editable;
          } else {
            ed = SW.of(context).findEditable(settings.name?.substring(6) ?? "");
          }
          if (ed != null) {
            ed.route = PageRouteBuilder(
              pageBuilder: (context, anim, secondaryAnim) {
                if(initSwitch){
                  Frame.of(context).selected = "/edit/" + ed!.uid.toString();
                  initSwitch = false;
                }
                return EditingEditable(ed!);
              },
              settings: RouteSettings(name: "/edit/" + ed.uid),
              maintainState: false,
              transitionsBuilder: (context, anim, secondary, child) => FadeTransition(opacity: anim, child: child)
            );
            return ed.route;
          } else {
            widy = EditableList(EditableList.character, uidToLoad: settings.name?.substring(6));
          }
        }else if(settings.name == "/gm") {
          widy = GMMode();
        } else if(settings.name == "/settings") {
          widy = const Settings();
        }else if(settings.name == "/vehicles"){
          widy = const EditableList(EditableList.vehicle);
        }else if(settings.name == "/minions"){
          widy = const EditableList(EditableList.minion);
        }else if(settings.name == "/trash"){
          widy = const TrashList();
        }
        if (widy == null){
          settings = settings.copyWith(name: "/characters");
          widy ??= const EditableList(EditableList.character);
        }
        return PageRouteBuilder(
          pageBuilder: (context, anim, secondaryAnim) {
            if(initSwitch){
              Frame.of(context).selected = settings.name ?? "";
              initSwitch = false;
            }
            return widy!;
          },
          settings: settings,
          maintainState: false,
          transitionsBuilder: (context, anim, secondary, child) => FadeTransition(opacity: anim, child: child)
        );
      },
      localizationsDelegates: const [
        AppLocalizations.delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate
      ],
      supportedLocales: const [
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
        primaryColorDark: Colors.lightBlue.shade600,
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
          cardColor: const Color.fromARGB(255, 15, 15, 15),
          snackBarTheme: snackTheme.copyWith(
            backgroundColor: const Color.fromARGB(255, 15, 15, 15),
            actionTextColor: Colors.amberAccent,
            contentTextStyle: const TextStyle(
              color: Colors.white
            ),
          ),
          primaryColor: Colors.red,
          primaryColorDark: Colors.red.shade900,
          colorScheme: ColorScheme.fromSwatch(
            primarySwatch: Colors.red,
            primaryColorDark: Colors.red.shade700,
            accentColor: Colors.lightBlueAccent.shade100,
            brightness: Brightness.dark
          ),
          bottomSheetTheme: bottomSheetTheme.copyWith(
            backgroundColor: Colors.black,
          ),
          dividerColor: Colors.grey.shade800,
          inputDecorationTheme: inputTheme
        ) : ThemeData( //Dark Theme
          primaryColor: Colors.red,
          primaryColorDark: Colors.red.shade900,
          colorScheme: ColorScheme.fromSwatch(
            primarySwatch: Colors.red,
            accentColor: Colors.lightBlueAccent.shade100,
            brightness: Brightness.dark
          ),
          bottomSheetTheme: bottomSheetTheme,
          inputDecorationTheme: inputTheme,
          snackBarTheme: snackTheme
        ),
    );
  }
}

class Observatory extends NavigatorObserver{

  List<Route> routeHistory = [];
  SW app;
  GlobalKey<FrameState> frame;

  Observatory(this.app, this.frame);

  String currentRoute(){
    if(routeHistory.isNotEmpty) return routeHistory.last.settings.name ?? "";
    return "";
  }

  void report(){
    if(routeHistory.isNotEmpty){
      frame.currentState?.selected = routeHistory.last.settings.name ?? "";
    }
    if(!kIsWeb && app.firebaseAvailable && app.getPreference(preferences.crashlytics, true)){
      FirebaseCrashlytics.instance.setCustomKey("page", routeHistory.last.settings.name ?? "unknown");
    }
  }

  @override
  void didPush(Route route, Route? previousRoute) {
    routeHistory.add(route);
    super.didPush(route, previousRoute);
    report();
  }

  @override
  void didPop(Route route, Route? previousRoute) {
    routeHistory.removeLast();
    super.didPop(route, previousRoute);
    report();
  }

  @override
  void didRemove(Route route, Route? previousRoute) {
    var beginIndex = routeHistory.indexOf(route);
    var endIndex = -1;
    if (previousRoute != null){
      endIndex = routeHistory.indexOf(previousRoute);
    }
    if((endIndex == -1 && beginIndex != -1) || beginIndex -1 == endIndex){
      routeHistory.remove(route);
    }else if(endIndex != -1 && beginIndex != -1){
      routeHistory.removeRange(beginIndex,endIndex);
    }
    super.didRemove(route, previousRoute);
    report();
  }

  @override
  void didReplace({Route? newRoute, Route? oldRoute}) {
    if (oldRoute != null && newRoute != null && routeHistory.contains(oldRoute)){
      routeHistory[routeHistory.indexOf(oldRoute)] = newRoute;
    }
    super.didReplace(newRoute: newRoute, oldRoute: oldRoute);
    report();
  }

  //Returns the route if it is in history, otherwise it return null.
  //It preferes the route give, then settings, then the name
  Route? containsRoute({Route? route, RouteSettings? settings, String? name}){
    if(route != null){
      return routeHistory.contains(route) ? route : null;
    }
    if(settings != null){
      try{
        return routeHistory.firstWhere(
          (element) => element.settings == settings,
        );
      } catch (e){
        if(e is StateError){
          return null;
        }
      }
    }
    if(name != null){
      try{
        return routeHistory.firstWhere(
          (element) => element.settings.name == name,
        );
      } catch (e){
        if(e is StateError){
          return null;
        }
      }
    }
    return null;
  }
}