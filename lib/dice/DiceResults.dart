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
      int tmp = results[side.toString()]!;
      tmp += (subtractMode ? -1 : 1);
      results[side.toString()] = tmp;
    }else if(side is ComplexSide){
      side.parts.forEach((element){
        int tmp = results[side.toString()]!;
        tmp += subtractMode ? -element.value : element.value;
        results[side.toString()] = tmp;
      });
    }else
      throw("Needs to be a SimpleSide or ComplexSide");
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
                    onUpPressed: (){
                      var tmp = results[suc]!;
                      tmp++;
                      results[suc] = tmp;
                    },
                    onDownPressed: (){
                      var tmp = results[suc]!;
                      tmp--;
                      results[suc] = tmp;
                    },
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
                    onUpPressed: (){
                      var tmp = results[fai]!;
                      tmp++;
                      results[fai] = tmp;
                    },
                    onDownPressed: (){
                      var tmp = results[fai]!;
                      tmp--;
                      results[fai] = tmp;
                    },
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
                    onUpPressed: (){
                      var tmp = results[adv]!;
                      tmp++;
                      results[adv] = tmp;
                    },
                    onDownPressed: (){
                      var tmp = results[adv]!;
                      tmp--;
                      results[adv] = tmp;
                    },
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
                    onUpPressed: (){
                      var tmp = results[thr]!;
                      tmp++;
                      results[thr] = tmp;
                    },
                    onDownPressed: (){
                      var tmp = results[thr]!;
                      tmp--;
                      results[thr] = tmp;
                    },
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
                    onUpPressed: (){
                      var tmp = results[tri]!;
                      tmp++;
                      results[tri] = tmp;
                    },
                    onDownPressed: (){
                      var tmp = results[tri]!;
                      tmp--;
                      results[tri] = tmp;
                    },
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
                    onUpPressed: (){
                      var tmp = results[des]!;
                      tmp++;
                      results[des] = tmp;
                    },
                    onDownPressed: (){
                      var tmp = results[des]!;
                      tmp--;
                      results[des] = tmp;
                    },
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
                    onUpPressed: (){
                      var tmp = results[lig]!;
                      tmp++;
                      results[lig] = tmp;
                    },
                    onDownPressed: (){
                      var tmp = results[lig]!;
                      tmp--;
                      results[lig] = tmp;
                    },
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
                    onUpPressed: (){
                      var tmp = results[dar]!;
                      tmp++;
                      results[dar] = tmp;
                    },
                    onDownPressed: (){
                      var tmp = results[dar]!;
                      tmp--;
                      results[dar] = tmp;
                    },
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