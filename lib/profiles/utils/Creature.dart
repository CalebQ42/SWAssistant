import 'package:swassistant/items/Item.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/items/Talent.dart';

mixin Creature{
  List<int> charVals;
  List<Skill> skills;
  List<Talent> talents;
  List<Item> inventory;
  int woundThresh;
  int woundCur;
  int defMelee,defRanged;
  int soak;
}