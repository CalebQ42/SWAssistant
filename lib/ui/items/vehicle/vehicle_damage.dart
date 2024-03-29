import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';
import 'package:swassistant/ui/misc/up_down.dart';

class VehicleDamage extends StatefulWidget{

  const VehicleDamage({Key? key}) : super(key: key);

  @override
  State<VehicleDamage> createState() => VehicleDamageState();
}

class VehicleDamageState extends State<VehicleDamage> with StatefulCard{

  var edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => false;

  TextEditingController? armorController;
  TextEditingController? hullTraumaThreshController;
  TextEditingController? sysStressThreshController;

  @override
  Widget build(BuildContext context) {
    var vehicle = Vehicle.of(context);
    if (vehicle == null) throw "VehicleDamage card called on non Vehicle";
    armorController ??= TextEditingController(text: vehicle.armor.toString())
      ..addListener(() => vehicle.armor = int.tryParse(armorController!.text) ?? 0);
    hullTraumaThreshController ??= TextEditingController(text: vehicle.hullTraumaThresh.toString())
      ..addListener(() => vehicle.hullTraumaThresh = int.tryParse(hullTraumaThreshController!.text) ?? 0);
    sysStressThreshController ??= TextEditingController(text: vehicle.sysStressThresh.toString())
      ..addListener(() => vehicle.sysStressThresh = int.tryParse(sysStressThreshController!.text) ?? 0);
    var app = SW.of(context);
    var subtractMode = app.prefs.subtractMode;
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text("${app.locale.armor}:"),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: edit,
                initialText: vehicle.armor.toString(),
                controller: armorController,
                textType: TextInputType.number,
                defaultSave: true,
                collapsed: true,
                fieldAlign: TextAlign.center,
                textAlign: TextAlign.center,
              ),
            )
          ],
        ),
        Container(height: 10),
        Row(
          children: [
            Expanded(
              child: SizedBox(
                height: 80,
                child: AnimatedSwitcher(
                  duration: const Duration(milliseconds: 300),
                  child: !edit ? Column(
                    children: [
                      Text(app.locale.hullTrauma, textAlign: TextAlign.center),
                      UpDownStat(
                        onUpPressed: (){
                          if(subtractMode){
                            vehicle.hullTraumaDmg--;
                          }else{
                            vehicle.hullTraumaDmg++;
                          }
                          vehicle.save(context: context);
                        },
                        onDownPressed: (){
                          if(subtractMode){
                            vehicle.hullTraumaDmg++;
                          }else{
                            vehicle.hullTraumaDmg--;
                          }
                          vehicle.save(context: context);
                        },
                        getValue: () => subtractMode ? vehicle.hullTraumaThresh - vehicle.hullTraumaDmg : vehicle.hullTraumaDmg,
                        getMax: () => vehicle.hullTraumaThresh,
                        min: 0
                      ),
                      Center(child: Text(app.locale.max(vehicle.hullTraumaThresh)))
                    ],
                  ) : TextField(
                    controller: hullTraumaThreshController,
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    keyboardType: TextInputType.number,
                    decoration: InputDecoration(labelText: app.locale.maxTrauma),
                    textAlign: TextAlign.center,
                  ),
                  transitionBuilder: (child, anim){
                    Tween<Offset> offset;
                    if(child is TextField){
                      offset = Tween(begin: const Offset(0.0,-1.0), end: Offset.zero);
                    }else{
                      offset = Tween(begin: const Offset(0.0,1.0), end: Offset.zero);
                    }
                    return ClipRect(
                      child: SlideTransition(
                        position: offset.animate(anim),
                        child: Center(child: child)
                      )
                    );
                  },
                )
              )
            ),
            Container(width: 10,),
            Expanded(
              child: SizedBox(
                height: 80,
                child: AnimatedSwitcher(
                  duration: const Duration(milliseconds: 300),
                  child: !edit ? Column(
                    children: [
                      Text(app.locale.sysStress, textAlign: TextAlign.center),
                      UpDownStat(
                        onUpPressed: (){
                          if(subtractMode){
                            vehicle.sysStressDmg--;
                          }else{
                            vehicle.sysStressDmg++;
                          }
                          vehicle.save(context: context);
                        },
                        onDownPressed: (){
                          if(subtractMode){
                            vehicle.sysStressDmg++;
                          }else{
                            vehicle.sysStressDmg--;
                          }
                          vehicle.save(context: context);
                        },
                        getValue: () => subtractMode ? vehicle.sysStressThresh - vehicle.sysStressDmg : vehicle.sysStressDmg,
                        getMax: () => vehicle.sysStressThresh,
                        getMin: () => 0
                      ),
                      Center(child: Text(app.locale.max(vehicle.sysStressThresh)))
                    ],
                  ) : TextField(
                    controller: sysStressThreshController,
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    keyboardType: TextInputType.number,
                    decoration: InputDecoration(labelText: app.locale.maxStress),
                    textAlign: TextAlign.center,
                  ),
                  transitionBuilder: (child, anim){
                    Tween<Offset> offset;
                    if(child is TextField){
                      offset = Tween(begin: const Offset(0.0,-1.0), end: Offset.zero);
                    }else{
                      offset = Tween(begin: const Offset(0.0,1.0), end: Offset.zero);
                    }
                    return ClipRect(
                      child: SlideTransition(
                        position: offset.animate(anim),
                        child: Center(child: child)
                      )
                    );
                  },
                )
              )
            ),
          ],
        )
      ],
    );
  }
}