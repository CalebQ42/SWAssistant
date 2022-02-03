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
import 'package:swassistant/ui/misc/bottom.dart';
import 'package:swassistant/ui/misc/edit_content.dart';

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
                (element) => element.name == Weapon.weaponSkills(context)[editable.weapons[i].skill!],
                orElse: () => Skill()
              );
              if(skill.name != "" && skill.base == editable.weapons[i].skillBase){
                var hold = SWDiceHolder(
                  ability: (skill.value! - editable.charVals[editable.weapons[i].skillBase!]).abs(),
                  proficiency: min(skill.value!, editable.charVals[editable.weapons[i].skillBase!]),
                  weaponPack: pack,
                );
                var accInd = editable.weapons[i].characteristics.indexWhere((element) => element.name == AppLocalizations.of(context)!.characteristicAccurate);
                var inaccInd = editable.weapons[i].characteristics.indexWhere((element) => element.name == AppLocalizations.of(context)!.characteristicInaccurate);
                //TODO: all passive characteristics
                if(accInd != -1){
                  hold.boost = editable.weapons[i].characteristics[accInd].value ?? 1;
                }
                if(inaccInd != -1){
                  hold.setback = editable.weapons[i].characteristics[inaccInd].value ?? 1;
                }
                hold.weaponPack = pack;
                hold.showDialog(context);
              }else{
                SWDiceHolder(
                  ability: editable.charVals[editable.weapons[i].skillBase!],
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
            ButtonBar(
              buttonPadding: EdgeInsets.zero,
              children: [
                IconButton(
                  constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                  icon: const Icon(Icons.info_outline),
                  splashRadius: 20,
                  onPressed: () =>
                    Bottom(
                      child: (context) {
                        String range;
                        switch(editable.weapons[i].range){
                          case 0:
                            range = AppLocalizations.of(context)!.rangeLevel1;
                            break;
                          case 1:
                            range = AppLocalizations.of(context)!.rangeLevel2;
                            break;
                          case 2:
                            range = AppLocalizations.of(context)!.rangeLevel3;
                            break;
                          case 3:
                            range = AppLocalizations.of(context)!.rangeLevel4;
                            break;
                          default:
                            range = AppLocalizations.of(context)!.rangeLevel5;
                            break;
                        }
                        String weapDamage = "";
                        switch(editable.weapons[i].itemState){
                          case 1:
                            weapDamage = AppLocalizations.of(context)!.damageLevel2;
                            break;
                          case 2:
                            weapDamage = AppLocalizations.of(context)!.damageLevel3;
                            break;
                          case 3:
                            weapDamage = AppLocalizations.of(context)!.damageLevel4;
                            break;
                          case 4:
                            weapDamage = AppLocalizations.of(context)!.damageLevel5;
                            break;
                        }
                        String skill;
                        switch(editable.weapons[i].skill){
                          case 0:
                            skill = AppLocalizations.of(context)!.skills3;
                            break;
                          case 1:
                            skill = AppLocalizations.of(context)!.skills11;
                            break;
                          case 2:
                            skill = AppLocalizations.of(context)!.skills13;
                            break;
                          case 3:
                            skill = AppLocalizations.of(context)!.skills16;
                            break;
                          case 4:
                            skill = AppLocalizations.of(context)!.skills21;
                            break;
                          default:
                            skill = AppLocalizations.of(context)!.skills22;
                            break;
                        }
                        String characteristic;
                        switch(editable.weapons[i].skillBase){
                          case 0:
                            characteristic = AppLocalizations.of(context)!.brawn;
                            break;
                          case 1:
                            characteristic = AppLocalizations.of(context)!.agility;
                            break;
                          case 2:
                            characteristic = AppLocalizations.of(context)!.intellect;
                            break;
                          case 3:
                            characteristic = AppLocalizations.of(context)!.cunning;
                            break;
                          case 4:
                            characteristic = AppLocalizations.of(context)!.willpower;
                            break;
                          default:
                            characteristic = AppLocalizations.of(context)!.presence;
                            break;
                        }
                        return Wrap(
                          alignment: WrapAlignment.center,
                          children: [
                            Container(height: 15),
                            Center(
                              child: Text(
                                editable.weapons[i].name,
                                style: Theme.of(context).textTheme.headline5,
                                textAlign: TextAlign.center,
                              )
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                skill + " (" + characteristic + ")",
                                style: Theme.of(context).textTheme.bodyText1,
                              ),
                            ),
                            if(!editable.weapons[i].loaded) Container(height: 5),
                            if(!editable.weapons[i].loaded) Center(
                              child: Text(
                                AppLocalizations.of(context)!.weaponOutOfAmmo,
                                style: Theme.of(context).textTheme.bodyText1,
                              ),
                            ),
                            if(weapDamage != "") Container(height: 5),
                            if(weapDamage != "") Center(
                              child: Text(
                                weapDamage + " " + AppLocalizations.of(context)!.itemDamage,
                                style: Theme.of(context).textTheme.bodyText1,
                              ),
                            ),
                            Container(height: 5,),
                            Center(
                              child: Text(
                                AppLocalizations.of(context)!.damage + ": " + editable.weapons[i].damage.toString() +
                                  (editable.weapons[i].addBrawn ? " + " + AppLocalizations.of(context)!.brawn : ""),
                                style: Theme.of(context).textTheme.bodyText1,
                              ),
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                AppLocalizations.of(context)!.critical + ": " + editable.weapons[i].critical.toString(),
                                style: Theme.of(context).textTheme.bodyText1,
                              ),
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                AppLocalizations.of(context)!.hardPoints + ": " + editable.weapons[i].hp.toString(),
                                style: Theme.of(context).textTheme.bodyText1,
                              ),
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                AppLocalizations.of(context)!.encum + ": " + editable.weapons[i].encumbrance.toString(),
                                style: Theme.of(context).textTheme.bodyText1,
                              ),
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                AppLocalizations.of(context)!.range + ": " + range,
                                style: Theme.of(context).textTheme.bodyText1,
                              ),
                            ),
                            if(editable.weapons[i].limitedAmmo) Container(height: 5),
                            if(editable.weapons[i].limitedAmmo) Center(
                              child: Text(
                                AppLocalizations.of(context)!.ammo + ": " + editable.weapons[i].ammo.toString(),
                                style: Theme.of(context).textTheme.bodyText1,
                              ),
                            ),
                            if(editable.weapons[i].characteristics.isNotEmpty) Container(height: 10),
                            if(editable.weapons[i].characteristics.isNotEmpty) Center(
                              child: Text(
                                AppLocalizations.of(context)!.characteristicPlural,
                                style: Theme.of(context).textTheme.headline6
                              )
                            ),
                            if(editable.weapons[i].characteristics.isNotEmpty) ...List.generate(
                              editable.weapons[i].characteristics.length * 2,
                              (ch){
                                if(ch%2 == 0) return Container(height:5);
                                var char = editable.weapons[i].characteristics[(ch/2).floor()];
                                String out = char.name;
                                if(char.value != 1 && char.value != null) out += " " + char.value.toString();
                                if(char.advantage != null) out += " " + AppLocalizations.of(context)!.advNeeded + ": " + char.advantage.toString();
                                return Center(
                                  child: Text(
                                    out,
                                    style: Theme.of(context).textTheme.bodyText1,
                                  ),
                                );
                              }
                            ),
                            Container(height: 10)
                          ],
                        );
                      }
                    ).show(context),
                )
              ]
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
                      editable.invKey.currentState?.update(() {});
                      editable.save(context: context);
                      ScaffoldMessenger.of(context).clearSnackBars();
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text(AppLocalizations.of(context)!.deletedWeapon),
                          action: SnackBarAction(
                            label: AppLocalizations.of(context)!.undo,
                            onPressed: (){
                              setState(() => editable.weapons.insert(i, temp));
                                editable.invKey.currentState?.update(() {});
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
                          if(editable.weapons[i].encumbrance != weapon.encumbrance){
                            setState(() => editable.weapons[i] = weapon);
                            editable.invKey.currentState?.update(() {});
                          } else {
                            setState(() => editable.weapons[i] = weapon);
                          }
                          editable.save(context: context);
                        },
                        weap: editable.weapons[i]
                      ).show(context)
                  )
                ]
              ),
              transitionBuilder: (child, anim){
                var offset = const Offset(1,0);
                if(child is Container){
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