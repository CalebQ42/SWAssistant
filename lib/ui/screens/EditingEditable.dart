
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:swassistant/SW.dart';
import 'package:swassistant/profiles/Character.dart';
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

  Widget build(BuildContext context) {
    return InheritedEditable(
      child: Scaffold(
        drawer: SWDrawer(),
        bottomNavigationBar: BottomNavigationBar(
          backgroundColor: Theme.of(context).cardColor,
          items: [
            BottomNavigationBarItem(icon: Icon(Icons.info), title: Text("Stats")),
            BottomNavigationBarItem(icon: Icon(Icons.note), title: Text("Notes"))
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
              checked: (profile as Character).disableDuty,
              child: Text("Disable Duty"),
              value: "disableDuty"
            ),
            if(profile is Character) CheckedPopupMenuItem(
              checked: (profile as Character).disableObligation,
              child: Text("Disable Obligation"),
              value: "disableObligation"
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
}

class InheritedEditable extends InheritedWidget{
  final Editable editable;

  InheritedEditable({Widget child, this.editable}): super(child: child);

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) => false;
}