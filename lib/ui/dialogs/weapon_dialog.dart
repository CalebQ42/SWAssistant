
//Displays a dialog to roll a specified number of SW Dice
import 'package:flutter/material.dart';
import 'package:swassistant/dice/dice_results.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/dice/swdice.dart' as swdice;
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/ui/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';

class SWWeaponDialog {
  final SWDiceHolder holder;
  final Weapon weapon;
  final int brawn;

  SWWeaponDialog({required this.holder, required this.weapon, this.brawn = 0});
  
  void show(BuildContext context) =>
    Bottom(
      buttons: (context) => [
        TextButton(
          child: Text(AppLocalizations.of(context)!.fire),
          onPressed: (){
            Navigator.of(context).pop();
            _WeaponResults(weapon: weapon, results: holder.getDice(context).roll(), brawn: brawn,).show(context);
          },
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: () => Navigator.of(context).pop(),
        )],
      child: (context) =>
        Wrap(
          children: List.generate(
            7, (index) =>
              Row(
                children: <Widget>[
                  Expanded(
                    child: Text(swdice.getName(context, index))
                  ),
                  Expanded(
                    child:UpDownStat(
                      onUpPressed: (){
                        switch(index){
                          case 0:
                            holder.ability++;
                            break;
                          case 1:
                            holder.proficiency++;
                            break;
                          case 2:
                            holder.difficulty++;
                            break;
                          case 3:
                            holder.challenge++;
                            break;
                          case 4:
                            holder.boost++;
                            break;
                          case 5:
                            holder.setback++;
                            break;
                          default:
                            holder.force++;
                        }
                      },
                      onDownPressed: (){
                        switch(index){
                          case 0:
                            holder.ability--;
                            break;
                          case 1:
                            holder.proficiency--;
                            break;
                          case 2:
                            holder.difficulty--;
                            break;
                          case 3:
                            holder.challenge--;
                            break;
                          case 4:
                            holder.boost--;
                            break;
                          case 5:
                            holder.setback--;
                            break;
                          default:
                            holder.force--;
                        }
                      },
                      getValue: (){
                        switch(index){
                          case 0:
                            return holder.ability;
                          case 1:
                            return holder.proficiency;
                          case 2:
                            return holder.difficulty;
                          case 3:
                            return holder.challenge;
                          case 4:
                            return holder.boost;
                          case 5:
                            return holder.setback;
                          default:
                            return holder.force;
                        }
                      },
                      min: 0,
                    )
                  )
                ],
            )
          )
        )
    ).show(context);
}

class _WeaponResults{
  final Weapon weapon;
  final DiceResults results;
  final int brawn;

  _WeaponResults({required this.weapon, required this.results, required this.brawn});

  Widget build(BuildContext context){
    bool isSuccess = true;
    var success = (results.getResult(AppLocalizations.of(context)!.success) + results.getResult(AppLocalizations.of(context)!.triumph)) - (results.getResult(AppLocalizations.of(context)!.failure) + results.getResult(AppLocalizations.of(context)!.despair));
    if(success <= 0){
      isSuccess = false;
      success = success.abs();
    }
    bool isAdvantaged = true;
    var advantage = results.getResult(AppLocalizations.of(context)!.advantage) - results.getResult(AppLocalizations.of(context)!.threat);
    if(advantage < 0){
      isAdvantaged = false;
      advantage = advantage.abs();
    }
    return Wrap(
      children: [
        Center(
          child: Text(
            isSuccess ? (weapon.addBrawn ? weapon.damage + success + brawn : weapon.damage + success).toString() + " " + AppLocalizations.of(context)!.damage
              : success.toString() + " " + AppLocalizations.of(context)!.failure,
            style: Theme.of(context).textTheme.headline6,
          ),
        ),
        if(advantage != 0) Center(
          child: Text(advantage.toString() + (isAdvantaged ? " " + AppLocalizations.of(context)!.advantage : " " + AppLocalizations.of(context)!.threat),
            style: Theme.of(context).textTheme.headline6,
          )
        ),
        if(results.getResult(AppLocalizations.of(context)!.triumph) > 0) Center(
          child: Text(results.getResult(AppLocalizations.of(context)!.triumph).toString() + " " + AppLocalizations.of(context)!.triumph,
            style: Theme.of(context).textTheme.headline6,
          )
        ),
        if(results.getResult(AppLocalizations.of(context)!.despair)> 0) Center(
          child: Text(results.getResult(AppLocalizations.of(context)!.despair).toString() + " " + AppLocalizations.of(context)!.despair,
            style: Theme.of(context).textTheme.headline6,
          )
        ),
        if(results.getResult(AppLocalizations.of(context)!.lightSide) > 0) Center(
          child: Text(results.getResult(AppLocalizations.of(context)!.lightSide).toString() + " " + AppLocalizations.of(context)!.lightSide,
            style: Theme.of(context).textTheme.headline6,
          ),
        ),
        if(results.getResult(AppLocalizations.of(context)!.darkSide) > 0) Center(
          child: Text(results.getResult(AppLocalizations.of(context)!.darkSide).toString() + " " + AppLocalizations.of(context)!.darkSide,
            style: Theme.of(context).textTheme.headline6,
          )
        ),
        if(weapon.critical > 0 || weapon.characteristics.isNotEmpty) Column(
          children: [
            Container(height: 15,),
            Row(
              children: [
                Expanded(
                  child: Text(AppLocalizations.of(context)!.characteristic),
                  flex: 4
                ),
                Expanded(
                  child: Center(child: Text(AppLocalizations.of(context)!.advantageShort)),
                )
              ]
            ),
            const Divider(),
            if(weapon.critical > 0) Row(
              children: [
                Expanded(
                  child: Text(AppLocalizations.of(context)!.critical),
                  flex: 4
                ),
                Expanded(
                  child: Center(child: Text(weapon.critical.toString())),
                )
              ],
            ), ...(){
            if(weapon.characteristics.isEmpty){
              return <Widget>[];
            }
            return List<Widget>.generate(weapon.characteristics.length,
              (index){
                var charText = weapon.characteristics[index].name;
                if(weapon.characteristics[index].value != 0){
                  charText += " " + weapon.characteristics[index].value.toString();
                }
                return Row(
                  children: [
                    Expanded(
                      child: Text(charText),
                      flex: 4,
                    ),
                    Expanded(
                      child: Center(child: Text(weapon.characteristics[index].advantage.toString())),
                    )
                  ],
                );
              }
            );
          }()
          ]
        )
      ],
    );
  }

  void show(BuildContext context) =>
    Bottom(
      child: build,
      buttons: (context) => [
        TextButton(
          child: Text(AppLocalizations.of(context)!.edit),
          onPressed: (){
            Navigator.of(context).pop();
            results.showResultsEdit(context, alternateReturn: (context, results){
              _WeaponResults(weapon: weapon, results: results, brawn: brawn,).show(context);
            });
          },
        )]
    ).show(context);
}