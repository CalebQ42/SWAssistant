
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/item.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';
import 'package:swassistant/ui/misc/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import 'package:swassistant/preferences.dart' as preferences;

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
  bool get defaultEdit => Character.of(context)!.soak == 0 && Character.of(context)!.woundThresh == 0 && Character.of(context)!.strainThresh == 0;
  Item? healingItem;

  TextEditingController? soakController;
  TextEditingController? woundThreshController;
  TextEditingController? strainThreshController;
  bool healsEditDetector = false;
  TextEditingController? healsTodayController;

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
    var subtractMode = SW.of(context).getPreference(preferences.subtractMode, true);
    if(soakController == null){
      soakController = TextEditingController(text: character.soak.toString());
      soakController!.addListener(() => character.soak = int.tryParse(soakController!.text) ?? 0);
    }
    if(woundThreshController == null){
      woundThreshController = TextEditingController(text: character.woundThresh.toString());
      woundThreshController!.addListener(() => character.woundThresh = int.tryParse(woundThreshController!.text) ?? 0);
    }
    if(strainThreshController == null){
      strainThreshController = TextEditingController(text: character.strainThresh.toString());
      strainThreshController!.addListener(() => character.strainThresh = int.tryParse(strainThreshController!.text) ?? 0);
    }
    if(healsTodayController == null || healsEditDetector != edit){
      healsTodayController = TextEditingController(text: character.healsToday.toString());
      healsTodayController!.addListener(() => character.healsToday = int.tryParse(healsTodayController!.text) ?? 0);
      healsEditDetector = edit;
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
                controller: soakController,
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
                          if(subtractMode){
                            character.woundDmg--;
                          }else{
                            character.woundDmg++;
                          }
                          character.save(context: context);
                        },
                        onDownPressed: (){
                          if(subtractMode){
                            character.woundDmg++;
                          }else{
                            character.woundDmg--;
                          }
                          character.save(context: context);
                        },
                        getValue: () => subtractMode ? character.woundThresh - character.woundDmg : character.woundDmg,
                        getMin: () => subtractMode ? -1*character.woundThresh : 0,
                        getMax: () => subtractMode ? character.woundThresh : 2*character.woundThresh,
                      ),
                      Center(child: Text(AppLocalizations.of(context)!.max(character.woundThresh)))
                    ]
                  ) : Padding(
                    padding: const EdgeInsets.only(right: 3.0, left: 3.0, top: 3.0),
                    child: TextField(
                      controller: woundThreshController,
                      keyboardType: TextInputType.number,
                      inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                      decoration: InputDecoration(labelText: AppLocalizations.of(context)!.maxWound),
                      textAlign: TextAlign.center,
                    )
                  )
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
                          if(subtractMode){
                            character.strainDmg--;
                          }else{
                            character.strainDmg++;
                          }
                          character.save(context: context);
                        },
                        onDownPressed: (){
                          if(subtractMode){
                            character.strainDmg++;
                          }else{
                            character.strainDmg--;
                          }
                          character.save(context: context);
                        },
                        getValue: () => subtractMode ? character.strainThresh - character.strainDmg : character.strainDmg,
                        getMin: () => 0,
                        getMax: () => character.strainThresh,
                      ),
                      Center(child: Text(AppLocalizations.of(context)!.max(character.strainThresh)))
                    ]
                  ) : Padding(
                    padding: const EdgeInsets.only(right: 3.0, left: 3.0, top: 3.0),
                    child: TextField(
                      controller: strainThreshController,
                      keyboardType: TextInputType.number,
                      inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                      decoration: InputDecoration(labelText: AppLocalizations.of(context)!.maxStrain),
                      textAlign: TextAlign.center,
                    )
                  )
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
                controller: healsTodayController,
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
                if(healingItem != null && healingItem!.count > 0 && character.healsToday < 5){
                  if(character.woundDmg > 0){
                    setState(() {
                      if(character.useRepair){
                        character.woundDmg -= 3;
                      }else{
                        character.woundDmg -= 5-character.healsToday;
                      }
                      if(character.woundDmg < 0){
                        character.woundDmg = 0;
                      }
                      character.healsToday++;
                      healingItem!.count--;
                      character.invKey.currentState?.setState(() {});
                    });
                  }
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