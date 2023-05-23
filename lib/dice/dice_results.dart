import 'dart:math';

import 'package:darkstorm_common/bottom.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/dice/sides.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/items/weapon_characteristic.dart';
import 'package:swassistant/ui/misc/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class WeaponPack{
  Weapon weapon;
  int brawn;

  WeaponPack(this.weapon, [this.brawn = 0]);
}

class DiceResults{

  final List<dynamic> _resultsMasterList = [];
  final Map<String,int> results = {};

  bool subtractMode = false;

  void add(dynamic side){
    _resultsMasterList.add(side);
    if(side is SimpleSide && side.toString() != ""){
      results[side.toString()] = (results[side.toString()] ?? 0) + (subtractMode ? -1 : 1);
    }else if(side is ComplexSide){
      for(var side in side.parts){
        results[side.name] = (results[side.name] ?? 0) + (subtractMode ? -side.value : side.value);
      }
    }
  }

  int getResult(String name) => results[name] ?? 0;

  void showCombinedResults(BuildContext context, {bool noSuccess = false, WeaponPack? weaponPack}){
    var botKey = GlobalKey<BottomState>();
    Bottom(
      key: botKey,
      buttons: (context) => [
        TextButton(
          child: Text(AppLocalizations.of(context)!.edit),
          onPressed: (){
            Navigator.pop(context);
            showResultsEdit(context, weaponPack: weaponPack, noSuccess: noSuccess);
          },
        )
      ],
      children: (context) =>
        combinedDialog(context, botKey, noSuccess: noSuccess, weaponPack: weaponPack),
    ).show(context);
  }

