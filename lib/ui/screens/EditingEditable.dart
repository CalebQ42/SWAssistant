
import 'package:flutter/material.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/Common.dart';

class EditingEditable extends StatelessWidget{

  final Editable profile;

  EditingEditable(this.profile);

  Widget build(BuildContext context) {
    return Scaffold(
      appBar: SWAppBar(title: Text(profile.name)),
      body: ListView(
        children: profile.cards(),
      ),
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