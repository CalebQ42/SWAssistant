import 'dart:async';

import 'package:darkstorm_common/top_resources.dart';

import 'package:darkstorm_common/frame.dart';
import 'package:darkstorm_common/intro.dart';
import 'package:darkstorm_common/nav.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_web_plugins/url_strategy.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/intro_pages.dart';
import 'package:swassistant/ui/screens/editable_list.dart';
import 'package:swassistant/ui/screens/editing_editable.dart';
import 'package:swassistant/ui/screens/gm_mode.dart';
import 'package:swassistant/ui/screens/loading.dart';
import 'package:swassistant/ui/screens/settings.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/screens/trash.dart';

late SW app;

Future<void> main() async {
  usePathUrlStrategy();
  runZonedGuarded<Future<void>>(() async =>
    SW.baseInit().then(
      (a) {
        app = a;
        runApp(TopInherit(
          resources: a,
          child: const SWApp()
        ));
      }
    ), (error, stack) async{
      if(FlutterError.onError != null){
        FlutterError.onError!(FlutterErrorDetails(exception: error, stack: stack));
      }else{
        if(kDebugMode) print("$error\n$stack");
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
    app.topLevelUpdate = () => setState(() {});
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
    var fabTheme = const FloatingActionButtonThemeData(
      shape: BeveledRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(25))
      )
    );
    return MaterialApp(
      builder: (c, child) =>
        Frame(
          key: app.frameKey,
          beveled: true,
          appName: "SWAssistant",
          navItems: [
            Nav(
              icon: const Icon(Icons.contacts),
              name: AppLocalizations.of(c)!.gmMode,
              routeName: "/gm"
            ),
            Nav(
              icon: const Icon(Icons.face),
              name: AppLocalizations.of(c)!.characters,
              routeName: "/characters",
            ),
            Nav(
              icon: const Icon(Icons.supervisor_account),
              name: AppLocalizations.of(c)!.minions,
              routeName: "/minions",
            ),
            Nav(
              icon: const Icon(Icons.motorcycle),
              name: AppLocalizations.of(c)!.vehicles,
              routeName: "/vehicles",
            )
          ],
          bottomNavItems: [
            Nav(
              icon: const Icon(Icons.settings),
              name: AppLocalizations.of(c)!.settings,
              routeName: "/settings"
            ),
            Nav(
              icon: const Icon(Icons.delete),
              name: AppLocalizations.of(c)!.trash,
              routeName: "/trash"
            )
          ],
          hideBar: (r) => r.startsWith("/intro") || r.startsWith("/loading"),
          floatingItem: FloatingNav(
            title: AppLocalizations.of(c)!.dice,
            icon: const Icon(Icons.casino_outlined),
            onTap: () =>
              SWDiceHolder().showDialog(c, showInstant: true)
          ),
          child: child ?? const Text("Something went wrong")
        ),
      navigatorKey: app.navKey,
      title: 'SWAssistant',
      navigatorObservers: [
        app.observatory
      ],
      onGenerateRoute: (settings) {
        Widget? widy;
        RouteSettings? newSettings;
        if(settings.name == "/intro" || app.prefs.showIntroPages) {
          widy = IntroScreen(
            pages: Intro(app).pages,
            onDone: (){
              app.prefs.showIntro = false;
              app.prefs.stupidIntro = false;
              app.nav.pushNamedAndRemoveUntil(settings.name ?? "/", (route) => false);
            }
          );
          newSettings = const RouteSettings(name: "/intro");
        }else if(!app.initialized){
          widy = LoadingScreen(
            startingRoute: settings,
            app: app,
          );
          newSettings =  const RouteSettings(name: "/loading");
        }else if(settings.name?.startsWith("/edit/") == true){
          Editable? ed;
          if(settings.arguments != null) {
            ed = settings.arguments as Editable;
          } else {
            ed = app.getEditable(settings.name?.substring(6) ?? "");
          }
          if (ed != null) {
            ed.route = PageRouteBuilder(
              pageBuilder: (context, anim, secondaryAnim) {
                return EditingEditable(ed!);
              },
              settings: RouteSettings(name: "/edit/${ed.uid}"),
              maintainState: false,
              transitionsBuilder: (context, anim, secondary, child) => FadeTransition(opacity: anim, child: child)
            );
            return ed.route;
          } else {
            widy = EditableList(Character, uidToLoad: settings.name?.substring(6));
          }
        }else if(settings.name == "/gm") {
          widy = GMMode();
        } else if(settings.name == "/settings") {
          widy = const Settings();
        }else if(settings.name == "/vehicles"){
          widy = const EditableList(Vehicle);
        }else if(settings.name == "/minions"){
          widy = const EditableList(Minion);
        }else if(settings.name == "/trash"){
          widy = const TrashList();
        }
        if (widy == null){
          newSettings = RouteSettings(name: "/characters", arguments: settings.arguments);
          widy ??= const EditableList(Character);
        }
        return PageRouteBuilder(
          pageBuilder: (context, anim, secondaryAnim) {
            return widy!;
          },
          settings: newSettings ?? settings,
          maintainState: false,
          transitionsBuilder: (context, anim, secondary, child) => FadeTransition(opacity: anim, child: child)
        );
      },
      localizationsDelegates: AppLocalizations.localizationsDelegates,
      supportedLocales: AppLocalizations.supportedLocales,
      locale: (){
        if(app.prefs.locale == "") return null;
        try{
          return AppLocalizations.supportedLocales.firstWhere((element) => element.toLanguageTag() == app.prefs.locale);
        }catch(e){
          return null;
        }
      }(),
      themeMode: app.prefs.lightTheme ?
        ThemeMode.light : app.prefs.darkTheme ?
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
        bottomSheetTheme: bottomSheetTheme,
        floatingActionButtonTheme: fabTheme
      ),
      darkTheme: app.prefs.amoledTheme ? 
        ThemeData( //Amoled Theme
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
            backgroundColor: Colors.black,
            primarySwatch: Colors.red,
            accentColor: Colors.lightBlueAccent.shade100,
            brightness: Brightness.dark
          ),
          bottomSheetTheme: bottomSheetTheme.copyWith(
            backgroundColor: Colors.black,
          ),
          floatingActionButtonTheme: fabTheme,
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
          floatingActionButtonTheme: fabTheme,
          bottomSheetTheme: bottomSheetTheme,
          inputDecorationTheme: inputTheme,
          snackBarTheme: snackTheme
        ),
    );
  }
}