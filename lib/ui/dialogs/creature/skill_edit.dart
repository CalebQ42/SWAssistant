import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';
import 'package:swassistant/ui/misc/updating_switch_tile.dart';

class SkillEditDialog{
  final void Function(Skill) onClose;
  final Creature creature;
  final Skill skill;

  late Bottom bot;

  late TextEditingController valueController;

  SkillEditDialog({required this.onClose, Skill? sk, required this.creature}) :
      skill = sk == null ? Skill() : 
      creature is Character ? Skill.from(sk) : Skill.from(sk..value = 0){
    valueController = TextEditingController(text: skill.value != -1 ? skill.value.toString() : "")
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

  const _SkillSelector(this.skill, this.bot, {Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _SkillSelectorState();
}

class _SkillSelectorState extends State<_SkillSelector>{

  bool manual = false;
  late Map<String, int> skillsList;

  late TextEditingController skillController;

  _SkillSelectorState(){
    skillController = TextEditingController(text: widget.skill.name)
      ..addListener(() {
        widget.skill.name = skillController.text;
        widget.bot.updateButtons();
      });
  }

  @override
  Widget build(BuildContext context) {
    skillsList = Skill.skillsList(context);
    if(widget.skill.name != null && !skillsList.containsKey(widget.skill.name)){
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
                  widget.skill.name = value!;
                  widget.skill.base = skillsList[value]!;
                }else{
                  manual = true;
                  widget.skill.name = "";
                }
              });
              widget.bot.updateButtons();
            },
            value: skillsList.containsKey(widget.skill.name) ? widget.skill.name : widget.skill.name == null ? null : skillsList.keys.last,
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
              onChanged: (value) => widget.skill.name = value,
              autofillHints: skillsList.keys,
              controller: skillController,
            ),
          duration: const Duration(milliseconds: 150),
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
            value: widget.skill.base == -1 ? null : widget.skill.base,
            onChanged: (value) {
              setState(() => widget.skill.base = value ?? -1);
              widget.bot.updateButtons();
            }
          )
        ),
      ]
    );
  }
}