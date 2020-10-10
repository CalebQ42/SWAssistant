import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

class SWAppBar extends AppBar{

  static List<PopupMenuItem> defPopup = [
    PopupMenuItem(
        value: "translate",
        child: const Text("Help Translate!"),
    )
  ];

  static Map<String, Function> defFunctions = {
    "translate" : () => _launchURL("https://crowdin.com/project/swrpg")
  };

  SWAppBar({Widget title, List<Widget> additionalActions, List<PopupMenuItem> additionalPopupActions, Map<String, Function> popupFunctions}) :
        super(title: title, actions: _getActions(additionalActions, additionalPopupActions, popupFunctions));

  static List<Widget> _getActions(List<Widget> additionalActions, List<PopupMenuItem> additionalPopupActions, Map<String, Function> popupFunctions) =>
    List<Widget>()
      ..addAll(additionalActions ?? List())
      ..add(_getPopupMenu(additionalPopupActions, popupFunctions));
  
  static PopupMenuButton _getPopupMenu(List<PopupMenuItem> additionalPopupActions, Map<String, Function> popupFunctions){
    popupFunctions ??= Map();
    popupFunctions.addAll(defFunctions);
    additionalPopupActions ??= List();
    additionalPopupActions.addAll(defPopup);
    return PopupMenuButton(
      itemBuilder: (context) => additionalPopupActions,
      onSelected:(t) {
        if(popupFunctions[t] != null)
          popupFunctions[t]();
      }
    );
  }

  static void _launchURL(String url) async {
    if (await canLaunch(url))
      await launch(url);
    else
      throw 'Could not launch $url';
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
              color: Colors.red
            )
          ),
          ListTile(
            title: Text("GM Mode"),
            leading: Icon(Icons.contacts),
            onTap: (){
              Navigator.of(context).popAndPushNamed("/gm");
            }
          ),
          Divider(),
          Padding(padding: EdgeInsets.only(left:5.0),child:Text("Profiles")),
          ListTile(
            title: Text("Characters"),
            leading: Icon(Icons.face),
            onTap: (){
              Navigator.of(context).popAndPushNamed("/characters");
            }
          ),
          ListTile(
            title: Text("Minions"),
            leading: Icon(Icons.supervisor_account),
            onTap: (){
              Navigator.of(context).popAndPushNamed("/minions");
            }
          ),
          ListTile(
            title: Text("Vehicles"),
            leading: Icon(Icons.motorcycle),
            onTap: (){
              Navigator.of(context).popAndPushNamed("/vehicles");
            }
          ),
          ListTile(
            title: Text("Download"),
            leading: Icon(Icons.cloud_download),
            onTap: (){
              Navigator.of(context).popAndPushNamed("/download");
            }
          ),
          Divider(),
          Padding(padding: EdgeInsets.only(left:5.0),child:Text("Other Stuff")),
          ListTile(
            title: Text("Dice"),
            leading: Icon(Icons.casino),
            onTap: (){
              Navigator.of(context).popAndPushNamed("/dice");
            }
          ),
          ListTile(
            title: Text("Guide"),
            leading: Icon(Icons.book),
            onTap: (){
              Navigator.of(context).popAndPushNamed("/guide");
            }
          ),
          ListTile(
            title: Text("Settings"),
            leading: Icon(Icons.settings),
            onTap: (){
              Navigator.of(context).popAndPushNamed("/settings");
            }
          )
        ],
      )
    );
}