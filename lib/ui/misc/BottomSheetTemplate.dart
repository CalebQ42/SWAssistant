import 'package:flutter/material.dart';

class Bottom extends StatelessWidget{

  final List<Widget> Function(BuildContext)? buttons;
  final Color? background;
  final Widget Function(BuildContext)? child;
  final bool padding;

  final _BottomMessager messager = _BottomMessager();

  Bottom({this.buttons, this.background, required this.child, this.padding = true});

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
              child: child!(context)
            )
          )
        ),
        if(buttons != null) _BottomButtons(
          builder: buttons!,
          messager: messager
        )
      ],
    );
  }

  void updateButtons() => messager.updateButtons();

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (c) => this,
      backgroundColor: background,
      isScrollControlled: true,
    );
}

class _BottomMessager{
  late Function() updateButtons;

  _BottomMessager();
}

class _BottomButtons extends StatefulWidget{
  final List<Widget> Function(BuildContext) builder;
  final _BottomMessager messager;

  _BottomButtons({required this.builder, required this.messager});

  @override
  State<StatefulWidget> createState() => _ButtonState(builder, messager);
}

class _ButtonState extends State{
  final List<Widget> Function(BuildContext) builder;

  _ButtonState(this.builder, _BottomMessager message){
    message.updateButtons = () => setState((){});
  }

  @override
  Widget build(BuildContext context) =>
    ButtonBar(
      alignment: MainAxisAlignment.end,
      children: builder(context)
    );
}