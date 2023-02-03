import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/frame_content.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/ui/misc/updating_switch_tile.dart';


class Loading extends StatefulWidget{

  final RouteSettings afterLoad;

  const Loading({Key? key, required this.afterLoad}) : super(key: key);

  @override
  State<Loading> createState() => LoadingState();
}

class LoadingState extends State<Loading> {

  int index = -1;
  bool started = false;
  bool driveErr = false;

  Widget? child;
  String? _loadText;
  set loadText(String s) => setState(() => _loadText = s);

  List<String> get neededAsks {
    var result = <String>[];
    for (var ask in preferences.newPrefs) {
      if (SW.of(context).getPref(ask)) {
        result.add(ask);
      }
    }
    return result;
  }
  void leave() {
    SW.of(context).initialized = true;
    Navigator.of(context).pushNamedAndRemoveUntil(widget.afterLoad.name ?? "", (route) => false, arguments: widget.afterLoad.arguments);
  }

  void advance() {
    if(!driveErr){
      index++;
      if (neededAsks.isEmpty) {
        leave();
      }else{
        switch(neededAsks[index]){
          case preferences.subtractNew:
            setState(() => child = subtractPref());
        }
      }
    }else{
      setState(() => child = driveError());
    }
  }

  @override
  void initState() {
    SW.of(context).postInit(this).then((_){
      advance();
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) =>
    FrameContent(
      allowPop: false,
      fab: child != null ? FloatingActionButton(
        onPressed: advance,
        child: const Icon(Icons.forward),
      ) : null,
      child: Align(
        child: ConstrainedBox(
          constraints: const BoxConstraints(maxWidth: 500),
          child: AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            child: child ?? Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const CircularProgressIndicator(),
                Container(height: 10),
                Text(
                  _loadText ?? AppLocalizations.of(context)!.loadingDialog,
                  style: Theme.of(context).textTheme.displaySmall,
                  textAlign: TextAlign.center,
                )
              ],
            )
          )
        )
      )
    );

  Widget subtractPref() =>
    Column(
      key: const Key("subby"),
      mainAxisSize: MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Padding(
          padding: const EdgeInsets.all(15),
          child: Text(
            AppLocalizations.of(context)!.healthMode,
            style: Theme.of(context).textTheme.headlineSmall,
          ),
        ),
        Padding(
          padding: const EdgeInsets.all(15),
          child: Row(
            mainAxisSize: MainAxisSize.max,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      AppLocalizations.of(context)!.additive,
                      style: Theme.of(context).textTheme.titleLarge,
                    ),
                    Text(
                      AppLocalizations.of(context)!.additiveExplaination,
                      textAlign: TextAlign.center,
                    )
                  ]
                )
              ),
              UpdatingSwitch(
                value: SW.of(context).getPref(preferences.subtractMode),
                onChanged: (b) => 
                  SW.of(context).prefs.setBool(preferences.subtractMode, b),
              ),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      AppLocalizations.of(context)!.subtractive,
                      style: Theme.of(context).textTheme.titleLarge
                    ),
                    Text(
                      AppLocalizations.of(context)!.subtractiveExplaination,
                      textAlign: TextAlign.center,
                    )
                  ]
                )
              ),
            ],
          )
        ),
      ],
    );

  Widget driveError() =>
    Column(
      key: const Key("drivErr"),
      mainAxisSize: MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Padding(
          padding: const EdgeInsets.all(15),
          child: Text(
            AppLocalizations.of(context)!.driveError,
            style: Theme.of(context).textTheme.headlineSmall,
          ),
        ),
        Padding(
          padding: const EdgeInsets.all(7),
          child: Text(
            kIsWeb ? AppLocalizations.of(context)!.driveErrorWebSub : AppLocalizations.of(context)!.driveErrorWebSub,
            style: Theme.of(context).textTheme.titleSmall,
          ),
        ),
        TextButton(
          onPressed: onPressed,
          child: child
        )
      ],
    );
}