import 'package:flutter/material.dart';

class SWAppBar extends AppBar{

  final List<PopupMenuItem> defPopup = [
    PopupMenuItem(
        value: "G+",
        child: const Text("G+ Community")
    ),
    PopupMenuItem(
        value: "Translate",
        child: const Text("Help Translate!")
    )
  ];

  SWAppBar({String title, List<Widget> additionalActions, List<Widget> additionalPopupActions}) :
        super(title: Text(title), actions: _getActions((additionalActions == null) ?
            List() : additionalActions, (additionalPopupActions == null) ? List() : additionalPopupActions));

  static List<Widget> _getActions(List<Widget> additionalActions, List<Widget> additionalPopupActions){
    var actions = new List<Widget>();
    actions.addAll(additionalActions);
    actions.add(_getPopupMenu(additionalPopupActions));
    return actions;
  }
  static PopupMenuButton _getPopupMenu(List<Widget> additionalPopupActions){
    return PopupMenuButton(
      itemBuilder: (context)=>additionalPopupActions,
      onSelected:(t){
        var txt = t;
        switch(txt){
          case "G+":
            //_launchInBrowser("https://plus.google.com/communities/117741233533206107778");
            break;
          case "Translate":
            //_launchInBrowser("https://crwd.in/customdiceroller");
            break;
        }
      }
    );
  }
}

class SWDrawer extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Drawer(
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
          Text("Profiles"),
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
          Text("Other Stuff"),
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
}