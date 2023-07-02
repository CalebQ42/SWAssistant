import 'dart:math';

import 'package:flutter/material.dart';
import 'package:swassistant/dice/dice_results.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/dialogs/editable/weapon_edit.dart';
import 'package:darkstorm_common/bottom.dart';
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
    var app = SW.of(context);
    var weaponsList = List.generate(editable.weapons.length, (i) =>
      InkResponse(
        containedInkWell: true,
        highlightShape: BoxShape.rectangle,
        onTap: (){
          if(editable.weapons[i].itemState == 4){
            ScaffoldMessenger.of(context).clearSnackBars();
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(app.locale.brokenWeapon),
              )
            );
          }else if ((editable.weapons[i].limitedAmmo && editable.weapons[i].ammo <= 0) || !editable.weapons[i].loaded){
            ScaffoldMessenger.of(context).clearSnackBars();
            ScaffoldMessenger.of(context).showSnackBar(
              SnackBar(
                content: Text(app.locale.weaponOutOfAmmo),
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
                  ability: ((skill.value ?? 0) - (editable.weapons[i].skillBase != null ? editable.charVals[editable.weapons[i].skillBase!] : 0)).abs(),
                  proficiency: min((skill.value ?? 0), (editable.weapons[i].skillBase != null ? editable.charVals[editable.weapons[i].skillBase!] : 0)),
                  weaponPack: pack,
                );
                var accInd = editable.weapons[i].characteristics.indexWhere((element) => element.name == app.locale.characteristicAccurate);
                var inaccInd = editable.weapons[i].characteristics.indexWhere((element) => element.name == app.locale.characteristicInaccurate);
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
                  ability: (editable.weapons[i].skillBase != null ? editable.charVals[editable.weapons[i].skillBase!] : 0),
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
                            range = app.locale.rangeLevel1;
                            break;
                          case 1:
                            range = app.locale.rangeLevel2;
                            break;
                          case 2:
                            range = app.locale.rangeLevel3;
                            break;
                          case 3:
                            range = app.locale.rangeLevel4;
                            break;
                          default:
                            range = app.locale.rangeLevel5;
                            break;
                        }
                        String weapDamage = "";
                        switch(editable.weapons[i].itemState){
                          case 1:
                            weapDamage = app.locale.damageLevel2;
                            break;
                          case 2:
                            weapDamage = app.locale.damageLevel3;
                            break;
                          case 3:
                            weapDamage = app.locale.damageLevel4;
                            break;
                          case 4:
                            weapDamage = app.locale.damageLevel5;
                            break;
                        }
                        String skill;
                        switch(editable.weapons[i].skill){
                          case 0:
                            skill = app.locale.skills3;
                            break;
                          case 1:
                            skill = app.locale.skills11;
                            break;
                          case 2:
                            skill = app.locale.skills13;
                            break;
                          case 3:
                            skill = app.locale.skills16;
                            break;
                          case 4:
                            skill = app.locale.skills21;
                            break;
                          default:
                            skill = app.locale.skills22;
                            break;
                        }
                        String characteristic;
                        switch(editable.weapons[i].skillBase){
                          case 0:
                            characteristic = app.locale.brawn;
                            break;
                          case 1:
                            characteristic = app.locale.agility;
                            break;
                          case 2:
                            characteristic = app.locale.intellect;
                            break;
                          case 3:
                            characteristic = app.locale.cunning;
                            break;
                          case 4:
                            characteristic = app.locale.willpower;
                            break;
                          default:
                            characteristic = app.locale.presence;
                            break;
                        }
                        return Wrap(
                          alignment: WrapAlignment.center,
                          children: [
                            Container(height: 15),
                            Center(
                              child: Text(
                                editable.weapons[i].name,
                                style: Theme.of(context).textTheme.headlineSmall,
                                textAlign: TextAlign.center,
                              )
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                "$skill ($characteristic)",
                                style: Theme.of(context).textTheme.bodyLarge,
                              ),
                            ),
                            if(!editable.weapons[i].loaded) Container(height: 5),
                            if(!editable.weapons[i].loaded) Center(
                              child: Text(
                                app.locale.weaponOutOfAmmo,
                                style: Theme.of(context).textTheme.bodyLarge,
                              ),
                            ),
                            if(weapDamage != "") Container(height: 5),
                            if(weapDamage != "") Center(
                              child: Text(
                                "$weapDamage ${app.locale.itemDamage}",
                                style: Theme.of(context).textTheme.bodyLarge,
                              ),
                            ),
                            Container(height: 5,),
                            Center(
                              child: Text(
                                "${app.locale.damage}: ${editable.weapons[i].damage}${editable.weapons[i].addBrawn ? " + ${app.locale.brawn}" : ""}",
                                style: Theme.of(context).textTheme.bodyLarge,
                              ),
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                "${app.locale.critical}: ${editable.weapons[i].critical}",
                                style: Theme.of(context).textTheme.bodyLarge,
                              ),
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                "${app.locale.hardPoints}: ${editable.weapons[i].hp}",
                                style: Theme.of(context).textTheme.bodyLarge,
                              ),
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                "${app.locale.encum}: ${editable.weapons[i].encumbrance}",
                                style: Theme.of(context).textTheme.bodyLarge,
                              ),
                            ),
                            Container(height: 5),
                            Center(
                              child: Text(
                                "${app.locale.range}: $range",
                                style: Theme.of(context).textTheme.bodyLarge,
                              ),
                            ),
                            if(editable.weapons[i].limitedAmmo) Container(height: 5),
                            if(editable.weapons[i].limitedAmmo) Center(
                              child: Text(
                                "${app.locale.ammo}: ${editable.weapons[i].ammo}",
                                style: Theme.of(context).textTheme.bodyLarge,
                              ),
                            ),
                            if(editable.weapons[i].characteristics.isNotEmpty) Container(height: 10),
                            if(editable.weapons[i].characteristics.isNotEmpty) Center(
                              child: Text(
                                app.locale.characteristicPlural,
                                style: Theme.of(context).textTheme.titleLarge
                              )
                            ),
                            if(editable.weapons[i].characteristics.isNotEmpty) ...List.generate(
                              editable.weapons[i].characteristics.length * 2,
                              (ch){
                                if(ch%2 == 0) return Container(height:5);
                                var char = editable.weapons[i].characteristics[(ch/2).floor()];
                                String out = char.name;
                                if(char.value != 1 && char.value != null) out += " ${char.value}";
                                if(char.advantage != null) out += " ${app.locale.advNeeded}: ${char.advantage}";
                                return Center(
                                  child: Text(
                                    out,
                                    style: Theme.of(context).textTheme.bodyLarge,
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
                          content: Text(app.locale.deletedWeapon),
                          action: SnackBarAction(
                            label: app.locale.undo,
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
                axisAlignment: -1.0,
                child: wid,
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