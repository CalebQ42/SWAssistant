import 'package:flutter/material.dart';

class Bottom extends StatelessWidget{

  final List<Widget> Function(BuildContext)? buttons;
  final Color? background;
  final Widget Function(BuildContext)? child;
  final bool padding;

  final _BottomMessenger messenger = _BottomMessenger();

  Bottom({this.buttons, this.background, required this.child, this.padding = true, Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: MediaQuery.of(context).viewInsets,
      child: Wrap(
        children: [
          ConstrainedBox(
            constraints: BoxConstraints(maxHeight: MediaQuery.of(context).size.height * 0.50),
            child: SingleChildScrollView(
              primary: false,
              child: Padding(
                padding: padding ? const EdgeInsets.only(
                  top: 10,
                  left: 10,
                  right: 10,
                  bottom: 15
                ) : EdgeInsets.zero,
                child: child!(context)
              )
            )
          ),
          if(buttons != null) _BottomButtons(
            builder: buttons!,
            messenger: messenger
          )
        ],
      )
    );
  }

  void updateButtons() => messenger.updateButtons();

  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (c) => this,
      backgroundColor: background,
      isScrollControlled: true,
    );
}

class _BottomMessenger{
  late Function() updateButtons;

  _BottomMessenger();
}

class _BottomButtons extends StatefulWidget{
  final List<Widget> Function(BuildContext) builder;
  final _BottomMessenger messenger;

  const _BottomButtons({required this.builder, required this.messenger, Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _ButtonState();
}

class _ButtonState extends State<_BottomButtons>{

  @override
  void initState(){
    super.initState();
    widget.messenger.updateButtons = () => setState((){});
  }

  @override
  Widget build(BuildContext context) =>
    ButtonBar(
      alignment: MainAxisAlignment.end,
      children: widget.builder(context)
    );
}