
import 'package:swassistant/profiles/utils/Editable.dart';

import 'utils/Creature.dart';

class Character extends Editable with Creature{
  Character.fromJson(Map<String, dynamic> json) : super.fromJson(json);
}