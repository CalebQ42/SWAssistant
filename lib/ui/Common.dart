import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/ui/dialogs/Donate.dart';
import 'package:swassistant/ui/misc/BottomSheetTemplate.dart';
import 'package:url_launcher/url_launcher.dart';

class SWAppBar extends AppBar{

  static List<PopupMenuItem> defPopup = [
    PopupMenuItem(
      value: "translate",
      child: const Text("Help Translate!"),
    ),
    PopupMenuItem(
      value: "discuss",
      child: const Text("Suggestions/Announcements")
    )
  ];

  static Map<String, Function> defFunctions = {
    "translate" : () => _launchURL("https://crowdin.com/project/swrpg"),
    "discuss" : () => _launchURL("https://github.com/CalebQ42/SWAssistant/discussions")
  };

  SWAppBar({Widget? title, List<Widget>? additionalActions, List<PopupMenuItem>? additionalPopupActions,
      Map<String, Function>? popupFunctions, PreferredSizeWidget? bottom, required Color backgroundColor}) : 
        super(backgroundColor: backgroundColor, title: title, actions: _getActions(additionalActions, additionalPopupActions, popupFunctions), bottom: bottom);

  static List<Widget> _getActions(List<Widget>? additionalActions, List<PopupMenuItem>? additionalPopupActions, Map<String, Function>? popupFunctions) =>
    <Widget>[]
      ..addAll(additionalActions ?? [])
      ..add(_getPopupMenu(additionalPopupActions ?? [], popupFunctions ?? {}));
  
  static PopupMenuButton _getPopupMenu(List<PopupMenuItem> additionalPopupActions, Map<String, Function> popupFunctions){
    popupFunctions.addAll(defFunctions);
    additionalPopupActions.addAll(defPopup);
    return PopupMenuButton(
      itemBuilder: (context) => additionalPopupActions,
      onSelected:(t) {
        if(popupFunctions[t] != null)
          popupFunctions[t]!();
      }
    );
  }

  static void _launchURL(String url) async {
    if (await canLaunch(url))
      await launch(url);
  }
}

class SWDrawer extends StatelessWidget{
  @override
  Widget build(BuildContext context) =>
    Drawer(
      key: Key("SWDrawerKey"),
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          DrawerHeader(
            child: Text("SW Assistant"),
            decoration: BoxDecoration(
              color: Theme.of(context).primaryColor
            )
          ),
          // TODO: GM Mode
          // ListTile(
          //   title: Text("GM Mode"),
          //   leading: Icon(Icons.contacts),
          //   onTap: (){
          //     Navigator.of(context).pop();
          //     var out = SW.of(context).observatory.containsRoute(name: "/gm");
          //     if(out != null){
          //       Navigator.of(context).removeRoute(out);
          //       Navigator.of(context).pushNamed("/gm");
          //     }else
          //       Navigator.of(context).pushNamed("/gm");
          //   }
          // ),
          // Divider(),
          Padding(padding: EdgeInsets.symmetric(vertical: 5.0, horizontal: 10.0),child:Text("Profiles")),
          ListTile(
            title: Text("Characters"),
            leading: Icon(Icons.face),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/characters");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/characters");
              }else
                Navigator.of(context).pushNamed("/characters");
            }
          ),
          ListTile(
            title: Text("Minions"),
            leading: Icon(Icons.supervisor_account),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/minions");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/minions");
              }else
                Navigator.of(context).pushNamed("/minions");
            }
          ),
          ListTile(
            title: Text("Vehicles"),
            leading: Icon(Icons.motorcycle),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/vehicles");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/vehicles");
              }else
                Navigator.of(context).pushNamed("/vehicles");
            }
          ),
          // TODO: Download
          // ListTile(
          //   title: Text("Download"),
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
          Divider(),
          Padding(padding: EdgeInsets.symmetric(vertical: 5.0, horizontal: 10.0),child:Text("Other Stuff")),
          ListTile(
            title: Text("Dice"),
            leading: Icon(Icons.casino),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/dice");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/dice");
              }else
                Navigator.of(context).pushNamed("/dice");
            }
          ),
          // TODO: Guide
          // ListTile(
          //   title: Text("Guide"),
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
            title: Text("Settings"),
            leading: Icon(Icons.settings),
            onTap: (){
              Navigator.of(context).pop();
              var out = SW.of(context).observatory.containsRoute(name: "/settings");
              if(out != null){
                Navigator.of(context).removeRoute(out);
                Navigator.of(context).pushNamed("/settings");
              }else
                Navigator.of(context).pushNamed("/settings");
            }
          ),
          Divider(),
          ListTile(
            title: Text("Donate"),
            leading: Icon(Icons.monetization_on),
            onTap: (){
              Navigator.of(context).pop();
              Bottom(
                child: DonateDialog(),
              ).show(context);
            }
          ),
        ],
      )
    );
}