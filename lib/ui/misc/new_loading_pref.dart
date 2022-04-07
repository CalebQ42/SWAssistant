import 'package:flutter/material.dart';

class NewPrefLoading extends StatelessWidget{
  final Widget? child;

  const NewPrefLoading({Key? key, this.child}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    ConstrainedBox(
      constraints: const BoxConstraints(maxWidth: 500),
      child: SingleChildScrollView(
        child: child
      )
    );
}