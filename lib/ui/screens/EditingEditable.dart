
import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/Common.dart';

class EditingEditable extends StatelessWidget{

  final Editable profile;
  final Function refreshList;

  EditingEditable(this.profile,this.refreshList);

  Widget build(BuildContext context) {
    var cards = profile.cards(refreshList, context);
    return InheritedEditable(
      child: Scaffold(
        drawer: SWDrawer(),
        appBar: SWAppBar(title: Text(profile.name)),
        body: ListView.builder(itemCount: profile.cardNames.length,itemBuilder: (context,i) => cards[i]),
      ),
      editable: profile
    );
  }
}

class InheritedEditable extends InheritedWidget{
  final Editable editable;

  InheritedEditable({Widget child, this.editable}): super(child: child);

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) => false;
  
}