import 'dart:math';

import 'package:darkstorm_common/ui/frame_content.dart';
import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/screens/editable_list.dart';
import 'package:swassistant/ui/screens/editing_editable.dart';

class GMModeSize extends InheritedWidget {
  final double width;
  const GMModeSize({super.key, required super.child, required this.width});

  @override
  bool updateShouldNotify(covariant InheritedWidget oldWidget) => false;

  static GMModeSize? of(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<GMModeSize>();
}

class GMMode extends StatefulWidget {
  const GMMode({super.key});

  @override
  State<GMMode> createState() => _GMModeState();
}

class _GMModeState extends State<GMMode> {
  final GMModeMessager message = GMModeMessager();

  @override
  Widget build(BuildContext context) {
    var width = MediaQuery.of(context).size.width;
    width = min(450, width / 3);
    var remain = MediaQuery.of(context).size.width - width;
    return PopScope(
      canPop: message.backStack.length <= 1,
      onPopInvokedWithResult: (didPop, res) {
        if (message.backStack.length <= 1) {
          return;
        }
        setState(() {
          message.backStack.removeLast();
          if (message.onChange != null) {
            message.onChange!(message.backStack.last);
          }
        });
      },
      child: FrameContent(
        allowPop: message.backStack.length <= 1,
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          mainAxisSize: MainAxisSize.max,
          children: [
            ConstrainedBox(
              constraints: BoxConstraints(
                maxWidth: width,
              ),
              child: EditableList(
                null,
                key: message.listKey,
                onTap: (ed) {
                  var ind = message.backStack.indexWhere((element) =>
                      element.fileExtension == ed.fileExtension &&
                      element.uid == ed.uid);
                  message.backStack.add(ed);
                  if (message.onChange != null) {
                    message.onChange!(message.backStack.last);
                  }
                  if (ind != -1) {
                    message.backStack.removeAt(ind);
                  }
                  setState(() {});
                },
              ),
            ),
            Container(width: 1, color: Theme.of(context).dividerColor),
            Expanded(
              child: GMModeSize(
                key: ValueKey(remain),
                width: remain,
                child: _GMModeEditor(message),
              ),
            )
          ],
        ),
      ),
    );
    // return WillPopScope(
    //   onWillPop: (){
    //     if (message.backStack.length == 1 || message.backStack.isEmpty) {
    //       return Future.value(true);
    //     }
    //     message.backStack.removeLast();
    //     if (message.onChange != null) message.onChange!(message.backStack.last);
    //     return Future.value(false);
    //   },
    //   child: FrameContent(
    //     child: Row(
    //       crossAxisAlignment: CrossAxisAlignment.stretch,
    //       mainAxisSize: MainAxisSize.max,
    //       children: [
    //         ConstrainedBox(
    //           constraints: BoxConstraints(
    //             maxWidth: width,
    //           ),
    //           child: EditableList(
    //             null,
    //             key: message.listKey,
    //             onTap: (ed) {
    //               var ind = message.backStack.indexWhere((element) => element.fileExtension == ed.fileExtension && element.uid == ed.uid);
    //               message.backStack.add(ed);
    //               if (message.onChange != null) message.onChange!(message.backStack.last);
    //               if (ind != -1) {
    //                 message.backStack.removeAt(ind);
    //               }
    //             }
    //           )
    //         ),
    //         Container(
    //           width: 1,
    //           color: Theme.of(context).dividerColor
    //         ),
    //         Expanded(
    //           child: GMModeSize(
    //             key: ValueKey(remain),
    //             width: remain,
    //             child: _GMModeEditor(message)
    //           )
    //         )
    //       ],
    //     ),
    //   )
    // );
  }
}

class GMModeMessager {
  Function(Editable)? onChange;
  late void Function() editingState;
  final GlobalKey<EditableListState> listKey = GlobalKey();

  final List<Editable> backStack = [];
}

class _GMModeEditor extends StatefulWidget {
  final GMModeMessager message;

  const _GMModeEditor(this.message);

  @override
  State<StatefulWidget> createState() => _GMModeEditorState();
}

class _GMModeEditorState extends State<_GMModeEditor> {
  Editable? curEdit;
  GlobalKey stuff = GlobalKey();

  @override
  void initState() {
    super.initState();
    widget.message.onChange = (ed) => setState(() => curEdit = ed);
    widget.message.editingState = () => setState(() {});
    if (widget.message.backStack.isNotEmpty) {
      curEdit = widget.message.backStack[widget.message.backStack.length - 1];
    }
  }

  @override
  Widget build(BuildContext context) => AnimatedSwitcher(
        duration: const Duration(milliseconds: 300),
        child: curEdit == null
            ? Center(
                child: Text(
                  SW.of(context).locale.gmModeTap,
                  textAlign: TextAlign.justify,
                ),
              )
            : EditingEditable(
                curEdit!,
                key: stuff,
                contained: true,
              ),
      );
}
