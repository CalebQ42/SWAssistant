// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for Spanish Castilian (`es`).
class AppLocalizationsEs extends AppLocalizations {
  AppLocalizationsEs([String locale = 'es']) : super(locale);

  @override
  String get introWelcome => 'Welcome!';

  @override
  String get introPage0Line1 => 'Thank you for trying SWAssistant. Before we get started, you first have to set up some preferences.';

  @override
  String get introPage0WebNoticev2 => 'As this is the web vesion, it makes use of Google Drive to save and load. After setup you will be asked to sign into Google. Additionally it requires cookies to function and by using it you agree to it\'s ';

  @override
  String get introPage0WebNoticeEnd => 'cookie policy.';

  @override
  String get introPage0Line2 => 'Additionally, if you used a previous version of this app, we will need to import your old profiles first.';

  @override
  String get introPage0ImportButton => 'Import Profiles';

  @override
  String get introPage0ManualImport => 'Manual Import';

  @override
  String get introPage0ManualImportLine0 => 'Due to the stupid way old versions of SWAssistant saved profiles and changes to Android, you\'ll need to manually import your profiles. If you used cloud saving, you can alternatively skip this and turn on cloud saving. When you hit continue, a file explorer should pop up.';

  @override
  String get introPage0ManualImportLine1 => '1) Open up the Navigation Drawer (the three lines in the top left corner) and select your phone';

  @override
  String get introPage0ManualImportLine1a => '1a) If your phone is not there, you may have to open up the overflow menu (the three dots in the top right corner) and tap \"Show Internal Storage\"';

  @override
  String get introPage0ManualImportLine2 => '2) Select the folder named SWChars';

  @override
  String get introPage0ManualImportLine3 => '3) Long press on one of the files until it goes into multi-select mode, then select all the other files in the folder';

  @override
  String get introPage0ManualImportLine4 => '4) Click the Select Files button.';

  @override
  String get introPage1Title => 'Stupid Backend';

  @override
  String get introPage1DarkstormExplaination => 'This app (optionally) uses my custom backend called Darkstorm Backend. It is completely open source and collects zero information other then what\'s needed. It\'s used for crash reporting, user counts, and sharing profiles. No sensitive information is collected or stored.';

  @override
  String get introDarkstormExistingUsers => 'Firebase has been replaced with my custom backend called Darkstorm Backend. It is completely open source and collects zero information other then what\'s needed. It\'s used for crash reporting, logging the app\'s startup (for user count), and sharing profiles. No sensitive information is collected or stored.';

  @override
  String importSuccess(int number) {
    return 'Successfully imported $number profiles.';
  }

  @override
  String get importDialog => 'Importing Profiles...';

  @override
  String get importNone => 'You seemed to have not selected any files';

  @override
  String get loadingDialog => 'Loading...';

  @override
  String get driveError => 'Can\'t sync with Google Drive';

  @override
  String get driveErrorWebSub => 'Google Drive syncing is required for the web version of SWAssistant. The sign in pop-up might have been blocked by your browser.';

  @override
  String get driveErrorOfflineSub => 'If you continue offline, you can refresh on the character list to re-connect.';

  @override
  String get driveContinue => 'Continue offline';

  @override
  String get retry => 'Retry';

  @override
  String get driveSyncing => 'Syncing Google Drive';

  @override
  String get driveSyncingWeb => 'You may need to allow and open a pop-up to coninue';

  @override
  String get syncFail => 'Syncing failed';

  @override
  String get syncComplete => 'Syncing Complete';

  @override
  String get driveSyncingNotice => 'Google Drive is syncing';

  @override
  String get driveDisconnectNotice => 'Google Drive is disconnect';

  @override
  String get driveFirstLaunch => 'Syncing your profiles to Google Drive is now available. Go to Settings to enable it?';

  @override
  String get driveEnableFail => 'Google Drive failed to initialize';

  @override
  String get driveFull => 'Google Drive storage full';

