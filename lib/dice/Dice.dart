import 'dart:math';

import 'package:swassistant/dice/DiceResults.dart';
import 'package:swassistant/dice/Sides.dart';

class Die{

  String name;
  List<dynamic> sides;

  Die({required this.name, this.sides = const []}){
    if(sides.any((element) => element is !SimpleSide && element is !ComplexSide))
      throw("All sides MUST be SimpleSide or ComplexSide");
  }

  dynamic roll({Random? random}) => sides[(random ?? Random()).nextInt(sides.length)];
}

class Dice{

  String name;
  List<Die> dies;

  Dice({required this.name, this.dies = const []});

  DiceResults roll(){
    Random random = Random();
    DiceResults results = DiceResults();
    dies.forEach((element) => results.add(element.roll(random: random)));
    return results;
  }
}