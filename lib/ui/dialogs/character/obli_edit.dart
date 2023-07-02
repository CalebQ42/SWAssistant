import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:swassistant/items/obligation.dart';
import 'package:darkstorm_common/bottom.dart';
import 'package:swassistant/sw.dart';

class ObligationEditDialog{

  final Obligation obligation;
  final Function(Obligation) onClose;

  late Bottom bot;

  ObligationEditDialog({Obligation? obli, required this.onClose}) :
    obligation = obli != null ? Obligation.from(obli) : Obligation(){
    var nameController = TextEditingController(text: obligation.name);
    nameController.addListener(() {
      obligation.name = nameController.text;
      bot.updateButtons();
    });
    var valueController = TextEditingController(text: obligation.value?.toString());
    valueController.addListener(() {
      obligation.value = int.tryParse(valueController.text);
      bot.updateButtons();
    });
    var descController = TextEditingController(text: obligation.desc);
    descController.addListener(() =>
      obligation.desc = descController.text
    );
    bot = Bottom(
      child: (context) =>
        Wrap(
          children: [
            Container(height: 15),
            TextField(
              controller: nameController,
              textCapitalization: TextCapitalization.words,
              decoration: InputDecoration(
                labelText: SW.of(context).locale.name
              ),
            ),
            Container(height: 10),
            TextField(
              controller: valueController,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              keyboardType: TextInputType.number,
              decoration: InputDecoration(
                labelText: SW.of(context).locale.obligation
              ),
            ),
            Container(height: 10),
            TextField(
              controller: descController,
              textCapitalization: TextCapitalization.sentences,
              maxLines: 3,
              minLines: 1,
              decoration: InputDecoration(
                labelText: SW.of(context).locale.desc
              ),
            )
          ],
        ),
      buttons: (context) =>[
        TextButton(
          onPressed: obligation.name != "" && obligation.value != null ? (){
            onClose(obligation);
            Navigator.pop(context);
          } : null,
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: () => Navigator.pop(context),
        )]
    );
  }

  void show(BuildContext context) =>
    bot.show(context);
}