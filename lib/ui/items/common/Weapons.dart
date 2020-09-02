import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/items/WeaponCharacteristic.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
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
  final void Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;

  TextEditingController nameController;
  TextEditingController damageController;
  TextEditingController criticalController;
  TextEditingController hpController;
  TextEditingController encumbranceController;
  TextEditingController arcController;
  TextEditingController ammoController;

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
    arcController = TextEditingController(text: weapon.firingArc)
      ..addListener(() {
        if((weapon.firingArc == "" && arcController.text != "") || (weapon.firingArc != "" && arcController.text == ""))
          setState(() => weapon.firingArc = arcController.text);
        else
          weapon.firingArc = arcController.text;
      });
    ammoController = TextEditingController(text: weapon.ammo.toString())
        ..addListener(() {
          var val = int.tryParse(ammoController.text);
          if((weapon.ammo == null && val != null) || (weapon.ammo != null && val == null))
            setState(() => weapon.ammo = val);
          else
            weapon.ammo = val;
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
            //Firing Arc
            if(editable is Vehicle) TextField(
              controller: arcController,
              decoration: InputDecoration(
                prefixIcon: Text("Firing Arc: ", style: TextStyle(color:Theme.of(context).hintColor),),
                prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
              ),
            ),
            //Range
            DropdownButton<int>(
              hint: Text("Weapon Range"),
              items: [
                DropdownMenuItem(
                  child: Text("Engaged"),
                  value: 0,
                ),
                DropdownMenuItem(
                  child: Text("Short"),
                  value: 1,
                ),
                DropdownMenuItem(
                  child: Text("Medium"),
                  value: 2,
                ),
                DropdownMenuItem(
                  child: Text("Long"),
                  value: 3,
                ),
                DropdownMenuItem(
                  child: Text("Extreme"),
                  value: 4,
                )
              ],
              value: weapon.range,
              onChanged: (value) => setState(()=>weapon.range = value),
            ),
            //Damage
            Center(child: Text("Weapon damage")),
            DropdownButton<int>(
              items: [
                DropdownMenuItem(
                  child: Text("None"),
                  value: 0,
                ),
                DropdownMenuItem(
                  child: Text("Short"),
                  value: 1,
                ),
                DropdownMenuItem(
                  child: Text("Medium"),
                  value: 2,
                ),
                DropdownMenuItem(
                  child: Text("Long"),
                  value: 3,
                ),
              ],
              value: weapon.itemState,
              onChanged: (value) => setState(() => weapon.itemState = value),
            ),
            //Skill
            DropdownButton<int>(
              items: List.generate(
                Weapon.weaponSkills.length,
                (i) => DropdownMenuItem(
                  child: Text(Weapon.weaponSkills[i]),
                  value: i
                )
              ),
              value: weapon.skill,
              onChanged: (value) => setState((){
                weapon.skill = value;
                weapon.skillBase = Skill.skillsList[value];
              }),
            ),
            //SkillBase
            DropdownButton<int>(
              items: List.generate(
                Creature.characteristics.length,
                (i) => DropdownMenuItem(
                  child: Text(Creature.characteristics[i]),
                  value: i
                )
              ),
              value: weapon.skill,
              onChanged: (value) => setState(() => weapon.skillBase = value),
            ),
            //Characteristics
            ListView.builder(
              itemBuilder: (context, i){
                return InkResponse(
                  containedInkWell: true,
                  onTap: () => showDialog(
                    context: context,
                    child: WeaponCharacteristicDialog(
                      wc: weapon.characteristics[i],
                      onClose: (wc){
                        if(wc != null)
                          setState(() => weapon.characteristics[i] = wc);
                      }
                    )
                  ),
                  child: Row(
                    children: [
                      Expanded(
                        child: Text(weapon.characteristics[i].name),
                      ),
                      Text(weapon.characteristics[i].value.toString())
                    ],
                  )
                );
              }
            ),
            IconButton(
              icon: Icon(Icons.add),
              onPressed: (){
                showDialog(
                  context: context,
                  child: WeaponCharacteristicDialog(onClose: (wc){
                    if(wc != null)
                      weapon.characteristics.add(wc);
                  })
                );
              },
            ),
            //Add Brawn
            if(editable is Character || editable is Minion) SwitchListTile(
              value: weapon.addBrawn,
              onChanged: (value) => setState(() => weapon.addBrawn = value),
              title: Text("Add Brawn to Damage"),
            ),
            //Loaded
            SwitchListTile(
              value: weapon.loaded,
              onChanged: (value) => setState(() => weapon.loaded = value),
              title: Text("Loaded")
            ),
            //Limited Ammo
            SwitchListTile(
              value: weapon.limitedAmmo,
              onChanged: (value) => setState((){
                weapon.limitedAmmo = value;
                weapon.ammo = 0;
              }),
              title: Text("Slugthrower (Needs Ammo)")
            ),
            //Ammo
            AnimatedSwitcher(
              duration: Duration(milliseconds: 300),
              transitionBuilder: (child, anim){
                return SizeTransition(
                  sizeFactor: anim,
                  child: child
                );
              },
              child: weapon.limitedAmmo ? TextField(
                controller: ammoController,
                decoration: InputDecoration(
                  prefixIcon: Text("Ammo: ", style: TextStyle(color:Theme.of(context).hintColor),),
                  prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
              ) : Container(),
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
                      && (editable is Character && weapon.encumbrance != null) && (editable is Vehicle && weapon.firingArc != null)
                      && weapon.range != null && weapon.damage != null && weapon.skill != null && weapon.skillBase != null ? (){
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

class WeaponCharacteristicDialog extends StatefulWidget{

  final WeaponCharacteristic wc;
  final void Function(WeaponCharacteristic) onClose;

  WeaponCharacteristicDialog({WeaponCharacteristic wc, this.onClose}) :
      this.wc = wc == null ? WeaponCharacteristic.nulled() : WeaponCharacteristic.from(wc);

  @override
  State<StatefulWidget> createState() => _WeaponCharacteristicDialogState(wc, onClose);
}

class _WeaponCharacteristicDialogState extends State{
  
  final WeaponCharacteristic wc;
  final Function(WeaponCharacteristic) onClose;

  TextEditingController nameController;
  TextEditingController valueController;
  TextEditingController advantageController;

  _WeaponCharacteristicDialogState(this.wc, this.onClose){
    nameController = TextEditingController(text: wc.name)
        ..addListener((){
          if((wc.name == "" && nameController.text != "") || (wc.name != "" && nameController.text == ""))
            setState(() => wc.name = nameController.text);
          else
            wc.name = nameController.text;
        });
    valueController = TextEditingController(text: wc.value.toString())
        ..addListener(() {
          var val = int.tryParse(valueController.text);
          if((wc.value == null && val != null) || (wc.value != null && val == null))
            setState(() => wc.value = val);
          else
            wc.value = val;
        });
    advantageController = TextEditingController(text: wc.advantage.toString())
        ..addListener(() {
          var val = int.tryParse(advantageController.text);
          if((wc.advantage == null && val != null) || (wc.advantage != null && val == null))
            setState(() => wc.advantage = val);
          else
            wc.advantage = val;
        });
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      content: Column(
        children: [
          TextField(
            controller: nameController,
            decoration: InputDecoration(
              prefixIcon: Text("Name: ", style: TextStyle(color:Theme.of(context).hintColor),),
              prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
            ),
          ),
          TextField(
            controller: valueController,
            decoration: InputDecoration(
              prefixIcon: Text("Value: ", style: TextStyle(color:Theme.of(context).hintColor),),
              prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
            ),
            keyboardType: TextInputType.number,
            inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
          ),
          TextField(
            controller: nameController,
            decoration: InputDecoration(
              prefixIcon: Text("Advantage Needed: ", style: TextStyle(color:Theme.of(context).hintColor),),
              prefixIconConstraints: BoxConstraints(minHeight: 0, minWidth: 0)
            ),
            keyboardType: TextInputType.number,
            inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
          ),
        ],
      ),
      actions: [
        FlatButton(
          onPressed: (){
            onClose(wc);
            Navigator.of(context).pop();
          },
          child: Text("Save")
        ),
        FlatButton(
          onPressed: (){
            onClose(null);
            Navigator.of(context).pop();
          },
          child: Text("Cancel")
        )
      ],
    );
  }
}