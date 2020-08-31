import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class Weapons extends StatelessWidget{
  final bool editing;
  final Function refresh;

  Weapons({this.editing, this.refresh});

  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    var weaponsList = List.generate(editable.weapons.length, (i){
      return InkResponse(
        containedInkWell: true,
        onTap: (){
          //TODO: weapon tap!
        },
        child: Row(
          children: [
            Expanded(
              child: Padding(
                padding: EdgeInsets.symmetric(vertical: 20),
                child: Text(editable.weapons[i].name)
              )
            ),
            AnimatedSwitcher(
              duration: Duration(milliseconds: 250),
              child: !editing ? Container(height: 24,)
              : ButtonBar(
                children: [
                  IconButton(
                    iconSize: 24.0,
                    constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: Icon(Icons.delete_forever),
                    onPressed: (){
                      editable.weapons.removeAt(i);
                      refresh();
                    }
                  ),
                  IconButton(
                    constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: Icon(Icons.edit),
                    onPressed: (){
                      showModalBottomSheet(
                        context: context,
                        builder: (context){
                          return WeaponEditDialog(
                            editable: editable,
                            onClose: (weapon){
                              if(weapon != null){
                                editable.weapons[i] = weapon;
                                refresh();
                              }
                            },
                            weapon: editable.weapons[i]
                          );
                        }
                      );
                    }
                  )
                ]
              ),
              transitionBuilder: (child, anim){
                var offset = Offset(1,0);
                if((!editing && child is ButtonBar) || (editing && child is Container))
                  offset = Offset(-1,0);
                return ClipRect(
                  child: SizeTransition(
                    sizeFactor: anim,
                    axis: Axis.horizontal,
                    child: SlideTransition(
                      position: Tween<Offset>(
                        begin: offset,
                        end: Offset.zero
                      ).animate(anim),
                      child: child,
                    )
                  )
                );
              },
            )
          ],
        )
      );
    });
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          Column(children: weaponsList),
          AnimatedSwitcher(
            duration: Duration(milliseconds: 300),
            transitionBuilder: (wid,anim){
              return SizeTransition(
                sizeFactor: anim,
                child: wid,
                axisAlignment: -1.0,
              );
            },
            child: editing ? Center(
              child: IconButton(
                icon: Icon(Icons.add),
                onPressed: (){
                  showModalBottomSheet(
                    isScrollControlled: true,
                    context: context,
                    builder: (context){
                      return WeaponEditDialog(
                        onClose: (weapon){
                          if(weapon != null){
                            editable.weapons.add(weapon);
                            refresh();
                          }
                        },
                        weapon: null
                      );
                    }
                  );
                },
              )
            ) : Container(),
          )
        ],
      ),
    );
  }
}

class WeaponEditDialog extends StatefulWidget{
  final Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;

  WeaponEditDialog({this.onClose, Weapon weapon, this.editable}) :
      this.weapon = weapon == null ? Weapon.nulled() : Weapon.from(weapon);

  @override
  State<StatefulWidget> createState() => _WeaponEditDialogState(onClose: onClose, weapon: weapon, editable: editable);
}

class _WeaponEditDialogState extends State{
  final Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;

  TextEditingController nameController;
  TextEditingController damageController;
  TextEditingController criticalController;
  TextEditingController hpController;
  TextEditingController encumbranceController;

  _WeaponEditDialogState({this.onClose, this.weapon, this.editable}){
    nameController = TextEditingController(text: weapon.name)
        ..addListener(() {
          if((weapon.name == "" && nameController.text != "") || (weapon.name != "" && nameController.text == ""))
            setState(() => weapon.name = nameController.text);
          else
            weapon.name = nameController.text;
        });
    damageController = TextEditingController(text: weapon.damage.toString())
        ..addListener(() {
          var val = int.tryParse(damageController.text);
          if((weapon.damage == null && val != null) || (weapon.damage != null && val == null))
            setState(() => weapon.damage = val);
          else
            weapon.damage = val;
        });
    criticalController = TextEditingController(text: weapon.critical.toString())
        ..addListener(() {
          var val = int.tryParse(criticalController.text);
          if((weapon.critical == null && val != null) || (weapon.critical != null && val == null)){
            setState(() => weapon.critical = val);
          }else
            weapon.critical = val;
        });
    hpController = TextEditingController(text: weapon.hp.toString())
        ..addListener(() {
          var val = int.tryParse(hpController.text);
          if((weapon.hp == null && val != null) || (weapon.hp != null && val == null))
            setState(() => weapon.hp = val);
          else
            weapon.hp = val;
        });
    encumbranceController = TextEditingController(text: weapon.encumbrance.toString())
        ..addListener(() {
          var val = int.tryParse(encumbranceController.text);
          if((weapon.encumbrance == null && val != null) || (weapon.encumbrance != null && val == null))
            setState(() => weapon.encumbrance = val);
          else
            weapon.encumbrance = val;
        });
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Padding(
        padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 5)),
        child: Wrap(
          children: [
            //Name
            TextField(
              controller: nameController,
              decoration: InputDecoration(
                prefixIcon: Text("Name: ", style: TextStyle(color:Theme.of(context).hintColor),),
                prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
              ),
            ),
            //Damage
            TextField(
              controller: damageController,
              decoration: InputDecoration(
                prefixIcon: Text("Damage: ", style: TextStyle(color:Theme.of(context).hintColor),),
                prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
            ),
            //Critical
            TextField(
              controller: criticalController,
              decoration: InputDecoration(
                prefixIcon: Text("Critical: ", style: TextStyle(color:Theme.of(context).hintColor),),
                prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
            ),
            //Hard Points
            TextField(
              controller: hpController,
              decoration: InputDecoration(
                prefixIcon: Text("Hard Points: ", style: TextStyle(color:Theme.of(context).hintColor),),
                prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
            ),
            //Encumbrance
            if(editable is Character) TextField(
              controller: encumbranceController,
              decoration: InputDecoration(
                prefixIcon: Text("Encumbrance: ", style: TextStyle(color:Theme.of(context).hintColor),),
                prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
            ),
            ButtonBar(
              children: [
                FlatButton(
                  child: Text("Cancel"),
                  onPressed: (){
                    onClose(null);
                    Navigator.of(context).pop();
                  },
                ),
                FlatButton(
                  child: Text("Save"),
                  onPressed: weapon.name != "" && weapon.damage != null && weapon.critical != null && weapon.hp != null
                      && weapon.encumbrance != null ? (){
                    onClose(weapon);
                    Navigator.of(context).pop();
                  } : null
                )
              ],
            )
          ],
        )
      )
    );
  }
}