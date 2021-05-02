
//Displays a dialog to roll a specified number of SW Dice
import 'package:flutter/material.dart';
import 'package:swassistant/dice/DiceResults.dart';
import 'package:swassistant/dice/SWDice.dart';
import 'package:swassistant/dice/SWDiceHolder.dart';
import 'package:swassistant/dice/SWDice.dart' as SWDice;
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/ui/UpDownStat.dart';

class SWWeaponDialog extends StatelessWidget{
  final SWDiceHolder holder;
  final BuildContext context;
  final Weapon weapon;

  SWWeaponDialog({@required this.holder, @required this.context, @required this.weapon});

  @override
  Widget build(BuildContext context) =>
    Padding(
      padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 15)),
      child: Wrap(
        children: List.generate(
          SWDice.SWDice.length, (index) =>
            Row(
              children: <Widget>[
                Expanded(
                  child:Text(SWDice.SWDice[index])
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
        )..add(ButtonBar(
          children: [
            TextButton(
              child: Text("Fire!"),
              onPressed: (){
                Navigator.of(context).pop();
                _WeaponResults(weapon: weapon, results: holder.getDice().roll()).show(context);
              },
            ),
            TextButton(
              child: Text("Cancel"),
              onPressed: () => Navigator.of(context).pop(),
            )
          ],
        )
      ))
    );
  
  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) =>
        this
    );
}

class _WeaponResults extends StatelessWidget{
  final Weapon weapon;
  final DiceResults results;

  _WeaponResults({this.weapon, this.results});

  @override
  Widget build(BuildContext context){
    bool isSuccess = true;
    var success = (results.getResult(suc) + results.getResult(tri)) - (results.getResult(fai) + results.getResult(des));
    if(success <= 0){
      isSuccess = false;
      success = success.abs();
    }
    bool isAdvantaged = true;
    var advantage = results.getResult(adv) - results.getResult(thr);
    if(advantage < 0){
      isAdvantaged = false;
      advantage = advantage.abs();
    }
    return Padding(
      padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 15)),
      child: Wrap(
        children: [
          Center(
            child: Text(success.toString() + (isSuccess ? " Success" : " Failure"),
              style: Theme.of(context).textTheme.headline6,
            ),
          ),
          if(advantage != 0) Center(
            child: Text(advantage.toString() + (isAdvantaged ? " Advantage" : " Threat"),
              style: Theme.of(context).textTheme.headline6,
            )
          ),
          if(results.getResult(tri) > 0) Center(
            child: Text(results.getResult(tri).toString() + " Triumph",
              style: Theme.of(context).textTheme.headline6,
            )
          ),
          if(results.getResult(des)> 0) Center(
            child: Text(results.getResult(des).toString() + " Despair",
              style: Theme.of(context).textTheme.headline6,
            )
          ),
          if(results.getResult(lig) > 0) Center(
            child: Text(results.getResult(lig).toString() + " Light Side",
              style: Theme.of(context).textTheme.headline6,
            ),
          ),
          if(results.getResult(dar) > 0) Center(
            child: Text(results.getResult(dar).toString() + " Dark Side",
              style: Theme.of(context).textTheme.headline6,
            )
          ),
          if(weapon.critical > 0 || weapon.characteristics.length > 0) Column(
            children: [
              Container(height: 15,),
              Row(
                children: [
                  Expanded(
                    child: Text("Characteristic"),
                    flex: 4
                  ),
                  Expanded(
                    child: Center(child: Text("Adv")),
                  )
                ]
              ),
              Divider(),
              if(weapon.critical > 0) Row(
                children: [
                  Expanded(
                    child: Text("Critical"),
                    flex: 4
                  ),
                  Expanded(
                    child: Center(child: Text(weapon.critical.toString())),
                  )
                ],
              )
            ]..addAll((){
              if(weapon.characteristics.length == 0)
                return <Widget>[];
              return List<Widget>.generate(weapon.characteristics.length,
                (index){
                  var charText = weapon.characteristics[index].name;
                  if(weapon.characteristics[index].value != 0)
                    charText += " " + weapon.characteristics[index].value.toString();
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
            }())
          ),
          ButtonBar(
            children: [
              TextButton(
                child: Text("Edit"),
                onPressed: (){
                  Navigator.of(context).pop();
                  _WeaponResultsEdit(weapon: weapon, results: results,).show(context);
                },
              ),
            ],
          )
        ],
      )
    );
  }

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) =>
        this
    );
}

class _WeaponResultsEdit extends StatelessWidget{
  final Weapon weapon;
  final DiceResults results;

  _WeaponResultsEdit({this.weapon, this.results});

  Widget build(BuildContext context) =>
    Padding(
      padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 15)),
        child: Wrap(
        children: [
          Row(
            children: [
              Expanded(child: Text("Success:")),
              Expanded(
                child: UpDownStat(
                  onUpPressed: () => results.results[suc]++,
                  onDownPressed: () => results.results[suc] --,
                  getValue: () => results.results[suc],
                  min: 0,
                )
              )
            ]
          ),
          Row(
            children: [
              Expanded(child: Text("Failure:")),
              Expanded(
                child: UpDownStat(
                  onUpPressed: () => results.results[fai]++,
                  onDownPressed: () => results.results[fai] --,
                  getValue: () => results.results[fai],
                  min: 0,
                )
              )
            ]
          ),
          Row(
            children: [
              Expanded(child: Text("Advantage:")),
              Expanded(
                child: UpDownStat(
                  onUpPressed: () => results.results[adv]++,
                  onDownPressed: () => results.results[adv] --,
                  getValue: () => results.results[adv],
                  min: 0,
                )
              )
            ]
          ),
          Row(
            children: [
              Expanded(child: Text("Threat:")),
              Expanded(
                child: UpDownStat(
                  onUpPressed: () => results.results[thr]++,
                  onDownPressed: () => results.results[thr] --,
                  getValue: () => results.results[thr],
                  min: 0,
                )
              )
            ]
          ),
          Row(
            children: [
              Expanded(child: Text("Triumph:")),
              Expanded(
                child: UpDownStat(
                  onUpPressed: () => results.results[tri]++,
                  onDownPressed: () => results.results[tri] --,
                  getValue: () => results.results[tri],
                  min: 0,
                )
              )
            ]
          ),
          Row(
            children: [
              Expanded(child: Text("Despair:")),
              Expanded(
                child: UpDownStat(
                  onUpPressed: () => results.results[des]++,
                  onDownPressed: () => results.results[des] --,
                  getValue: () => results.results[des],
                  min: 0,
                )
              )
            ]
          ),
          Row(
            children: [
              Expanded(child: Text("Light Side:")),
              Expanded(
                child: UpDownStat(
                  onUpPressed: () => results.results[lig]++,
                  onDownPressed: () => results.results[lig] --,
                  getValue: () => results.results[lig],
                  min: 0,
                )
              )
            ]
          ),
          Row(
            children: [
              Expanded(child: Text("Dark Side:")),
              Expanded(
                child: UpDownStat(
                  onUpPressed: () => results.results[dar]++,
                  onDownPressed: () => results.results[dar] --,
                  getValue: () => results.results[dar],
                  min: 0,
                )
              )
            ]
          ),
          ButtonBar(
            children: [
              TextButton(
                child: Text("Return"),
                onPressed: (){
                  Navigator.of(context).pop();
                  _WeaponResults(weapon: weapon, results: results).show(context);
                },
              )
            ],
          )
        ],
      )
    );

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) =>
        this
    );
}