import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Creature.dart';

class SkillEditDialog extends StatefulWidget{
  final void Function(Skill) onClose;
  final Creature creature;
  final Skill skill;

  SkillEditDialog({required this.onClose, Skill? skill, required this.creature}) :
      this.skill = skill == null ? Skill() : 
      creature is Character ? Skill.from(skill) : Skill.from(skill..value = 0);

  @override
  State<StatefulWidget> createState() => _SkillEditDialogState(onClose: onClose, skill: skill, creature: creature);
  
  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) =>
        this
    );
}

class _SkillEditDialogState extends State{
  final Function(Skill) onClose;
  final Skill skill;
  final Creature creature;
  bool manual = false;

  late TextEditingController valueController;

  _SkillEditDialogState({required this.onClose, required this.skill, required this.creature}){
    if(skill.name != "" && !Skill.skillsList.containsKey(skill.name))
      manual = true;
    valueController = new TextEditingController(text: skill.value != -1 ? skill.value.toString() : "")
        ..addListener(() =>
          setState(() => skill.value = int.tryParse(valueController.text) ?? -1)
        );
  }
  
  Widget build(BuildContext context) {
    return DropdownButtonHideUnderline(
      child: Padding(
        padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15)),
        child: Wrap(
          children: [
            Container(height: 15),
            InputDecorator(
              decoration: InputDecoration(
                labelText: "Skill",
              ),
              child: DropdownButton<String>(
                isDense: true,
                isExpanded: true,
                onTap: () => FocusScope.of(context).unfocus(),
                onChanged: (value) {
                  setState((){
                    if(value != "Other..."){
                      manual = false;
                      skill.name = value!;
                      skill.base = Skill.skillsList[value]!;
                    }else{
                      manual = true;
                      skill.name = "";
                    }
                  });
                },
                value: Skill.skillsList.containsKey(skill.name) ? skill.name : skill.name == "" ? null : Skill.skillsList.keys.last,
                items: List.generate(
                  Skill.skillsList.length,
                  (i) =>
                    DropdownMenuItem<String>(
                      value: Skill.skillsList.keys.elementAt(i),
                      child: Text(Skill.skillsList.keys.elementAt(i))
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
                  autofillHints: Skill.skillsList.keys,
                  controller: TextEditingController(text: skill.name),
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
                labelText: "Characteristic",
              ),
              child: DropdownButton<int>(
                isDense: true,
                isExpanded: true,
                onTap: () => FocusScope.of(context).unfocus(),
                items: List.generate(
                  Creature.characteristics.length,
                  (i) => DropdownMenuItem(
                    child: Text(Creature.characteristics[i]),
                    value: i
                  )
                ),
                value: skill.base == -1 ? null : skill.base,
                onChanged: (value) => setState(() => skill.base = value ?? -1),
              )
            ),
            if(creature is Character) Container(height: 10),
            if(creature is Character) TextField(
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              controller: valueController,
              decoration: InputDecoration(
                labelText: "Value",
              ),
            ),
            SwitchListTile(
              contentPadding: EdgeInsets.zero,
              title: Text("Career"),
              value: skill.career,
              onChanged: (b) => setState(() => skill.career = b),
            ),
            ButtonBar(
              children: [
                TextButton(
                  child: Text("Save"),
                  onPressed: skill.name != "" && skill.base != -1 && (creature is Character ? skill.value != -1 : true) ? (){
                    onClose(skill);
                    Navigator.of(context).pop();
                  } : null,
                ),
                TextButton(
                  child: Text("Cancel"),
                  onPressed: (){
                    Navigator.of(context).pop();
                  },
                )
              ]
            )
          ],
        )
      )
    );
  }
}