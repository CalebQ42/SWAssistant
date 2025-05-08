import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:intl/intl.dart' as intl;

import 'app_localizations_de.dart';
import 'app_localizations_en.dart';
import 'app_localizations_es.dart';
import 'app_localizations_fr.dart';
import 'app_localizations_it.dart';

// ignore_for_file: type=lint

/// Callers can lookup localized strings with an instance of AppLocalizations
/// returned by `AppLocalizations.of(context)`.
///
/// Applications need to include `AppLocalizations.delegate()` in their app's
/// `localizationDelegates` list, and the locales they support in the app's
/// `supportedLocales` list. For example:
///
/// ```dart
/// import 'l10n/app_localizations.dart';
///
/// return MaterialApp(
///   localizationsDelegates: AppLocalizations.localizationsDelegates,
///   supportedLocales: AppLocalizations.supportedLocales,
///   home: MyApplicationHome(),
/// );
/// ```
///
/// ## Update pubspec.yaml
///
/// Please make sure to update your pubspec.yaml to include the following
/// packages:
///
/// ```yaml
/// dependencies:
///   # Internationalization support.
///   flutter_localizations:
///     sdk: flutter
///   intl: any # Use the pinned version from flutter_localizations
///
///   # Rest of dependencies
/// ```
///
/// ## iOS Applications
///
/// iOS applications define key application metadata, including supported
/// locales, in an Info.plist file that is built into the application bundle.
/// To configure the locales supported by your app, you’ll need to edit this
/// file.
///
/// First, open your project’s ios/Runner.xcworkspace Xcode workspace file.
/// Then, in the Project Navigator, open the Info.plist file under the Runner
/// project’s Runner folder.
///
/// Next, select the Information Property List item, select Add Item from the
/// Editor menu, then select Localizations from the pop-up menu.
///
/// Select and expand the newly-created Localizations item then, for each
/// locale your application supports, add a new item and select the locale
/// you wish to add from the pop-up menu in the Value field. This list should
/// be consistent with the languages listed in the AppLocalizations.supportedLocales
/// property.
abstract class AppLocalizations {
  AppLocalizations(String locale) : localeName = intl.Intl.canonicalizedLocale(locale.toString());

  final String localeName;

  static AppLocalizations? of(BuildContext context) {
    return Localizations.of<AppLocalizations>(context, AppLocalizations);
  }

  static const LocalizationsDelegate<AppLocalizations> delegate = _AppLocalizationsDelegate();

  /// A list of this localizations delegate along with the default localizations
  /// delegates.
  ///
  /// Returns a list of localizations delegates containing this delegate along with
  /// GlobalMaterialLocalizations.delegate, GlobalCupertinoLocalizations.delegate,
  /// and GlobalWidgetsLocalizations.delegate.
  ///
  /// Additional delegates can be added by appending to this list in
  /// MaterialApp. This list does not have to be used at all if a custom list
  /// of delegates is preferred or required.
  static const List<LocalizationsDelegate<dynamic>> localizationsDelegates = <LocalizationsDelegate<dynamic>>[
    delegate,
    GlobalMaterialLocalizations.delegate,
    GlobalCupertinoLocalizations.delegate,
    GlobalWidgetsLocalizations.delegate,
  ];

  /// A list of this localizations delegate's supported locales.
  static const List<Locale> supportedLocales = <Locale>[
    Locale('de'),
    Locale('en'),
    Locale('es'),
    Locale('fr'),
    Locale('it')
  ];

  /// No description provided for @introWelcome.
  ///
  /// In en, this message translates to:
  /// **'Welcome!'**
  String get introWelcome;

  /// No description provided for @introPage0Line1.
  ///
  /// In en, this message translates to:
  /// **'Thank you for trying SWAssistant. Before we get started, you first have to set up some preferences.'**
  String get introPage0Line1;

  /// Should end with a space as introPage0WebNoticeEnd is added to the end
  ///
  /// In en, this message translates to:
  /// **'As this is the web vesion, it makes use of Google Drive to save and load. After setup you will be asked to sign into Google. Additionally it requires cookies to function and by using it you agree to it\'s '**
  String get introPage0WebNoticev2;

  /// Added to the end of introPage0CookieNotice. Will be a hyperlink.
  ///
  /// In en, this message translates to:
  /// **'cookie policy.'**
  String get introPage0WebNoticeEnd;

  /// No description provided for @introPage0Line2.
  ///
  /// In en, this message translates to:
  /// **'Additionally, if you used a previous version of this app, we will need to import your old profiles first.'**
  String get introPage0Line2;

  /// No description provided for @introPage0ImportButton.
  ///
  /// In en, this message translates to:
  /// **'Import Profiles'**
  String get introPage0ImportButton;

  /// No description provided for @introPage0ManualImport.
  ///
  /// In en, this message translates to:
  /// **'Manual Import'**
  String get introPage0ManualImport;

  /// No description provided for @introPage0ManualImportLine0.
  ///
  /// In en, this message translates to:
  /// **'Due to the stupid way old versions of SWAssistant saved profiles and changes to Android, you\'ll need to manually import your profiles. If you used cloud saving, you can alternatively skip this and turn on cloud saving. When you hit continue, a file explorer should pop up.'**
  String get introPage0ManualImportLine0;

  /// No description provided for @introPage0ManualImportLine1.
  ///
  /// In en, this message translates to:
  /// **'1) Open up the Navigation Drawer (the three lines in the top left corner) and select your phone'**
  String get introPage0ManualImportLine1;

  /// No description provided for @introPage0ManualImportLine1a.
  ///
  /// In en, this message translates to:
  /// **'1a) If your phone is not there, you may have to open up the overflow menu (the three dots in the top right corner) and tap \"Show Internal Storage\"'**
  String get introPage0ManualImportLine1a;

  /// No description provided for @introPage0ManualImportLine2.
  ///
  /// In en, this message translates to:
  /// **'2) Select the folder named SWChars'**
  String get introPage0ManualImportLine2;

  /// No description provided for @introPage0ManualImportLine3.
  ///
  /// In en, this message translates to:
  /// **'3) Long press on one of the files until it goes into multi-select mode, then select all the other files in the folder'**
  String get introPage0ManualImportLine3;

