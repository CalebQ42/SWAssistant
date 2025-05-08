// ignore: unused_import
import 'package:intl/intl.dart' as intl;
import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for Italian (`it`).
class AppLocalizationsIt extends AppLocalizations {
  AppLocalizationsIt([String locale = 'it']) : super(locale);

  @override
  String get introWelcome => 'Benvenuti!';

  @override
  String get introPage0Line1 => 'Grazie per aver scelto SWAssistant. Prima di iniziare, devi impostare alcune preferenze.';

  @override
  String get introPage0WebNoticev2 => 'As this is the web vesion, it makes use of Google Drive to save and load. After setup you will be asked to sign into Google. Additionally it requires cookies to function and by using it you agree to it\'s ';

  @override
  String get introPage0WebNoticeEnd => 'cookie policy.';

  @override
  String get introPage0Line2 => 'Inoltre, se hai usato una versione precedente di questa applicazione, dovremo prima importare i tuoi vecchi profili.';

  @override
  String get introPage0ImportButton => 'Importa Profili';

  @override
  String get introPage0ManualImport => 'Importa Manualmente';

  @override
  String get introPage0ManualImportLine0 => 'A causa del modo stupido in cui le vecchie versioni di SWAssistant salvavano i profili e le modifiche ad Android, è necessario importare manualmente i tuoi profili. Se hai usato il cloud saving, puoi in alternativa saltare questo passaggio e attivare il Salvataggio Cloud. Quando premi Continua, dovrebbe comparire un file explorer.';

  @override
  String get introPage0ManualImportLine1 => '1) Aprire il Pannello di Navigazione (le tre linee nell\'angolo in alto a sinistra) e selezionare il telefono';

  @override
  String get introPage0ManualImportLine1a => '1a) Se il telefono non è lì, potrebbe essere necessario aprire il menu extra (i tre punti nell\'angolo in alto a destra) e toccare \"Mostra memoria interna\"';

  @override
  String get introPage0ManualImportLine2 => '2) Selezionare la cartella chiamata SWChars';

  @override
  String get introPage0ManualImportLine3 => '3) Premere a lungo su uno dei file fino a quando non entra in modalità multi-selezione, quindi selezionare tutti gli altri file nella cartella';

  @override
  String get introPage0ManualImportLine4 => '4) Fare clic su Seleziona File.';

  @override
  String get introPage1Title => 'Stupid Backend';

  @override
  String get introPage1DarkstormExplaination => 'This app (optionally) uses my custom backend called Darkstorm Backend. It is completely open source and collects zero information other then what\'s needed. It\'s used for crash reporting, user counts, and sharing profiles. No sensitive information is collected or stored.';

  @override
  String get introDarkstormExistingUsers => 'Firebase has been replaced with my custom backend called Darkstorm Backend. It is completely open source and collects zero information other then what\'s needed. It\'s used for crash reporting, logging the app\'s startup (for user count), and sharing profiles. No sensitive information is collected or stored.';

  @override
  String importSuccess(int number) {
    return 'Importati con successo $number profili.';
  }

  @override
  String get importDialog => 'Importazione Profili...';

  @override
  String get importNone => 'Sembra che tu non abbia selezionato alcun file';

  @override
  String get loadingDialog => 'Caricamento...';

  @override
  String get driveError => 'Impossibile sincronizzare con Google Drive';

  @override
  String get driveErrorWebSub => 'La sincronizzazione Google Drive è necessaria per la versione web di SWAssistant. Il pop-up di log in potrebbe essere stato bloccato dal browser.';

  @override
  String get driveErrorOfflineSub => 'Se si continua offline, è possibile aggiornare l\'elenco dei personaggi per riconnettersi.';

  @override
  String get driveContinue => 'Continua offline';

  @override
  String get retry => 'Riprova';

  @override
  String get driveSyncing => 'Sincronizzazione con Google Drive';

  @override
  String get driveSyncingWeb => 'Potrebbe essere necessario consentire e aprire un pop-up per continuare';

  @override
  String get syncFail => 'Sincronizzazione fallita';

  @override
  String get syncComplete => 'Sincronizzazione completata.';

  @override
  String get driveSyncingNotice => 'Google Drive sta sincronizzando';

