import 'package:darkstorm_common/frame_content.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/items/note.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';
import 'package:swassistant/ui/misc/mini_icon_button.dart';

class EditableNotes extends StatefulWidget{

  const EditableNotes({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => EditableNotesState();
}

class EditableNotesState extends State{

  final GlobalKey<AnimatedListState> listy = GlobalKey<AnimatedListState>();

  @override
  Widget build(BuildContext context) =>
    FrameContent(
      fab: FloatingActionButton(
        child: const Icon(Icons.note_add),
        onPressed: (){
          Editable.of(context).notes.add(Note());
          listy.currentState?.insertItem(Editable.of(context).notes.length-1);
        },
      ),
      child: AnimatedList(
        physics: const BouncingScrollPhysics(),
        key: listy,
        initialItemCount: Editable.of(context).notes.length,
        itemBuilder: (context, index, anim) {
          return SlideTransition(
            position: Tween(begin: const Offset(1.0, 0.0), end: Offset.zero).animate(anim),
            child: NoteCard(
              key: ValueKey(index),
              index: index,
              list: listy,
            )
          );
        },
        padding: const EdgeInsets.only(bottom: 75)
      ),
    );
}

class NoteCard extends StatefulWidget{

  final int index;
  final GlobalKey<AnimatedListState> list;

  const NoteCard({Key? key, required this.index, required this.list}) : super(key: key);

  @override
  State<NoteCard> createState() => _NCState();
}

class _NCState extends State<NoteCard> {
  final GlobalKey<_NoteCardState> contentKey = GlobalKey();

  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
    return Dismissible(
      key: ValueKey(widget.index.toString() + Editable.of(context).notes[widget.index].note),
      child: Card(
        child: Padding(
          padding: const EdgeInsets.all(10.0),
          child: EditContent(
            extraEditButtons: (context) => [
              MiniIconButton(
                icon: const Icon(Icons.format_align_left),
                color: Theme.of(context).buttonTheme.colorScheme?.onSurface,
                onPressed: () =>
                  contentKey.currentState?.update(() => Editable.of(context).notes[widget.index].align = 0)
              ),
              MiniIconButton(
                icon: const Icon(Icons.format_align_center),
                color: Theme.of(context).buttonTheme.colorScheme?.onSurface,
                onPressed: () =>
                  contentKey.currentState?.update(() => Editable.of(context).notes[widget.index].align = 1)
              ),
              MiniIconButton(
                icon: const Icon(Icons.format_align_right),
                color: Theme.of(context).buttonTheme.colorScheme?.onSurface,
                onPressed: () =>
                  contentKey.currentState?.update(() => Editable.of(context).notes[widget.index].align = 2)
              )
            ],
            content: NoteCardContents(index: widget.index, key: contentKey),
            contentKey: contentKey,
            defaultEdit: () => Editable.of(context).notes[widget.index].note.isEmpty,
          ),
        )
      ),
      onDismissed: (direction) {
        var editable = Editable.of(context);
        var temp = Editable.of(context).notes[widget.index];
        editable.notes.removeAt(widget.index);
        widget.list.currentState?.removeItem(widget.index, (cont,anim) => Container());
        ScaffoldMessenger.of(context).clearSnackBars();
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(app.locale.deletedNote),
            action: SnackBarAction(
              label: app.locale.undo,
              onPressed: (){
                editable.notes.insert(widget.index, temp);
                widget.list.currentState?.insertItem(widget.index);
              }
            ),
          )
        );
      },
    );
  }
}

class NoteCardContents extends StatefulWidget{

  final int index;

  const NoteCardContents({Key? key, required this.index}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _NoteCardState();
}

class _NoteCardState extends State<NoteCardContents> with StatefulCard{

  bool edit = false;
  @override
  set editing(bool b) => setState(() => edit = b);
  @override
  bool get defaultEdit => Editable.of(context).notes[widget.index].note.isEmpty;

  void update(void Function() updateFunc) => setState(updateFunc);

  TextEditingController? control;

  @override
  Widget build(BuildContext context) {
    control ??= TextEditingController(text: Editable.of(context).notes[widget.index].note)
      ..addListener(() => Editable.of(context).notes[widget.index].note = control!.text);
    TextAlign align;
    switch(Editable.of(context).notes[widget.index].align){
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
      editing: edit,
      initialText: Editable.of(context).notes[widget.index].note,
      defaultSave: true,
      controller: control,
      textAlign: align,
      fieldAlign: align,
      multiline: true,
      textCapitalization: TextCapitalization.sentences,
    );
  }
}