  /// No description provided for @introPage0ManualImportLine4.
  ///
  /// In en, this message translates to:
  /// **'4) Click the Select Files button.'**
  String get introPage0ManualImportLine4;

  /// No description provided for @introPage1Title.
  ///
  /// In en, this message translates to:
  /// **'Darkstorm Backend'**
  String get introPage1Title;

  /// No description provided for @introPage1DarkstormExplaination.
  ///
  /// In en, this message translates to:
  /// **'This app (optionally) uses my custom backend called Darkstorm Backend. It is completely open source and collects zero information other then what\'s needed. It\'s used for crash reporting, user counts, and sharing profiles. No sensitive information is collected or stored.'**
  String get introPage1DarkstormExplaination;

  /// No description provided for @introDarkstormExistingUsers.
  ///
  /// In en, this message translates to:
  /// **'Firebase has been replaced with my custom backend called Darkstorm Backend. It is completely open source and collects zero information other then what\'s needed. It\'s used for crash reporting, logging the app\'s startup (for user count), and sharing profiles. No sensitive information is collected or stored.'**
  String get introDarkstormExistingUsers;

  /// Shown after imported old profiles. number is how many profiles imported.
  ///
  /// In en, this message translates to:
  /// **'Successfully imported {number} profiles.'**
  String importSuccess(int number);

  /// No description provided for @importDialog.
  ///
  /// In en, this message translates to:
  /// **'Importing Profiles...'**
  String get importDialog;

  /// No description provided for @importNone.
  ///
  /// In en, this message translates to:
  /// **'You seemed to have not selected any files'**
  String get importNone;

  /// No description provided for @loadingDialog.
  ///
  /// In en, this message translates to:
  /// **'Loading...'**
  String get loadingDialog;

  /// No description provided for @driveError.
  ///
  /// In en, this message translates to:
  /// **'Can\'t sync with Google Drive'**
  String get driveError;

  /// No description provided for @driveErrorWebSub.
  ///
  /// In en, this message translates to:
  /// **'Google Drive syncing is required for the web version of SWAssistant. The sign in pop-up might have been blocked by your browser.'**
  String get driveErrorWebSub;

  /// No description provided for @driveErrorOfflineSub.
  ///
  /// In en, this message translates to:
  /// **'If you continue offline, you can refresh on the character list to re-connect.'**
  String get driveErrorOfflineSub;

  /// No description provided for @driveContinue.
  ///
  /// In en, this message translates to:
  /// **'Continue offline'**
  String get driveContinue;

  /// No description provided for @retry.
  ///
  /// In en, this message translates to:
  /// **'Retry'**
  String get retry;

  /// No description provided for @driveSyncing.
  ///
  /// In en, this message translates to:
  /// **'Syncing Google Drive'**
  String get driveSyncing;

  /// No description provided for @driveSyncingWeb.
  ///
  /// In en, this message translates to:
  /// **'You may need to allow and open a pop-up to coninue'**
  String get driveSyncingWeb;

  /// No description provided for @syncFail.
  ///
  /// In en, this message translates to:
  /// **'Syncing failed'**
  String get syncFail;

  /// No description provided for @syncComplete.
  ///
  /// In en, this message translates to:
  /// **'Syncing Complete'**
  String get syncComplete;

  /// Shown when the user tries to do an action while Google Drive is syncing.
  ///
  /// In en, this message translates to:
  /// **'Google Drive is syncing'**
  String get driveSyncingNotice;

  /// No description provided for @driveDisconnectNotice.
  ///
  /// In en, this message translates to:
  /// **'Google Drive is disconnect'**
  String get driveDisconnectNotice;

  /// No description provided for @driveFirstLaunch.
  ///
  /// In en, this message translates to:
  /// **'Syncing your profiles to Google Drive is now available. Go to Settings to enable it?'**
  String get driveFirstLaunch;

  /// No description provided for @driveEnableFail.
  ///
  /// In en, this message translates to:
  /// **'Google Drive failed to initialize'**
  String get driveEnableFail;

  /// No description provided for @driveFull.
  ///
  /// In en, this message translates to:
  /// **'Google Drive storage full'**
  String get driveFull;

  /// No description provided for @gmModeTap.
  ///
  /// In en, this message translates to:
  /// **'Select a profile on the left to edit.'**
  String get gmModeTap;

  /// Used for the selector for GM Mode to switch between Characters, Minions, and Vehicles
  ///
  /// In en, this message translates to:
  /// **'Type...'**
  String get gmModeType;

  /// No description provided for @translate.
  ///
  /// In en, this message translates to:
  /// **'Help Translate!'**
  String get translate;

  /// No description provided for @discuss.
  ///
  /// In en, this message translates to:
  /// **'Discuss/Announcements'**
  String get discuss;

  /// No description provided for @success.
  ///
  /// In en, this message translates to:
  /// **'Success'**
  String get success;

  /// No description provided for @failure.
  ///
  /// In en, this message translates to:
  /// **'Failure'**
  String get failure;

  /// No description provided for @advantage.
  ///
  /// In en, this message translates to:
  /// **'Advantage'**
  String get advantage;

  /// No description provided for @threat.
  ///
  /// In en, this message translates to:
  /// **'Threat'**
  String get threat;

  /// No description provided for @triumph.
  ///
  /// In en, this message translates to:
  /// **'Triumph'**
  String get triumph;

  /// No description provided for @despair.
  ///
  /// In en, this message translates to:
  /// **'Despair'**
  String get despair;

  /// No description provided for @lightSide.
  ///
  /// In en, this message translates to:
  /// **'Light Side'**
  String get lightSide;

  /// No description provided for @darkSide.
  ///
  /// In en, this message translates to:
  /// **'Dark Side'**
  String get darkSide;

  /// No description provided for @ability.
  ///
  /// In en, this message translates to:
  /// **'Ability'**
  String get ability;

  /// No description provided for @proficiency.
  ///
  /// In en, this message translates to:
  /// **'Proficiency'**
  String get proficiency;

  /// No description provided for @difficulty.
  ///
  /// In en, this message translates to:
  /// **'Difficulty'**
  String get difficulty;

  /// No description provided for @challenge.
  ///
  /// In en, this message translates to:
  /// **'Challenge'**
  String get challenge;

