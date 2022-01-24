import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/ui/editable_common.dart';
import 'package:swassistant/ui/up_down.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class VehicleDamage extends StatelessWidget{

  final bool editing;

  const VehicleDamage({required this.editing, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var vehicle = Vehicle.of(context);
    if (vehicle == null) throw "VehicleDamage card called on non Vehicle";
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
                editing: editing,
                initialText: vehicle.armor.toString(),
                controller: (){
                  var controller = TextEditingController(text: vehicle.armor.toString());
                  controller.addListener(() =>
                    vehicle.armor = int.tryParse(controller.text) ?? 0
                  );
                  return controller;
                }(),
                textType: TextInputType.number,
                defaultSave: true,
                collapsed: true,
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
                height: 80,
                child: AnimatedSwitcher(
                  duration: const Duration(milliseconds: 300),
                  child: !editing ? Column(
                    children: [
                      Text(AppLocalizations.of(context)!.hullTrauma, textAlign: TextAlign.center),
                      UpDownStat(
                        onUpPressed: (){
                          vehicle.hullTraumaCur++;
                          vehicle.save(context: context);
                        },
                        onDownPressed: (){
                          vehicle.hullTraumaCur--;
                          vehicle.save(context: context);
                        },
                        getValue: () => vehicle.hullTraumaCur,
                      ),
                      Center(child: Text(AppLocalizations.of(context)!.max(vehicle.hullTraumaThresh)))
                    ],
                  ) : TextField(
                    controller: (){
                      var controller = TextEditingController(text: vehicle.hullTraumaThresh.toString());
                      controller.addListener(() =>
                        vehicle.hullTraumaThresh = int.tryParse(controller.text) ?? 0
                      );
                      return controller;
                    }(),
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    keyboardType: TextInputType.number,
                    decoration: InputDecoration(labelText: AppLocalizations.of(context)!.maxTrauma),
                    textAlign: TextAlign.center,
                  ),
                  transitionBuilder: (child, anim){
                    Tween<Offset> offset;
                    if((!editing && child is TextField) || (editing && child is Column)){
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
                  child: !editing ? Column(
                    children: [
                      Text(AppLocalizations.of(context)!.sysStress, textAlign: TextAlign.center),
                      UpDownStat(
                        onUpPressed: (){
                          vehicle.sysStressCur++;
                          vehicle.save(context: context);
                        },
                        onDownPressed: (){
                          vehicle.sysStressCur--;
                          vehicle.save(context: context);
                        },
                        getValue: () => vehicle.sysStressCur,
                      ),
                      Center(child: Text(AppLocalizations.of(context)!.max(vehicle.sysStressThresh)))
                    ],
                  ) : TextField(
                    controller: (){
                      var controller = TextEditingController(text: vehicle.sysStressThresh.toString());
                      controller.addListener(() =>
                        vehicle.sysStressThresh = int.tryParse(controller.text) ?? 0
                      );
                      return controller;
                    }(),
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    keyboardType: TextInputType.number,
                    decoration: InputDecoration(labelText: AppLocalizations.of(context)!.maxStress),
                    textAlign: TextAlign.center,
                  ),
                  transitionBuilder: (child, anim){
                    Tween<Offset> offset;
                    if((!editing && child is TextField) || (editing && child is Column)){
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