  @override
  String get gmModeTap => 'Select a profile on the left to edit.';

  @override
  String get gmModeType => 'Type...';

  @override
  String get translate => 'Ayuda a traducir!';

  @override
  String get discuss => 'Discuss/Announcements';

  @override
  String get success => 'Éxito';

  @override
  String get failure => 'Fallo';

  @override
  String get advantage => 'Ventaja';

  @override
  String get threat => 'Amenaza';

  @override
  String get triumph => 'Triunfo';

  @override
  String get despair => 'Desesperación';

  @override
  String get lightSide => 'Lado luminoso';

  @override
  String get darkSide => 'Dark Side';

  @override
  String get ability => 'Ability';

  @override
  String get proficiency => 'Proficiency';

  @override
  String get difficulty => 'Difficulty';

  @override
  String get challenge => 'Challenge';

  @override
  String get boost => 'Boost';

  @override
  String get setback => 'Setback';

  @override
  String get force => 'Force';

  @override
  String get brawn => 'Fortaleza';

  @override
  String get agility => 'Agilidad';

  @override
  String get intellect => 'Intelecto';

  @override
  String get cunning => 'Astucia';

  @override
  String get willpower => 'Voluntad';

  @override
  String get presence => 'Presencia';

  @override
  String get roll => 'Lanzar';

  @override
  String get rollCrit => 'Roll Critical';

  @override
  String get gmMode => 'Modo DJ';

  @override
  String get profiles => 'Perfiles';

  @override
  String get characters => 'Personajes';

  @override
  String get minions => 'Esbirros';

  @override
  String get vehicles => 'Vehículos';

  @override
  String get trash => 'Trash';

  @override
  String get download => 'Descargar';

  @override
  String get otherStuff => 'Otras cosas';

  @override
  String get dice => 'Dados';

  @override
  String get guide => 'Guía';

  @override
  String get settings => 'Configuración';

  @override
  String get donate => 'Donar';

  @override
  String get all => 'All';

  @override
  String get uncategorized => 'Uncategorized';

  @override
  String get categories => 'Categories';

  @override
  String get edit => 'Edit';

  @override
  String get donoOptions => 'Donation Options';

  @override
  String get gPlay => 'Google Play';

  @override
  String get gPlayDesc => 'Easy to use with support for most currencies, but donations are capped and Google takes a 15% cut';

  @override
  String get sponsors => 'Github Sponsors';

  @override
  String get sponsorsDesc => 'You can donate either one time or on a monthly basis, with custom amounts, AND 100% of your money goes to the developer';

  @override
  String gPlayDonate(String amount) {
    return 'Donate $amount';
  }

  @override
  String get gPlayPurchase => 'Purchase';

  @override
  String get gPlayUnavailable => 'Google Play is currently unavailable';

  @override
  String get gPlayThanks => 'Thank you for your donation :D';

  @override
  String get gPlayPurchasePending => 'Purchase Pending...';

  @override
  String get newCharacter => 'New Character';

  @override
  String get newMinion => 'New Minion';

  @override
  String get newVehicle => 'New Vehicle';

  @override
  String get woundStrain => 'Wound and Strain';

  @override
  String get specializationPlural => 'Specializations';

  @override
  String get forcePowerPlural => 'Force Powers';

  @override
  String get xp => 'XP';

  @override
  String get morality => 'Morality';

  @override
  String get skillPlural => 'Skills';

  @override
  String get saveWeaponsFirst => 'Save weapons first!';

  @override
  String get restoreWeapons => 'Restore saved weapons';

  @override
  String get weaponsRestored => 'Weapons Restored';

  @override
  String get saveWeapons => 'Save weapons';

  @override
  String get overwriteWeapons => 'Overwrite saved weapons';

  @override
  String get weaponsSaved => 'Weapons Saved';

  @override
  String get saveInventoryFirst => 'Save inventory first!';

  @override
  String get restoreInventory => 'Restore saved inventory';

