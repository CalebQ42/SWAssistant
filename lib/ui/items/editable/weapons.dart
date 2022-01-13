import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/dialogs/SWWeaponDialog.dart';
import 'package:swassistant/ui/dialogs/editable/WeaponEditDialog.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Weapons extends StatelessWidget{
  final bool editing;
  final Function() refresh;

  const Weapons({Key? key, required this.editing, required this.refresh}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    var weaponsList = List.generate(editable.weapons.length, (i) =>
      InkResponse(
        containedInkWell: true,
        highlightShape: BoxShape.rectangle,
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
            SWWeaponDialog(
              holder: (){
                if(editable is Creature){
                  var skill = editable.skills.firstWhere(
                    (element) => element.name == Weapon.weaponSkills(context)[editable.weapons[i].skill],
                    orElse: () => Skill()
                  );
                  if(skill.name != "" && skill.base == editable.weapons[i].skillBase){
                    return skill.getDice(editable);
                  }else{
                    return SWDiceHolder(
                      ability: min(skill.value, editable.charVals[editable.weapons[i].skillBase]),
                      proficiency: (skill.value - editable.charVals[editable.weapons[i].skillBase]).abs()
                    );
                  }
                }else{
                  return SWDiceHolder();
                }
              }(),
              weapon: editable.weapons[i],
              brawn: (editable is Creature) ? editable.charVals[0] : 0
            ).show(context);
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
              child: !editing ? Container(height: 24,)
              : ButtonBar(
                buttonPadding: EdgeInsets.zero,
                children: [
                  IconButton(
                    constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: const Icon(Icons.delete_forever),
                    onPressed: (){
                      var temp = Weapon.from(editable.weapons[i]);
                      editable.weapons.removeAt(i);
                      refresh();
                      editable.save(context: context);
                      ScaffoldMessenger.of(context).clearSnackBars();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(AppLocalizations.of(context)!.deletedWeapon),
                          action: SnackBarAction(
                            label: AppLocalizations.of(context)!.undo,
                            onPressed: (){
                              editable.weapons.insert(i, temp);
                              refresh();
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
                          editable.weapons[i] = weapon;
                          refresh();
                          editable.save(context: context);
                        },
                        weap: editable.weapons[i]
                      ).show(context)
                  )
                ]
              ),
              transitionBuilder: (child, anim){
                var offset = const Offset(1,0);
                if((!editing && child is ButtonBar) || (editing && child is Container)){
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
            child: editing ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  WeaponEditDialog(
                    editable: editable,
                    onClose: (weapon){
                      editable.weapons.add(weapon);
                      refresh();
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