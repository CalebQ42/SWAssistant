import 'package:flutter/material.dart';
import 'package:swassistant/utils/driver.dart';

class TestScreen extends StatelessWidget{

  const TestScreen({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) =>
    Padding(
      padding: const EdgeInsets.symmetric(horizontal: 30, vertical: 100),
      child: Row(
        mainAxisSize: MainAxisSize.max,
        children: [
          Expanded(child: ElevatedButton(
            child: const Text("Test"),
            onPressed: () async {
              var driv = Driver();
              print(await driv.init());
            }
          ))
        ]
      )
    );
}