import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/Skill.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Creature.dart';

class SkillEditDialog extends StatefulWidget{
  //onClose Skill argument is the edited Skill (or new Skill). Skill argument is null if dialog is cancelled.
  //Doesn't get called when dialog is cancelled.
  final void Function(Skill) onClose;
  final Creature creature;
  final Skill skill;

  SkillEditDialog({this.onClose, Skill skill, this.creature}) :
      this.skill = skill == null ? Skill.nulled() : 
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

  TextEditingController valueController;

  _SkillEditDialogState({this.onClose, this.skill, this.creature}){
    if(skill.name != null && !Skill.skillsList.containsKey(skill.name))
      manual = true;
    valueController = new TextEditingController(text: skill.value != null ? skill.value.toString() : "")
        ..addListener(() {
          var val = int.tryParse(valueController.text);
          if((skill.value == null && val != null) || (skill.value != null && val == null))
            setState(() => skill.value = val);
          else
            skill.value = val;
        });
  }
  
  Widget build(BuildContext context) {
    return DropdownButtonHideUnderline(
      child: Padding(
        padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 15)),
        child: Wrap(
          children: [
            InputDecorator(
              decoration: InputDecoration(
                prefixText: "Skill: ",
                labelText: "Skill: ",
                floatingLabelBehavior: FloatingLabelBehavior.never
              ),
              child: DropdownButton<String>(
                isDense: true,
                isExpanded: true,
                onTap: () => FocusScope.of(context).unfocus(),
                onChanged: (value) {
                  setState((){
                    if(value != "Other..."){
                      manual = false;
                      skill.name = value;
                      skill.base = Skill.skillsList[value];
                    }else{
                      manual = true;
                      skill.name = "";
                    }
                  });
                },
                value: (Skill.skillsList.containsKey(skill.name) || skill.name == null) ? skill.name : Skill.skillsList.keys.last,
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
                prefixText: "Characteristic: ",
                labelText: "Characteristic: ",
                floatingLabelBehavior: FloatingLabelBehavior.never
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
                value: skill.base,
                onChanged: (value) => setState(() => skill.base = value),
              )
            ),
            if(creature is Character) Container(height: 10),
            if(creature is Character) TextField(
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
              controller: valueController,
              decoration: InputDecoration(
                prefixText: "Skill Value: ",
                labelText: "Skill Value: ",
                floatingLabelBehavior: FloatingLabelBehavior.never
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
                FlatButton(
                  child: Text("Save"),
                  onPressed: (skill.name != null && skill.base != null && (creature is Character ? skill.value != null : true)) ? (){
                    onClose(skill);
                    Navigator.of(context).pop();
                  } : null,
                ),
                FlatButton(
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