  @override
  String get driveDisconnectNotice => 'Google Drive è disconnesso';

  @override
  String get driveFirstLaunch => 'La Sincronizzazione dei tuoi profili su Google Drive è ora disponibile. Vai su Impostazioni per abilitarlo?';

  @override
  String get driveEnableFail => 'Google Drive non è riuscito ad inizializzare';

  @override
  String get driveFull => 'Google Drive storage full';

  @override
  String get gmModeTap => 'Seleziona un profilo a sinistra da editare.';

  @override
  String get gmModeType => 'Genere...';

  @override
  String get translate => 'Aiuta a tradurre!';

  @override
  String get discuss => 'Discussione/Avvisi';

  @override
  String get success => 'Successo';

  @override
  String get failure => 'Fallimento';

  @override
  String get advantage => 'Vantaggio';

  @override
  String get threat => 'Minaccia';

  @override
  String get triumph => 'Trionfo';

  @override
  String get despair => 'Disfatta';

  @override
  String get lightSide => 'Lato Chiaro';

  @override
  String get darkSide => 'Lato Oscuro';

  @override
  String get ability => 'Abilità';

  @override
  String get proficiency => 'Competenza';

  @override
  String get difficulty => 'Difficoltà';

  @override
  String get challenge => 'Sfida';

  @override
  String get boost => 'Potenziamento';

  @override
  String get setback => 'Ostacolo';

  @override
  String get force => 'Forza';

  @override
  String get brawn => 'Vigore';

  @override
  String get agility => 'Agilità';

  @override
  String get intellect => 'Intelletto';

  @override
  String get cunning => 'Astuzia';

  @override
  String get willpower => 'Volontà';

  @override
  String get presence => 'Presenza';

  @override
  String get roll => 'Roll';

  @override
  String get rollCrit => 'Tira Critico';

  @override
  String get gmMode => 'Modalità GM';

  @override
  String get profiles => 'Profili';

  @override
  String get characters => 'Personaggi';

  @override
  String get minions => 'Servitori';

  @override
  String get vehicles => 'Veicoli';

  @override
  String get trash => 'Cestino';

  @override
  String get download => 'Download';

  @override
  String get otherStuff => 'Altro';

  @override
  String get dice => 'Dado';

  @override
  String get guide => 'Guida';

  @override
  String get settings => 'Impostazioni';

  @override
  String get donate => 'Effettua una donazione';

  @override
  String get all => 'Tutto';

  @override
  String get uncategorized => 'Non Categorizzato';

  @override
  String get categories => 'Categorie';

  @override
  String get edit => 'Modifica';

  @override
  String get donoOptions => 'Opzioni Donazione';

  @override
  String get gPlay => 'Google Play';

  @override
  String get gPlayDesc => 'Facile da usare con il supporto per la maggior parte delle valute, ma le donazioni sono limitate e Google prende una fetta del 15%';

  @override
  String get sponsors => 'Github Sponsor';

  @override
  String get sponsorsDesc => 'È possibile donare una volta o su base mensile, con importi personalizzati, E il 100% del tuo denaro va allo sviluppatore';

  @override
  String gPlayDonate(String amount) {
    return 'Dona $amount';
  }

  @override
  String get gPlayPurchase => 'Acquista';

  @override
  String get gPlayUnavailable => 'Google Play non è attualmente disponibile';

  @override
  String get gPlayThanks => 'Grazie per la tua donazione! :D';

  @override
  String get gPlayPurchasePending => 'Acquisto In Attesa...';

  @override
  String get newCharacter => 'Nuovo Personaggio';

  @override
  String get newMinion => 'Nuovo PNG';

  @override
  String get newVehicle => 'Nuovo Veicolo';

  @override
  String get woundStrain => 'Ferite e Fatica';

  @override
  String get specializationPlural => 'Specializzazioni';

  @override
  String get forcePowerPlural => 'Poteri della Forza';

  @override
  String get xp => 'XP';

  @override
  String get morality => 'Moralità';

  @override
  String get skillPlural => 'Abilita\'';

  @override
  String get saveWeaponsFirst => 'Salva le armi prima!';