  /// No description provided for @boost.
  ///
  /// In en, this message translates to:
  /// **'Boost'**
  String get boost;

  /// No description provided for @setback.
  ///
  /// In en, this message translates to:
  /// **'Setback'**
  String get setback;

  /// No description provided for @force.
  ///
  /// In en, this message translates to:
  /// **'Force'**
  String get force;

  /// No description provided for @brawn.
  ///
  /// In en, this message translates to:
  /// **'Brawn'**
  String get brawn;

  /// No description provided for @agility.
  ///
  /// In en, this message translates to:
  /// **'Agility'**
  String get agility;

  /// No description provided for @intellect.
  ///
  /// In en, this message translates to:
  /// **'Intellect'**
  String get intellect;

  /// No description provided for @cunning.
  ///
  /// In en, this message translates to:
  /// **'Cunning'**
  String get cunning;

  /// No description provided for @willpower.
  ///
  /// In en, this message translates to:
  /// **'Willpower'**
  String get willpower;

  /// No description provided for @presence.
  ///
  /// In en, this message translates to:
  /// **'Presence'**
  String get presence;

  /// No description provided for @roll.
  ///
  /// In en, this message translates to:
  /// **'Roll'**
  String get roll;

  /// No description provided for @rollCrit.
  ///
  /// In en, this message translates to:
  /// **'Roll Critical'**
  String get rollCrit;

  /// No description provided for @gmMode.
  ///
  /// In en, this message translates to:
  /// **'GM Mode'**
  String get gmMode;

  /// No description provided for @profiles.
  ///
  /// In en, this message translates to:
  /// **'Profiles'**
  String get profiles;

  /// No description provided for @characters.
  ///
  /// In en, this message translates to:
  /// **'Characters'**
  String get characters;

  /// No description provided for @minions.
  ///
  /// In en, this message translates to:
  /// **'Minions'**
  String get minions;

  /// No description provided for @vehicles.
  ///
  /// In en, this message translates to:
  /// **'Vehicles'**
  String get vehicles;

  /// No description provided for @trash.
  ///
  /// In en, this message translates to:
  /// **'Trash'**
  String get trash;

  /// No description provided for @download.
  ///
  /// In en, this message translates to:
  /// **'Download'**
  String get download;

  /// No description provided for @otherStuff.
  ///
  /// In en, this message translates to:
  /// **'Other Stuff'**
  String get otherStuff;

  /// No description provided for @dice.
  ///
  /// In en, this message translates to:
  /// **'Dice'**
  String get dice;

  /// No description provided for @guide.
  ///
  /// In en, this message translates to:
  /// **'Guide'**
  String get guide;

  /// No description provided for @settings.
  ///
  /// In en, this message translates to:
  /// **'Settings'**
  String get settings;

  /// No description provided for @donate.
  ///
  /// In en, this message translates to:
  /// **'Donate'**
  String get donate;

  /// No description provided for @all.
  ///
  /// In en, this message translates to:
  /// **'All'**
  String get all;

  /// No description provided for @uncategorized.
  ///
  /// In en, this message translates to:
  /// **'Uncategorized'**
  String get uncategorized;

  /// No description provided for @categories.
  ///
  /// In en, this message translates to:
  /// **'Categories'**
  String get categories;

  /// No description provided for @edit.
  ///
  /// In en, this message translates to:
  /// **'Edit'**
  String get edit;

  /// No description provided for @donoOptions.
  ///
  /// In en, this message translates to:
  /// **'Donation Options'**
  String get donoOptions;

  /// No description provided for @gPlay.
  ///
  /// In en, this message translates to:
  /// **'Google Play'**
  String get gPlay;

  /// No description provided for @gPlayDesc.
  ///
  /// In en, this message translates to:
  /// **'Easy to use with support for most currencies, but donations are capped and Google takes a 15% cut'**
  String get gPlayDesc;

  /// No description provided for @sponsors.
  ///
  /// In en, this message translates to:
  /// **'Github Sponsors'**
  String get sponsors;

  /// No description provided for @sponsorsDesc.
  ///
  /// In en, this message translates to:
  /// **'You can donate either one time or on a monthly basis, with custom amounts, AND 100% of your money goes to the developer'**
  String get sponsorsDesc;

  /// Shown in the Google Play donation dialog. amount is the amount in whatever local currency the donation is for.
  ///
  /// In en, this message translates to:
  /// **'Donate {amount}'**
  String gPlayDonate(String amount);

  /// No description provided for @gPlayPurchase.
  ///
  /// In en, this message translates to:
  /// **'Purchase'**
  String get gPlayPurchase;

  /// No description provided for @gPlayUnavailable.
  ///
  /// In en, this message translates to:
  /// **'Google Play is currently unavailable'**
  String get gPlayUnavailable;

  /// No description provided for @gPlayThanks.
  ///
  /// In en, this message translates to:
  /// **'Thank you for your donation :D'**
  String get gPlayThanks;

  /// No description provided for @gPlayPurchasePending.
  ///
  /// In en, this message translates to:
  /// **'Purchase Pending...'**
  String get gPlayPurchasePending;

  /// No description provided for @newCharacter.
  ///
  /// In en, this message translates to:
  /// **'New Character'**
  String get newCharacter;

  /// No description provided for @newMinion.
  ///
  /// In en, this message translates to:
  /// **'New Minion'**
  String get newMinion;

  /// No description provided for @newVehicle.
  ///
  /// In en, this message translates to:
  /// **'New Vehicle'**
  String get newVehicle;

  /// No description provided for @woundStrain.
  ///
  /// In en, this message translates to:
  /// **'Wound and Strain'**
  String get woundStrain;

  /// No description provided for @specializationPlural.
  ///
  /// In en, this message translates to:
  /// **'Specializations'**
  String get specializationPlural;

  /// No description provided for @forcePowerPlural.
  ///
  /// In en, this message translates to:
  /// **'Force Powers'**
  String get forcePowerPlural;

  /// No description provided for @xp.
  ///
  /// In en, this message translates to:
  /// **'XP'**
  String get xp;

  /// No description provided for @morality.
  ///
  /// In en, this message translates to:
  /// **'Morality'**
  String get morality;