  void showResultsEdit(BuildContext context,{bool noSuccess = false, WeaponPack? weaponPack, void Function(BuildContext, DiceResults)? alternateReturn}) =>
    Bottom(
      buttons: (context) => [
        TextButton(
          child: Text(AppLocalizations.of(context)!.ret),
          onPressed: (){
            Navigator.of(context).pop();
            if (alternateReturn == null){
              showCombinedResults(context, weaponPack: weaponPack, noSuccess: noSuccess);
            }else{
              alternateReturn(context, this);
            }
          },
        )],
      children: (context) => [
        Row(
          children: [
            Expanded(child: Text("${AppLocalizations.of(context)!.success}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[AppLocalizations.of(context)!.success] = getResult(AppLocalizations.of(context)!.success) + 1,
                onDownPressed: () =>
                  results[AppLocalizations.of(context)!.success] = getResult(AppLocalizations.of(context)!.success) - 1,
                getValue: () => getResult(AppLocalizations.of(context)!.success),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${AppLocalizations.of(context)!.failure}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[AppLocalizations.of(context)!.failure] = getResult(AppLocalizations.of(context)!.failure) + 1,
                onDownPressed: () =>
                  results[AppLocalizations.of(context)!.failure] = getResult(AppLocalizations.of(context)!.failure) - 1,
                getValue: () => getResult(AppLocalizations.of(context)!.failure),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${AppLocalizations.of(context)!.advantage}:")),
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
            Expanded(child: Text("${AppLocalizations.of(context)!.threat}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[AppLocalizations.of(context)!.threat] = getResult(AppLocalizations.of(context)!.threat) + 1,
                onDownPressed: () =>
                  results[AppLocalizations.of(context)!.threat] = getResult(AppLocalizations.of(context)!.threat) - 1,
                getValue: () => getResult(AppLocalizations.of(context)!.threat),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${AppLocalizations.of(context)!.triumph}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[AppLocalizations.of(context)!.triumph] = getResult(AppLocalizations.of(context)!.triumph) + 1,
                onDownPressed: () =>
                  results[AppLocalizations.of(context)!.triumph] = getResult(AppLocalizations.of(context)!.triumph) - 1,
                getValue: () => getResult(AppLocalizations.of(context)!.triumph),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${AppLocalizations.of(context)!.despair}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[AppLocalizations.of(context)!.despair] = getResult(AppLocalizations.of(context)!.despair) + 1,
                onDownPressed: () =>
                  results[AppLocalizations.of(context)!.despair] = getResult(AppLocalizations.of(context)!.despair) - 1,
                getValue: () => getResult(AppLocalizations.of(context)!.despair),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${AppLocalizations.of(context)!.lightSide}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[AppLocalizations.of(context)!.lightSide] = getResult(AppLocalizations.of(context)!.lightSide) + 1,
                onDownPressed: () =>
                  results[AppLocalizations.of(context)!.lightSide] = getResult(AppLocalizations.of(context)!.lightSide) - 1,
                getValue: () => getResult(AppLocalizations.of(context)!.lightSide),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${AppLocalizations.of(context)!.darkSide}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[AppLocalizations.of(context)!.darkSide] = getResult(AppLocalizations.of(context)!.darkSide) + 1,
                onDownPressed: () =>
                  results[AppLocalizations.of(context)!.darkSide] = getResult(AppLocalizations.of(context)!.darkSide) - 1,
                getValue: () => results[AppLocalizations.of(context)!.darkSide] ?? 0,
              )
            )
          ]
        )
      ],
    ).show(context);

  List<Widget> combinedDialog(BuildContext context, GlobalKey<BottomState> botKey, {bool noSuccess = false, WeaponPack? weaponPack}) {
    bool isSuccess = true;
    var success = (getResult(AppLocalizations.of(context)!.success) + getResult(AppLocalizations.of(context)!.triumph)) -
        (getResult(AppLocalizations.of(context)!.failure) + getResult(AppLocalizations.of(context)!.despair));
    if(success <= 0){
      isSuccess = false;
      success = success.abs();
    }
    bool isAdvantaged = true;
    var advantage = getResult(AppLocalizations.of(context)!.advantage) - getResult(AppLocalizations.of(context)!.threat);
    if(advantage < 0){
      isAdvantaged = false;
      advantage = advantage.abs();
    }
    List<WeaponCharacteristic>? advChars;
    if(weaponPack != null){
      advChars = weaponPack.weapon.characteristics.where((element) => element.advantage != null).toList();
    }
    var pierceInd = weaponPack?.weapon.characteristics.indexWhere((element) => element.name == AppLocalizations.of(context)!.characteristicPierce);
    int? d100Result;
    print(success);
    return [
      if(weaponPack == null && (!noSuccess || success != 0)) Center(
        child: Text(success.toString() + (isSuccess ? " ${AppLocalizations.of(context)!.success}" : " ${AppLocalizations.of(context)!.failure}"),
          style: Theme.of(context).textTheme.titleLarge,
        ),
      ),
      if(weaponPack != null) Center(
        child: Text(
          isSuccess ? "${weaponPack.weapon.addBrawn ? weaponPack.weapon.damage! + success + weaponPack.brawn :
            weaponPack.weapon.damage! + success} ${AppLocalizations.of(context)!.damage}" :
              "$success ${AppLocalizations.of(context)!.failure}",
          style: Theme.of(context).textTheme.titleLarge,
        ),
      ),
      if(weaponPack != null && isSuccess && pierceInd != null && pierceInd != -1) Center(
        child: Text(
          AppLocalizations.of(context)!.ignoreSoak(weaponPack.weapon.characteristics[pierceInd].value ?? 1),
          style: Theme.of(context).textTheme.titleLarge?.apply(fontSizeFactor: .75),
        )
      ),
      if(advantage != 0) Center(
        child: Text(advantage.toString() + (isAdvantaged ? " ${AppLocalizations.of(context)!.advantage}" : " ${AppLocalizations.of(context)!.threat}"),
          style: Theme.of(context).textTheme.titleLarge,
        )
      ),
      if(getResult(AppLocalizations.of(context)!.triumph) > 0) Center(
        child: Text("${results[AppLocalizations.of(context)!.triumph]} ${AppLocalizations.of(context)!.triumph}",
          style: Theme.of(context).textTheme.titleLarge,
        )
      ),
      if(getResult(AppLocalizations.of(context)!.despair) > 0) Center(
        child: Text("${results[AppLocalizations.of(context)!.despair]} ${AppLocalizations.of(context)!.despair}",
          style: Theme.of(context).textTheme.titleLarge,
        )
      ),
      if(getResult(AppLocalizations.of(context)!.lightSide) > 0) Center(
        child: Text("${results[AppLocalizations.of(context)!.lightSide]} ${AppLocalizations.of(context)!.lightSide}",
          style: Theme.of(context).textTheme.titleLarge,
        ),
      ),
      if(getResult(AppLocalizations.of(context)!.darkSide) > 0) Center(
        child: Text("${results[AppLocalizations.of(context)!.darkSide]} ${AppLocalizations.of(context)!.darkSide}",
          style: Theme.of(context).textTheme.titleLarge,
        )
      ),
      if(weaponPack != null && ((weaponPack.weapon.critical ?? 0) > 0 || (advChars != null && advChars.isNotEmpty))) ...[
        Container(height: 15,),
        Row(
          children: [
            Expanded(
              flex: 4,
              child: Text(AppLocalizations.of(context)!.characteristic)
            ),
            Expanded(
              child: Center(child: Text(AppLocalizations.of(context)!.advantageShort)),
            )
          ]
        ),
        const Divider(),
        if((weaponPack.weapon.critical ?? 0) > 0) Row(
          children: [
            Expanded(
              flex: 4,
              child: Text(AppLocalizations.of(context)!.critical)
            ),
            Expanded(
              child: Center(child: Text(weaponPack.weapon.critical.toString())),
            )
          ],
        ), ...(){
          if(advChars!.isEmpty){
            return <Widget>[];
          }
          return List<Widget>.generate(advChars.length,
            (index){
              var charText = advChars![index].name;
              if(advChars[index].value != null && advChars[index].value != 0){
                charText += " ${advChars[index].value}";
              }
              return Row(
                children: [
                  Expanded(
                    flex: 4,
                    child: Text(charText),
                  ),
                  Expanded(
                    child: Center(child: Text(advChars[index].advantage.toString())),
                  )
                ],
              );
            }
          );
        }()
      ],
      if(weaponPack != null && isSuccess && (weaponPack.weapon.critical ?? 0) > 0 && (getResult(AppLocalizations.of(context)!.triumph) > 0 ||
          getResult(AppLocalizations.of(context)!.advantage) >= (weaponPack.weapon.critical ?? 0))) Padding(
        padding: const EdgeInsets.all(15),
        child: Row(
          children: [
            Expanded(child: TextButton(
              child: Text(AppLocalizations.of(context)!.rollCrit),
              onPressed: () {
                d100Result = Random().nextInt(100) + 1;
                botKey.currentState?.update();
              }
            )),
            Expanded(child: Text(
              d100Result?.toString() ?? "",
              textAlign: TextAlign.center,
            ))
          ]
        )
      )
    ];
  }
}