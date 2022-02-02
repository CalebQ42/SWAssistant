
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/item.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/misc/editable_common.dart';
import 'package:swassistant/ui/misc/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class WoundStrain extends StatefulWidget{

  const WoundStrain({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => WoundStrainState();
}

class WoundStrainState extends State<WoundStrain> with StatefulCard {

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => false;
  Item? healingItem;

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null) throw "Wound Strain card used on non Character";
    healingItem = null;
    for(var i in character.inventory){
      if(i.name.toLowerCase() == (character.useRepair ? AppLocalizations.of(context)!.emergencyRepairPatches : AppLocalizations.of(context)!.stimpacks).toLowerCase()){
        healingItem = i;
        break;
      }
    }
    return Column(
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(AppLocalizations.of(context)!.soak),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: edit,
                initialText: character.soak.toString(),
                collapsed: true,
                fieldAlign: TextAlign.center,
                fieldInsets: const EdgeInsets.all(3),
                controller: (){
                  var controller = TextEditingController(text: character.soak.toString());
                  controller.addListener(() =>
                    character.soak = int.tryParse(controller.text) ?? 0
                  );
                  return controller;
                }(),
                textType: TextInputType.number,
                defaultSave: true,
              )
            )
          ],
        ),
        Container(height: 10), 
        Row(
          children: <Widget>[
            Expanded(
              child: SizedBox(
                height: 80,
                child: AnimatedSwitcher(
                  duration: const Duration(milliseconds: 300),
                  transitionBuilder: (wid, anim){
                    Tween<Offset> offset;
                    if(wid is Padding){
                      offset = Tween(begin: const Offset(0.0,1.0), end: Offset.zero);
                    }else{
                      offset = Tween(begin: const Offset(0.0,-1.0), end: Offset.zero);
                    }
                    return ClipRect(
                      child: SlideTransition(
                        position: offset.animate(anim),
                        child: Center(child: wid),
                      )
                    );
                  },
                  child: !edit ? Column(
                    children: [
                      Center(child: Text(AppLocalizations.of(context)!.wound)),
                      UpDownStat(
                        key: const ValueKey("UpDownWound"),
                        onUpPressed: () {
                          character.woundCur++;
                          character.save(context: context);
                        },
                        onDownPressed: (){
                          character.woundCur--;
                          character.save(context: context);
                        },
                        getValue: () => character.woundCur,
                        getMin: () => -1*character.woundThresh
                      ),
                      Center(child: Text(AppLocalizations.of(context)!.max(character.woundThresh)))
                    ]
                  ) : (){
                    var controll = TextEditingController(text: character.woundThresh.toString());
                    controll.addListener(() {
                      character.woundThresh = int.tryParse(controll.text) ?? 0;
                      character.save(context: context);
                    });
                    return Padding(
                      child: TextField(
                        controller: controll,
                        keyboardType: TextInputType.number,
                        inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                        decoration: InputDecoration(labelText: AppLocalizations.of(context)!.maxWound),
                        textAlign: TextAlign.center,
                      ),
                      padding: const EdgeInsets.only(right: 3.0, left: 3.0, top: 3.0)
                    ); 
                  }()
                )
              )
            ),
            Expanded(
              child: SizedBox(
                height: 80,
                child: AnimatedSwitcher(
                  duration: const Duration(milliseconds: 300),
                  transitionBuilder: (wid, anim){
                    Tween<Offset> offset;
                    if(wid is Padding){
                      offset = Tween(begin: const Offset(0.0,1.0), end: Offset.zero);
                    }else{
                      offset = Tween(begin: const Offset(0.0,-1.0), end: Offset.zero);
                    }
                    return ClipRect(
                      child: SlideTransition(
                        position: offset.animate(anim),
                        child: Center(child: wid)
                      )
                    );
                  },
                  child: !edit ? Column(
                    children: [
                      Center(child: Text(AppLocalizations.of(context)!.strain),),
                      UpDownStat(
                        key: const ValueKey("UpDownStrain"),
                        onUpPressed: (){
                          character.strainCur++;
                          character.save(context: context);
                        },
                        onDownPressed: (){
                          character.strainCur--;
                          character.save(context: context);
                        },
                        getValue: ()=>character.strainCur,
                      ),
                      Center(child: Text(AppLocalizations.of(context)!.max(character.strainThresh)))
                    ]
                  ) : (){
                    var controll = TextEditingController(text: character.strainThresh.toString());
                    controll.addListener(() {
                      character.strainThresh = int.tryParse(controll.text) ?? 0;
                      character.save(context: context);
                    });
                    return Padding(
                      child: TextField(
                        controller: controll,
                        keyboardType: TextInputType.number,
                        inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                        decoration: InputDecoration(labelText: AppLocalizations.of(context)!.maxStrain),
                        textAlign: TextAlign.center,
                      ),
                      padding: const EdgeInsets.only(right: 3.0, left: 3.0, top: 3.0)
                    ); 
                  }()
                )
              )
            )
          ],
        ),
        Container(height: 5),
        const Divider(),
        Container(height: 5),
        AnimatedSize(
          duration: const Duration(milliseconds: 300),
          child: AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            transitionBuilder: (wid, anim){
              Tween<Offset> slide;
              if(wid is Text){
                slide = Tween(begin: const Offset(0.0,-1.0),end: Offset.zero);
              }else{
                slide = Tween(begin: const Offset(0.0,1.0), end: Offset.zero);
              }
              return ClipRect(
                child: SlideTransition(
                  position: slide.animate(anim),
                  child: Center(child: wid)
                )
              );
            },
            layoutBuilder: (child, oldStack){
              List<Widget> newStack = [];
              for(var chil in oldStack){
                newStack.add(
                  SizedOverflowBox(
                    size: Size.zero,
                    child: chil
                  )
                );
              }
              return Stack(
                alignment: AlignmentDirectional.center,
                children:[
                  ...newStack,
                  child!,
                ]
              );
            },
            child: (edit) ?
              Row(
                children: [
                  Expanded(
                    child: Text(
                      AppLocalizations.of(context)!.stimpacks,
                      textAlign: TextAlign.center,
                    )
                  ),
                  Switch(
                    value: character.useRepair,
                    onChanged: (b) {
                      character.useRepair = b;
                      setState((){});
                    },
                    activeColor: Theme.of(context).switchTheme.trackColor?.resolve({MaterialState.disabled}),
                  ),
                  Expanded(
                    child: Text(
                      AppLocalizations.of(context)!.emergencyRepairPatches,
                      textAlign: TextAlign.center,
                    )
                  )
                ],
              ) :
              Text(
                (character.useRepair ? AppLocalizations.of(context)!.emergencyRepairPatches : AppLocalizations.of(context)!.stimpacks),
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.titleSmall,
              )
          )
        ),
        Container(height: 5),
        UpDownStat(
          onUpPressed: (){
            if(healingItem == null){
              for(var i in character.inventory){
                if(i.name == (character.useRepair ? AppLocalizations.of(context)!.emergencyRepairPatches : AppLocalizations.of(context)!.stimpacks)){
                  healingItem = i;
                  break;
                }
              }
              if(healingItem == null) {
                healingItem = Item(
                  name: (character.useRepair ? AppLocalizations.of(context)!.emergencyRepairPatches : AppLocalizations.of(context)!.stimpacks),
                  count: 1
                );
                character.inventory.add(healingItem!);
                character.invKey.currentState?.setState(() {});
              }
            }else{
              healingItem!.count++;
              character.invKey.currentState?.setState(() {});
            }
          },
          onDownPressed: (){
            if(healingItem != null){
              healingItem!.count--;
              character.invKey.currentState?.setState(() {});
            }
          },
          min: 0,
          getValue: (){
            if(healingItem == null) return 0;
            return healingItem!.count;
          },
        ),
        Container(height: 10),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(AppLocalizations.of(context)!.healsUsedToday),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: edit,
                initialText: character.healsToday.toString(),
                collapsed: true,
                fieldAlign: TextAlign.center,
                fieldInsets: const EdgeInsets.all(3),
                controller: (){
                  var controller = TextEditingController(text: character.healsToday.toString());
                  controller.addListener(() =>
                    character.healsToday = int.tryParse(controller.text) ?? 0
                  );
                  return controller;
                }(),
                textType: TextInputType.number,
                defaultSave: true,
              )
            )
          ],
        ),
        Container(height: 10),
        ButtonBar(
          alignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: (){
                if(healingItem != null && healingItem!.count > 0 && character.healsToday < 5 && character.woundCur < character.woundThresh){
                  setState(() {
                    if(character.useRepair){
                      character.woundCur += 3;
                    }else{
                      character.woundCur += 5-character.healsToday;
                    }
                    if(character.woundCur > character.woundThresh){
                      character.woundCur = character.woundThresh;
                    }
                    character.healsToday++;
                    healingItem!.count--;
                  });
                }
              },
              child: Text(AppLocalizations.of(context)!.heal)
            ),
            ElevatedButton(
              onPressed: () => setState(() => character.healsToday = 0),
              child: Text(AppLocalizations.of(context)!.reset)
            )
          ]
        )
      ],
    );
  }
}