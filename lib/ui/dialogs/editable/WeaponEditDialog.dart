import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Creature.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/dialogs/editable/WeaponCharacteristicDialog.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class WeaponEditDialog extends StatefulWidget{
  final Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;

  WeaponEditDialog({required this.onClose, Weapon? weapon, required this.editable}) :
      this.weapon = weapon == null ? Weapon() : Weapon.from(weapon);

  @override
  State<StatefulWidget> createState() => _WeaponEditDialogState(onClose: onClose, weapon: weapon, editable: editable);

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (context) =>
        Container(
          child: this,
          height: MediaQuery.of(context).size.height * 0.60,
        )
    );
}

class _WeaponEditDialogState extends State{
  final void Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;

  late TextEditingController nameController;
  late TextEditingController damageController;
  late TextEditingController criticalController;
  late TextEditingController hpController;
  late TextEditingController encumbranceController;
  late TextEditingController arcController;
  late TextEditingController ammoController;

  _WeaponEditDialogState({required this.onClose, required this.weapon, required this.editable}){
    nameController = TextEditingController(text: weapon.name)
        ..addListener(() =>
          setState(() => weapon.name = nameController.text)
        );
    damageController = TextEditingController(text: weapon.damage != -1 ? weapon.damage.toString() : "")
        ..addListener(() =>
          setState(() => weapon.damage = int.tryParse(damageController.text) ?? -1)
        );
    criticalController = TextEditingController(text: weapon.critical != -1 ? weapon.critical.toString() : "")
        ..addListener(() =>
          setState(() => weapon.critical = int.tryParse(criticalController.text) ?? -1)
        );
    hpController = TextEditingController(text: weapon.hp.toString())
        ..addListener(() =>
          weapon.hp = int.tryParse(hpController.text) ?? 0
        );
    encumbranceController = TextEditingController(text: weapon.encumbrance.toString())
        ..addListener(() =>
          weapon.encumbrance = int.tryParse(encumbranceController.text) ?? 0
        );
    arcController = TextEditingController(text: weapon.firingArc)
        ..addListener(() =>
          weapon.firingArc = arcController.text
        );
    ammoController = TextEditingController(text: weapon.ammo.toString())
        ..addListener(() =>
          weapon.ammo = int.tryParse(ammoController.text) ?? 0
        );
  }

