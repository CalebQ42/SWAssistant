import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/dice/dice_results.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/dialogs/editable/weapon_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/editable_common.dart';

class Weapons extends StatefulWidget{

  const Weapons({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => WeaponsState();
}

class WeaponsState extends State<Weapons> with StatefulCard{

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Editable.of(context).weapons.isEmpty;

  void update(void Function() updateFunc) => setState(updateFunc);

  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    var weaponsList = List.generate(editable.weapons.length, (i) =>
      InkResponse(
        containedInkWell: true,
        highlightShape: BoxShape.rectangle,
        //TODO: Make nice weapon info displya.
        // onLongPress: () =>
        //   Bottom(
        //     child: (context) =>
        //       Wrap(
        //         alignment: WrapAlignment.center,
        //         children: [
        //           Container(height: 15),
        //           Center(
        //             child: Text(
        //               editable.weapons[i].name,
        //               style: Theme.of(context).textTheme.headline5,
        //               textAlign: TextAlign.center,
        //             )
        //           ),
        //           Container(height: 5,),
        //           Center(
        //             child: Text(
        //               AppLocalizations.of(context)!.damage + ": " + editable.weapons[i].damage.toString(),
        //               style: Theme.of(context).textTheme.bodyText1,
        //             ),
        //           ),
        //           Container(height: 5),
        //           Center(
        //             child: Text(
        //               AppLocalizations.of(context)!.critical + ": " + editable.weapons[i].critical.toString(),
        //               style: Theme.of(context).textTheme.bodyText1,
        //             ),
        //           ),
        //           Container(height: 5),
        //           Center(
        //             child: Text(
        //               AppLocalizations.of(context)!.hardPoints + ": " + editable.weapons[i].hp.toString(),
        //               style: Theme.of(context).textTheme.bodyText1,
        //             ),
        //           ),
        //           Container(height: 5),
        //           Center(
        //             child: Text(
        //               AppLocalizations.of(context)!.hardPoints + ": " + editable.weapons[i].hp.toString(),
        //               style: Theme.of(context).textTheme.bodyText1,
        //             ),
        //           ),
        //           Container(height: 5),
        //           Center(
        //             child: Text(
        //               AppLocalizations.of(context)!.hardPoints + ": " + editable.weapons[i].hp.toString(),
        //               style: Theme.of(context).textTheme.bodyText1,
        //             ),
        //           ),
        //           Container(height: 5),
        //           Center(
        //             child: Text(
        //               AppLocalizations.of(context)!.hardPoints + ": " + editable.weapons[i].hp.toString(),
        //               style: Theme.of(context).textTheme.bodyText1,
        //             ),
        //           ),
        //           Container(height: 5),
        //           Center(
        //             child: Text(
        //               AppLocalizations.of(context)!.hardPoints + ": " + editable.weapons[i].hp.toString(),
        //               style: Theme.of(context).textTheme.bodyText1,
        //             ),
        //           ),
        //           Container(height: 5),
        //           Center(
        //             child: Text(
        //               AppLocalizations.of(context)!.hardPoints + ": " + editable.weapons[i].hp.toString(),
        //               style: Theme.of(context).textTheme.bodyText1,
        //             ),
        //           ),
        //           Container(height: 10),
        //         ],
        //       )
        //   ).show(context),
        onTap: (){
          if(editable.weapons[i].itemState == 4){
            ScaffoldMessenger.of(context).clearSnackBars();
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(AppLocalizations.of(context)!.brokenWeapon),
              )
            );
          }else if ((editable.weapons[i].limitedAmmo && editable.weapons[i].ammo <= 0) || !editable.weapons[i].loaded){
            ScaffoldMessenger.of(context).clearSnackBars();
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(AppLocalizations.of(context)!.weaponOutOfAmmo),
              )
            );
          }else{
            var pack = WeaponPack(editable.weapons[i], (editable is Creature) ? editable.charVals[0] : 0);
            if(editable is Creature){
              var skill = editable.skills.firstWhere(
                (element) => element.name == Weapon.weaponSkills(context)[editable.weapons[i].skill],
                orElse: () => Skill(value: 0)
              );
              if(skill.name != "" && skill.base == editable.weapons[i].skillBase){
                var hold = skill.getDice(editable);
                hold.weaponPack = pack;
                hold.showDialog(context);
              }else{
                SWDiceHolder(
                  ability: (skill.value - editable.charVals[editable.weapons[i].skillBase]).abs(),
                  proficiency: min(skill.value, editable.charVals[editable.weapons[i].skillBase]),
                  weaponPack: pack,
                ).showDialog(context);
              }
            }else{
              SWDiceHolder(weaponPack: pack).showDialog(context);
            }
          }
        },
        child: Row(
          children: [
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(vertical: 20),
                child: Text(editable.weapons[i].name)
              )
            ),
            AnimatedSwitcher(
              duration: const Duration(milliseconds: 250),
              child: !edit ? Container(height: 24,)
              : ButtonBar(
                buttonPadding: EdgeInsets.zero,
                children: [
                  IconButton(
                    constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: const Icon(Icons.delete_forever),
                    onPressed: (){
                      var temp = Weapon.from(editable.weapons[i]);
                      setState(() => editable.weapons.removeAt(i));
                      editable.save(context: context);
                      ScaffoldMessenger.of(context).clearSnackBars();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(AppLocalizations.of(context)!.deletedWeapon),
                          action: SnackBarAction(
                            label: AppLocalizations.of(context)!.undo,
                            onPressed: (){
                              setState(() => editable.weapons.insert(i, temp));
                              editable.save(context: context);
                            },
                          ),
                        )
                      );
                    }
                  ),
                  IconButton(
                    constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: const Icon(Icons.edit),
                    onPressed: () =>
                      WeaponEditDialog(
                        editable: editable,
                        onClose: (weapon){
                          setState(() => editable.weapons[i] = weapon);
                          editable.save(context: context);
                        },
                        weap: editable.weapons[i]
                      ).show(context)
                  )
                ]
              ),
              transitionBuilder: (child, anim){
                var offset = const Offset(1,0);
                if((!edit && child is ButtonBar) || (edit && child is Container)){
                  offset = const Offset(-1,0);
                }
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
      )
    );
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          Column(children: weaponsList),
          AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            transitionBuilder: (wid,anim){
              return SizeTransition(
                sizeFactor: anim,
                child: wid,
                axisAlignment: -1.0,
              );
            },
            child: edit ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  WeaponEditDialog(
                    editable: editable,
                    onClose: (weapon){
                      setState(() => editable.weapons.add(weapon));
                      editable.save(context: context);
                    }
                  ).show(context),
              )
            ) : Container(),
          )
        ],
      ),
    );
  }
}