  @override
  String get inventoryRestored => 'Inventory Restored';

  @override
  String get saveInventory => 'Save inventory';

  @override
  String get overwriteInventory => 'Overwrite saved inventory';

  @override
  String get inventorySaved => 'Inventory Saved';

  @override
  String get criticalHits => 'Critical Hits';

  @override
  String get basicInfo => 'Basic Information';

  @override
  String get defense => 'Defense';

  @override
  String get weaponPlural => 'Weapons';

  @override
  String get characteristicPlural => 'Characteristics';

  @override
  String get talentPlural => 'Talents';

  @override
  String get inventory => 'Inventory';

  @override
  String get criticalInj => 'Critical Injuries';

  @override
  String get characteristicAccurate => 'Accurate';

  @override
  String get characteristicBreach => 'Breach';

  @override
  String get characteristicCumbersome => 'Cumbersome';

  @override
  String get characteristicInaccurate => 'Inaccurate';

  @override
  String get characteristicInferior => 'Inferior';

  @override
  String get characteristicPierce => 'Pierce';

  @override
  String get characteristicVicious => 'Vicious';

  @override
  String get ret => 'Return';

  @override
  String get desc => 'Description';

  @override
  String get name => 'Name';

  @override
  String get rank => 'Rank';

  @override
  String get characteristic => 'Characteristic';

  @override
  String get category => 'Category';

  @override
  String get duty => 'Duty';

  @override
  String get forcePower => 'Force Power';

  @override
  String get obligation => 'Obligation';

  @override
  String get specialization => 'Specialization';

  @override
  String get skill => 'Skill';

  @override
  String get talent => 'Talent';

  @override
  String get item => 'Item';

  @override
  String get weapon => 'Weapon';

  @override
  String get value => 'Value';

  @override
  String get career => 'Career';

  @override
  String get skills1 => 'Astronavegación';

  @override
  String get skills2 => 'Atletismo';

  @override
  String get skills3 => 'Pelea';

  @override
  String get skills4 => 'Carisma';

  @override
  String get skills5 => 'Coacción';

  @override
  String get skills6 => 'Computadoras';

  @override
  String get skills7 => 'Frialdad';

  @override
  String get skills8 => 'Coordinación';

  @override
  String get skills9 => 'Engaño';

  @override
  String get skills10 => 'Disciplina';

  @override
  String get skills11 => 'Artillería';

  @override
  String get skills12 => 'Liderazgo';

  @override
  String get skills13 => 'Sable de luz';

  @override
  String get skills14 => 'Mecánica';

  @override
  String get skills15 => 'Medicina';

  @override
  String get skills16 => 'Armas cuerpo a cuerpo';

  @override
  String get skills17 => 'Negociación';

  @override
  String get skills18 => 'Percepción';

  @override
  String get skills19 => 'Pilotar (planetario)';

  @override
  String get skills20 => 'Pilotar (Espacial)';

  @override
  String get skills21 => 'Armas a distancia (pesadas)';

  @override
  String get skills22 => 'Armas a distancia (ligeras)';

  @override
  String get skills23 => 'Aguante';

  @override
  String get skills24 => 'Actividad Criminal';

  @override
  String get skills25 => 'Sigilo';

  @override
  String get skills26 => 'Callejeo';

  @override
  String get skills27 => 'Supervivencia';

  @override
  String get skills28 => 'Alerta';

  @override
  String get skills29 => 'Mundos del Núcleo';

  @override
  String get skills30 => 'Educación';

  @override
  String get skills31 => 'Saber arcaico';

  @override
  String get skills32 => 'Borde Exterior';

  @override
  String get skills33 => 'Bajos fondos';

  @override
  String get skills34 => 'Xenología';

  @override
  String get skills35 => 'Other...';

  @override
  String get skillName => 'Skill Name';

  @override
  String get severity => 'Severity';

  @override
  String get severityLevel1 => 'Fácil';

