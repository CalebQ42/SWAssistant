import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/frame_content.dart';
import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/ui/misc/new_loading_pref.dart';


class Loading extends StatefulWidget{

  final RouteSettings afterLoad;

  const Loading({Key? key, required this.afterLoad}) : super(key: key);

  @override
  State<Loading> createState() => _LoadingState();

  static final List<String> asks = [
    preferences.subtractNew
  ];
}

class _LoadingState extends State<Loading> {

  int index = -1;

  Widget? child;

  List<String> get neededAsks {
    var result = <String>[];
    for (var ask in Loading.asks) {
      if (SW.of(context).getPreference(ask, true)) {
        result.add(ask);
      }
    }
    return result;
  }
  void leave() => Navigator.of(context).pushNamedAndRemoveUntil(widget.afterLoad.name ?? "", (route) => false, arguments: widget.afterLoad.arguments);

  @override
  Widget build(BuildContext context) {
    var nav = Navigator.of(context);
    SW.of(context).postInit().then((_){
      index = 0;
      if (neededAsks.isEmpty) {
        leave();
      }else{
        switch(neededAsks[index]){
          case preferences.subtractNew:
            setState(() => child = subtractPref());
        }
      }
    });
    return FrameContent(
      allowPop: false,
      child: AnimatedSwitcher(
        duration: const Duration(milliseconds: 300),
        child: Align(
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 500),
            child: child ?? Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const CircularProgressIndicator(),
                Container(height: 10),
                Text(
                  AppLocalizations.of(context)!.loadingDialog,
                  style: Theme.of(context).textTheme.displaySmall,
                  textAlign: TextAlign.center,
                )
              ],
            )
          )
        )
      ),
      fab: child != null ? FloatingActionButton(
        child: const Icon(Icons.forward),
        onPressed: (){
          if(index < neededAsks.length - 1){
            setState(() {
              index++;
              switch(neededAsks[index]){
                case preferences.subtractNew:
                  child = subtractPref();
                  break;
              }
            });
          }else{
            leave();
          }
        },
      ) : null
    );
  }

  Widget subtractPref() => Column(
      mainAxisSize: MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Padding(
          padding: const EdgeInsets.all(15),
          child: Text(
            AppLocalizations.of(context)!.healthMode,
            style: Theme.of(context).textTheme.titleMedium,
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
                    Text(AppLocalizations.of(context)!.additiveExplaination)
                  ]
                )
              ),
              Switch(
                value: SW.of(context).getPreference(preferences.subtractMode, true),
                onChanged: (b) => setState(() {
                  SW.of(context).prefs.setBool(preferences.subtractMode, b);
                }),
              ),
              Expanded(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      AppLocalizations.of(context)!.subtractive,
                      style: Theme.of(context).textTheme.titleLarge
                    ),
                    Text(AppLocalizations.of(context)!.subtractiveExplaination)
                  ]
                )
              ),
            ],
          )
        ),
      ],
    
  );
}