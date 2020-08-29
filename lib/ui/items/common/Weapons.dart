import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/items/Weapon.dart';
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
                        showDialog(
                          context: context,
                          builder: (context){
                            return WeaponEditDialog(
                              onClose: (weapon){
                                editable.weapons[i] = weapon;
                                refresh();
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
                    context: context,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(20.0),
                        topRight: Radius.circular(20.0)
                      )
                    ),
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

  WeaponEditDialog({this.onClose, this.weapon});

  @override
  State<StatefulWidget> createState() => _WeaponEditDialogState(onClose: onClose, weapon: weapon);
}

class _WeaponEditDialogState extends State{
  final Function(Weapon) onClose;
  final Weapon weapon;

  _WeaponEditDialogState({this.onClose, this.weapon});

  @override
  Widget build(BuildContext context) {
    //TODO
  }
}