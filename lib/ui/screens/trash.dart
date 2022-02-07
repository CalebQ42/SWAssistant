import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';

class TrashList extends StatelessWidget{
  final GlobalKey<AnimatedListState> listKey = GlobalKey();

  TrashList({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    AnimatedList(
      initialItemCount: SW.of(context).trashCan.length,
      itemBuilder: (context, i, anim) =>
        Dismissible(
          key: ValueKey(SW.of(context).trashCan[i].uid),
          child: Card(),
        ),
    );
}