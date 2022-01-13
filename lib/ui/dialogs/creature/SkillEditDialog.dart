import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';
import 'package:swassistant/ui/misc/UpdatingSwitchTile.dart';

class SkillEditDialog{
  final void Function(Skill) onClose;
  final Creature creature;
  final Skill skill;

  late Bottom bot;

  late TextEditingController valueController;

  SkillEditDialog({required this.onClose, Skill? sk, required this.creature}) :
      this.skill = sk == null ? Skill() : 
      creature is Character ? Skill.from(sk) : Skill.from(sk..value = 0){
    valueController = new TextEditingController(text: skill.value != -1 ? skill.value.toString() : "")
      ..addListener(() {
        skill.value = int.tryParse(valueController.text) ?? -1;
        bot.updateButtons();
      });
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
          onPressed: skill.name != "" && skill.name != null && skill.base != -1 && (creature is Character ? skill.value != -1 : true) ? (){
            onClose(skill);
            Navigator.of(context).pop();
          } : null,
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: (){
            Navigator.of(context).pop();
          },
        )],
      child: (context) => 
        DropdownButtonHideUnderline(
          child: Wrap(
            children: [
              Container(height: 15),
              _SkillSelector(skill, bot),
              if(creature is Character) Container(height: 10),
              if(creature is Character) TextField(
                keyboardType: TextInputType.number,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                controller: valueController,
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.value,
                ),
              ),
              UpdatingSwitchTile(
                contentPadding: EdgeInsets.zero,
                title: Text(AppLocalizations.of(context)!.career),
                value: skill.career,
                onChanged: (b) => skill.career = b,
              ),
            ],
          )
        )
    );    
  }

  void show(BuildContext context) => bot.show(context);
}

class _SkillSelector extends StatefulWidget{
  final Skill skill;
  final Bottom bot;

  _SkillSelector(this.skill, this.bot);

  @override
  State<StatefulWidget> createState() => _SkillSelectorState(skill, bot);
}

class _SkillSelectorState extends State{
  final Skill skill;
  final Bottom bot;
  bool manual = false;
  late Map<String, int> skillsList;

  late TextEditingController skillController;

  _SkillSelectorState(this.skill, this.bot){
    skillController = TextEditingController(text: skill.name)
      ..addListener(() {
        skill.name = skillController.text;
        bot.updateButtons();
      });
  }

  @override
  Widget build(BuildContext context) {
    skillsList = Skill.skillsList(context);
    if(skill.name != null && !skillsList.containsKey(skill.name)){
      manual = true;
    }
    return Column(
      children: [
        InputDecorator(
          decoration: InputDecoration(
            labelText: AppLocalizations.of(context)!.skill,
          ),
          child: DropdownButton<String>(
            isDense: true,
            isExpanded: true,
            onTap: () => FocusScope.of(context).unfocus(),
            onChanged: (value) {
              setState((){
                if(value != AppLocalizations.of(context)!.skills35){
                  manual = false;
                  skill.name = value!;
                  skill.base = skillsList[value]!;
                }else{
                  manual = true;
                  skill.name = "";
                }
              });
              bot.updateButtons();
            },
            value: skillsList.containsKey(skill.name) ? skill.name : skill.name == null ? null : skillsList.keys.last,
            items: List.generate(
              skillsList.length,
              (i) =>
                DropdownMenuItem<String>(
                  value: skillsList.keys.elementAt(i),
                  child: Text(skillsList.keys.elementAt(i))
                )
            ),
          )
        ),
        if(manual) Container(height: 10),
        AnimatedSwitcher(
          child: !manual ? Container() :
            TextField(
              textCapitalization: TextCapitalization.words,
              onChanged: (value) => skill.name = value,
              autofillHints: skillsList.keys,
              controller: skillController,
            ),
          duration: Duration(milliseconds: 150),
          transitionBuilder: (child, anim) =>
            SizeTransition(
              sizeFactor: anim,
              child: child,
            ) 
        ),
        Container(height: 10),
        InputDecorator(
          decoration: InputDecoration(
            labelText: AppLocalizations.of(context)!.characteristic,
          ),
          child: DropdownButton<int>(
            isDense: true,
            isExpanded: true,
            onTap: () => FocusScope.of(context).unfocus(),
            items: List.generate(
              6,
              (i) => DropdownMenuItem(
                child: Text(Creature.characteristics(context)[i]),
                value: i
              )
            ),
            value: skill.base == -1 ? null : skill.base,
            onChanged: (value) {
              setState(() => skill.base = value ?? -1);
              bot.updateButtons();
            }
          )
        ),
      ]
    );
  }
}