  @override
  String get restoreWeapons => 'Ripristina le armi salvate';

  @override
  String get weaponsRestored => 'Armi Ripristinate';

  @override
  String get saveWeapons => 'Salva armi';

  @override
  String get overwriteWeapons => 'Sovrascrivi le armi salvate';

  @override
  String get weaponsSaved => 'Armi Salvate';

  @override
  String get saveInventoryFirst => 'Salva prima l\'inventario!';

  @override
  String get restoreInventory => 'Ripristina l\'inventario salvato';

  @override
  String get inventoryRestored => 'Inventario Ripristinato';

  @override
  String get saveInventory => 'Salva inventario';

  @override
  String get overwriteInventory => 'Sovrascrivi l\'inventario salvato';

  @override
  String get inventorySaved => 'Inventario Salvato';

  @override
  String get criticalHits => 'Colpi Critici';

  @override
  String get basicInfo => 'Informazioni Di Base';

  @override
  String get defense => 'Difesa';

  @override
  String get weaponPlural => 'Armi';

  @override
  String get characteristicPlural => 'Caratteristiche';

  @override
  String get talentPlural => 'Talenti';

  @override
  String get inventory => 'Inventario';

  @override
  String get criticalInj => 'Ferite Critiche';

  @override
  String get characteristicAccurate => 'Precisa';

  @override
  String get characteristicBreach => 'Sfondamento';

  @override
  String get characteristicCumbersome => 'Ingombrante';

  @override
  String get characteristicInaccurate => 'Impreciso';

  @override
  String get characteristicInferior => 'Scadente';

  @override
  String get characteristicPierce => 'Perforare';

  @override
  String get characteristicVicious => 'Corrotto';

  @override
  String get ret => 'Torna';

  @override
  String get desc => 'Descrizione';

  @override
  String get name => 'Nome';

  @override
  String get rank => 'Rango';

  @override
  String get characteristic => 'Caratteristica';

  @override
  String get category => 'Categoria';

  @override
  String get duty => 'Dovere';

  @override
  String get forcePower => 'Poteri della Forza';

  @override
  String get obligation => 'Obbligo';

  @override
  String get specialization => 'Specializzazione';

  @override
  String get skill => 'Abilità';

  @override
  String get talent => 'Talento';

  @override
  String get item => 'Oggetto';

  @override
  String get weapon => 'Arma';

  @override
  String get value => 'Valore';

  @override
  String get career => 'Professione';

  @override
  String get skills1 => 'Astronavigazione';

  @override
  String get skills2 => 'Atletica';

  @override
  String get skills3 => 'Lotta';

  @override
  String get skills4 => 'Fascino';

  @override
  String get skills5 => 'Coercizione';

  @override
  String get skills6 => 'Computer';

  @override
  String get skills7 => 'Sangue Freddo';

  @override
  String get skills8 => 'Coordinazione';

  @override
  String get skills9 => 'Inganno';

  @override
  String get skills10 => 'Disciplina';

  @override
  String get skills11 => 'Artiglieria';

  @override
  String get skills12 => 'Autorità';

  @override
  String get skills13 => 'Spada Laser';

  @override
  String get skills14 => 'Meccanica';

  @override
  String get skills15 => 'Medicina';

  @override
  String get skills16 => 'Corpo a corpo';

  @override
  String get skills17 => 'Contrattare';

  @override
  String get skills18 => 'Percezione';

  @override
  String get skills19 => 'Pilotaggio(Planetario)';

  @override
  String get skills20 => 'Pilotaggio(Spazio)';

  @override
  String get skills21 => 'Distanza(Pesante)';

  @override
  String get skills22 => 'Distanza(Leggera)';

  @override
  String get skills23 => 'Resilienza';

  @override
  String get skills24 => 'Criminalità';

  @override
  String get skills25 => 'Furtività';

  @override
  String get skills26 => 'Bassifondi';

  @override
  String get skills27 => 'Sopravvivenza';

  @override
  String get skills28 => 'Vigilanza';

  @override
  String get skills29 => 'Conoscenza (Mondi Centrali)';

  @override
  String get skills30 => 'Conoscenza (istruzione)';

  @override
  String get skills31 => 'Conoscenza (Cultura)';

