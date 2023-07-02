import 'dart:math';

import 'package:darkstorm_common/bottom.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/dice/sides.dart';
import 'package:swassistant/items/weapon.dart';
import 'package:swassistant/items/weapon_characteristic.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/misc/up_down.dart';

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
          child: Text(SW.of(context).locale.edit),
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

  void showResultsEdit(BuildContext context,{bool noSuccess = false, WeaponPack? weaponPack, void Function(BuildContext, DiceResults)? alternateReturn}) {
    var app = SW.of(context);
    Bottom(
      buttons: (context) => [
        TextButton(
          child: Text(app.locale.ret),
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
            Expanded(child: Text("${app.locale.success}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[app.locale.success] = getResult(app.locale.success) + 1,
                onDownPressed: () =>
                  results[app.locale.success] = getResult(app.locale.success) - 1,
                getValue: () => getResult(app.locale.success),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${app.locale.failure}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[app.locale.failure] = getResult(app.locale.failure) + 1,
                onDownPressed: () =>
                  results[app.locale.failure] = getResult(app.locale.failure) - 1,
                getValue: () => getResult(app.locale.failure),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${app.locale.advantage}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[app.locale.advantage] = (results[app.locale.advantage] ?? 0) + 1,
                onDownPressed: () =>
                  results[app.locale.advantage] = (results[app.locale.advantage] ?? 0) - 1,
                getValue: () => results[app.locale.advantage] ?? 0,
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${app.locale.threat}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[app.locale.threat] = getResult(app.locale.threat) + 1,
                onDownPressed: () =>
                  results[app.locale.threat] = getResult(app.locale.threat) - 1,
                getValue: () => getResult(app.locale.threat),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${app.locale.triumph}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[app.locale.triumph] = getResult(app.locale.triumph) + 1,
                onDownPressed: () =>
                  results[app.locale.triumph] = getResult(app.locale.triumph) - 1,
                getValue: () => getResult(app.locale.triumph),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${app.locale.despair}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[app.locale.despair] = getResult(app.locale.despair) + 1,
                onDownPressed: () =>
                  results[app.locale.despair] = getResult(app.locale.despair) - 1,
                getValue: () => getResult(app.locale.despair),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${app.locale.lightSide}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[app.locale.lightSide] = getResult(app.locale.lightSide) + 1,
                onDownPressed: () =>
                  results[app.locale.lightSide] = getResult(app.locale.lightSide) - 1,
                getValue: () => getResult(app.locale.lightSide),
              )
            )
          ]
        ),
        Row(
          children: [
            Expanded(child: Text("${app.locale.darkSide}:")),
            Expanded(
              child: UpDownStat(
                onUpPressed: () =>
                  results[app.locale.darkSide] = getResult(app.locale.darkSide) + 1,
                onDownPressed: () =>
                  results[app.locale.darkSide] = getResult(app.locale.darkSide) - 1,
                getValue: () => results[app.locale.darkSide] ?? 0,
              )
            )
          ]
        )
      ],
    ).show(context);
  }

  List<Widget> combinedDialog(BuildContext context, GlobalKey<BottomState> botKey, {bool noSuccess = false, WeaponPack? weaponPack}) {
    bool isSuccess = true;
    var app = SW.of(context);
    var success = (getResult(app.locale.success) + getResult(app.locale.triumph)) -
        (getResult(app.locale.failure) + getResult(app.locale.despair));
    if(success <= 0){
      isSuccess = false;
      success = success.abs();
    }
    bool isAdvantaged = true;
    var advantage = getResult(app.locale.advantage) - getResult(app.locale.threat);
    if(advantage < 0){
      isAdvantaged = false;
      advantage = advantage.abs();
    }
    List<WeaponCharacteristic>? advChars;
    if(weaponPack != null){
      advChars = weaponPack.weapon.characteristics.where((element) => element.advantage != null).toList();
    }
    var pierceInd = weaponPack?.weapon.characteristics.indexWhere((element) => element.name == app.locale.characteristicPierce);
    int? d100Result;
    return [
      if(weaponPack == null && (!noSuccess || success != 0)) Center(
        child: Text(success.toString() + (isSuccess ? " ${app.locale.success}" : " ${app.locale.failure}"),
          style: Theme.of(context).textTheme.titleLarge,
        ),
      ),
      if(weaponPack != null) Center(
        child: Text(
          isSuccess ? "${weaponPack.weapon.addBrawn ? weaponPack.weapon.damage! + success + weaponPack.brawn :
            weaponPack.weapon.damage! + success} ${app.locale.damage}" :
              "$success ${app.locale.failure}",
          style: Theme.of(context).textTheme.titleLarge,
        ),
      ),
      if(weaponPack != null && isSuccess && pierceInd != null && pierceInd != -1) Center(
        child: Text(
          app.locale.ignoreSoak(weaponPack.weapon.characteristics[pierceInd].value ?? 1),
          style: Theme.of(context).textTheme.titleLarge?.apply(fontSizeFactor: .75),
        )
      ),
      if(advantage != 0) Center(
        child: Text(advantage.toString() + (isAdvantaged ? " ${app.locale.advantage}" : " ${app.locale.threat}"),
          style: Theme.of(context).textTheme.titleLarge,
        )
      ),
      if(getResult(app.locale.triumph) > 0) Center(
        child: Text("${results[app.locale.triumph]} ${app.locale.triumph}",
          style: Theme.of(context).textTheme.titleLarge,
        )
      ),
      if(getResult(app.locale.despair) > 0) Center(
        child: Text("${results[app.locale.despair]} ${app.locale.despair}",
          style: Theme.of(context).textTheme.titleLarge,
        )
      ),
      if(getResult(app.locale.lightSide) > 0) Center(
        child: Text("${results[app.locale.lightSide]} ${app.locale.lightSide}",
          style: Theme.of(context).textTheme.titleLarge,
        ),
      ),
      if(getResult(app.locale.darkSide) > 0) Center(
        child: Text("${results[app.locale.darkSide]} ${app.locale.darkSide}",
          style: Theme.of(context).textTheme.titleLarge,
        )
      ),
      if(weaponPack != null && ((weaponPack.weapon.critical ?? 0) > 0 || (advChars != null && advChars.isNotEmpty))) ...[
        Container(height: 15,),
        Row(
          children: [
            Expanded(
              flex: 4,
              child: Text(app.locale.characteristic)
            ),
            Expanded(
              child: Center(child: Text(app.locale.advantageShort)),
            )
          ]
        ),
        const Divider(),
        if((weaponPack.weapon.critical ?? 0) > 0) Row(
          children: [
            Expanded(
              flex: 4,
              child: Text(app.locale.critical)
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
      if(weaponPack != null && isSuccess && (weaponPack.weapon.critical ?? 0) > 0 && (getResult(app.locale.triumph) > 0 ||
          getResult(app.locale.advantage) >= (weaponPack.weapon.critical ?? 0))) Padding(
        padding: const EdgeInsets.all(15),
        child: Row(
          children: [
            Expanded(child: TextButton(
              child: Text(app.locale.rollCrit),
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