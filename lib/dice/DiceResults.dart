import 'package:flutter/material.dart';
import 'package:swassistant/dice/Sides.dart';
import 'package:swassistant/ui/UpDownStat.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class DiceResults{

  List<dynamic> _resultsMasterList = [];
  Map<String,int> results = {};

  bool subtractMode = false;

  void add(dynamic side){
    _resultsMasterList.add(side);
    if(side is SimpleSide && side.toString() != "")
      results[side.toString()] = (results[side.toString()] ?? 0) + (subtractMode ? -1 : 1);
    else if(side is ComplexSide){
      side.parts.forEach((element) =>
        results[element.name] = (results[element.name] ?? 0) + (subtractMode ? -element.value : element.value)
      );
    }
  }

  int getResult(String name) => results[name] ?? 0;

  void showCombinedResults(BuildContext context,{bool noSuccess = false}){
    bool isSuccess = true;
    var success = (results[AppLocalizations.of(context)!.success]! + results[AppLocalizations.of(context)!.triumph]!) - (results[AppLocalizations.of(context)!.failure]! + results[AppLocalizations.of(context)!.despair]!);
    if(success <= 0){
      isSuccess = false;
      success = success.abs();
    }
    bool isAdvantaged = true;
    var advantage = results[AppLocalizations.of(context)!.advantage]! - results[AppLocalizations.of(context)!.threat]!;
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
                child: Text(success.toString() + (isSuccess ? " " + AppLocalizations.of(context)!.success : " " + AppLocalizations.of(context)!.failure),
                  style: Theme.of(context).textTheme.headline6,
                ),
              ),
              if(advantage != 0) Center(
                child: Text(advantage.toString() + (isAdvantaged ? " " + AppLocalizations.of(context)!.advantage : " " + AppLocalizations.of(context)!.threat),
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(results[AppLocalizations.of(context)!.triumph]! > 0) Center(
                child: Text(results[AppLocalizations.of(context)!.triumph].toString() + " " + AppLocalizations.of(context)!.triumph,
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(results[AppLocalizations.of(context)!.despair]! > 0) Center(
                child: Text(results[AppLocalizations.of(context)!.despair].toString() + " " + AppLocalizations.of(context)!.despair,
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              if(results[AppLocalizations.of(context)!.lightSide]! > 0) Center(
                child: Text(results[AppLocalizations.of(context)!.lightSide].toString() + " " + AppLocalizations.of(context)!.lightSide,
                  style: Theme.of(context).textTheme.headline6,
                ),
              ),
              if(results[AppLocalizations.of(context)!.darkSide]! > 0) Center(
                child: Text(results[AppLocalizations.of(context)!.darkSide].toString() + " " + AppLocalizations.of(context)!.darkSide,
                  style: Theme.of(context).textTheme.headline6,
                )
              ),
              ButtonBar(
                children: [
                  TextButton(
                    child: Text(AppLocalizations.of(context)!.edit),
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

  void showResultsEdit(BuildContext context,{bool noSuccess = false, void Function(BuildContext, DiceResults)? alternateReturn}) =>
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
                Expanded(child: Text(AppLocalizations.of(context)!.success + ":")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[AppLocalizations.of(context)!.success] = (results[AppLocalizations.of(context)!.success] ?? 0) + 1,
                    onDownPressed: () =>
                      results[AppLocalizations.of(context)!.success] = (results[AppLocalizations.of(context)!.success] ?? 0) - 1,
                    getValue: () => results[AppLocalizations.of(context)!.success] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text(AppLocalizations.of(context)!.failure + ":")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[AppLocalizations.of(context)!.failure] = (results[AppLocalizations.of(context)!.failure] ?? 0) + 1,
                    onDownPressed: () =>
                      results[AppLocalizations.of(context)!.failure] = (results[AppLocalizations.of(context)!.failure] ?? 0) - 1,
                    getValue: () => results[AppLocalizations.of(context)!.failure] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text(AppLocalizations.of(context)!.advantage + ":")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[AppLocalizations.of(context)!.advantage] = (results[AppLocalizations.of(context)!.advantage] ?? 0) + 1,
                    onDownPressed: () =>
                      results[AppLocalizations.of(context)!.advantage] = (results[AppLocalizations.of(context)!.advantage] ?? 0) - 1,
                    getValue: () => results[AppLocalizations.of(context)!.advantage] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text(AppLocalizations.of(context)!.threat + ":")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[AppLocalizations.of(context)!.threat] = (results[AppLocalizations.of(context)!.threat] ?? 0) + 1,
                    onDownPressed: () =>
                      results[AppLocalizations.of(context)!.threat] = (results[AppLocalizations.of(context)!.threat] ?? 0) - 1,
                    getValue: () => results[AppLocalizations.of(context)!.threat] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text(AppLocalizations.of(context)!.triumph + ":")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[AppLocalizations.of(context)!.triumph] = (results[AppLocalizations.of(context)!.triumph] ?? 0) + 1,
                    onDownPressed: () =>
                      results[AppLocalizations.of(context)!.triumph] = (results[AppLocalizations.of(context)!.triumph] ?? 0) - 1,
                    getValue: () => results[AppLocalizations.of(context)!.triumph] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text(AppLocalizations.of(context)!.despair + ":")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[AppLocalizations.of(context)!.despair] = (results[AppLocalizations.of(context)!.despair] ?? 0) + 1,
                    onDownPressed: () =>
                      results[AppLocalizations.of(context)!.despair] = (results[AppLocalizations.of(context)!.despair] ?? 0) - 1,
                    getValue: () => results[AppLocalizations.of(context)!.despair] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text(AppLocalizations.of(context)!.lightSide + ":")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[AppLocalizations.of(context)!.lightSide] = (results[AppLocalizations.of(context)!.lightSide] ?? 0) + 1,
                    onDownPressed: () =>
                      results[AppLocalizations.of(context)!.lightSide] = (results[AppLocalizations.of(context)!.lightSide] ?? 0) - 1,
                    getValue: () => results[AppLocalizations.of(context)!.lightSide] ?? 0,
                  )
                )
              ]
            ),
            Row(
              children: [
                Expanded(child: Text(AppLocalizations.of(context)!.darkSide + ":")),
                Expanded(
                  child: UpDownStat(
                    onUpPressed: () =>
                      results[AppLocalizations.of(context)!.darkSide] = (results[AppLocalizations.of(context)!.darkSide] ?? 0) + 1,
                    onDownPressed: () =>
                      results[AppLocalizations.of(context)!.darkSide] = (results[AppLocalizations.of(context)!.darkSide] ?? 0) - 1,
                    getValue: () => results[AppLocalizations.of(context)!.darkSide] ?? 0,
                  )
                )
              ]
            ),
            ButtonBar(
              children: [
                TextButton(
                  child: Text(AppLocalizations.of(context)!.ret),
                  onPressed: (){
                    Navigator.of(context).pop();
                    if (alternateReturn == null){
                      if(!noSuccess && results[AppLocalizations.of(context)!.success] == 0 && results[AppLocalizations.of(context)!.failure] == 0)
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