  /// No description provided for @skillPlural.
  ///
  /// In en, this message translates to:
  /// **'Skills'**
  String get skillPlural;

  /// No description provided for @saveWeaponsFirst.
  ///
  /// In en, this message translates to:
  /// **'Save weapons first!'**
  String get saveWeaponsFirst;

  /// No description provided for @restoreWeapons.
  ///
  /// In en, this message translates to:
  /// **'Restore saved weapons'**
  String get restoreWeapons;

  /// No description provided for @weaponsRestored.
  ///
  /// In en, this message translates to:
  /// **'Weapons Restored'**
  String get weaponsRestored;

  /// No description provided for @saveWeapons.
  ///
  /// In en, this message translates to:
  /// **'Save weapons'**
  String get saveWeapons;

  /// No description provided for @overwriteWeapons.
  ///
  /// In en, this message translates to:
  /// **'Overwrite saved weapons'**
  String get overwriteWeapons;

  /// No description provided for @weaponsSaved.
  ///
  /// In en, this message translates to:
  /// **'Weapons Saved'**
  String get weaponsSaved;

  /// No description provided for @saveInventoryFirst.
  ///
  /// In en, this message translates to:
  /// **'Save inventory first!'**
  String get saveInventoryFirst;

  /// No description provided for @restoreInventory.
  ///
  /// In en, this message translates to:
  /// **'Restore saved inventory'**
  String get restoreInventory;

  /// No description provided for @inventoryRestored.
  ///
  /// In en, this message translates to:
  /// **'Inventory Restored'**
  String get inventoryRestored;

  /// No description provided for @saveInventory.
  ///
  /// In en, this message translates to:
  /// **'Save inventory'**
  String get saveInventory;

  /// No description provided for @overwriteInventory.
  ///
  /// In en, this message translates to:
  /// **'Overwrite saved inventory'**
  String get overwriteInventory;

  /// No description provided for @inventorySaved.
  ///
  /// In en, this message translates to:
  /// **'Inventory Saved'**
  String get inventorySaved;

  /// No description provided for @criticalHits.
  ///
  /// In en, this message translates to:
  /// **'Critical Hits'**
  String get criticalHits;

  /// No description provided for @basicInfo.
  ///
  /// In en, this message translates to:
  /// **'Basic Information'**
  String get basicInfo;

  /// No description provided for @defense.
  ///
  /// In en, this message translates to:
  /// **'Defense'**
  String get defense;

  /// No description provided for @weaponPlural.
  ///
  /// In en, this message translates to:
  /// **'Weapons'**
  String get weaponPlural;

  /// No description provided for @characteristicPlural.
  ///
  /// In en, this message translates to:
  /// **'Characteristics'**
  String get characteristicPlural;

  /// No description provided for @talentPlural.
  ///
  /// In en, this message translates to:
  /// **'Talents'**
  String get talentPlural;

  /// No description provided for @inventory.
  ///
  /// In en, this message translates to:
  /// **'Inventory'**
  String get inventory;

  /// No description provided for @criticalInj.
  ///
  /// In en, this message translates to:
  /// **'Critical Injuries'**
  String get criticalInj;

  /// No description provided for @characteristicAccurate.
  ///
  /// In en, this message translates to:
  /// **'Accurate'**
  String get characteristicAccurate;

  /// No description provided for @characteristicBreach.
  ///
  /// In en, this message translates to:
  /// **'Breach'**
  String get characteristicBreach;

  /// No description provided for @characteristicCumbersome.
  ///
  /// In en, this message translates to:
  /// **'Cumbersome'**
  String get characteristicCumbersome;

  /// No description provided for @characteristicInaccurate.
  ///
  /// In en, this message translates to:
  /// **'Inaccurate'**
  String get characteristicInaccurate;

  /// No description provided for @characteristicInferior.
  ///
  /// In en, this message translates to:
  /// **'Inferior'**
  String get characteristicInferior;

  /// No description provided for @characteristicPierce.
  ///
  /// In en, this message translates to:
  /// **'Pierce'**
  String get characteristicPierce;

  /// No description provided for @characteristicVicious.
  ///
  /// In en, this message translates to:
  /// **'Vicious'**
  String get characteristicVicious;

  /// No description provided for @ret.
  ///
  /// In en, this message translates to:
  /// **'Return'**
  String get ret;

  /// No description provided for @desc.
  ///
  /// In en, this message translates to:
  /// **'Description'**
  String get desc;

  /// No description provided for @name.
  ///
  /// In en, this message translates to:
  /// **'Name'**
  String get name;

  /// No description provided for @rank.
  ///
  /// In en, this message translates to:
  /// **'Rank'**
  String get rank;

  /// No description provided for @characteristic.
  ///
  /// In en, this message translates to:
  /// **'Characteristic'**
  String get characteristic;

  /// No description provided for @category.
  ///
  /// In en, this message translates to:
  /// **'Category'**
  String get category;

  /// No description provided for @duty.
  ///
  /// In en, this message translates to:
  /// **'Duty'**
  String get duty;

  /// No description provided for @forcePower.
  ///
  /// In en, this message translates to:
  /// **'Force Power'**
  String get forcePower;

  /// No description provided for @obligation.
  ///
  /// In en, this message translates to:
  /// **'Obligation'**
  String get obligation;

  /// No description provided for @specialization.
  ///
  /// In en, this message translates to:
  /// **'Specialization'**
  String get specialization;

  /// No description provided for @skill.
  ///
  /// In en, this message translates to:
  /// **'Skill'**
  String get skill;

  /// No description provided for @talent.
  ///
  /// In en, this message translates to:
  /// **'Talent'**
  String get talent;

  /// No description provided for @item.
  ///
  /// In en, this message translates to:
  /// **'Item'**
  String get item;

  /// No description provided for @weapon.
  ///
  /// In en, this message translates to:
  /// **'Weapon'**
  String get weapon;

  /// No description provided for @value.
  ///
  /// In en, this message translates to:
  /// **'Value'**
  String get value;

  /// No description provided for @career.
  ///
  /// In en, this message translates to:
  /// **'Career'**
  String get career;

  /// No description provided for @skills1.
  ///
  /// In en, this message translates to:
  /// **'Astrogation'**
  String get skills1;

