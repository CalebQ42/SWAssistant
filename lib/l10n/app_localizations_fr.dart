// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for French (`fr`).
class AppLocalizationsFr extends AppLocalizations {
  AppLocalizationsFr([String locale = 'fr']) : super(locale);

  @override
  String get introWelcome => 'Bienvenue !';

  @override
  String get introPage0Line1 => 'Merci d\'avoir essayé SWAssistant. Avant de commencer, vous devez d\'abord configurer certaines préférences.';

  @override
  String get introPage0WebNoticev2 => 'As this is the web vesion, it makes use of Google Drive to save and load. After setup you will be asked to sign into Google. Additionally it requires cookies to function and by using it you agree to it\'s ';

  @override
  String get introPage0WebNoticeEnd => 'cookie policy.';

  @override
  String get introPage0Line2 => 'De plus, si vous avez utilisé une version précédente de cette application, nous devrons d\'abord importer vos anciens profils.';

  @override
  String get introPage0ImportButton => 'Importer des profils';

  @override
  String get introPage0ManualImport => 'Import manuel';

  @override
  String get introPage0ManualImportLine0 => 'En raison de la stupidité des anciennes versions de SWAssistant enregistrées des profils et des modifications apportées à Android, vous devrez importer manuellement vos profils. Si vous avez utilisé la sauvegarde du cloud, vous pouvez également sauter cela et activer la sauvegarde du cloud. Lorsque vous appuyez sur continuer, un explorateur de fichiers devrait apparaître.';

  @override
  String get introPage0ManualImportLine1 => '1) Ouvrez le tiroir de navigation (les trois lignes en haut à gauche) et sélectionnez votre téléphone';

  @override
  String get introPage0ManualImportLine1a => '1a) Si votre téléphone n\'est pas là, vous devrez peut-être ouvrir le menu de débordement (les trois points dans le coin supérieur droit) et appuyez sur \"Afficher le stockage interne\"';

  @override
  String get introPage0ManualImportLine2 => '2) Sélectionnez le dossier SWChars';

  @override
  String get introPage0ManualImportLine3 => '3) Appuyez longuement sur l\'un des fichiers jusqu\'à ce qu\'il passe en mode multi-sélection, puis sélectionnez tous les autres fichiers du dossier';

  @override
  String get introPage0ManualImportLine4 => '4) Cliquez sur le bouton Sélectionner les fichiers.';

  @override
  String get introPage1Title => 'Stupid Backend';

  @override
  String get introPage1DarkstormExplaination => 'This app (optionally) uses my custom backend called Darkstorm Backend. It is completely open source and collects zero information other then what\'s needed. It\'s used for crash reporting, user counts, and sharing profiles. No sensitive information is collected or stored.';

  @override
  String get introDarkstormExistingUsers => 'Firebase has been replaced with my custom backend called Darkstorm Backend. It is completely open source and collects zero information other then what\'s needed. It\'s used for crash reporting, logging the app\'s startup (for user count), and sharing profiles. No sensitive information is collected or stored.';

  @override
  String importSuccess(int number) {
    return '$number profils importés avec succès.';
  }

  @override
  String get importDialog => 'Importer des profils...';

  @override
  String get importNone => 'Vous semblez n\'avoir sélectionné aucun fichier';

  @override
  String get loadingDialog => 'Chargement ...';

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
  String get driveSyncing => 'Synchroniser avec Google Drive';

  @override
  String get driveSyncingWeb => 'You may need to allow and open a pop-up to coninue';

  @override
  String get syncFail => 'Échec de la synchronisation';

  @override
  String get syncComplete => 'Synchronisation Terminée';

  @override
  String get driveSyncingNotice => 'Synchronisation en cours de Google Drive';

  @override
  String get driveDisconnectNotice => 'Google Drive est désactivé';

  @override
  String get driveFirstLaunch => 'La synchronisation de vos profils avec Google Drive est maintenant disponible. Allez dans Paramètres pour l\'activer ?';

  @override
  String get driveEnableFail => 'L\'initialisation de Google Drive a échoué';

  @override
  String get driveFull => 'Google Drive storage full';

  @override
  String get gmModeTap => 'Sélectionnez un type de journal sur la gauche pour le modifier';

  @override
  String get gmModeType => 'Type...';

  @override
  String get translate => 'Aidez à la traduction!';

  @override
  String get discuss => 'Discuter/Annonces';

  @override
  String get success => 'Succès';

  @override
  String get failure => 'Échec';

  @override
  String get advantage => 'Avantage';

  @override
  String get threat => 'Menace';

  @override
  String get triumph => 'Triomphe';

