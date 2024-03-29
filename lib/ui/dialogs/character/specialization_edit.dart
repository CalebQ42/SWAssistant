import 'package:flutter/material.dart';
import 'package:darkstorm_common/bottom.dart';
import 'package:swassistant/sw.dart';

class SpecializationEditDialog{

  final Function(String) onClose;
  String specialization;

  late Bottom bot;

  SpecializationEditDialog({required this.onClose, this.specialization = ""}){
    var specCont = TextEditingController(text: specialization);
    specCont.addListener(() {
      specialization = specCont.text;
      bot.updateButtons();
    });
    bot = Bottom(
      child: (context) =>
        Wrap(
          children: [
            Container(height: 15),
            TextField(
              controller: specCont,
              textCapitalization: TextCapitalization.words,
              decoration: InputDecoration(
                labelText: SW.of(context).locale.specialization
              ),
            ),
          ],
        ),
      buttons: (context) => [
        TextButton(
          onPressed: specialization != "" ? (){
            onClose(specialization);
            Navigator.of(context).pop();
          } : null,
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: () =>
            Navigator.of(context).pop(),
        )]
    );
  }

  void show(BuildContext context) => bot.show(context);
}