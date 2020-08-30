import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/items/Weapon.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class Weapons extends StatelessWidget{
  final bool editing;
  final Function refresh;

  Weapons({this.editing, this.refresh});

  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    var weaponsList = List.generate(editable.weapons.length, (i){
      return InkResponse(
        containedInkWell: true,
        onTap: (){
          //TODO: weapon tap!
        },
        child: Row(
          children: [
            Expanded(
              child: Text(editable.weapons[i].name)
            ),
            Expanded(
              child: AnimatedSwitcher(
                duration: Duration(milliseconds: 250),
                child: !editing ? Container(height: 24,)
                : ButtonBar(
                  children: [
                    IconButton(
                      iconSize: 24.0,
                      constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                      icon: Icon(Icons.delete_forever),
                      onPressed: (){
                        editable.weapons.removeAt(i);
                        refresh();
                      }
                    ),
                    IconButton(
                      constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                      icon: Icon(Icons.edit),
                      onPressed: (){
                        showModalBottomSheet(
                          context: context,
                          builder: (context){
                            return WeaponEditDialog(
                              editable: editable,
                              onClose: (weapon){
                                if(weapon != null){
                                  editable.weapons[i] = weapon;
                                  refresh();
                                }
                              },
                              weapon: editable.weapons[i]
                            );
                          }
                        );
                      }
                    )
                  ]
                ),
                transitionBuilder: (child, anim){
                  var offset = Offset(1,0);
                  if((!editing && child is ButtonBar) || (editing && child is Container))
                    offset = Offset(-1,0);
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
            )
          ],
        )
      );
    });
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          Column(children: weaponsList),
          AnimatedSwitcher(
            duration: Duration(milliseconds: 300),
            transitionBuilder: (wid,anim){
              return SizeTransition(
                sizeFactor: anim,
                child: wid,
                axisAlignment: -1.0,
              );
            },
            child: editing ? Center(
              child: IconButton(
                icon: Icon(Icons.add),
                onPressed: (){
                  showModalBottomSheet(
                    isScrollControlled: true,
                    context: context,
                    builder: (context){
                      return WeaponEditDialog(onClose: (weapon){
                        editable.weapons.add(weapon);
                        refresh();
                      },weapon: null);
                    }
                  );
                },
              )
            ) : Container(),
          )
        ],
      ),
    );
  }
}

class WeaponEditDialog extends StatefulWidget{
  final Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;

  WeaponEditDialog({this.onClose, Weapon weapon, this.editable}) :
      this.weapon = weapon == null ? Weapon.nulled() : Weapon.from(weapon);

  @override
  State<StatefulWidget> createState() => _WeaponEditDialogState(onClose: onClose, weapon: weapon, editable: editable);
}

class _WeaponEditDialogState extends State{
  final Function(Weapon) onClose;
  final Weapon weapon;
  final Editable editable;
  TextSelection prevSelection;

  _WeaponEditDialogState({this.onClose, this.weapon, this.editable});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Padding(
        padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15, top: 5)),
        child: Wrap(
          children: [
            //Name
            TextField(
              controller: (){
                var cont = TextEditingController(text: weapon.name);
                if(prevSelection != null){
                  cont.selection = prevSelection;
                  prevSelection = null;
                }
                cont.addListener(() {
                  if((weapon.name == "" && cont.text != "") || (weapon.name != "" && cont.text == "")){
                    prevSelection = cont.selection;
                    setState(() => weapon.name = cont.text);
                  }else
                    weapon.name = cont.text;
                });
                return cont;
              }(),
              decoration: InputDecoration(
                prefix: Text("Name: "),
                hintText: "Name"
              ),
            ),
            //Damage
            TextField(
              controller: (){
                var cont = TextEditingController(text: weapon.damage != null ? weapon.damage.toString() : "");
                if(prevSelection != null){
                  cont.selection = prevSelection;
                  prevSelection = null;
                }
                cont.addListener(() {
                  var val = int.tryParse(cont.text);
                  if((weapon.damage == null && val != null) || weapon.damage != null && val == null){
                    prevSelection = cont.selection;
                    setState(() => weapon.damage = val);
                  }else
                    weapon.damage = val;
                });
                return cont;
              }(),
              decoration: InputDecoration(
                prefix: Text("Damage: "),
                hintText: "Damage"
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
            ),
            //Critical
            TextField(
              controller: (){
                var cont = TextEditingController(text: weapon.critical != null ? weapon.critical.toString() : "");
                if(prevSelection != null){
                  cont.selection = prevSelection;
                  prevSelection = null;
                }
                cont.addListener(() {
                  var val = int.tryParse(cont.text);
                  if((weapon.critical == null && val != null) || weapon.critical != null && val == null){
                    prevSelection = cont.selection;
                    setState(() => weapon.critical = val);
                  }else
                    weapon.critical = val;
                });
                return cont;
              }(),
              decoration: InputDecoration(
                prefix: Text("Critical: "),
                hintText: "Critical"
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
            ),
            //Hard Points
            TextField(
              controller: (){
                var cont = TextEditingController(text: weapon.hp != null ? weapon.hp.toString() : "");
                if(prevSelection != null){
                  cont.selection = prevSelection;
                  prevSelection = null;
                }
                cont.addListener(() {
                  var val = int.tryParse(cont.text);
                  if((weapon.hp == null && val != null) || weapon.hp != null && val == null){
                    prevSelection = cont.selection;
                    setState(() => weapon.hp = val);
                  }else
                    weapon.hp = val;
                });
                return cont;
              }(),
              decoration: InputDecoration(
                prefix: Text("Hard Points: "),
                hintText: "Hard Points"
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
            ),
            //Encumbrance
            if(editable is Character) TextField(
              controller: (){
                var cont = TextEditingController(text: weapon.encumbrance != null ? weapon.encumbrance.toString() : "");
                if(prevSelection != null){
                  cont.selection = prevSelection;
                  prevSelection = null;
                }
                cont.addListener(() {
                  var val = int.tryParse(cont.text);
                  if((weapon.encumbrance == null && val != null) || weapon.encumbrance != null && val == null){
                    prevSelection = cont.selection;
                    setState(() => weapon.encumbrance = val);
                  }else
                    weapon.encumbrance = val;
                });
                return cont;
              }(),
              decoration: InputDecoration(
                prefix: Text("Encumbrance: "),
                hintText: "Encumbrance"
              ),
              keyboardType: TextInputType.number,
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
            ),
            ButtonBar(
              children: [
                FlatButton(
                  child: Text("Cancel"),
                  onPressed: () => onClose(null),
                ),
                FlatButton(
                  child: Text("Save"),
                  onPressed: () => onClose(weapon)
                )
              ],
            )
          ],
        )
      )
    );
  }
}