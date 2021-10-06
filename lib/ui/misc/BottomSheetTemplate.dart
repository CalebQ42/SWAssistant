import 'package:flutter/material.dart';

class Bottom extends StatelessWidget{

  final List<Widget>? buttons;
  final Color? background;
  final Widget? child;
  final bool padding;

  Bottom({this.buttons, this.background, this.child, this.padding = true});

  @override
  Widget build(BuildContext context) {
    return Wrap(
      children: [
        ConstrainedBox(
          constraints: BoxConstraints(maxHeight: MediaQuery.of(context).size.height * 0.70),
          child: SingleChildScrollView(
            primary: false,
            child: Padding(
              padding: padding ? EdgeInsets.only(
                top: 10,
                left: 10,
                right: 10,
              ) : EdgeInsets.zero,
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
  }

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (c) => this,
      backgroundColor: background,
      isScrollControlled: true,
    );
}