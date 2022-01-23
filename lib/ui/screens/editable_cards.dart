import 'dart:math';

import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/editable.dart';

class EditableCards extends StatelessWidget{

  final double? w;

  const EditableCards({this.w, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var cards = Editable.of(context).cards(context);
    double width = min(w ?? MediaQuery.of(context).size.height, 350);
    int rows = ((w ?? MediaQuery.of(context).size.width) / width).floor();
    List<List<Widget>> columns = [];
    int leftovers = (cards.length-2) % rows;
    for(var i = 1; i<=rows; i++){
      columns.add(
        List.generate(((cards.length-2-leftovers)/rows).floor(), (index) =>
          Container(
            key: ValueKey(index*rows+i),
            child: cards[index*rows+i]
          )
        )
      );
    }
    return SingleChildScrollView(
      physics: const ScrollPhysics(parent: BouncingScrollPhysics()),
      child: Column(
        children: [
          cards[0],
          Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: List.generate(rows,
              (i) => Expanded(child: Wrap(
                children: columns[i]
              ))
            )
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: List.generate(leftovers, (i) =>
              ConstrainedBox(
                key: ValueKey(cards.length-leftovers+i-1),
                constraints: BoxConstraints(
                  maxWidth: (w ?? MediaQuery.of(context).size.width)/rows
                ),
                child: cards[cards.length-leftovers+i-1]
              )
            )
          ),
          cards[cards.length-1]
        ]
      )
    );
  }


}