import 'dart:math';

import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditableCards extends StatelessWidget{

  final Function() refreshList;
  final double? w;

  EditableCards({required this.refreshList, this.w});

  @override
  Widget build(BuildContext context) {
    var cards = Editable.of(context).cards(context, refreshList);
    double width = w ?? min(MediaQuery.of(context).size.height, 500);
    int rows = (MediaQuery.of(context).size.width / width).floor();
    width = MediaQuery.of(context).size.width / rows;
    return SingleChildScrollView(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          cards[0],
          Wrap(
            alignment: WrapAlignment.spaceEvenly,
            children: List<Widget>.generate(cards.length-1, (index){
              return ConstrainedBox(
                key: ValueKey(index),
                constraints: BoxConstraints(
                  maxWidth: width
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