  @override
  String get despair => 'Désastre';

  @override
  String get lightSide => 'Côté Lumineux';

  @override
  String get darkSide => 'Coté Obscur';

  @override
  String get ability => 'Avantage';

  @override
  String get proficiency => 'Compétence';

  @override
  String get difficulty => 'Difficulté';

  @override
  String get challenge => 'Défi';

  @override
  String get boost => 'Boost';

  @override
  String get setback => 'Echecs';

  @override
  String get force => 'Force';

  @override
  String get brawn => 'Vigueur';

  @override
  String get agility => 'Agilité';

  @override
  String get intellect => 'Intelligence';

  @override
  String get cunning => 'Ruse';

  @override
  String get willpower => 'Volonté';

  @override
  String get presence => 'Présence';

  @override
  String get roll => 'Lancer';

  @override
  String get rollCrit => 'Roll Critical';

  @override
  String get gmMode => 'Mode MJ';

  @override
  String get profiles => 'Profils';

  @override
  String get characters => 'Personnages';

  @override
  String get minions => 'Sbires';

  @override
  String get vehicles => 'Véhicules';

  @override
  String get trash => 'Poubelle';

  @override
  String get download => 'Télécharger';

  @override
  String get otherStuff => 'Autres trucs';

  @override
  String get dice => 'Dés';

  @override
  String get guide => 'Guide';

  @override
  String get settings => 'Paramètres';

  @override
  String get donate => 'Faire un don';

  @override
  String get all => 'Tout';

  @override
  String get uncategorized => 'Non-classés';

  @override
  String get categories => 'Catégories';

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
  String get name => 'Nom';

  @override
  String get rank => 'Rang';

  @override
  String get characteristic => 'Caractéristique';

  @override
  String get category => 'Catégorie';

  @override
  String get duty => 'Devoir';

  @override
  String get forcePower => 'Pouvoir de Force';

  @override
  String get obligation => 'Obligation';

  @override
  String get specialization => 'Spécialisation';

  @override
  String get skill => 'Compétence';

  @override
  String get talent => 'Talent';

  @override
  String get item => 'Objet';

  @override
  String get weapon => 'Arme';

  @override
  String get value => 'Valeur';

  @override
  String get career => 'Carrière';

  @override
  String get skills1 => 'Astrogation';

  @override
  String get skills2 => 'Athlétisme';

  @override
  String get skills3 => 'Pugilat';

  @override
  String get skills4 => 'Charme';

  @override
  String get skills5 => 'Coercition';

  @override
  String get skills6 => 'Informatique';

  @override
  String get skills7 => 'Calme';

  @override
  String get skills8 => 'Coordination';

  @override
  String get skills9 => 'Tromperie';

  @override
  String get skills10 => 'Sang-froid';

  @override
  String get skills11 => 'Artillerie';

  @override
  String get skills12 => 'Commandement';

  @override
  String get skills13 => 'Sabre laser';

  @override
  String get skills14 => 'Mécanique';

  @override
  String get skills15 => 'Médecine';

  @override
  String get skills16 => 'Corps à Corps';

  @override
  String get skills17 => 'Négociation';

  @override
  String get skills18 => 'Perception';

  @override
  String get skills19 => 'Pilitage(Planétaire)';

  @override
  String get skills20 => 'Pilotage(Espace)';

  @override
  String get skills21 => 'Distance(arme lourde)';

  @override
  String get skills22 => 'Distance(arme légère)';

  @override
  String get skills23 => 'Résistance';

  @override
  String get skills24 => 'Magouilles';

  @override
  String get skills25 => 'Discrétion';

  @override
  String get skills26 => 'Système D';

  @override
  String get skills27 => 'Survie';

  @override
  String get skills28 => 'Vigilance';

  @override
  String get skills29 => 'Connaissance(Monde du Noyau)';

  @override
  String get skills30 => 'Connaissance(Éducation)';

  @override
  String get skills31 => 'Connaissance(Culture)';

  @override
  String get skills32 => 'Connaissance(Bordure Extérieure)';

  @override
  String get skills33 => 'Connaissance(Pègre)';

  @override
  String get skills34 => 'Connaissance(Xenologie)';

  @override
  String get skills35 => 'Autre...';

  @override
  String get skillName => 'Skill Name';

  @override
  String get severity => 'Gravité';

  @override
  String get severityLevel1 => 'Facile';

  @override
  String get severityLevel2 => 'Moyen';

  @override
  String get severityLevel3 => 'Difficile';

  @override
  String get severityLevel4 => 'Intimidant';

  @override
  String get count => 'Nombre';

