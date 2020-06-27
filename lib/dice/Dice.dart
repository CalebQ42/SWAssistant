import 'dart:math';

import 'DiceResults.dart';
import 'Sides.dart';

class Die{
  static Die numberDie(int sides){
    var d = Die("D"+sides.toString());
    for(int i = 1;i<=sides;i++){
      d.sides.add(SimpleSide(i.toString()));
    }
    return d;
  }

  List sides;
  String name;

  Die([this.name = "New Die"]){
    sides = new List();
  }
  Die.withSides([this.name,this.sides]);

  Die clone() => Die.withSides(name,new List.from(sides));
  bool isComplex(int i) => sides[i] is ComplexSide;
  ComplexSide getComplex(int i) => sides[i];
  SimpleSide getSimple(int i) => sides[i];
  int rollIndex() => new Random().nextInt(sides.length);
  String toString() => name + " " + sides.toString();
}

class Dice{
  static var fileExtension = ".dice";
  static Dice numberDice(int number,int sides){
    var d = Dice(number.toString()+"D"+sides.toString());
    for(var i = 0;i<number;i++)
      d.dice.add(Die.numberDie(sides));
    return d;
  }

  List<Die> dice;
  String name;

  Dice([this.name = "New Dice"]){
    dice = new List<Die>();
  }
  Dice.withDice(this.name,this.dice);

  Dice clone() => Dice.withDice(name,new List<Die>.from(dice));
  DiceResults roll(){
    var dr = DiceResults();
    dice.forEach((d){
      if(d.sides.length>0){
        var i = d.rollIndex();
        if(d.isComplex(i))
          dr.addComplexSide(d.getComplex(i), d.name);
        else
          dr.addSimpleSide(d.getSimple(i), d.name);
      }
    });
    return dr;
  }
}