  @override
  String get skills32 => 'Conoscenza (Orlo Esterno)';

  @override
  String get skills33 => 'Conoscenza (Submondo)';

  @override
  String get skills34 => 'Conoscenza (Xenologia)';

  @override
  String get skills35 => 'Altro...';

  @override
  String get skillName => 'Skill Name';

  @override
  String get severity => 'Gravità';

  @override
  String get severityLevel1 => 'Facile';

  @override
  String get severityLevel2 => 'Moderato';

  @override
  String get severityLevel3 => 'Difficile';

  @override
  String get severityLevel4 => 'Scoraggiante';

  @override
  String get count => 'Quantità';

  @override
  String get encumTotal => 'Ingombro (Totale)';

  @override
  String get advNeeded => 'Vantaggio necessario';

  @override
  String get damage => 'Danno';

  @override
  String get critical => 'Critico';

  @override
  String get hardPoints => 'Punti Durezza';

  @override
  String get encum => 'Ingombro';

  @override
  String get firingArc => 'Angolo di Tiro';

  @override
  String get range => 'Portata';

  @override
  String get weapRange => 'Gittata Arma';

  @override
  String get rangeLevel1 => 'Ravvicinato';

  @override
  String get rangeLevel2 => 'Corta';

  @override
  String get rangeLevel3 => 'Media';

  @override
  String get rangeLevel4 => 'Lunga';

  @override
  String get rangeLevel5 => 'Estrema';

  @override
  String get itemDamage => 'Danno Oggetto';

  @override
  String get damageLevel1 => 'Non danneggiato';

  @override
  String get damageLevel2 => 'Minore';

  @override
  String get damageLevel3 => 'Moderato';

  @override
  String get damageLevel4 => 'Maggiore';

  @override
  String get damageLevel5 => 'Rotto';

  @override
  String get addCharacteristic => 'Aggiungi Caratteristica';

  @override
  String get addBrawn => 'Aggiungi Rissa al Danno';

  @override
  String get loaded => 'Caricato';

  @override
  String get limitedAmmo => 'Munizioni Limitate';

  @override
  String get ammo => 'Munizioni';

  @override
  String ignoreSoak(int num) {
    return 'Ignora $num Assorbimento';
  }

  @override
  String get species => 'Specie';

  @override
  String get age => 'Età';

  @override
  String get motivation => 'Motivazione';

  @override
  String get forceRating => 'Classificazione della Forza:';

  @override
  String get conflict => 'Conflitto';

  @override
  String get resolveConflict => 'Risolvi conflitto:';

  @override
  String conflictResult(int result) {
    return 'Il tuo risultato è $result';
  }

  @override
  String get emoStr => 'Forza Emozionale:';

  @override
  String get emoWeak => 'Debolezza Emozionale:';

  @override
  String get wound => 'Ferita';

  @override
  String get maxWound => 'Massima Ferita';

  @override
  String get strain => 'Fatica';

  @override
  String get maxStrain => 'Fatica Massima';

  @override
  String max(int number) {
    return 'Max: $number';
  }

  @override
  String get stimpacks => 'Stimpack';

  @override
  String get emergencyRepairPatches => 'Riparazione d\'Emmergenza';

  @override
  String get healsUsedToday => 'Cure Usate Oggi:';

  @override
  String get heal => 'Cura';

  @override
  String get reset => 'Reset';

  @override
  String get current => 'Attuale:';

  @override
  String get total => 'Totale:';

  @override
  String get addXP => 'Aggiungi XP';

  @override
  String get melee => 'Corpo a corpo';

  @override
  String get ranged => 'A distanza';

  @override
  String get credits => 'Riconoscimenti:';

  @override
  String get encumCap => 'Capacità di Carico:';

  @override
  String get overEncumNotice => 'Sei sovraccarico! Aggiungi una dado Ostacolo alle prove di Agilità e Rissa.';

  @override
  String get brokenWeapon => 'L\'arma è rotta e non può sparare!';

  @override
  String get weaponOutOfAmmo => 'L\'arma è scarica!';

  @override
  String get minNum => 'Numero di mostri:';

  @override
  String get minWound => 'Ferite per mostro';

