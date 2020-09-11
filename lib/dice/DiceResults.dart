import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/dice/SWDice.dart';
import 'package:swassistant/dice/Sides.dart';
import 'package:swassistant/ui/UpDownStat.dart';

class DiceResults{

  List<dynamic> _resultsMasterList = List();
  Map<String,int> _results = {
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
    if(side is SimpleSide){
      _results[side.toString()] += (subtractMode ? -1 : 1);
    }else if(side is ComplexSide)
      side.parts.forEach((element) =>
        _results[element.name] += subtractMode ? -element.value : element.value);
    else
      throw("Needs to be a SimpleSide or ComplexSide");
  }

  int getResult(String name) => _results[name];

  void showCombinedResults(BuildContext context){
    bool isSuccess = true;
    var success = (_results[suc] + _results[tri]) - (_results[fai] + _results[des]);
    if(success <= 0){
      isSuccess = false;
      success = success.abs();
    }
    bool isAdvantaged = true;
    var advantage = _results[adv] - _results[thr];
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
              if(_results[tri] > 0) Center(
                child: Text(_results[tri].toString() + " Triumph",
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(_results[des] > 0) Center(
                child: Text(_results[des].toString() + " Despair",
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(_results[lig] > 0) Center(
                child: Text(_results[lig].toString() + " Light Side",
                  style: Theme.of(context).textTheme.headline6,
                ),
              ),
              if(_results[dar] > 0) Center(
                child: Text(_results[dar].toString() + " Dark Side",
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              ButtonBar(
                children: [
                  FlatButton(
                    child: Text("Edit"),
                    onPressed: (){
                      Navigator.of(context).pop();
                      showResultsEdit(context);
                    },
                  ),
                ],
              )
            ],
          )
        )
    );
  }

  void showResultsEdit(BuildContext context) =>
    showModalBottomSheet(
      context: context,
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
                      onUpPressed: () => _results[suc]++,
                      onDownPressed: () => _results[suc] --,
                      getValue: () => _results[suc],
                      getMin: () => 0,
                    )
                  )
                ]
              ),
              Row(
                children: [
                  Expanded(child: Text("Failure:")),
                  Expanded(
                    child: UpDownStat(
                      onUpPressed: () => _results[fai]++,
                      onDownPressed: () => _results[fai] --,
                      getValue: () => _results[fai],
                      getMin: () => 0,
                    )
                  )
                ]
              ),
              Row(
                children: [
                  Expanded(child: Text("Advantage:")),
                  Expanded(
                    child: UpDownStat(
                      onUpPressed: () => _results[adv]++,
                      onDownPressed: () => _results[adv] --,
                      getValue: () => _results[adv],
                      getMin: () => 0,
                    )
                  )
                ]
              ),
              Row(
                children: [
                  Expanded(child: Text("Threat:")),
                  Expanded(
                    child: UpDownStat(
                      onUpPressed: () => _results[thr]++,
                      onDownPressed: () => _results[thr] --,
                      getValue: () => _results[thr],
                      getMin: () => 0,
                    )
                  )
                ]
              ),
              Row(
                children: [
                  Expanded(child: Text("Triumph:")),
                  Expanded(
                    child: UpDownStat(
                      onUpPressed: () => _results[tri]++,
                      onDownPressed: () => _results[tri] --,
                      getValue: () => _results[tri],
                      getMin: () => 0,
                    )
                  )
                ]
              ),
              Row(
                children: [
                  Expanded(child: Text("Despair:")),
                  Expanded(
                    child: UpDownStat(
                      onUpPressed: () => _results[des]++,
                      onDownPressed: () => _results[des] --,
                      getValue: () => _results[des],
                      getMin: () => 0,
                    )
                  )
                ]
              ),
              Row(
                children: [
                  Expanded(child: Text("Light Side:")),
                  Expanded(
                    child: UpDownStat(
                      onUpPressed: () => _results[lig]++,
                      onDownPressed: () => _results[lig] --,
                      getValue: () => _results[lig],
                      getMin: () => 0,
                    )
                  )
                ]
              ),
              Row(
                children: [
                  Expanded(child: Text("Dark Side:")),
                  Expanded(
                    child: UpDownStat(
                      onUpPressed: () => _results[dar]++,
                      onDownPressed: () => _results[dar] --,
                      getValue: () => _results[dar],
                      getMin: () => 0,
                    )
                  )
                ]
              ),
              ButtonBar(
                children: [
                  FlatButton(
                    child: Text("Return"),
                    onPressed: (){
                      Navigator.of(context).pop();
                      showCombinedResults(context);
                    },
                  )
                ],
              )
            ],
          )
      )
    );
}