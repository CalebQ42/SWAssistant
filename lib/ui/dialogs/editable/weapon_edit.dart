import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/dialogs/editable/weapon_char_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';
import 'package:swassistant/ui/misc/updating_switch_tile.dart';

class WeaponEditDialog{
  final Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;

  late Bottom bot;

  WeaponEditDialog({required this.onClose, Weapon? weap, required this.editable}) :
    weapon = weap == null ? Weapon() : Weapon.from(weap){
    var nameController = TextEditingController(text: weapon.name);
    nameController.addListener(() {
      weapon.name = nameController.text;
      bot.updateButtons();
    });
    var damageController = TextEditingController(text: weapon.damage?.toString());
    damageController.addListener(() {
      weapon.damage = int.tryParse(damageController.text);
      bot.updateButtons();
    });
    var criticalController = TextEditingController(text: weapon.critical?.toString());
    criticalController.addListener(() {
      weapon.critical = int.tryParse(criticalController.text);
      bot.updateButtons();
    });
    var hpController = TextEditingController(text: weapon.hp.toString());
    hpController.addListener(() =>
      weapon.hp = int.tryParse(hpController.text) ?? 0
    );
    var encumbranceController = TextEditingController(text: weapon.encumbrance.toString());
    encumbranceController.addListener(() =>
      weapon.encumbrance = int.tryParse(encumbranceController.text) ?? 0
    );
    var arcController = TextEditingController(text: weapon.firingArc);
    arcController.addListener(() =>
      weapon.firingArc = arcController.text
    );
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          onPressed: weapon.name != "" && weapon.damage != null && weapon.critical != null
              && weapon.skill != null && weapon.skillBase != null ? (){
            onClose(weapon);
            Navigator.of(context).pop();
          } : null,
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: (){
            Navigator.of(context).pop();
          },
        )],
      child: (context) =>
        Wrap(
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
            _WeaponDropdowns(weapon, bot),
            //Characteristics
            Container(height: 15),
            Center(child: Text(AppLocalizations.of(context)!.characteristic, style: Theme.of(context).textTheme.headline6),),
            _WeaponCharacteristics(weapon),
            //Add Brawn
            if(editable is Character || editable is Minion) UpdatingSwitchTile(
              value: weapon.addBrawn,
              onChanged: (value) => weapon.addBrawn = value,
              title: Text(AppLocalizations.of(context)!.addBrawn),
            ),
            //Loaded
            UpdatingSwitchTile(
              value: weapon.loaded,
              onChanged: (value) => weapon.loaded = value,
              title: Text(AppLocalizations.of(context)!.loaded)
            ),
            _WeaponAmmo(weapon)
          ],
        )
    );
  }

  void show(BuildContext context) => bot.show(context);
}

class _WeaponAmmo extends StatefulWidget{

  final Weapon weapon;

