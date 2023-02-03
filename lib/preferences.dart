//Usage prefs
const String googleDrive = "cloudEnabled";
const String colorDice = "colorDice";
const String subtractMode = "subtract";
const String locale = "locale";

//Firebase prefs
const String crashlytics = "crashlytics";
const String firebase = "firebase";
const String ads = "adsOn";

//Starting prefs
const String firstStart = "first";
const String startCount = "startCount";
const String startingScreen = "startScreen";
const String driveFirstLoad = "driveFirstLoad";
const String newDrive = "newDrive";

//Theme prefs
const String forceLight = "forceLight";
const String forceDark = "forceDark";
const String amoled = "amoledTheme";

const String initialTrash = "initialTrash";

//destiny
const String destinyLight = "destinyLight";
const String destinyDark = "destinyDark";

//New prefs check
const String subtractNew = "subtractNew";

const List<String> newPrefs = [
  subtractNew
];

Map<String, dynamic> defaultPreference = {
  googleDrive: false,
  colorDice: true,
  subtractMode: true,
  locale: "",
  crashlytics: true,
  firebase: true,
  ads: true,
  firstStart: true,
  startCount: 0,
  startingScreen: "/characters",
  driveFirstLoad: true,
  newDrive: false,
  forceLight: false,
  forceDark: false,
  amoled: false,
  initialTrash: true,
  destinyLight: 0,
  destinyDark: 0,
  subtractNew: true,
};