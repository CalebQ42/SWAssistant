import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/dialogs/editable/CriticalInjuryEditDialog.dart';

class CriticalInjuries extends StatelessWidget{
  final bool editing;
  final Function refresh;

  CriticalInjuries({this.editing, this.refresh});
  
  @override
  Widget build(BuildContext context) {
    var editable = Editable.of(context);
    var criticalinjuriesList = List.generate(editable.criticalInjuries.length, (i) =>
      InkResponse(
        containedInkWell: true,
        highlightShape: BoxShape.rectangle,
        onTap: (){
          //TODO: Critical tap (show info)
        },
        child: Row(
          children: [
            Expanded(
              child: Padding(
                padding: EdgeInsets.symmetric(vertical: 20),
                child: Text(editable.criticalInjuries[i].name)
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
                      editable.criticalInjuries.removeAt(i);
                      refresh();
                    }
                  ),
                  IconButton(
                    constraints: BoxConstraints(maxHeight: 40.0, maxWidth: 40.0),
                    icon: Icon(Icons.edit),
                    onPressed: () =>
                      CriticalInjuryEditDialog(
                        onClose: (criticalinjury){
                          if(criticalinjury != null){
                            editable.criticalInjuries[i] = criticalinjury;
                            refresh();
                          }
                        },
                        criticalInjury: editable.criticalInjuries[i]
                      ).show(context)
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
      )
    );
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 5),
      child: Column(
        children: [
          Column(children: criticalinjuriesList),
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
                onPressed: () =>
                  CriticalInjuryEditDialog(
                    onClose: (criticalinjury){
                      if(criticalinjury != null){
                        editable.criticalInjuries.add(criticalinjury);
                        refresh();
                      }
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