  const _WeaponAmmo(this.weapon, {Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _AmmoState();
}

class _AmmoState extends State<_WeaponAmmo>{

  late TextEditingController ammoController;

  @override
  void initState() {
    super.initState();
    ammoController = TextEditingController(text: widget.weapon.ammo.toString())
      ..addListener(() =>
        widget.weapon.ammo = int.tryParse(ammoController.text) ?? 0
      );
  }

  @override
  Widget build(BuildContext context) =>
    Column(
      children: [
        //Ammo
        AnimatedSwitcher(
          duration: const Duration(milliseconds: 300),
          transitionBuilder: (child, anim){
            return SizeTransition(
              sizeFactor: anim,
              axisAlignment: -1,
              child: child
            );
          },
          child: widget.weapon.limitedAmmo ? Padding(
            padding: const EdgeInsets.only(top: 5),
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
        //Limited Ammo
        SwitchListTile(
          value: widget.weapon.limitedAmmo,
          onChanged: (value) => setState(() => widget.weapon.limitedAmmo = value),
          title: Text(AppLocalizations.of(context)!.limitedAmmo)
        ),
      ],
    );
}

class _WeaponCharacteristics extends StatefulWidget{

  final Weapon weapon;

  const _WeaponCharacteristics(this.weapon, {Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _CharacteristicsState();
}

class _CharacteristicsState extends State<_WeaponCharacteristics>{

  @override
  Widget build(BuildContext context) =>
    Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: List.generate(widget.weapon.characteristics.length,(i){
        return Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Expanded(child: InkResponse(
              containedInkWell: true,
              onTap: () =>
                WeaponCharacteristicDialog(
                  characteristic: widget.weapon.characteristics[i],
                  onClose: (wc){
                    setState(() => widget.weapon.characteristics[i] = wc);
                  }
                ).show(context),
              child: Padding(
                padding: const EdgeInsets.all(5),
                child: Row(
                  children: [
                    Expanded(
                      child: Text(widget.weapon.characteristics[i].name +
                        (widget.weapon.characteristics[i].value != null ? " ${widget.weapon.characteristics[i].value}" : "")),
                    ),
                    if(widget.weapon.characteristics[i].advantage != null) Text(widget.weapon.characteristics[i].advantage.toString())
                  ],
                )
              )
            )),
            IconButton(
              onPressed: () =>
                setState(()=> widget.weapon.characteristics.removeAt(i)),
              icon: const Icon(Icons.delete_forever)
            )
          ]
        );
      })..add(
        TextButton.icon(
          label: Text(AppLocalizations.of(context)!.addCharacteristic),
          icon: const Icon(Icons.add),
          onPressed: () =>
            WeaponCharacteristicDialog(
              onClose: (wc) => setState(() => widget.weapon.characteristics.add(wc))
            ).show(context),
        ),
      )
    );
}

class _WeaponDropdowns extends StatefulWidget{

  final Weapon weapon;
  final Bottom bot;

  const _WeaponDropdowns(this.weapon, this.bot, {Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _WeaponState();
}

class _WeaponState extends State<_WeaponDropdowns>{

  @override
  Widget build(BuildContext context) {
    var weaponSkills = Weapon.weaponSkills(context);
    return DropdownButtonHideUnderline(
      child: Column(
        children: [
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
                  value: 0,
                  child: Text(AppLocalizations.of(context)!.rangeLevel1),
                ),
                DropdownMenuItem(
                  value: 1,
                  child: Text(AppLocalizations.of(context)!.rangeLevel2),
                ),
                DropdownMenuItem(
                  value: 2,
                  child: Text(AppLocalizations.of(context)!.rangeLevel3),
                ),
                DropdownMenuItem(
                  value: 3,
                  child: Text(AppLocalizations.of(context)!.rangeLevel4),
                ),
                DropdownMenuItem(
                  value: 4,
                  child: Text(AppLocalizations.of(context)!.rangeLevel5),
                )
              ],
              value: widget.weapon.range,
              onChanged: (value) => setState(() => widget.weapon.range = value ?? 0),
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
                  value: 0,
                  child: Text(AppLocalizations.of(context)!.damageLevel1),
                ),
                DropdownMenuItem(
                  value: 1,
                  child: Text(AppLocalizations.of(context)!.damageLevel2),
                ),
                DropdownMenuItem(
                  value: 2,
                  child: Text(AppLocalizations.of(context)!.damageLevel3),
                ),
                DropdownMenuItem(
                  value: 3,
                  child: Text(AppLocalizations.of(context)!.damageLevel4),
                ),
                DropdownMenuItem(
                  value: 4,
                  child: Text(AppLocalizations.of(context)!.damageLevel5),
                ),
              ],
              value: widget.weapon.itemState,
              onChanged: (value) => setState(() => widget.weapon.itemState = value ?? 0),
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
                  value: i,
                  child: Text(weaponSkills[i])
                )
              ),
              value: widget.weapon.skill,
              onChanged: (value) => setState((){
                widget.weapon.skill = value;
                widget.weapon.skillBase = Skill.skillsList(context)[weaponSkills[widget.weapon.skill!]]!;
                widget.bot.updateButtons();
              }),
              hint: Text(AppLocalizations.of(context)!.skill),
            )
          ),
          Container(height: 10),
          InputDecorator(
            decoration: InputDecoration(
              labelText: AppLocalizations.of(context)!.characteristic,
            ),
            child: DropdownButton<int>(
              isDense: true,
              isExpanded: true,
              onTap: () => FocusScope.of(context).unfocus(),
              items: List.generate(
                6,
                (i) => DropdownMenuItem(
                  value: i,
                  child: Text(Creature.characteristics(context)[i])
                )
              ),
              value: widget.weapon.skillBase,
              onChanged: (value) => setState(() {
                widget.weapon.skillBase = value;
                widget.bot.updateButtons();
              }),
              hint: Text(AppLocalizations.of(context)!.characteristic),
            )
          ),
        ]
      )
    );
  }
}