  @override
  Widget build(BuildContext context) {
    var weaponSkills = Weapon.weaponSkills(context);
    return SingleChildScrollView(
      child: DropdownButtonHideUnderline(
        child: Padding(
          padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15)),
          child: Column(
            children: [
              //Name
              Container(height: 15),
              TextField(
                controller: nameController,
                textCapitalization: TextCapitalization.words,
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.weapon
                ),
              ),
              Container(height: 10),
              //Damage
              TextField(
                controller: damageController,
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.damage
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              ),
              Container(height: 10),
              //Critical
              TextField(
                controller: criticalController,
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.critical
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              ),
              Container(height: 10),
              //Hard Points
              TextField(
                controller: hpController,
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.hardPoints
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              ),
              //Encumbrance
              if(editable is Character) Container(height: 10),
              if(editable is Character) TextField(
                controller: encumbranceController,
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.encum
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              ),
              //Firing Arc
              if(editable is Vehicle) Container(height: 10),
              if(editable is Vehicle) TextField(
                controller: arcController,
                textCapitalization: TextCapitalization.words,
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.firingArc
                ),
              ),
              //Range
              Container(height: 10),
              InputDecorator(
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.range
                ),
                child: DropdownButton<int>(
                  isDense: true,
                  isExpanded: true,
                  hint: Text(AppLocalizations.of(context)!.weapRange),
                  onTap: () => FocusScope.of(context).unfocus(),
                  items: [
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.rangeLevel1),
                      value: 0,
                    ),
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.rangeLevel2),
                      value: 1,
                    ),
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.rangeLevel3),
                      value: 2,
                    ),
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.rangeLevel4),
                      value: 3,
                    ),
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.rangeLevel5),
                      value: 4,
                    )
                  ],
                  value: weapon.range,
                  onChanged: (value) => setState(()=>weapon.range = value ?? 0),
                )
              ),
              //Damage
              Container(height: 10),
              InputDecorator(
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.itemDamage,
                ),
                child: DropdownButton<int>(
                  isDense: true,
                  isExpanded: true,
                  onTap: () => FocusScope.of(context).unfocus(),
                  items: [
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.damageLevel1),
                      value: 0,
                    ),
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.damageLevel2),
                      value: 1,
                    ),
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.damageLevel3),
                      value: 2,
                    ),
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.damageLevel4),
                      value: 3,
                    ),
                    DropdownMenuItem(
                      child: Text(AppLocalizations.of(context)!.damageLevel5),
                      value: 4,
                    ),
                  ],
                  value: weapon.itemState,
                  onChanged: (value) => setState(() => weapon.itemState = value ?? 0),
                )
              ),
              //Skill
              Container(height: 10),
              InputDecorator(
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.skill,
                ),
                child: DropdownButton<int>(
                  isDense: true,
                  isExpanded: true,
                  onTap: () => FocusScope.of(context).unfocus(),
                  items: List.generate(
                    weaponSkills.length,
                    (i) => DropdownMenuItem(
                      child: Text(weaponSkills[i]),
                      value: i
                    )
                  ),
                  value: weapon.skill == -1 ? null : weapon.skill,
                  onChanged: (value) => setState((){
                    weapon.skill = value ?? -1;
                    weapon.skillBase = Skill.skillsList(context)[weaponSkills[weapon.skill]]!;
                  }),
                  hint: Text(AppLocalizations.of(context)!.skill),
                )
              ),
              //SkillBase
              Container(height: 10),
              InputDecorator(
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.skillBase,
                ),
                child: DropdownButton<int>(
                  isDense: true,
                  isExpanded: true,
                  onTap: () => FocusScope.of(context).unfocus(),
                  items: List.generate(
                    6,
                    (i) => DropdownMenuItem(
                      child: Text(Creature.characteristics(context)[i]),
                      value: i
                    )
                  ),
                  value: weapon.skillBase == -1 ? null : weapon.skillBase,
                  onChanged: (value) => setState(() => weapon.skillBase = value ?? -1),
                  hint: Text(AppLocalizations.of(context)!.skillBase),
                )
              ),
              //Characteristics
              Container(height: 15),
              Center(child: Text(AppLocalizations.of(context)!.characteristic, style: Theme.of(context).textTheme.headline6),),
              Column(
                children: List.generate(weapon.characteristics.length,(i){
                  return InkResponse(
                    containedInkWell: true,
                    onTap: () => WeaponCharacteristicDialog(
                      wc: weapon.characteristics[i],
                      onClose: (wc){
                        setState(() => weapon.characteristics[i] = wc);
                      }
                    ).show(context),
                    child: Padding(
                      padding: EdgeInsets.all(5),
                      child: Row(
                        children: [
                          Expanded(
                            child: Text(weapon.characteristics[i].name + " " +weapon.characteristics[i].value.toString()),
                          ),
                          Text(weapon.characteristics[i].advantage.toString())
                        ],
                      )
                    )
                  );
                })
              ),
              TextButton.icon(
                label: Text(AppLocalizations.of(context)!.addCharacteristic),
                icon: Icon(Icons.add),
                onPressed: () =>
                  WeaponCharacteristicDialog(
                    onClose: (wc){
                      weapon.characteristics.add(wc);
                    }
                  ).show(context),
              ),
              //Add Brawn
              if(editable is Character || editable is Minion) SwitchListTile(
                value: weapon.addBrawn,
                onChanged: (value) => setState(() => weapon.addBrawn = value),
                title: Text(AppLocalizations.of(context)!.addBrawn),
              ),
              //Loaded
              SwitchListTile(
                value: weapon.loaded,
                onChanged: (value) => setState(() => weapon.loaded = value),
                title: Text(AppLocalizations.of(context)!.loaded)
              ),
              //Limited Ammo
              SwitchListTile(
                value: weapon.limitedAmmo,
                onChanged: (value) => setState((){
                  weapon.limitedAmmo = value;
                  weapon.ammo = 0;
                  if(value)
                    ammoController.text = "0";
                }),
                title: Text(AppLocalizations.of(context)!.slugthrower)
              ),
              //Ammo
              AnimatedSwitcher(
                duration: Duration(milliseconds: 300),
                transitionBuilder: (child, anim){
                  return SizeTransition(
                    sizeFactor: anim,
                    axisAlignment: -1,
                    child: child
                  );
                },
                child: weapon.limitedAmmo ? Padding(
                  padding: EdgeInsets.only(top: 2),
                  child: TextField(
                    controller: ammoController,
                    decoration: InputDecoration(
                      labelText: AppLocalizations.of(context)!.ammo,
                    ),
                    keyboardType: TextInputType.number,
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                  )
                ) : Container(),
              ),
              ButtonBar(
                children: [
                  TextButton(
                    child: Text(MaterialLocalizations.of(context).saveButtonLabel),
                    onPressed: weapon.name != "" && weapon.damage != -1 && weapon.critical != -1
                        && weapon.skill != -1 && weapon.skillBase != -1 ? (){
                      onClose(weapon);
                      Navigator.of(context).pop();
                    } : null,
                  ),
                  TextButton(
                    child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
                    onPressed: (){
                      Navigator.of(context).pop();
                    },
                  ),
                ],
              )
            ],
          )
        )
      )
    );
  }
}