import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/ui/Common.dart';
import 'package:swassistant/ui/screens/EditingEditable.dart';

class EditableList extends StatefulWidget{

  //This determines which list is shown
  static final character = 0;
  static final minion = 1;
  static final vehicle = 2;

  final SW app;
  final int type;

  EditableList(this.app, this.type){
    if(this.type <0 || this.type > 2)
      throw("Invlid type");
  }

  @override
  State<StatefulWidget> createState() => EditableListState(app, type);
}

class EditableListState extends State{

  SW app;
  Function refreshCallback;
  int type;

  EditableListState(this.app, this.type){
    refreshCallback = () => setState((){});
  }

  Widget build(BuildContext context) {
    List list;
    switch(type){
      case 0:
        list = app.characters;
        break;
      case 1:
        list = app.minions;
        break;
      case 2:
        list = app.vehicles;
        break;
      default:
        throw("invalid list type");
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
          return Card(
            child: InkResponse(
              onTap: (){
                Navigator.push(c,MaterialPageRoute(builder: (BuildContext bc)=> EditingEditable(list[index],app, refreshCallback),fullscreenDialog: false));
              },
              child:Padding(
                padding: EdgeInsets.all(10.0),
                child: Hero(
                  transitionOnUserGestures: true,
                  tag: (){
                    String out = "";
                    switch(type){
                      case 0:
                        out = "character/";
                        break;
                      case 1:
                        out = "minion/";
                        break;
                      case 2:
                        out = "vehicle/";
                        break;
                    }
                    return out + list[index].id.toString();
                  }(),
                  child:Text(list[index].name, style: Theme.of(c).textTheme.headline5)
                ),
              )
            )
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: (){
          var id = 0;
          switch (type){
            case 0:
              while(app.characters.any((e)=>e.id==id)){
                id++;
              }
              setState(() => app.characters.add(new Character(id: id)));
              break;
            case 1:
              while(app.minions.any((e)=>e.id==id)){
                id++;
              }
              setState(() => app.minions.add(new Minion(id: id)));
              break;
            case 2:
              while(app.vehicles.any((e)=>e.id==id)){
                id++;
              }
              setState(() => app.vehicles.add(new Vehicle(id: id)));
              break;
          }
        }
      ),
    );
  }
}