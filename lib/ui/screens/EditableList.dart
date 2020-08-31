import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/Common.dart';
import 'package:swassistant/ui/screens/EditingEditable.dart';

class EditableList extends StatefulWidget{

  //This determines which list is shown
  static final character = 0;
  static final minion = 1;
  static final vehicle = 2;

  final int type;

  EditableList(this.type){
    if(this.type <0 || this.type > 2)
      throw("Invlid type");
  }

  @override
  State<StatefulWidget> createState() => _EditableListState(type);
}

class _EditableListState extends State{

  Function refreshCallback;
  int type;
  List list;

  _EditableListState(this.type){
    refreshCallback = () => setState((){});
  }

  Widget build(BuildContext context) {
    if(list == null){
      switch(type){
        case 0:
          list = SW.of(context).characters();
          break;
        case 1:
          list = SW.of(context).minions();
          break;
        case 2:
          list = SW.of(context).vehicles();
          break;
        default:
          throw("invalid list type");
      }
    }
    return Scaffold(
      drawer: SWDrawer(),
      appBar: SWAppBar(title: Text((){
        switch(type){
          case 0:
            return "Characters";
            break;
          case 1:
            return "Minions";
            break;
          case 2:
            return "Vehicles";
            break;
          default:
            return "";
        }
      }())),
      body: ListView.builder(
        itemCount: list.length,
        itemBuilder: (BuildContext c, int index){
          return EditableCard(editable: list[index], refreshCallback: refreshCallback);
        },
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: (){
          var id = 0;
          switch (type){
            case 0:
              while(SW.of(context).characters().any((e)=>e.id==id)){
                id++;
              }
              setState(() => SW.of(context).add(new Character(id: id, saveOnCreation: true, app: SW.of(context))));
              break;
            case 1:
              while(SW.of(context).minions().any((e)=>e.id==id)){
                id++;
              }
              setState(() => SW.of(context).add(new Minion(id: id, saveOnCreation: true, app: SW.of(context))));
              break;
            case 2:
              while(SW.of(context).vehicles().any((e)=>e.id==id)){
                id++;
              }
              setState(() => SW.of(context).add(new Vehicle(id: id, saveOnCreation: true, app: SW.of(context))));
              break;
          }
        }
      ),
    );
  }
}

class EditableCard extends StatelessWidget{
  final Editable editable;
  final Function refreshCallback;

  EditableCard({this.editable, this.refreshCallback});
  @override
  Widget build(BuildContext context) {
    return Dismissible(
      key: UniqueKey(),
      onDismissed: (direction){
        var tmp = editable;
        editable.delete(SW.of(context));
        SW.of(context).remove(editable);
        String msg;
        if(editable is Character)
          msg = "Character Deleted";
        else if (editable is Minion)
          msg = "Minion Deleted";
        else if (editable is Vehicle)
          msg = "Vehicle Deleted";
        Scaffold.of(context).showSnackBar(
          SnackBar(
            content: Text(msg),
            action: SnackBarAction(
              label: "Undo",
              onPressed: (){
                SW.of(context).add(tmp);
                tmp.save(tmp.getFileLocation(SW.of(context)));
                refreshCallback();
              }
            ),
          )
        );
        refreshCallback();
      },
      child: InheritedEditable(
        child:Card(
          child: InkResponse(
            onTap: (){
              Navigator.push(context,MaterialPageRoute(builder: (BuildContext bc)=> EditingEditable(editable,refreshCallback),fullscreenDialog: false));
            },
            child:Padding(
              padding: EdgeInsets.all(10.0),
              child: Hero(
                transitionOnUserGestures: true,
                tag: (){
                  String out;
                  if(editable is Character)
                    out = "character/";
                  else if(editable is Minion)
                    out = "minion/";
                  else if(editable is Vehicle)
                    out = "vehicle/";
                  
                  return out + editable.id.toString();
                }(),
                child: Text(editable.name, style: Theme.of(context).textTheme.headline5)
              ),
            )
          )
        ),
        editable: editable
      )
    );
  }
}