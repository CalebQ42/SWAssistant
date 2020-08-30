import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditableCards extends StatelessWidget{

  final Function refreshList;

  EditableCards({this.refreshList});

  @override
  Widget build(BuildContext context) {
    var cards = Editable.of(context).cards(refreshList,context);
    return ListView.builder(
      itemCount: cards.length,
      itemBuilder: (context,i) => cards[i],
    );
  }
}