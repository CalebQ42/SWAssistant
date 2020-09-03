import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/dialogs/WeaponEditDialog.dart';

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
              child: Padding(
                padding: EdgeInsets.symmetric(vertical: 20),
                child: Text(editable.weapons[i].name)
              )
            ),
            AnimatedSwitcher(
              duration: Duration(milliseconds: 250),
              child: !editing ? Container(height: 24,)
              : ButtonBar(
                buttonPadding: EdgeInsets.zero,
                children: [
                  IconButton(
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
                      return WeaponEditDialog(
                        onClose: (weapon){
                          if(weapon != null){
                            editable.weapons.add(weapon);
                            refresh();
                          }
                        }
                      );
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