  /// No description provided for @skills2.
  ///
  /// In en, this message translates to:
  /// **'Athletics'**
  String get skills2;

  /// No description provided for @skills3.
  ///
  /// In en, this message translates to:
  /// **'Brawl'**
  String get skills3;

  /// No description provided for @skills4.
  ///
  /// In en, this message translates to:
  /// **'Charm'**
  String get skills4;

  /// No description provided for @skills5.
  ///
  /// In en, this message translates to:
  /// **'Coercion'**
  String get skills5;

  /// No description provided for @skills6.
  ///
  /// In en, this message translates to:
  /// **'Computers'**
  String get skills6;

  /// No description provided for @skills7.
  ///
  /// In en, this message translates to:
  /// **'Cool'**
  String get skills7;

  /// No description provided for @skills8.
  ///
  /// In en, this message translates to:
  /// **'Coordination'**
  String get skills8;

  /// No description provided for @skills9.
  ///
  /// In en, this message translates to:
  /// **'Deception'**
  String get skills9;

  /// No description provided for @skills10.
  ///
  /// In en, this message translates to:
  /// **'Discipline'**
  String get skills10;

  /// No description provided for @skills11.
  ///
  /// In en, this message translates to:
  /// **'Gunnery'**
  String get skills11;

  /// No description provided for @skills12.
  ///
  /// In en, this message translates to:
  /// **'Leadership'**
  String get skills12;

  /// No description provided for @skills13.
  ///
  /// In en, this message translates to:
  /// **'Lightsaber'**
  String get skills13;

  /// No description provided for @skills14.
  ///
  /// In en, this message translates to:
  /// **'Mechanics'**
  String get skills14;

  /// No description provided for @skills15.
  ///
  /// In en, this message translates to:
  /// **'Medicine'**
  String get skills15;

  /// No description provided for @skills16.
  ///
  /// In en, this message translates to:
  /// **'Melee'**
  String get skills16;

  /// No description provided for @skills17.
  ///
  /// In en, this message translates to:
  /// **'Negotiation'**
  String get skills17;

  /// No description provided for @skills18.
  ///
  /// In en, this message translates to:
  /// **'Perception'**
  String get skills18;

  /// No description provided for @skills19.
  ///
  /// In en, this message translates to:
  /// **'Piloting(Planetary)'**
  String get skills19;

  /// No description provided for @skills20.
  ///
  /// In en, this message translates to:
  /// **'Piloting(Space)'**
  String get skills20;

  /// No description provided for @skills21.
  ///
  /// In en, this message translates to:
  /// **'Ranged(Heavy)'**
  String get skills21;

  /// No description provided for @skills22.
  ///
  /// In en, this message translates to:
  /// **'Ranged(Light)'**
  String get skills22;

  /// No description provided for @skills23.
  ///
  /// In en, this message translates to:
  /// **'Resilience'**
  String get skills23;

  /// No description provided for @skills24.
  ///
  /// In en, this message translates to:
  /// **'Skulduggery'**
  String get skills24;

  /// No description provided for @skills25.
  ///
  /// In en, this message translates to:
  /// **'Stealth'**
  String get skills25;

  /// No description provided for @skills26.
  ///
  /// In en, this message translates to:
  /// **'Streetwise'**
  String get skills26;

  /// No description provided for @skills27.
  ///
  /// In en, this message translates to:
  /// **'Survival'**
  String get skills27;

  /// No description provided for @skills28.
  ///
  /// In en, this message translates to:
  /// **'Vigilance'**
  String get skills28;

  /// No description provided for @skills29.
  ///
  /// In en, this message translates to:
  /// **'Knowledge(Core Worlds)'**
  String get skills29;

  /// No description provided for @skills30.
  ///
  /// In en, this message translates to:
  /// **'Knowledge(Education)'**
  String get skills30;

  /// No description provided for @skills31.
  ///
  /// In en, this message translates to:
  /// **'Knowledge(Lore)'**
  String get skills31;

  /// No description provided for @skills32.
  ///
  /// In en, this message translates to:
  /// **'Knowledge(Outer Rim)'**
  String get skills32;

  /// No description provided for @skills33.
  ///
  /// In en, this message translates to:
  /// **'Knowledge(Underworld)'**
  String get skills33;

  /// No description provided for @skills34.
  ///
  /// In en, this message translates to:
  /// **'Knowledge(Xenology)'**
  String get skills34;

  /// No description provided for @skills35.
  ///
  /// In en, this message translates to:
  /// **'Other...'**
  String get skills35;

  /// No description provided for @skillName.
  ///
  /// In en, this message translates to:
  /// **'Skill Name'**
  String get skillName;

  /// No description provided for @severity.
  ///
  /// In en, this message translates to:
  /// **'Severity'**
  String get severity;

  /// No description provided for @severityLevel1.
  ///
  /// In en, this message translates to:
  /// **'Easy'**
  String get severityLevel1;

  /// No description provided for @severityLevel2.
  ///
  /// In en, this message translates to:
  /// **'Average'**
  String get severityLevel2;

  /// No description provided for @severityLevel3.
  ///
  /// In en, this message translates to:
  /// **'Hard'**
  String get severityLevel3;

  /// No description provided for @severityLevel4.
  ///
  /// In en, this message translates to:
  /// **'Daunting'**
  String get severityLevel4;

  /// No description provided for @count.
  ///
  /// In en, this message translates to:
  /// **'Count'**
  String get count;

  /// No description provided for @encumTotal.
  ///
  /// In en, this message translates to:
  /// **'Encumbrance (Total)'**
  String get encumTotal;

  /// No description provided for @advNeeded.
  ///
  /// In en, this message translates to:
  /// **'Advantage Needed'**
  String get advNeeded;

  /// No description provided for @damage.
  ///
  /// In en, this message translates to:
  /// **'Damage'**
  String get damage;

  /// No description provided for @critical.
  ///
  /// In en, this message translates to:
  /// **'Critical'**
  String get critical;

  /// No description provided for @hardPoints.
  ///
  /// In en, this message translates to:
  /// **'Hard Points'**
  String get hardPoints;

  /// No description provided for @encum.
  ///
  /// In en, this message translates to:
  /// **'Encumbrance'**
  String get encum;

