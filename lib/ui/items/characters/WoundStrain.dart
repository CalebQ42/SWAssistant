
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:swassistant/ui/UpDownStat.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class WoundStrain extends StatelessWidget{

  final bool editing;
  final EditableContentState state;

  WoundStrain({required this.editing, required this.state});

  @override
  Widget build(BuildContext context) {
    var character = Character.of(context);
    if (character == null)
      throw "Wound Strain card used on non Character";
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
                editing: editing,
                initialText: character.soak.toString(),
                collapsed: true,
                fieldAlign: TextAlign.center,
                fieldInsets: EdgeInsets.all(3),
                controller: (){
                  var controller = new TextEditingController(text: character.soak.toString());
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
                  duration: Duration(milliseconds: 300),
                  transitionBuilder: (wid, anim){
                    Tween<Offset> offset;
                    if((!editing && wid is Padding) || (editing && wid is Column))
                      offset = Tween(begin: Offset(0.0,-1.0), end: Offset.zero);
                    else
                      offset = Tween(begin: Offset(0.0,1.0), end: Offset.zero);
                    return ClipRect(
                      child: SlideTransition(
                        position: offset.animate(anim),
                        child: Center(child: wid),
                      )
                    );
                  },
                  child: !editing ? Column(
                    children: [
                      Center(child: Text(AppLocalizations.of(context)!.wound)),
                      UpDownStat(
                        key: ValueKey("UpDownWound"),
                        onUpPressed: () {
                          character.woundCur++;
                          character.save(context: context);
                        },
                        onDownPressed: (){
                          character.woundCur--;
                          character.save(context: context);
                        },
                        getValue: ()=>character.woundCur,
                      ),
                      Center(child: Text(AppLocalizations.of(context)!.max(character.woundThresh)))
                    ]
                  ) : (){
                    var controll = TextEditingController(text: character.woundThresh.toString());
                    controll.addListener(() {
                      character.woundThresh = int.parse(controll.text);
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
                      padding: EdgeInsets.only(right: 3.0, left: 3.0, top: 3.0)
                    ); 
                  }()
                )
              )
            ),
            Expanded(
              child: SizedBox(
                height: 80,
                child: AnimatedSwitcher(
                  duration: Duration(milliseconds: 300),
                  transitionBuilder: (wid, anim){
                    Tween<Offset> offset;
                    if((!editing && wid is Padding) || (editing && wid is Column))
                      offset = Tween(begin: Offset(0.0,-1.0), end: Offset.zero);
                    else
                      offset = Tween(begin: Offset(0.0,1.0), end: Offset.zero);
                    return ClipRect(
                      child: SlideTransition(
                        position: offset.animate(anim),
                        child: Center(child: wid)
                      )
                    );
                  },
                  child: !editing ? Column(
                    children: [
                      Center(child: Text(AppLocalizations.of(context)!.strain),),
                      UpDownStat(
                        key: ValueKey("UpDownStrain"),
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
                      character.strainThresh = int.parse(controll.text);
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
                      padding: EdgeInsets.only(right: 3.0, left: 3.0, top: 3.0)
                    ); 
                  }()
                )
              )
            )
          ],
        )
      ],
    );
  }
}