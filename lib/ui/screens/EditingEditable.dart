
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
import 'package:swassistant/profiles/Minion.dart';
import 'package:swassistant/profiles/Vehicle.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/Common.dart';
import 'package:swassistant/ui/screens/EditableCards.dart';
import 'package:swassistant/ui/screens/EditableNotes.dart';

class EditingEditable extends StatefulWidget{

  final Editable profile;
  final Function refreshList;

  EditingEditable(this.profile,this.refreshList);

  @override
  State<StatefulWidget> createState() => _EditingEditableState(profile, refreshList);
}

class _EditingEditableState extends State{

  final Editable profile;
  final Function refreshList;
  int _index = 0;

  _EditingEditableState(this.profile,this.refreshList);

  Widget build(BuildContext context) =>
    InheritedEditable(
      child: Scaffold(
        drawer: SWDrawer(),
        bottomNavigationBar: BottomNavigationBar(
          backgroundColor: Theme.of(context).cardColor,
          items: [
            BottomNavigationBarItem(icon: Icon(Icons.info), label: "Stats"),
            BottomNavigationBarItem(icon: Icon(Icons.note), label: "Notes")
          ],
          onTap: (value) => setState(() => _index = value),
          currentIndex: _index,
          elevation: 8.0,
          showSelectedLabels: true,
        ),
        appBar: SWAppBar(
          additionalPopupActions: [
            if(profile is Character) CheckedPopupMenuItem(
              checked: (profile as Character).disableForce,
              child: Text("Disable Force"),
              value: "disableForce"
            ),
            if(profile is Character) CheckedPopupMenuItem(
              checked: (profile as Character).disableObligation,
              child: Text("Disable Morality"),
              value: "disableMorality"
            ),
            if(profile is Character) CheckedPopupMenuItem(
              checked: (profile as Character).disableDuty,
              child: Text("Disable Duty"),
              value: "disableDuty"
            ),
            if(profile is Character) CheckedPopupMenuItem(
              checked: (profile as Character).disableObligation,
              child: Text("Disable Obligation"),
              value: "disableObligation"
            ),
            PopupMenuItem(
              child: Text("Clone"),
              value: "clone"
            ),
            if(SW.of(context).devMode) PopupMenuItem(
              child: Text("Export"),
              value: "export"
            ),
          ],
          popupFunctions: {
            "disableForce" : () => setState(() => (profile as Character).disableForce = !(profile as Character).disableForce),
            "disableDuty" : () => setState(() => (profile as Character).disableDuty = !(profile as Character).disableDuty),
            "disableObligation" : () => setState(() => (profile as Character).disableObligation = !(profile as Character).disableObligation),
            "disableMorality" : () => setState(() => (profile as Character).disableMorality = !(profile as Character).disableMorality),
            "clone" : () =>
              showModalBottomSheet(
                context: context,
                builder: (context){
                  var nameController = TextEditingController(text: "Copy of " + profile.name);
                  return Padding(
                    padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 5, right: 5, bottom: 10)),
                    child: Wrap(
                      children: [
                        Container(height: 10),
                        TextField(
                          controller: nameController,
                          decoration: InputDecoration(
                            labelText: "Name"
                          ),
                        ),
                        ButtonBar(
                          children: [
                            FlatButton(
                              child: Text("Save"),
                              onPressed: (){
                                Editable out;
                                if(profile is Character){
                                  var id = 0;
                                  while(SW.of(context).characters().any((element) => element.id == id))
                                    id++;
                                  out = Character.from(profile, id: id);
                                }else if (profile is Minion){
                                  var id = 0;
                                  while(SW.of(context).minions().any((element) => element.id == id))
                                    id++;
                                  out = Minion.from(profile, id: id);
                                }else if (profile is Vehicle){
                                  var id = 0;
                                  while(SW.of(context).vehicles().any((element) => element.id == id))
                                    id++;
                                  out = Vehicle.from(profile, id: id);
                                }
                                out.name = nameController.text;
                                SW.of(context).add(out);
                                out.save(context: context);
                                Navigator.of(context).pop();
                              },
                            ),
                            FlatButton(
                              child: Text("Cancel"),
                              onPressed: () => Navigator.of(context).pop()
                            )
                          ],
                        )
                      ],
                    )
                  );
                }
              ),
            "export" : (){
              Permission.storage.status.then((value) {
                if(value.isUndetermined || value.isDenied)
                  Permission.storage.request().then((value){
                    if(value.isGranted)
                      FilePicker.platform.getDirectoryPath().then((value){
                        profile.save(filename: value + profile.name + profile.fileExtension);
                      });
                  });
                else if(value.isGranted)
                  FilePicker.platform.getDirectoryPath().then((value){
                    if(value != null)
                      profile.save(filename: value + "/" + profile.name + profile.fileExtension);
                  });
                else
                  print(value);
              });
            }
          },
        ),
        body: _index == 0 ? EditableCards(refreshList: refreshList) : EditableNotes()
      ),
      editable: profile
    );
  }

class InheritedEditable extends InheritedWidget{
  final Editable editable;

  InheritedEditable({Widget child, this.editable}): super(child: child);

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) => false;
}