import 'package:flutter/material.dart';
import 'package:swassistant/items/Note.dart';
import 'package:swassistant/profiles/utils/Editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class EditableNotes extends StatefulWidget{
  @override
  State<StatefulWidget> createState() => _EditableNotesState();
}

class _EditableNotesState extends State{
   Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.note_add),
        onPressed: (){
          setState(() => Editable.of(context).notes.add(Note()));
        },
      ),
      body: AnimatedList(
        key: UniqueKey(),
        initialItemCount: Editable.of(context).notes.length + 1,
        itemBuilder: (context, index, animation) {
          if(index < Editable.of(context).notes.length)
            return NoteCard(index: index, onDelete: (){
              var temp = Editable.of(context).notes[index];
              Editable.of(context).notes.removeAt(index);
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(
                  content: Text(AppLocalizations.of(context)!.deletedNote),
                  action: SnackBarAction(
                    label: AppLocalizations.of(context)!.undo,
                    onPressed: (){
                      setState((){
                        Editable.of(context).notes.insert(index, temp);
                      });
                    }
                  ),
                )
              );
              setState((){});
            },);
          else
            return Container(height: 75,);
        },
      )
    );
  }
}

class NoteCard extends StatefulWidget{

  final int index;
  final Function() onDelete;

  NoteCard({required this.index, required this.onDelete});

  @override
  State<StatefulWidget> createState() => _NoteCardState(index: index, onDelete: onDelete);
}

class _NoteCardState extends State{
  final int index;
  final Function() onDelete;

  _NoteCardState({required this.index, required this.onDelete});

  @override
  Widget build(BuildContext context) {
    return Dismissible(
      key: UniqueKey(),
      child: Card(
        child: Padding(
          padding: EdgeInsets.all(10.0),
          child: EditableContent(
            builder: (b,refresh, state){
              return EditingText(
                editing: b,
                initialText: Editable.of(context).notes[index].note,
                defaultSave: true,
                controller: (){
                  var cont = TextEditingController(text: Editable.of(context).notes[index].note);
                  cont.addListener(() {
                    Editable.of(context).notes[index].note = cont.text;
                  });
                  return cont;
                }(),
                multiline: true,
                textCapitalization: TextCapitalization.sentences,
              );
            }
          ),
        )
      ),
      onDismissed: (direction) {
        onDelete();
      },
    );
  }
}