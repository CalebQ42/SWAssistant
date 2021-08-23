import 'package:flutter/material.dart';

class Bottom extends StatelessWidget{

  final List<Widget>? buttons;
  final Color? background;
  final Widget? child;

  Bottom({this.buttons, this.background, this.child});

  @override
  Widget build(BuildContext context) =>
    Wrap(
      children: [
        Expanded(
          child: ConstrainedBox(
            constraints: BoxConstraints(maxHeight: MediaQuery.of(context).size.height * 0.70),
            child: SingleChildScrollView(
              primary: false,
              child: child
            )
          )
        ),
        if(buttons != null && buttons!.length > 0) ButtonBar(
          alignment: MainAxisAlignment.end,
          children: buttons!,
        )
      ],
    );

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (c) => this,
      backgroundColor: background,
      isScrollControlled: true,
    );
}