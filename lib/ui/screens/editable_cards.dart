import 'dart:math';

import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/screens/gm_mode.dart';

class EditableCards extends StatefulWidget{

  const EditableCards({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => EditableCardsState();
}

class EditableCardsState extends State<EditableCards>{

  @override
  Widget build(BuildContext context) {
    var cards = Editable.of(context).cards(context);
    double width = min(GMModeSize.of(context)?.width ?? MediaQuery.of(context).size.height, 350);
    int rows = ((GMModeSize.of(context)?.width ?? MediaQuery.of(context).size.width) / width).floor();
    if(rows == 0) rows = 1;
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
      physics: const BouncingScrollPhysics(),
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
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisAlignment: MainAxisAlignment.center,
            children: List.generate(leftovers, (i) =>
              ConstrainedBox(
                key: ValueKey(cards.length-leftovers+i-1),
                constraints: BoxConstraints(
                  maxWidth: (GMModeSize.of(context)?.width ?? MediaQuery.of(context).size.width)/rows
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