  @override
  String get severityLevel2 => 'Media';

  @override
  String get severityLevel3 => 'Difícil';

  @override
  String get severityLevel4 => 'Desalentadora';

  @override
  String get count => 'Count';

  @override
  String get encumTotal => 'Encumbrance (Total)';

  @override
  String get advNeeded => 'Ventajas necesarias';

  @override
  String get damage => 'Damage';

  @override
  String get critical => 'Crítico';

  @override
  String get hardPoints => 'Hard Points';

  @override
  String get encum => 'Encumbrance';

  @override
  String get firingArc => 'Firing Arc';

  @override
  String get range => 'Range';

  @override
  String get weapRange => 'Weapon Range';

  @override
  String get rangeLevel1 => 'Interacción';

  @override
  String get rangeLevel2 => 'Corto';

  @override
  String get rangeLevel3 => 'Medio';

  @override
  String get rangeLevel4 => 'Largo';

  @override
  String get rangeLevel5 => 'Extremo';

  @override
  String get itemDamage => 'Item Damage';

  @override
  String get damageLevel1 => 'Undamaged';

  @override
  String get damageLevel2 => 'Menor';

  @override
  String get damageLevel3 => 'Moderado';

  @override
  String get damageLevel4 => 'Extremo';

  @override
  String get damageLevel5 => 'Broken';

  @override
  String get addCharacteristic => 'Add Characteristic';

  @override
  String get addBrawn => 'Add Brawn to Damage';

  @override
  String get loaded => 'Cargada';

  @override
  String get limitedAmmo => 'Limited Ammo';

  @override
  String get ammo => 'Ammo';

  @override
  String ignoreSoak(int num) {
    return 'Ignore $num Soak';
  }

  @override
  String get species => 'Species';

  @override
  String get age => 'Age';

  @override
  String get motivation => 'Motivation';

  @override
  String get forceRating => 'Dominio de la Fuerza:';

  @override
  String get conflict => 'Conflict';

  @override
  String get resolveConflict => 'Resolver conflicto';

  @override
  String conflictResult(int result) {
    return 'You rolled a $result';
  }

  @override
  String get emoStr => 'Emotional Strength:';

  @override
  String get emoWeak => 'Emotional Weakness:';

  @override
  String get wound => 'Wound';

  @override
  String get maxWound => 'Max Wound';

  @override
  String get strain => 'Strain';

  @override
  String get maxStrain => 'Max Strain';

  @override
  String max(int number) {
    return 'Max: $number';
  }

  @override
  String get stimpacks => 'Stimpacks';

  @override
  String get emergencyRepairPatches => 'Emergency Repair Patches';

  @override
  String get healsUsedToday => 'Heals Used Today:';

  @override
  String get heal => 'Heal';

  @override
  String get reset => 'Reset';

  @override
  String get current => 'Current:';

  @override
  String get total => 'Total:';

  @override
  String get addXP => 'Add XP';

  @override
  String get melee => 'Armas cuerpo a cuerpo';

  @override
  String get ranged => 'Ranged';

  @override
  String get credits => 'Créditos:';

  @override
  String get encumCap => 'Capacidad de impedimento:';

  @override
  String get overEncumNotice => 'You are over-encumbered! Add a setback die to all Agility and Brawn checks.';

  @override
  String get brokenWeapon => 'Weapon is broken and cannot fire!';

  @override
  String get weaponOutOfAmmo => 'Weapon is out of Ammo!';

  @override
  String get minNum => 'Número de Esbirros:';

  @override
  String get minWound => 'Heridas por Esbirro:';

  @override
  String get hullTrauma => 'Hull Trauma';

  @override
  String get maxTrauma => 'Max Hull Trauma';

  @override
  String get sysStress => 'System Stress';

  @override
  String get maxStress => 'Max System Stress';

  @override
  String get totalDefense => 'Total Defense (For Angling)';

  @override
  String get fore => 'Fore';

  @override
  String get port => 'Port';

