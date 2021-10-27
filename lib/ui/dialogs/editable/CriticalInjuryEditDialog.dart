import 'package:flutter/material.dart';
import 'package:swassistant/items/CriticalInjury.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/Bottom.dart';

class CriticalInjuryEditDialog {
  final CriticalInjury criticalInjury;
  final Function(CriticalInjury) onClose;

  late TextEditingController nameController;
  late TextEditingController descController;

  late Bottom bot;

  CriticalInjuryEditDialog({CriticalInjury? inj, required this.onClose}) :
    this.criticalInjury = inj == null ? CriticalInjury() : CriticalInjury.from(inj){
    nameController = TextEditingController(text: criticalInjury.name)
        ..addListener(() {
          criticalInjury.name = nameController.text;
          bot.updateButtons();
        });
    descController = TextEditingController(text: criticalInjury.desc)
        ..addListener(() =>
          criticalInjury.desc = descController.text
        );
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
          onPressed: criticalInjury.name != "" ? (){
            Navigator.of(context).pop();
            onClose(criticalInjury);
          } : null,
        ),
        TextButton(
          child: Text(MaterialLocalizations.of(context).cancelButtonLabel),
          onPressed: (){
            Navigator.of(context).pop();
          },
        )],
      child: (context) =>
        DropdownButtonHideUnderline(
          child: Wrap(
            children: [
              Container(height: 15),
              TextField(
                textCapitalization: TextCapitalization.words,
                controller: nameController,
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.name,
                ),
              ),
              Container(height: 10),
              TextField(
                textCapitalization: TextCapitalization.sentences,
                controller: descController,
                maxLines: 3,
                minLines: 1,
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.desc
                ),
              ),
              Container(height: 10),
              InputDecorator(
                decoration: InputDecoration(
                  labelText: AppLocalizations.of(context)!.severity
                ),
                child: _SeverityDropdown(criticalInjury, bot)
              )
            ],
          )
        )
    );
  }
  
  void show(BuildContext context) => bot.show(context);
}

class _SeverityDropdown extends StatefulWidget{
  final CriticalInjury criticalInjury;
  final Bottom bot;

  _SeverityDropdown(this.criticalInjury, this.bot);

  @override
  State<StatefulWidget> createState() => _SeverityState(criticalInjury, bot);
}

class _SeverityState extends State{
  final CriticalInjury criticalInjury;
  final Bottom bot;

  _SeverityState(this.criticalInjury, this.bot);

  @override
  Widget build(BuildContext context) =>
    DropdownButton<int>(
      isDense: true,
      isExpanded: true,
      items: [
        DropdownMenuItem(
          child: Text(AppLocalizations.of(context)!.severityLevel1),
          value: 0
        ),
        DropdownMenuItem(
          child: Text(AppLocalizations.of(context)!.severityLevel2),
          value: 1,
        ),
        DropdownMenuItem(
          child: Text(AppLocalizations.of(context)!.severityLevel3),
          value: 2,
        ),
        DropdownMenuItem(
          child: Text(AppLocalizations.of(context)!.severityLevel4),
          value: 3,
        ),
      ],
      onChanged: (value) {
        setState(() => criticalInjury.severity = value ?? 0);
        bot.updateButtons();
      },
      value: criticalInjury.severity,
      onTap: () => FocusScope.of(context).unfocus(),
    );
}