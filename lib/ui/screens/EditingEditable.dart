
import 'package:flutter/material.dart';
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
          items: [
            BottomNavigationBarItem(icon: Icon(Icons.info), title: Text("Stats")),
            BottomNavigationBarItem(icon: Icon(Icons.note), title: Text("Notes"))
          ],
          onTap: (value) {
            setState(()=>_index = value);
          },
          currentIndex: _index,
        ),
        appBar: SWAppBar(title: Text(profile.name)),
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