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
    if(side is SimpleSide && side.toString() != ""){
      results[side.toString()] += (subtractMode ? -1 : 1);
    }else if(side is ComplexSide)
      side.parts.forEach((element) =>
        results[element.name] += subtractMode ? -element.value : element.value);
    else
      throw("Needs to be a SimpleSide or ComplexSide");
  }

  int getResult(String name) => results[name] ?? 0;

  void showCombinedResults(BuildContext context,{bool noSuccess = true}){
    bool isSuccess = true;
    var success = (results[suc] + results[tri]) - (results[fai] + results[des]);
    if(success <= 0){
      isSuccess = false;
      success = success.abs();
    }
    bool isAdvantaged = true;
    var advantage = results[adv] - results[thr];
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
              if(results[tri] > 0) Center(
                child: Text(results[tri].toString() + " Triumph",
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(results[des] > 0) Center(
                child: Text(results[des].toString() + " Despair",
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(results[lig] > 0) Center(
                child: Text(results[lig].toString() + " Light Side",
                  style: Theme.of(context).textTheme.headline6,
                ),
              ),
              if(results[dar] > 0) Center(
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
                    onUpPressed: () => results[suc]++,
                    onDownPressed: () => results[suc] --,
                    getValue: () => results[suc],
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Failure:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () => results[fai]++,
                    onDownPressed: () => results[fai] --,
                    getValue: () => results[fai],
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Advantage:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () => results[adv]++,
                    onDownPressed: () => results[adv] --,
                    getValue: () => results[adv],
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Threat:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () => results[thr]++,
                    onDownPressed: () => results[thr] --,
                    getValue: () => results[thr],
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Triumph:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () => results[tri]++,
                    onDownPressed: () => results[tri] --,
                    getValue: () => results[tri],
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Despair:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () => results[des]++,
                    onDownPressed: () => results[des] --,
                    getValue: () => results[des],
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Light Side:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () => results[lig]++,
                    onDownPressed: () => results[lig] --,
                    getValue: () => results[lig],
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text("Dark Side:")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () => results[dar]++,
                    onDownPressed: () => results[dar] --,
                    getValue: () => results[dar],
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