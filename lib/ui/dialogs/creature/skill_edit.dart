import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/skill.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/utils/creature.dart';
import 'package:darkstorm_common/bottom.dart';
import 'package:darkstorm_common/updating_switch_tile.dart';
import 'package:swassistant/sw.dart';

class SkillEditDialog{
  final void Function(Skill) onClose;
  final Creature creature;
  final Skill skill;

  late Bottom bot;

  SkillEditDialog({required this.onClose, Skill? sk, required this.creature}) :
      skill = (sk == null) ? Skill() : 
      creature is Character ? Skill.from(sk) : Skill.from(sk..value = 0){
    var valueController = TextEditingController(text: skill.value?.toString());
    valueController.addListener(() {
      skill.value = int.tryParse(valueController.text);
      bot.updateButtons();
    });
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          onPressed: skill.name != "" && skill.name != null && skill.base != null && (creature is Character ? skill.value != null : true) ? (){
            onClose(skill);
            Navigator.of(context).pop();
          } : null,
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
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
                  labelText: SW.of(context).locale.value,
                ),
              ),
              UpdatingSwitchTile(
                contentPadding: EdgeInsets.zero,
                title: Text(SW.of(context).locale.career),
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

  @override
  void initState() {
    super.initState();
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
            labelText: SW.of(context).locale.skill,
          ),
          child: DropdownButton<String>(
            isDense: true,
            isExpanded: true,
            onTap: () => FocusScope.of(context).unfocus(),
            onChanged: (value) {
              if(value != SW.of(context).locale.skills35){
                manual = false;
                widget.skill.name = value!;
                widget.skill.base = skillsList[value]!;
              }else{
                manual = true;
                widget.skill.name = "";
              }
              widget.bot.updateButtons();
              setState((){});
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
          duration: const Duration(milliseconds: 150),
          transitionBuilder: (child, anim) =>
            SizeTransition(
              sizeFactor: anim,
              child: child,
            ),
          child: !manual ? Container() :
            TextField(
              textCapitalization: TextCapitalization.words,
              onChanged: (value) => widget.skill.name = value,
              autofillHints: skillsList.keys,
              controller: skillController,
            ) 
        ),
        Container(height: 10),
        InputDecorator(
          decoration: InputDecoration(
            labelText: SW.of(context).locale.characteristic,
          ),
          child: DropdownButton<int>(
            isDense: true,
            isExpanded: true,
            onTap: () => FocusScope.of(context).unfocus(),
            items: List.generate(
              6,
              (i) => DropdownMenuItem(
                value: i,
                child: Text(Creature.characteristics(context)[i])
              )
            ),
            value: widget.skill.base,
            onChanged: (value) {
              setState(() => widget.skill.base = value);
              widget.bot.updateButtons();
            }
          )
        ),
      ]
    );
  }
}