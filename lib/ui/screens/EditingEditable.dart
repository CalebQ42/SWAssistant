
import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/Common.dart';

class EditingEditable extends StatelessWidget{

  final Editable profile;
  final SW app;
  final Function refreshList;

  EditingEditable(this.profile, this.app, this.refreshList);

  Widget build(BuildContext context) {
    var cards = profile.cards(refreshList,app);
    return Scaffold(
      drawer: SWDrawer(),
      appBar: SWAppBar(title: Text(profile.name)),
      body: ListView.builder(itemCount: profile.cardNum,itemBuilder: (context,i) => cards[i]),
    );
  }
}