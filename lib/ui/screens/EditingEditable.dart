
import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/Common.dart';

class EditingEditable extends StatelessWidget{

  final Editable profile;
  final Function refreshList;

  EditingEditable(this.profile,this.refreshList);

  Widget build(BuildContext context) {
    var cards = profile.cards(refreshList, context);
    return Scaffold(
      drawer: SWDrawer(),
      appBar: SWAppBar(title: Text(profile.name)),
      body: ListView.builder(itemCount: profile.cardNames.length,itemBuilder: (context,i) => cards[i]),
    );
  }
}