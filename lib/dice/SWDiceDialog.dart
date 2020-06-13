
//Displays a dialog to roll a specified number of SW Dice
import 'package:flutter/material.dart';

class SWDiceDialog extends AlertDialog{
  int ability, difficulty, proficiency, challenge, boost, setback, force;
  SWDiceDialog({this.ability, this.difficulty, this.proficiency, this.challenge, this.boost, this.setback, this.force}){
  }
}