  /// No description provided for @firingArc.
  ///
  /// In en, this message translates to:
  /// **'Firing Arc'**
  String get firingArc;

  /// No description provided for @range.
  ///
  /// In en, this message translates to:
  /// **'Range'**
  String get range;

  /// No description provided for @weapRange.
  ///
  /// In en, this message translates to:
  /// **'Weapon Range'**
  String get weapRange;

  /// No description provided for @rangeLevel1.
  ///
  /// In en, this message translates to:
  /// **'Engaged'**
  String get rangeLevel1;

  /// No description provided for @rangeLevel2.
  ///
  /// In en, this message translates to:
  /// **'Short'**
  String get rangeLevel2;

  /// No description provided for @rangeLevel3.
  ///
  /// In en, this message translates to:
  /// **'Medium'**
  String get rangeLevel3;

  /// No description provided for @rangeLevel4.
  ///
  /// In en, this message translates to:
  /// **'Long'**
  String get rangeLevel4;

  /// No description provided for @rangeLevel5.
  ///
  /// In en, this message translates to:
  /// **'Extreme'**
  String get rangeLevel5;

  /// No description provided for @itemDamage.
  ///
  /// In en, this message translates to:
  /// **'Item Damage'**
  String get itemDamage;

  /// No description provided for @damageLevel1.
  ///
  /// In en, this message translates to:
  /// **'Undamaged'**
  String get damageLevel1;

  /// No description provided for @damageLevel2.
  ///
  /// In en, this message translates to:
  /// **'Minor'**
  String get damageLevel2;

  /// No description provided for @damageLevel3.
  ///
  /// In en, this message translates to:
  /// **'Moderate'**
  String get damageLevel3;

  /// No description provided for @damageLevel4.
  ///
  /// In en, this message translates to:
  /// **'Major'**
  String get damageLevel4;

  /// No description provided for @damageLevel5.
  ///
  /// In en, this message translates to:
  /// **'Broken'**
  String get damageLevel5;

  /// No description provided for @addCharacteristic.
  ///
  /// In en, this message translates to:
  /// **'Add Characteristic'**
  String get addCharacteristic;

  /// No description provided for @addBrawn.
  ///
  /// In en, this message translates to:
  /// **'Add Brawn to Damage'**
  String get addBrawn;

  /// No description provided for @loaded.
  ///
  /// In en, this message translates to:
  /// **'Loaded'**
  String get loaded;

  /// No description provided for @limitedAmmo.
  ///
  /// In en, this message translates to:
  /// **'Limited Ammo'**
  String get limitedAmmo;

  /// No description provided for @ammo.
  ///
  /// In en, this message translates to:
  /// **'Ammo'**
  String get ammo;

  /// Shown when weapons have at least one point in Pierce
  ///
  /// In en, this message translates to:
  /// **'Ignore {num} Soak'**
  String ignoreSoak(int num);

  /// No description provided for @species.
  ///
  /// In en, this message translates to:
  /// **'Species'**
  String get species;

  /// No description provided for @age.
  ///
  /// In en, this message translates to:
  /// **'Age'**
  String get age;

  /// No description provided for @motivation.
  ///
  /// In en, this message translates to:
  /// **'Motivation'**
  String get motivation;

  /// No description provided for @forceRating.
  ///
  /// In en, this message translates to:
  /// **'Force Rating:'**
  String get forceRating;

  /// No description provided for @conflict.
  ///
  /// In en, this message translates to:
  /// **'Conflict'**
  String get conflict;

  /// No description provided for @resolveConflict.
  ///
  /// In en, this message translates to:
  /// **'Resolve Conflict'**
  String get resolveConflict;

  /// Shown as a snackbar when resolving conflict.
  ///
  /// In en, this message translates to:
  /// **'You rolled a {result}'**
  String conflictResult(int result);

  /// No description provided for @emoStr.
  ///
  /// In en, this message translates to:
  /// **'Emotional Strength:'**
  String get emoStr;

  /// No description provided for @emoWeak.
  ///
  /// In en, this message translates to:
  /// **'Emotional Weakness:'**
  String get emoWeak;

  /// No description provided for @wound.
  ///
  /// In en, this message translates to:
  /// **'Wound'**
  String get wound;

  /// No description provided for @maxWound.
  ///
  /// In en, this message translates to:
  /// **'Max Wound'**
  String get maxWound;

  /// No description provided for @strain.
  ///
  /// In en, this message translates to:
  /// **'Strain'**
  String get strain;

  /// No description provided for @maxStrain.
  ///
  /// In en, this message translates to:
  /// **'Max Strain'**
  String get maxStrain;

  /// Used to display the max wound, strain, threashhold, and stress
  ///
  /// In en, this message translates to:
  /// **'Max: {number}'**
  String max(int number);

  /// No description provided for @stimpacks.
  ///
  /// In en, this message translates to:
  /// **'Stimpacks'**
  String get stimpacks;

  /// No description provided for @emergencyRepairPatches.
  ///
  /// In en, this message translates to:
  /// **'Emergency Repair Patches'**
  String get emergencyRepairPatches;

  /// No description provided for @healsUsedToday.
  ///
  /// In en, this message translates to:
  /// **'Heals Used Today:'**
  String get healsUsedToday;

  /// No description provided for @heal.
  ///
  /// In en, this message translates to:
  /// **'Heal'**
  String get heal;

  /// No description provided for @reset.
  ///
  /// In en, this message translates to:
  /// **'Reset'**
  String get reset;

  /// No description provided for @current.
  ///
  /// In en, this message translates to:
  /// **'Current:'**
  String get current;

  /// No description provided for @total.
  ///
  /// In en, this message translates to:
  /// **'Total:'**
  String get total;

  /// No description provided for @addXP.
  ///
  /// In en, this message translates to:
  /// **'Add XP'**
  String get addXP;

  /// No description provided for @melee.
  ///
  /// In en, this message translates to:
  /// **'Melee'**
  String get melee;

  /// No description provided for @ranged.
  ///
  /// In en, this message translates to:
  /// **'Ranged'**
  String get ranged;

  /// No description provided for @credits.
  ///
  /// In en, this message translates to:
  /// **'Credits:'**
  String get credits;

