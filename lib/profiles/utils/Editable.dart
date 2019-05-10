
//Editable holds all common components of Vehicles, Minions, and Characters and
//provides a framework on how to display, load, and save profiles
abstract class Editable{

  //Common components

  int id = 0;
  String name;
  //Notes nts = new Notes();
  //Weapons weapons = new Weapons();
  String category;
  //CriticalInjuries criticalInjuries;
  String desc;

  //Saving variables

  bool _editing = false;
  bool _saving = false;
  String _loc;
  bool _external;

  //Editable.fromJson(Map<String,dynamic> json){}

  Map<String, dynamic> toJson();

  void saving(){
    print("Oh howdy yall");
  }
}