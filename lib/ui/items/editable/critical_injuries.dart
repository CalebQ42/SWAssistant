import 'package:flutter/material.dart';
import 'package:swassistant/items/critical_injury.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/dialogs/editable/crit_inj_edit.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/bottom.dart';

class CriticalInjuries extends StatefulWidget{

  const CriticalInjuries({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => CritState();
}

class CritState extends State<CriticalInjuries> with StatefulCard{

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Editable.of(context).criticalInjuries.isEmpty;

  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    var criticalinjuriesList = List.generate(editable.criticalInjuries.length, (i) =>
      Row(
        children: [
          Expanded(
            child: Padding(
              padding: const EdgeInsets.symmetric(vertical: 20),
              child: Text(editable.criticalInjuries[i].name)
            )
          ),
          ButtonBar(
            buttonPadding: EdgeInsets.zero,
            children: [
              IconButton(
                constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                icon: const Icon(Icons.info_outline),
                splashRadius: 20,
                onPressed: (){
                  String severity;
                  switch (editable.criticalInjuries[i].severity) {
                    case 0:
                      severity = AppLocalizations.of(context)!.severityLevel1;
                      break;
                    case 1:
                      severity = AppLocalizations.of(context)!.severityLevel2;
                      break;
                    case 2:
                      severity = AppLocalizations.of(context)!.severityLevel3;
                      break;
                    default:
                      severity = AppLocalizations.of(context)!.severityLevel4;
                      break;
                  }
                  Bottom(
                    child: (context) =>
                      Wrap(
                        alignment: WrapAlignment.center,
                        children: [
                          Container(height: 15),
                          Center(
                            child: Text(
                              editable.criticalInjuries[i].name,
                              style: Theme.of(context).textTheme.headline5,
                              textAlign: TextAlign.justify,
                            )
                          ),
                          Container(height: 5,),
                          Center(
                            child: Text(
                              AppLocalizations.of(context)!.severity + ": " + severity,
                              style: Theme.of(context).textTheme.bodyText1,
                            ),
                          ),
                          Container(height: 10),
                          Text(editable.criticalInjuries[i].desc, textAlign: TextAlign.justify)
                        ],
                      )
                  ).show(context);
                }
              ),
            ],
          ),
          AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            child: !edit ? Container() : ButtonBar(
              buttonPadding: EdgeInsets.zero,
              children: [
                IconButton(
                  splashRadius: 20,
                  constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                  icon: const Icon(Icons.delete_forever),
                  onPressed: (){
                    var temp = CriticalInjury.from(editable.criticalInjuries[i]);
                    setState(() => editable.criticalInjuries.removeAt(i));
                    editable.save(context: context);
                    ScaffoldMessenger.of(context).clearSnackBars();
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        content: Text(AppLocalizations.of(context)!.deletedInjury),
                        action: SnackBarAction(
                          label: AppLocalizations.of(context)!.undo,
                          onPressed: (){
                            setState(() => editable.criticalInjuries.insert(i, temp));
                            editable.save(context: context);
                          }
                        ),
                      )
                    );
                  }
                ),
                IconButton(
                  splashRadius: 20,
                  constraints: const BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                  icon: const Icon(Icons.edit),
                  onPressed: () =>
                    CriticalInjuryEditDialog(
                      onClose: (criticalinjury){
                        setState(() => editable.criticalInjuries[i] = criticalinjury);
                        editable.save(context: context);
                      },
                      inj: editable.criticalInjuries[i]
                    ).show(context)
                )
              ]
            ),
            transitionBuilder: (child, anim){
              var offset = const Offset(1,0);
              if(child is Container){
                offset = const Offset(-1,0);
              }
              return ClipRect(
                child: SizeTransition(
                  sizeFactor: anim,
                  axis: Axis.horizontal,
                  child: SlideTransition(
                    position: Tween<Offset>(
                      begin: offset,
                      end: Offset.zero
                    ).animate(anim),
                    child: child,
                  )
                )
              );
            },
          )
        ],
      )
    );
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          Column(children: criticalinjuriesList),
          AnimatedSwitcher(
            duration: const Duration(milliseconds: 300),
            transitionBuilder: (wid,anim){
              return SizeTransition(
                sizeFactor: anim,
                child: wid,
                axisAlignment: -1.0,
              );
            },
            child: edit ? Center(
              child: IconButton(
                icon: const Icon(Icons.add),
                onPressed: () =>
                  CriticalInjuryEditDialog(
                    onClose: (criticalinjury){
                      setState(() => editable.criticalInjuries.add(criticalinjury));
                      editable.save(context: context);
                    }
                  ).show(context)
              )
            ) : Container(),
          )
        ],
      ),
    );
  }
}