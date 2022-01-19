import 'dart:math';

import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/editable.dart';

class EditableCards extends StatelessWidget {
  final double? w;

  const EditableCards({this.w, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var cards = Editable.of(context).cards(context);
    double width = min(w ?? MediaQuery.of(context).size.height, 350);
    int rows = (MediaQuery.of(context).size.width / width).floor();
    width = w ?? MediaQuery.of(context).size.width / rows;
    return SingleChildScrollView(
        child:
            Column(crossAxisAlignment: CrossAxisAlignment.stretch, children: [
      cards[0],
      Wrap(
        alignment: WrapAlignment.spaceEvenly,
        children: List<Widget>.generate(cards.length - 1, (index) {
          return ConstrainedBox(
              key: ValueKey(index),
              constraints: BoxConstraints(maxWidth: width),
              child: cards[index + 1]);
        }),
      )
    ]));
  }
}
