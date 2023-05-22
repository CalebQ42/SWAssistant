import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Prefs{
  final SharedPreferences prefs;

  Prefs(this.prefs);

  //Locale
  String get locale => prefs.getString("locale") ?? "";
  set locale(String value) => prefs.setString("locale", value);

  //Google Drive
  bool get googleDrive => prefs.getBool("cloudEnabled") ?? false;
  set googleDrive(bool value) => prefs.setBool("cloudEnabled", value);

  bool get driveFirstLoad => prefs.getBool("driveFirstLoad") ?? true;
  set driveFirstLoad(bool value) => prefs.setBool("driveFirstLoad", value);

  bool get newDrive => prefs.getBool("newDrive") ?? false;
  set newDrive(bool value) => prefs.setBool("newDrive", value);

  //Firebase
  bool get firebase => prefs.getBool("firebase") ?? kIsWeb || Platform.isAndroid || Platform.isIOS;
  set firebase(bool value) => prefs.setBool("firebase", value);

  bool get crashlytics => prefs.getBool("crashlytics") ?? true;
  set crashlytics(bool value) => prefs.setBool("crashlytics", value);

  //Theme
  bool get lightTheme => prefs.getBool("forceLight") ?? false;
  set lightTheme(bool value) {
    prefs.setBool("forceLight", value);
    if(value && darkTheme) darkTheme = false;
  }

  bool get darkTheme => prefs.getBool("forceDark") ?? false;
  set darkTheme(bool value) {
    prefs.setBool("forceDark", value);
    if(value && lightTheme) lightTheme = false;
  }

  bool get amoledTheme => prefs.getBool("amoledTheme") ?? false;
  set amoledTheme(bool value) => prefs.setBool("amoledTheme", value);

  //Destiny
  int get destinyLight => prefs.getInt("destinyLight") ?? 0;
  set destinyLight(int value) => prefs.setInt("destinyLight", value);
  
  int get destinyDark => prefs.getInt("destinyDark") ?? 0;
  set destinyDark(int value) => prefs.setInt("destinyDark", value);

  //Misc
  bool get showIntro => prefs.getBool("first") ?? true;
  set showIntro(bool value) => prefs.setBool("first", value);

  bool get initialTrash => prefs.getBool("initialTrash") ?? true;
  set initialTrash(bool value) => prefs.setBool("initialTrash", value);

  bool get colorDice => prefs.getBool("colorDice") ?? true;
  set colorDice(bool value) => prefs.setBool("colorDice", value);

  bool get subtractMode => prefs.getBool("subtract") ?? true;
  set subtractMode(bool value) => prefs.setBool("subtract", value);

}