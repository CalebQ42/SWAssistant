import 'package:flutter/material.dart';
import 'package:swassistant/items/critical_injury.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/bottom.dart';

class CriticalInjuryEditDialog {
  final CriticalInjury criticalInjury;
  final Function(CriticalInjury) onClose;

  late Bottom bot;

  CriticalInjuryEditDialog({CriticalInjury? inj, required this.onClose}) :
    criticalInjury = inj == null ? CriticalInjury() : CriticalInjury.from(inj){
    var nameController = TextEditingController(text: criticalInjury.name);
    nameController.addListener(() {
      criticalInjury.name = nameController.text;
      bot.updateButtons();
    });
    var descController = TextEditingController(text: criticalInjury.desc);
    descController.addListener(() =>
      criticalInjury.desc = descController.text
    );
    bot = Bottom(
      buttons: (context) => [
        TextButton(
          onPressed: criticalInjury.name != "" ? (){
            Navigator.of(context).pop();
            onClose(criticalInjury);
          } : null,
          child: Text(MaterialLocalizations.of(context).saveButtonLabel),
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

  const _SeverityDropdown(this.criticalInjury, this.bot, {Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _SeverityState();
}

class _SeverityState extends State<_SeverityDropdown>{

  @override
  Widget build(BuildContext context) =>
    DropdownButton<int>(
      isDense: true,
      isExpanded: true,
      items: [
        DropdownMenuItem(
          value: 0,
          child: Text(AppLocalizations.of(context)!.severityLevel1)
        ),
        DropdownMenuItem(
          value: 1,
          child: Text(AppLocalizations.of(context)!.severityLevel2),
        ),
        DropdownMenuItem(
          value: 2,
          child: Text(AppLocalizations.of(context)!.severityLevel3),
        ),
        DropdownMenuItem(
          value: 3,
          child: Text(AppLocalizations.of(context)!.severityLevel4),
        ),
      ],
      onChanged: (value) {
        setState(() => widget.criticalInjury.severity = value ?? 0);
        widget.bot.updateButtons();
      },
      value: widget.criticalInjury.severity,
      onTap: () => FocusScope.of(context).unfocus(),
    );
}