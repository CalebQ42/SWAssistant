import 'dart:async';

import 'package:darkstorm_common/frame.dart';
import 'package:darkstorm_common/intro.dart';
import 'package:darkstorm_common/top_inherit.dart';
import 'package:firebase_crashlytics/firebase_crashlytics.dart' deferred as crash;
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
import 'package:swassistant/ui/screens/dice_roller.dart';
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
        runApp(TopInherit<SW>(
          resources: a,
          child: const SWApp()
        ));
      }
    ), (error, stack) async{
      if(kDebugMode) {
        print("$error\n$stack");
      }else if(app.crashReporting){
        await crash.loadLibrary();
        crash.FirebaseCrashlytics.instance.recordError(error, stack);
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
              name: AppLocalizations.of(context)!.gmMode,
              routeName: "/gm"
            ),
            Nav(
              icon: const Icon(Icons.face),
              name: AppLocalizations.of(context)!.characters,
              routeName: "/characters",
            ),
            Nav(
              icon: const Icon(Icons.supervisor_account),
              name: AppLocalizations.of(context)!.minions,
              routeName: "/minions",
            ),
            Nav(
              icon: const Icon(Icons.motorcycle),
              name: AppLocalizations.of(context)!.vehicles,
              routeName: "/vehicles",
            )
          ],
          bottomNavItems: [
            Nav(
              icon: const Icon(Icons.settings),
              name: AppLocalizations.of(context)!.settings,
              routeName: "/settings"
            ),
            Nav(
              icon: const Icon(Icons.delete),
              name: AppLocalizations.of(context)!.trash,
              routeName: "/trash"
            )
          ],
          hideBar: (r) => r.startsWith("/intro") || r.startsWith("/loading"),
          floatingItem: FloatingNav(
            title: AppLocalizations.of(context)!.dice,
            icon: const Icon(Icons.casino_outlined),
            onTap: () =>
              SWDiceHolder().showDialog(context, showInstant: true)
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
        if(settings.name == "/dice") {
          widy = DiceRoller();
        }else if(settings.name == "/intro" || app.prefs.showIntro) {
          widy = IntroScreen(
            pages: Intro(app).pages,
            onDone: (){
              app.prefs.showIntro = false;
              app.nav.pushNamedAndRemoveUntil(settings.name ?? "/", (route) => false);
            }
          );
          settings = RouteSettings(name: "/intro", arguments: settings.arguments);
        }else if(!app.initialized){
          return PageRouteBuilder(
            pageBuilder: (context, anim, secondaryAnim) {
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
          settings = RouteSettings(name: "/characters", arguments: settings.arguments);
          widy ??= const EditableList(Character);
        }
        return PageRouteBuilder(
          pageBuilder: (context, anim, secondaryAnim) {
            return widy!;
          },
          settings: settings,
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
            primaryColorDark: Colors.red.shade700,
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