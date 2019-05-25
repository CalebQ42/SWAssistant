import 'package:flutter/material.dart';

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
              color: Theme.of(context).appBarTheme.color
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