
import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditingEditable extends StatelessWidget{

  final Editable profile;

  EditingEditable(this.profile);

  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(profile.name)
      ),
      body: Column(
        children: profile.cards(),
      )
    );
  }

}