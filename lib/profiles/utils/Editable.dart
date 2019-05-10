
//Editable holds all common components of Vehicles, Minions, and Characters
class Editable{

  //Common components

  int id;
  String name;
  //Notes nts = new Notes();
  //Weapons weapons = new Weapons();
  String Category;
  //CriticalInjuries criticalInjuries;
  String desc;

  //Saving variables

  bool _editing = false;
  bool _saving = false;
  String _loc;
  bool _external;
}