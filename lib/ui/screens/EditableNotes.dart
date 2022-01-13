import 'package:flutter/material.dart';
import 'package:swassistant/items/note.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/EditableCommon.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class EditableNotes extends StatelessWidget{

  final GlobalKey<AnimatedListState> listy = GlobalKey<AnimatedListState>(); 

  Widget build(BuildContext context) =>
    Scaffold(
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.note_add),
        onPressed: (){
          Editable.of(context).notes.add(Note());
          listy.currentState?.insertItem(Editable.of(context).notes.length-1);
        },
      ),
      body: AnimatedList(
        key: listy,
        initialItemCount: Editable.of(context).notes.length,
        itemBuilder: (context, index, anim) =>
          SlideTransition(
            position: Tween(begin: Offset(1.0, 0.0), end: Offset.zero).animate(anim),
            child: NoteCard(
              index: index,
              list: listy,
            )
          ),
        padding: EdgeInsets.only(bottom: 75)
      )
    );
}

class NoteCard extends StatelessWidget{

  final int index;
  final GlobalKey<AnimatedListState> list;
  final EditableContentStatefulHolder holder;

  NoteCard({required this.index, required this.list}) : holder = EditableContentStatefulHolder();

  @override
  Widget build(BuildContext context) =>
    Dismissible(
      key: ValueKey(index.toString() + Editable.of(context).notes[index].note),
      child: Card(
        child: Padding(
          padding: EdgeInsets.all(10.0),
          child: EditableContent(
            editingButtons: () => [
              IconButton(
                iconSize: 20.0,
                splashRadius: 20,
                padding: EdgeInsets.all(5.0),
                constraints: BoxConstraints.tight(Size.square(30.0)),
                icon: Icon(Icons.format_align_left),
                color: Theme.of(context).buttonTheme.colorScheme?.onSurface,
                onPressed: () {
                  Editable.of(context).notes[index].align = 0;
                  if(holder.reloadFunction != null)
                    holder.reloadFunction!();
                }
              ),
              IconButton(
                iconSize: 20.0,
                splashRadius: 20,
                padding: EdgeInsets.all(5.0),
                constraints: BoxConstraints.tight(Size.square(30.0)),
                icon: Icon(Icons.format_align_center),
                color: Theme.of(context).buttonTheme.colorScheme?.onSurface,
                onPressed: () {
                  Editable.of(context).notes[index].align = 1;
                  if(holder.reloadFunction != null)
                    holder.reloadFunction!();
                }
              ),
              IconButton(
                iconSize: 20.0,
                splashRadius: 20,
                padding: EdgeInsets.all(5.0),
                constraints: BoxConstraints.tight(Size.square(30.0)),
                icon: Icon(Icons.format_align_right),
                color: Theme.of(context).buttonTheme.colorScheme?.onSurface,
                onPressed: () {
                  Editable.of(context).notes[index].align = 2;
                  if(holder.reloadFunction != null)
                    holder.reloadFunction!();
                }
              )
            ],
            stateful: NoteCardContents(holder: holder, index: index),
          ),
        )
      ),
      onDismissed: (direction) {
        var editable = Editable.of(context);
        var temp = Editable.of(context).notes[index];
        editable.notes.removeAt(index);
        list.currentState?.removeItem(index, (cont,anim) => Container());
        ScaffoldMessenger.of(context).clearSnackBars();
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(AppLocalizations.of(context)!.deletedNote),
            action: SnackBarAction(
              label: AppLocalizations.of(context)!.undo,
              onPressed: (){
                editable.notes.insert(index, temp);
                list.currentState?.insertItem(index);
              }
            ),
          )
        );
      },
    );
}

class NoteCardContents extends StatefulWidget with StatefulCard{

  final int index;
  final EditableContentStatefulHolder holder;

  NoteCardContents({required this.index, required this.holder});

  @override
  State<StatefulWidget> createState() => _NoteCardState(index: index, holder: holder);

  EditableContentStatefulHolder getHolder() => holder;
}

class _NoteCardState extends State{
  final int index;
  final EditableContentStatefulHolder holder;

  TextEditingController? control;

  _NoteCardState({required this.index, required this.holder}){
    holder.reloadFunction = () => setState((){});
  }

  @override
  Widget build(BuildContext context) {
    control ??= TextEditingController(text: Editable.of(context).notes[index].note)
      ..addListener(() {
        Editable.of(context).notes[index].note = control!.text;
      });
    TextAlign align;
    switch(Editable.of(context).notes[index].align){
      case 0:
        align = TextAlign.left;
        break;
      case 1:
        align = TextAlign.center;
        break;
      default:
        align = TextAlign.right;
    }
    return EditingText(
      editing: holder.editing,
      initialText: Editable.of(context).notes[index].note,
      defaultSave: true,
      controller: control,
      textAlign: align,
      fieldAlign: align,
      multiline: true,
      textCapitalization: TextCapitalization.sentences,
    );
  }
}