import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/UpDownStat.dart';

class VehicleDamage extends StatelessWidget{

  final bool editing;
  final EditableContentState state;

  VehicleDamage({required this.editing, required this.state});

  @override
  Widget build(BuildContext context) {
    var vehicle = Vehicle.of(context);
    if (vehicle == null)
      throw "VehicleDamage card called on non Vehicle";
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text("Soak:"),
            SizedBox(
              width: 50,
              height: 25,
              child: EditingText(
                editing: editing,
                initialText: vehicle.armor.toString(),
                controller: (){
                  var controller = TextEditingController(text: vehicle.armor.toString());
                  controller.addListener(() {
                    var temp = int.tryParse(controller.text);
                    if(temp == null)
                      vehicle.armor = 0;
                    else
                      vehicle.armor = temp;
                  });
                  return controller;
                }(),
                textType: TextInputType.number,
                defaultSave: true,
                collapsed: true,
                state: state,
                fieldAlign: TextAlign.center,
                textAlign: TextAlign.center,
              ),
            )
          ],
        ),
        Row(
          children: [
            Expanded(
              child: SizedBox(
                height: 65,
                child: AnimatedSwitcher(
                  duration: Duration(milliseconds: 300),
                  child: !editing ? Column(
                    children: [
                      Text("Hull Trauma", textAlign: TextAlign.center),
                      UpDownStat(
                        onUpPressed: (){
                          vehicle.hullTraumaCur++;
                          vehicle.save(context: context);
                        },
                        onDownPressed: (){
                          vehicle.hullTraumaCur--;
                          vehicle.save(context: context);
                        },
                        getMax: () => vehicle.hullTraumaThresh,
                        getValue: () => vehicle.hullTraumaCur,
                      )
                    ],
                  ) : TextField(
                    controller: (){
                      var controller = TextEditingController(text: vehicle.hullTraumaThresh.toString());
                      controller.addListener(() {
                        var temp = int.tryParse(controller.text);
                        if(temp == null)
                          vehicle.hullTraumaThresh = 0;
                        else
                          vehicle.hullTraumaThresh = temp;
                        vehicle.save(context: context);
                      });
                      return controller;
                    }(),
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    keyboardType: TextInputType.number,
                    decoration: InputDecoration(labelText: "Max Hull Trauma"),
                    textAlign: TextAlign.center,
                  ),
                  transitionBuilder: (child, anim){
                    Tween<Offset> offset;
                    if((!editing && child is TextField) || (editing && child is Column))
                      offset = Tween(begin: Offset(0.0,-1.0), end: Offset.zero);
                    else
                      offset = Tween(begin: Offset(0.0,1.0), end: Offset.zero);
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
                height: 65,
                child: AnimatedSwitcher(
                  duration: Duration(milliseconds: 300),
                  child: !editing ? Column(
                    children: [
                      Text("System Stress", textAlign: TextAlign.center),
                      UpDownStat(
                        onUpPressed: (){
                          vehicle.sysStressCur++;
                          vehicle.save(context: context);
                        },
                        onDownPressed: (){
                          vehicle.sysStressCur--;
                          vehicle.save(context: context);
                        },
                        getMax: () => vehicle.sysStressThresh,
                        getValue: () => vehicle.sysStressCur,
                      )
                    ],
                  ) : TextField(
                    controller: (){
                      var controller = TextEditingController(text: vehicle.sysStressThresh.toString());
                      controller.addListener(() {
                        var temp = int.tryParse(controller.text);
                        if(temp == null)
                          vehicle.sysStressThresh = 0;
                        else
                          vehicle.sysStressThresh = temp;
                        vehicle.save(context: context);
                      });
                      return controller;
                    }(),
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    keyboardType: TextInputType.number,
                    decoration: InputDecoration(labelText: "Max System Stress"),
                    textAlign: TextAlign.center,
                  ),
                  transitionBuilder: (child, anim){
                    Tween<Offset> offset;
                    if((!editing && child is TextField) || (editing && child is Column))
                      offset = Tween(begin: Offset(0.0,-1.0), end: Offset.zero);
                    else
                      offset = Tween(begin: Offset(0.0,1.0), end: Offset.zero);
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