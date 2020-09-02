import 'dart:math';

import 'package:flutter/rendering.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditableCards extends StatelessWidget{

  final Function refreshList;

  EditableCards({this.refreshList});

  @override
  Widget build(BuildContext context) {
    var cards = Editable.of(context).cards(refreshList,context);
    return SingleChildScrollView(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          cards[0],
          Wrap(
            alignment: WrapAlignment.spaceEvenly,
            children: List<Widget>.generate(cards.length-1, (index){
              return ConstrainedBox(
                constraints: BoxConstraints(
                  maxWidth: min(MediaQuery.of(context).size.height, 500)
                ),
                child: cards[index+1]
              );
            }),
          )
        ]
      )
    );
  }
}