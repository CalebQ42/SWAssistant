import 'package:flutter/material.dart';
import 'package:swassistant/dice/SWDice.dart';
import 'package:swassistant/dice/Sides.dart';
import 'package:swassistant/ui/UpDownStat.dart';

class DiceResults{

  List<dynamic> _resultsMasterList = [];
  Map<String,int> results = {
    suc : 0,
    fai : 0,
    adv : 0,
    thr : 0,
    tri : 0,
    des : 0,
    lig : 0,
    dar : 0
  };

  bool subtractMode = false;

  void add(dynamic side){
    _resultsMasterList.add(side);
    if(side is SimpleSide && side.toString() != "")
      results[side.toString()] = (results[side.toString()] ?? 0) + (subtractMode ? -1 : 1);
    else if(side is ComplexSide){
      side.parts.forEach((element) =>
        results[side.toString()] = (results[side.toString()] ?? 0) + (subtractMode ? -element.value : element.value)
      );
    }
  }

  int getResult(String name) => results[name] ?? 0;

  void showCombinedResults(BuildContext context,{bool noSuccess = true}){
    bool isSuccess = true;
    var success = (results[suc]! + results[tri]!) - (results[fai]! + results[des]!);
    if(success <= 0){
      isSuccess = false;
      success = success.abs();
    }
    bool isAdvantaged = true;
    var advantage = results[adv]! - results[thr]!;
    if(advantage < 0){
      isAdvantaged = false;
      advantage = advantage.abs();
    }
    showModalBottomSheet(
      context: context,
      builder: (context) =>
        Padding(
          padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 15)),
          child: Wrap(
            children: [
              if(!noSuccess) Center(
                child: Text(success.toString() + (isSuccess ? " Success" : " Failure"),
                  style: Theme.of(context).textTheme.headline6,
                ),
              ),
              if(advantage != 0) Center(
                child: Text(advantage.toString() + (isAdvantaged ? " Advantage" : " Threat"),
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(results[tri]! > 0) Center(
                child: Text(results[tri].toString() + " Triumph",
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(results[des]! > 0) Center(
                child: Text(results[des].toString() + " Despair",
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(results[lig]! > 0) Center(
                child: Text(results[lig].toString() + " Light Side",
                  style: Theme.of(context).textTheme.headline6,
                ),
              ),
              if(results[dar]! > 0) Center(
                child: Text(results[dar].toString() + " Dark Side",
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              ButtonBar(
                children: [
                  TextButton(
                    child: Text("Edit"),
                    onPressed: (){
                      Navigator.of(context).pop();
                      showResultsEdit(context, noSuccess: noSuccess);
                    },
                  ),
                ],
              )
            ],
          )
        )
    );
  }

  void showResultsEdit(BuildContext context,{bool noSuccess = true, void Function(BuildContext, DiceResults)? alternateReturn}) =>
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (context) =>
        Padding(
          padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 15)),
          child: Wrap(
          children: [
            Row(
              children: [
                Expanded(child: Text("Success:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[suc] = (results[suc] ?? 0) + 1,
                    onDownPressed: () =>
                      results[suc] = (results[suc] ?? 0) - 1,
                    getValue: () => results[suc] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Failure:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[fai] = (results[fai] ?? 0) + 1,
                    onDownPressed: () =>
                      results[fai] = (results[fai] ?? 0) - 1,
                    getValue: () => results[fai] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Advantage:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[adv] = (results[adv] ?? 0) + 1,
                    onDownPressed: () =>
                      results[adv] = (results[adv] ?? 0) - 1,
                    getValue: () => results[adv] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Threat:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[thr] = (results[thr] ?? 0) + 1,
                    onDownPressed: () =>
                      results[thr] = (results[thr] ?? 0) - 1,
                    getValue: () => results[thr] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Triumph:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[tri] = (results[tri] ?? 0) + 1,
                    onDownPressed: () =>
                      results[tri] = (results[tri] ?? 0) - 1,
                    getValue: () => results[tri] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Despair:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[des] = (results[des] ?? 0) + 1,
                    onDownPressed: () =>
                      results[des] = (results[des] ?? 0) - 1,
                    getValue: () => results[des] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Light Side:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[lig] = (results[lig] ?? 0) + 1,
                    onDownPressed: () =>
                      results[lig] = (results[lig] ?? 0) - 1,
                    getValue: () => results[lig] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Dark Side:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[dar] = (results[dar] ?? 0) + 1,
                    onDownPressed: () =>
                      results[dar] = (results[dar] ?? 0) - 1,
                    getValue: () => results[dar] ?? 0,
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
                    if (alternateReturn == null){
                      if(!noSuccess && results[suc] == 0 && results[fai] == 0)
                        noSuccess = false;
                      showCombinedResults(context, noSuccess: noSuccess);
                    }else{
                      alternateReturn(context, this);
                    }
                  },
                ),
              ],
            )
          ],
        )
      )
    );
}