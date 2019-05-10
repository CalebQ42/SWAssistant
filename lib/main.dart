import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.red,
        primaryColor: Colors.red,
        accentColor: Colors.blueAccent,
        brightness: Brightness.dark,
      ),
      home: Scaffold(
        appBar: AppBar(title: Text("Hi Wold")),
        floatingActionButton: FloatingActionButton(onPressed: null),
      )
    );
  }
}