  @override
  String get starboard => 'Starboard';

  @override
  String get aft => 'Aft';

  @override
  String get anglingWarning => 'WARNING: Angled defense does not equal total defense';

  @override
  String get silhouette => 'Silhouette';

  @override
  String get speed => 'Speed';

  @override
  String get armor => 'Armor';

  @override
  String get handling => 'Handling';

  @override
  String get passengers => 'Passengers';

  @override
  String get soak => 'Protección:';

  @override
  String get disableForce => 'Disable Force';

  @override
  String get disableMorality => 'Disable Morality';

  @override
  String get disableDuty => 'Disable Duty';

  @override
  String get disableObligation => 'Disable Obligation';

  @override
  String get clone => 'Clonar';

  @override
  String copyOf(String name) {
    return 'Copy of $name';
  }

  @override
  String get stats => 'Stats';

  @override
  String get notes => 'Notas';

  @override
  String get deletedDuty => 'Duty Deleted';

  @override
  String get deletedFP => 'Force Power Deleted';

  @override
  String get deletedObli => 'Obligation Deleted';

  @override
  String get deletedSpecialization => 'Specialization Deleted';

  @override
  String get deletedSkill => 'Skill Deleted';

  @override
  String get deletedTalent => 'Talent Deleted';

  @override
  String get deletedItem => 'Item Deleted';

  @override
  String get deletedWeapon => 'Weapon Deleted';

  @override
  String get deletedNote => 'Note Deleted';

  @override
  String get deletedInjury => 'Critical Injury Deleted';

  @override
  String get undo => 'Undo';

  @override
  String get characterTrashed => 'Character moved to trash';

  @override
  String get minionTrashed => 'Minion moved to trash';

  @override
  String get vehicleTrashed => 'Vehicle moved to trash';

  @override
  String deletedPermanently(String name) {
    return '$name deleted permanently';
  }

  @override
  String restored(String name) {
    return 'Restored $name';
  }

  @override
  String get trashNotice => 'Trashed profiles are deleted after 30 days';

  @override
  String get fire => 'Fire!';

  @override
  String get advantageShort => 'Adv';

  @override
  String get forceDark => 'Force Dark Side';

  @override
  String get forceLight => 'Force Light Side';

  @override
  String get amoledTheme => 'Amoled (Pitch Black) Dark Theme';

  @override
  String get systemLanguage => 'System Language';

  @override
  String get healthMode => 'Health Mode';

  @override
  String get subtractive => 'Subtractive';

  @override
  String get subtractiveExplaination => 'Health starts at max and damage reduces it';

  @override
  String get additive => 'Additive';

  @override
  String get additiveExplaination => 'Health starts at 0 and damage increases it';

  @override
  String get colorDice => 'Color Dice Roller';

  @override
  String get darkstorm => 'Darkstorm backend';

  @override
  String get darkstormSub => 'Needed for profiles sharing';

  @override
  String get darkstormCrash => 'Crash reporting';

  @override
  String get darkstormCount => 'Darkstorm user count';

  @override
  String get aboutText => 'See https://github.com/CalebQ42/SWAssistant for license and other info';

  @override
  String get ads => 'Ads';

  @override
  String get cloudSave => 'Cloud Save (via Google Drive)';

  @override
  String get disableAnimations => 'Disable Animations (For Performance)';

  @override
  String get showIntro => 'Show Intro';

  @override
  String get shareCode => 'Share Code';

  @override
  String get uploadDisclaimer => 'The share code will be valid for 12 hours';

  @override
  String get uploading => 'Uploading profile';

  @override
  String get uploadFailed => 'Uploading profile failed:';

  @override
  String get failSize => 'Profile too large';

  @override
  String get failServer => 'Server error';

  @override
  String get failTimeout => 'Timed out';

  @override
  String get downloadFailed => 'Download Failed';

  @override
  String get noConnectionDarkstorm => 'Can\'t connect to Darkstorm backend';
}
