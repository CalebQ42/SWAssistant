import 'package:swassistant/profiles/utils/Editable.dart';

import 'utils/Creature.dart';

class Minion extends Editable with Creature{
  Minion.fromJson(Map<String, dynamic> json) : super.fromJson(json);
}