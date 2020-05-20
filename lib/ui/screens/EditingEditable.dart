
import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/Common.dart';

class EditingEditable extends StatelessWidget{

  final Editable profile;
  final SW app;
  final Function refreshList;

  EditingEditable(this.profile, this.app, this.refreshList);

  Widget build(BuildContext context) {
    return Scaffold(
      drawer: SWDrawer(),
      appBar: SWAppBar(title: Text(profile.name)),
      body: ListView.builder(itemBuilder: (context,i) => profile.cards(refreshList)[i]),
      floatingActionButton: FloatingActionButton(
         onPressed: null,
         child: (profile is Character) ? Icon(Icons.face) :
             (profile is Vehicle) ? Icon(Icons.motorcycle) :
             (profile is Minion) ? Icon(Icons.supervisor_account) :
             null,
       ),
    );
  }
}