  /// No description provided for @encumCap.
  ///
  /// In en, this message translates to:
  /// **'Encumbrance Capacity:'**
  String get encumCap;

  /// No description provided for @overEncumNotice.
  ///
  /// In en, this message translates to:
  /// **'You are over-encumbered! Add a setback die to all Agility and Brawn checks.'**
  String get overEncumNotice;

  /// No description provided for @brokenWeapon.
  ///
  /// In en, this message translates to:
  /// **'Weapon is broken and cannot fire!'**
  String get brokenWeapon;

  /// No description provided for @weaponOutOfAmmo.
  ///
  /// In en, this message translates to:
  /// **'Weapon is out of Ammo!'**
  String get weaponOutOfAmmo;

  /// No description provided for @minNum.
  ///
  /// In en, this message translates to:
  /// **'Number of Minions:'**
  String get minNum;

  /// No description provided for @minWound.
  ///
  /// In en, this message translates to:
  /// **'Wound per Minion:'**
  String get minWound;

  /// No description provided for @hullTrauma.
  ///
  /// In en, this message translates to:
  /// **'Hull Trauma'**
  String get hullTrauma;

  /// No description provided for @maxTrauma.
  ///
  /// In en, this message translates to:
  /// **'Max Hull Trauma'**
  String get maxTrauma;

  /// No description provided for @sysStress.
  ///
  /// In en, this message translates to:
  /// **'System Stress'**
  String get sysStress;

  /// No description provided for @maxStress.
  ///
  /// In en, this message translates to:
  /// **'Max System Stress'**
  String get maxStress;

  /// No description provided for @totalDefense.
  ///
  /// In en, this message translates to:
  /// **'Total Defense (For Angling)'**
  String get totalDefense;

  /// No description provided for @fore.
  ///
  /// In en, this message translates to:
  /// **'Fore'**
  String get fore;

  /// No description provided for @port.
  ///
  /// In en, this message translates to:
  /// **'Port'**
  String get port;

  /// No description provided for @starboard.
  ///
  /// In en, this message translates to:
  /// **'Starboard'**
  String get starboard;

  /// No description provided for @aft.
  ///
  /// In en, this message translates to:
  /// **'Aft'**
  String get aft;

  /// No description provided for @anglingWarning.
  ///
  /// In en, this message translates to:
  /// **'WARNING: Angled defense does not equal total defense'**
  String get anglingWarning;

  /// No description provided for @silhouette.
  ///
  /// In en, this message translates to:
  /// **'Silhouette'**
  String get silhouette;

  /// No description provided for @speed.
  ///
  /// In en, this message translates to:
  /// **'Speed'**
  String get speed;

  /// No description provided for @armor.
  ///
  /// In en, this message translates to:
  /// **'Armor'**
  String get armor;

  /// No description provided for @handling.
  ///
  /// In en, this message translates to:
  /// **'Handling'**
  String get handling;

  /// No description provided for @passengers.
  ///
  /// In en, this message translates to:
  /// **'Passengers'**
  String get passengers;

  /// No description provided for @soak.
  ///
  /// In en, this message translates to:
  /// **'Soak:'**
  String get soak;

  /// No description provided for @disableForce.
  ///
  /// In en, this message translates to:
  /// **'Disable Force'**
  String get disableForce;

  /// No description provided for @disableMorality.
  ///
  /// In en, this message translates to:
  /// **'Disable Morality'**
  String get disableMorality;

  /// No description provided for @disableDuty.
  ///
  /// In en, this message translates to:
  /// **'Disable Duty'**
  String get disableDuty;

  /// No description provided for @disableObligation.
  ///
  /// In en, this message translates to:
  /// **'Disable Obligation'**
  String get disableObligation;

  /// No description provided for @clone.
  ///
  /// In en, this message translates to:
  /// **'Clone'**
  String get clone;

  /// Default name of a cloned profile.
  ///
  /// In en, this message translates to:
  /// **'Copy of {name}'**
  String copyOf(String name);

  /// No description provided for @stats.
  ///
  /// In en, this message translates to:
  /// **'Stats'**
  String get stats;

  /// No description provided for @notes.
  ///
  /// In en, this message translates to:
  /// **'Notes'**
  String get notes;

  /// No description provided for @deletedDuty.
  ///
  /// In en, this message translates to:
  /// **'Duty Deleted'**
  String get deletedDuty;

  /// No description provided for @deletedFP.
  ///
  /// In en, this message translates to:
  /// **'Force Power Deleted'**
  String get deletedFP;

  /// No description provided for @deletedObli.
  ///
  /// In en, this message translates to:
  /// **'Obligation Deleted'**
  String get deletedObli;

  /// No description provided for @deletedSpecialization.
  ///
  /// In en, this message translates to:
  /// **'Specialization Deleted'**
  String get deletedSpecialization;

  /// No description provided for @deletedSkill.
  ///
  /// In en, this message translates to:
  /// **'Skill Deleted'**
  String get deletedSkill;

  /// No description provided for @deletedTalent.
  ///
  /// In en, this message translates to:
  /// **'Talent Deleted'**
  String get deletedTalent;

  /// No description provided for @deletedItem.
  ///
  /// In en, this message translates to:
  /// **'Item Deleted'**
  String get deletedItem;

  /// No description provided for @deletedWeapon.
  ///
  /// In en, this message translates to:
  /// **'Weapon Deleted'**
  String get deletedWeapon;

  /// No description provided for @deletedNote.
  ///
  /// In en, this message translates to:
  /// **'Note Deleted'**
  String get deletedNote;

  /// No description provided for @deletedInjury.
  ///
  /// In en, this message translates to:
  /// **'Critical Injury Deleted'**
  String get deletedInjury;

  /// No description provided for @undo.
  ///
  /// In en, this message translates to:
  /// **'Undo'**
  String get undo;

  /// No description provided for @characterTrashed.
  ///
  /// In en, this message translates to:
  /// **'Character moved to trash'**
  String get characterTrashed;

  /// No description provided for @minionTrashed.
  ///
  /// In en, this message translates to:
  /// **'Minion moved to trash'**
  String get minionTrashed;

  /// No description provided for @vehicleTrashed.
  ///
  /// In en, this message translates to:
  /// **'Vehicle moved to trash'**
  String get vehicleTrashed;