  @override
  String get hullTrauma => 'Danni allo Scafo';

  @override
  String get maxTrauma => 'Danni Massimi allo Scafo';

  @override
  String get sysStress => 'Stress Di Sistema';

  @override
  String get maxStress => 'Massimo Stress Di Sistema';

  @override
  String get totalDefense => 'Totale Difesa (per lato)';

  @override
  String get fore => 'Prua';

  @override
  String get port => 'Babordo';

  @override
  String get starboard => 'Tribordo';

  @override
  String get aft => 'Poppa';

  @override
  String get anglingWarning => 'ATTENZIONE: la Difesa Laterale non è pari alla Difesa Totale';

  @override
  String get silhouette => 'Sagoma';

  @override
  String get speed => 'Velocità';

  @override
  String get armor => 'Armatura';

  @override
  String get handling => 'Manovrabilità';

  @override
  String get passengers => 'Passeggeri';

  @override
  String get soak => 'Fradicio';

  @override
  String get disableForce => 'Disabilita Forza';

  @override
  String get disableMorality => 'Disabilita Moralità';

  @override
  String get disableDuty => 'Disabilita Dovere';

  @override
  String get disableObligation => 'Disabilita Obbligo';

  @override
  String get clone => 'Clone';

  @override
  String copyOf(String name) {
    return 'Copia di $name';
  }

  @override
  String get stats => 'Statistiche';

  @override
  String get notes => 'Note';

  @override
  String get deletedDuty => 'Dovere Eliminato';

  @override
  String get deletedFP => 'Potere della Forza Eliminato';

  @override
  String get deletedObli => 'Obbligo Eliminato';

  @override
  String get deletedSpecialization => 'Specializzazione Eliminata';

  @override
  String get deletedSkill => 'Abilità Eliminata';

  @override
  String get deletedTalent => 'Talento Eliminato';

  @override
  String get deletedItem => 'Oggetto cancellato.';

  @override
  String get deletedWeapon => 'Arma Eliminata';

  @override
  String get deletedNote => 'Nota Eliminata';

  @override
  String get deletedInjury => 'Infortunio Critico Eliminato';

  @override
  String get undo => 'Annulla';

  @override
  String get characterTrashed => 'Personaggio spostato nel cestino';

  @override
  String get minionTrashed => 'Minion spostato nel cestino';

  @override
  String get vehicleTrashed => 'Veicolo spostato nel cestino';

  @override
  String deletedPermanently(String name) {
    return '$name eliminato definitivamente';
  }

  @override
  String restored(String name) {
    return 'Ripristinato $name';
  }

  @override
  String get trashNotice => 'I profili cestinati vengono eliminati dopo 30 giorni';

  @override
  String get fire => 'Fuoco!';

  @override
  String get advantageShort => 'Adv';

  @override
  String get forceDark => 'Forza Lato Scuro';

  @override
  String get forceLight => 'Forza tema chiaro';

  @override
  String get amoledTheme => 'Tema Scuro Amoled (Pitch Black)';

  @override
  String get systemLanguage => 'Lingua di Sistema';

  @override
  String get healthMode => 'Modalità Salute';

  @override
  String get subtractive => 'Sottrazione';

  @override
  String get subtractiveExplaination => 'La salute inizia al massimo e il danno la riduce';

  @override
  String get additive => 'Aggiunta';

  @override
  String get additiveExplaination => 'La salute inizia a 0 e il danno l\'aumenta';

  @override
  String get colorDice => 'Colore Lancia Dadi';

  @override
  String get darkstorm => 'Darkstorm backend';

  @override
  String get darkstormSub => 'Needed for profiles sharing';

  @override
  String get darkstormCrash => 'Crash reporting';

  @override
  String get darkstormCount => 'Darkstorm user count';

  @override
  String get aboutText => 'Visita https://github.com/CalebQ42/SWAssistant per la licenza e altre informazioni';

  @override
  String get ads => 'Ads';

  @override
  String get cloudSave => 'Salvataggio Cloud (tramite Google Drive)';

  @override
  String get disableAnimations => 'Disable Animations (For Performance)';

  @override
  String get showIntro => 'Mostra Introduzione';

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
