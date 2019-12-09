import 'package:flutter/material.dart';
import 'package:swassistant/profiles/utils/Editable.dart';

class EditableCards{
  static Widget NameCard(Editable e){
    return Card(
      child: Padding(
        padding: EdgeInsets.all(10.0),
        child: Column(
          children: <Widget>[
            ListTile(
              title: Text("Name"),
            ),
            Text(e.name, textAlign: TextAlign.center,)
          ],
        )
      )
    );
  }
}