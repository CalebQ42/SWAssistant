import 'package:flutter/material.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';
import 'package:swassistant/ui/misc/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/preferences.dart' as preferences;

class MinionWound extends StatefulWidget{

  const MinionWound({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => MinionWoundState();
}

class MinionWoundState extends State<MinionWound> with StatefulCard{

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => false;

  TextEditingController? indWoundController;
  TextEditingController? soakController;

  @override
  Widget build(BuildContext context) {
    var minion = Minion.of(context);
    if (minion == null) throw "MinionWound card used on non Minion";
    indWoundController ??= TextEditingController(text: minion.woundThreshInd.toString())..addListener(() {
      var tmp = int.tryParse(indWoundController!.text);
      if(tmp == null){
        minion.woundThreshInd = 0;
      }else{
        minion.woundThreshInd = tmp;
      }
      setState((){});
    });
    soakController ??= TextEditingController(text: minion.soak.toString())
        ..addListener(() => minion.soak = int.tryParse(soakController!.text) ?? 0);
    var subtractMode = SW.of(context).getPreference(preferences.subtractMode, true);
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(AppLocalizations.of(context)!.soak),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: edit,
                initialText: minion.soak.toString(),
                controller: soakController,
                textAlign: TextAlign.center,
                fieldAlign: TextAlign.center,
                textType: TextInputType.number,
                collapsed: true,
                defaultSave: true,
              ),
            )
          ]
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(AppLocalizations.of(context)!.minWound),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: edit,
                initialText: minion.woundThreshInd.toString(),
                controller: indWoundController,
                textType: TextInputType.number,
                collapsed: true,
                defaultSave: true,
                textAlign: TextAlign.center,
                fieldAlign: TextAlign.center,
              ),
            )
          ],
        ),
        UpDownStat(
          onUpPressed: (){
            if(subtractMode){
              minion.woundDmg--;
            }else{
              minion.woundDmg++;
            }
            minion.save(context: context);
          },
          onDownPressed: (){
            if(subtractMode){
              minion.woundDmg++;
            }else{
              minion.woundDmg--;
            }
            minion.save(context: context);
          },
          getValue: () => subtractMode ? (minion.woundThreshInd * minion.minionNum) - minion.woundDmg : minion.woundDmg,
          getMax: () => minion.woundThreshInd * minion.minionNum,
          min: 0
        ),
        Container(height:5),
        ElevatedButton(
          onPressed: (){
            setState(() {
              minion.woundDmg = 0;
              minion.save(context: context);
            });
          },
          child: Text(AppLocalizations.of(context)!.reset),
        )
      ],
    );
  }
}