  /// Used when permanently deleting profiles from the trash can.
  ///
  /// In en, this message translates to:
  /// **'{name} deleted permanently'**
  String deletedPermanently(String name);

  /// Used when restoring trashed profiles
  ///
  /// In en, this message translates to:
  /// **'Restored {name}'**
  String restored(String name);

  /// No description provided for @trashNotice.
  ///
  /// In en, this message translates to:
  /// **'Trashed profiles are deleted after 30 days'**
  String get trashNotice;

  /// No description provided for @fire.
  ///
  /// In en, this message translates to:
  /// **'Fire!'**
  String get fire;

  /// Shortened form of advantage. Should be 2 or 3 characters long
  ///
  /// In en, this message translates to:
  /// **'Adv'**
  String get advantageShort;

  /// No description provided for @forceDark.
  ///
  /// In en, this message translates to:
  /// **'Force Dark Side'**
  String get forceDark;

  /// No description provided for @forceLight.
  ///
  /// In en, this message translates to:
  /// **'Force Light Side'**
  String get forceLight;

  /// No description provided for @amoledTheme.
  ///
  /// In en, this message translates to:
  /// **'Amoled (Pitch Black) Dark Theme'**
  String get amoledTheme;

  /// No description provided for @systemLanguage.
  ///
  /// In en, this message translates to:
  /// **'System Language'**
  String get systemLanguage;

  /// No description provided for @healthMode.
  ///
  /// In en, this message translates to:
  /// **'Health Mode'**
  String get healthMode;

  /// No description provided for @subtractive.
  ///
  /// In en, this message translates to:
  /// **'Subtractive'**
  String get subtractive;

  /// No description provided for @subtractiveExplaination.
  ///
  /// In en, this message translates to:
  /// **'Health starts at max and damage reduces it'**
  String get subtractiveExplaination;

  /// No description provided for @additive.
  ///
  /// In en, this message translates to:
  /// **'Additive'**
  String get additive;

  /// No description provided for @additiveExplaination.
  ///
  /// In en, this message translates to:
  /// **'Health starts at 0 and damage increases it'**
  String get additiveExplaination;

  /// No description provided for @colorDice.
  ///
  /// In en, this message translates to:
  /// **'Color Dice Roller'**
  String get colorDice;

  /// No description provided for @darkstorm.
  ///
  /// In en, this message translates to:
  /// **'Darkstorm backend'**
  String get darkstorm;

  /// No description provided for @darkstormSub.
  ///
  /// In en, this message translates to:
  /// **'Needed for profiles sharing'**
  String get darkstormSub;

  /// No description provided for @darkstormCrash.
  ///
  /// In en, this message translates to:
  /// **'Crash reporting'**
  String get darkstormCrash;

  /// No description provided for @darkstormCount.
  ///
  /// In en, this message translates to:
  /// **'Darkstorm user count'**
  String get darkstormCount;

  /// No description provided for @aboutText.
  ///
  /// In en, this message translates to:
  /// **'See https://github.com/CalebQ42/SWAssistant for license and other info'**
  String get aboutText;

  /// No description provided for @ads.
  ///
  /// In en, this message translates to:
  /// **'Ads'**
  String get ads;

  /// No description provided for @cloudSave.
  ///
  /// In en, this message translates to:
  /// **'Cloud Save (via Google Drive)'**
  String get cloudSave;

  /// No description provided for @disableAnimations.
  ///
  /// In en, this message translates to:
  /// **'Disable Animations (For Performance)'**
  String get disableAnimations;

  /// No description provided for @showIntro.
  ///
  /// In en, this message translates to:
  /// **'Show Intro'**
  String get showIntro;

  /// No description provided for @shareCode.
  ///
  /// In en, this message translates to:
  /// **'Share Code'**
  String get shareCode;

  /// No description provided for @uploadDisclaimer.
  ///
  /// In en, this message translates to:
  /// **'The share code will be valid for 12 hours'**
  String get uploadDisclaimer;

  /// No description provided for @uploading.
  ///
  /// In en, this message translates to:
  /// **'Uploading profile'**
  String get uploading;

  /// No description provided for @uploadFailed.
  ///
  /// In en, this message translates to:
  /// **'Uploading profile failed:'**
  String get uploadFailed;

  /// No description provided for @failSize.
  ///
  /// In en, this message translates to:
  /// **'Profile too large'**
  String get failSize;

  /// No description provided for @failServer.
  ///
  /// In en, this message translates to:
  /// **'Server error'**
  String get failServer;

  /// No description provided for @failTimeout.
  ///
  /// In en, this message translates to:
  /// **'Timed out'**
  String get failTimeout;

  /// No description provided for @downloadFailed.
  ///
  /// In en, this message translates to:
  /// **'Download Failed'**
  String get downloadFailed;

  /// No description provided for @noConnectionDarkstorm.
  ///
  /// In en, this message translates to:
  /// **'Can\'t connect to Darkstorm backend'**
  String get noConnectionDarkstorm;
}

class _AppLocalizationsDelegate extends LocalizationsDelegate<AppLocalizations> {
  const _AppLocalizationsDelegate();

  @override
  Future<AppLocalizations> load(Locale locale) {
    return SynchronousFuture<AppLocalizations>(lookupAppLocalizations(locale));
  }

  @override
  bool isSupported(Locale locale) => <String>['de', 'en', 'es', 'fr', 'it'].contains(locale.languageCode);

  @override
  bool shouldReload(_AppLocalizationsDelegate old) => false;
}

AppLocalizations lookupAppLocalizations(Locale locale) {


  // Lookup logic when only language code is specified.
  switch (locale.languageCode) {
    case 'de': return AppLocalizationsDe();
    case 'en': return AppLocalizationsEn();
    case 'es': return AppLocalizationsEs();
    case 'fr': return AppLocalizationsFr();
    case 'it': return AppLocalizationsIt();
  }

  throw FlutterError(
    'AppLocalizations.delegate failed to load unsupported locale "$locale". This is likely '
    'an issue with the localizations generation tool. Please file an issue '
    'on GitHub with a reproducible sample app and the gen-l10n configuration '
    'that was used.'
  );
}
