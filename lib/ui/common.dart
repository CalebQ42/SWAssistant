import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/dialogs/donate.dart';
import 'package:swassistant/ui/misc/bottom.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';


class SWAppBar extends AppBar{

  static List<PopupMenuItem> defPopup(BuildContext context) =>[
    PopupMenuItem(
      value: "translate",
      child: Text(AppLocalizations.of(context)!.translate),
    ),
    PopupMenuItem(
      value: "discuss",
      child: Text(AppLocalizations.of(context)!.discuss)
    )
  ];

  static Map<String, Function> defFunctions = {
    "translate" : () => _launchURL("https://crowdin.com/project/swrpg"),
    "discuss" : () => _launchURL("https://github.com/CalebQ42/SWAssistant/discussions")
  };

  SWAppBar(BuildContext context, {Key? key, Widget? title, List<Widget>? additionalActions, List<PopupMenuItem>? additionalPopupActions,
      Map<String, Function>? popupFunctions, PreferredSizeWidget? bottom, required Color backgroundColor}) : 
        super(key: key, backgroundColor: backgroundColor, title: title, actions: _getActions(context, additionalActions, additionalPopupActions, popupFunctions), bottom: bottom);

  static List<Widget> _getActions(BuildContext context, List<Widget>? additionalActions, List<PopupMenuItem>? additionalPopupActions, Map<String, Function>? popupFunctions) =>
    <Widget>[...?additionalActions, _getPopupMenu(context, additionalPopupActions ?? [], popupFunctions ?? {})];
  
  static PopupMenuButton _getPopupMenu(BuildContext context, List<PopupMenuItem> additionalPopupActions, Map<String, Function> popupFunctions){
    popupFunctions.addAll(defFunctions);
    additionalPopupActions.addAll(defPopup(context));
    return PopupMenuButton(
      itemBuilder: (context) => additionalPopupActions,
      onSelected:(t) {
        if(popupFunctions[t] != null){
          popupFunctions[t]!();
        }
      }
    );
  }

  static void _launchURL(String url) async {
    if (!await launch(url)) throw 'Could not launch $url';
  }
}

class SWDrawer extends StatelessWidget{

  const SWDrawer({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    Drawer(
      key: const Key("SWDrawerKey"),
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          DrawerHeader(
            child: const Text("SWAssistant"),
            decoration: BoxDecoration(
              color: Theme.of(context).primaryColor
            )
          ),
          ListTile(
            title: Text(AppLocalizations.of(context)!.gmMode),
            leading: const Icon(Icons.contacts),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/gm");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/gm");
              }else{
                Navigator.of(context).pushNamed("/gm");
              }
            }
          ),
          const Divider(),
          const Padding(padding: EdgeInsets.symmetric(vertical: 5.0, horizontal: 10.0),child:Text("Profiles")),
          ListTile(
            title: Text(AppLocalizations.of(context)!.characters),
            leading: const Icon(Icons.face),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/characters");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/characters");
              }else{
                Navigator.of(context).pushNamed("/characters");
              }
            }
          ),
          ListTile(
            title: Text(AppLocalizations.of(context)!.minions),
            leading: const Icon(Icons.supervisor_account),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/minions");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/minions");
              }else{
                Navigator.of(context).pushNamed("/minions");
              }
            }
          ),
          ListTile(
            title: Text(AppLocalizations.of(context)!.vehicles),
            leading: const Icon(Icons.motorcycle),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/vehicles");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/vehicles");
              }else{
                Navigator.of(context).pushNamed("/vehicles");
              }
            }
          ),
          //TODO:
          // ListTile(
          //   title: Text(AppLocalizations.of(context)!.download),
          //   leading: Icon(Icons.cloud_download),
          //   onTap: (){
          //     Navigator.of(context).pop();
          //     var out = SW.of(context).observatory.containsRoute(name: "/download");
          //     if(out != null){
          //       Navigator.of(context).removeRoute(out);
          //       Navigator.of(context).pushNamed("/download");
          //     }else
          //       Navigator.of(context).pushNamed("/download");
          //   }
          // ),
          const Divider(),
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 5.0, horizontal: 10.0),
            child: Text(AppLocalizations.of(context)!.otherStuff)
          ),
          ListTile(
            title: Text(AppLocalizations.of(context)!.dice),
            leading: const Icon(Icons.casino),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/dice");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/dice");
              }else{
                Navigator.of(context).pushNamed("/dice");
              }
            }
          ),
          // ListTile(
          //   title: Text(AppLocalizations.of(context)!.guide),
          //   leading: Icon(Icons.book),
          //   onTap: (){
          //     Navigator.of(context).pop();
          //     var out = SW.of(context).observatory.containsRoute(name: "/guide");
          //     if(out != null){
          //       Navigator.of(context).removeRoute(out);
          //       Navigator.of(context).pushNamed("/guide");
          //     }else
          //       Navigator.of(context).pushNamed("/guide");
          //   }
          // ),
          ListTile(
            title: Text(AppLocalizations.of(context)!.settings),
            leading: const Icon(Icons.settings),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/settings");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/settings");
              }else{
                Navigator.of(context).pushNamed("/settings");
              }
            }
          ),
          const Divider(),
          ListTile(
            title: Text(AppLocalizations.of(context)!.donate),
            leading: const Icon(Icons.monetization_on_outlined),
            onTap: (){
              Navigator.of(context).pop();
              Bottom(
                child: (context) => const DonateDialog(),
              ).show(context);
            }
          ),
        ],
      )
    );
}