  @override
  String get encumTotal => 'Encombrement (Total)';

  @override
  String get advNeeded => 'Avantage nécessaire';

  @override
  String get damage => 'Damage';

  @override
  String get critical => 'Critique';

  @override
  String get hardPoints => 'Emplacements';

  @override
  String get encum => 'Encombrement';

  @override
  String get firingArc => 'Angle de tir';

  @override
  String get range => 'Portée';

  @override
  String get weapRange => 'Portée de l\'Arme';

  @override
  String get rangeLevel1 => 'Au Contact';

  @override
  String get rangeLevel2 => 'Courte';

  @override
  String get rangeLevel3 => 'Moyenne';

  @override
  String get rangeLevel4 => 'Longue';

  @override
  String get rangeLevel5 => 'Extrême';

  @override
  String get itemDamage => 'Dégâts sur l\'objet';

  @override
  String get damageLevel1 => 'Non endommagé';

  @override
  String get damageLevel2 => 'Légers';

  @override
  String get damageLevel3 => 'Modérés';

  @override
  String get damageLevel4 => 'Graves';

  @override
  String get damageLevel5 => 'Cassé';

  @override
  String get addCharacteristic => 'Ajouter Caractéristiques';

  @override
  String get addBrawn => 'Ajouter la vigueur aux Dégâts';

  @override
  String get loaded => 'Chargé';

  @override
  String get limitedAmmo => 'Munitions Limitées';

  @override
  String get ammo => 'Munition';

  @override
  String ignoreSoak(int num) {
    return 'Ignore $num Soak';
  }

  @override
  String get species => 'Espèces';

  @override
  String get age => 'Age';

  @override
  String get motivation => 'Motivation';

  @override
  String get forceRating => 'Valeur de Force:';

  @override
  String get conflict => 'Conflit';

  @override
  String get resolveConflict => 'Résoudre le Conflit';

  @override
  String conflictResult(int result) {
    return 'Vous avez roulé un $result';
  }

  @override
  String get emoStr => 'Force émotionnelle:';

  @override
  String get emoWeak => 'Faiblesse émotionnelle:';

  @override
  String get wound => 'Blessure';

  @override
  String get maxWound => 'Blessure max';

  @override
  String get strain => 'Stress';

  @override
  String get maxStrain => 'Stress max';

  @override
  String max(int number) {
    return 'Nombre max';
  }

  @override
  String get stimpacks => 'Stimpack';

  @override
  String get emergencyRepairPatches => 'Trousse de secours d urgence';

  @override
  String get healsUsedToday => 'Soins utilisés aujourd\'hui:';

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
  String get melee => 'Corps à Corps';

  @override
  String get ranged => 'Ranged';

  @override
  String get credits => 'Crédits:';

  @override
  String get encumCap => 'Charge Utile:';

  @override
  String get overEncumNotice => 'You are over-encumbered! Add a setback die to all Agility and Brawn checks.';

  @override
  String get brokenWeapon => 'Weapon is broken and cannot fire!';

  @override
  String get weaponOutOfAmmo => 'Weapon is out of Ammo!';

  @override
  String get minNum => 'Nombre de Sbires:';

  @override
  String get minWound => 'Blessure par Sbire:';

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
  String get soak => 'Encaissement:';

  @override
  String get disableForce => 'Disable Force';

  @override
  String get disableMorality => 'Disable Morality';

  @override
  String get disableDuty => 'Disable Duty';

  @override
  String get disableObligation => 'Disable Obligation';

  @override
  String get clone => 'Clone';

  @override
  String copyOf(String name) {
    return 'Copy of $name';
  }

  @override
  String get stats => 'Stats';

  @override
  String get notes => 'Notes';

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
  String get forceDark => 'Forcer le côté obscur';

  @override
  String get forceLight => 'Forcer le côté lumineux';

  @override
  String get amoledTheme => 'Thème sombre Amoled (noir)';

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
  String get colorDice => 'Lanceur de dés rapide';

  @override
  String get darkstorm => 'Darkstorm backend';

  @override
  String get darkstormSub => 'Needed for profiles sharing';

  @override
  String get darkstormCrash => 'Crash reporting';

  @override
  String get darkstormCount => 'Darkstorm user count';

  @override
  String get aboutText => 'Voir https://github.com/CalebQ42/SWAssistant pour la licence et d\'autres informations';

  @override
  String get ads => 'Annonces';

  @override
  String get cloudSave => 'Sauvegarde dans le Cloud (via Google Drive)';

  @override
  String get disableAnimations => 'Disable Animations (For Performance)';

  @override
  String get showIntro => 'Ignorer l\'introduction';

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
