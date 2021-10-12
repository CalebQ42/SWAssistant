import 'package:flutter/material.dart';
import 'package:swassistant/items/CriticalInjury.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class CriticalInjuryEditDialog extends StatefulWidget{
  final CriticalInjury criticalInjury;
  final Function(CriticalInjury) onClose;

  CriticalInjuryEditDialog({CriticalInjury? criticalInjury, required this.onClose}) :
    this.criticalInjury = criticalInjury == null ? CriticalInjury() : CriticalInjury.from(criticalInjury);
  
  @override
  State<StatefulWidget> createState() => _CriticalInjuryEditState(criticalInjury: criticalInjury, onClose: onClose);
  
  void show(BuildContext context) =>
    showModalBottomSheet(
      context: context,
      builder: (context) =>
        this
    );
}

class _CriticalInjuryEditState extends State{
  final CriticalInjury criticalInjury;
  final Function(CriticalInjury) onClose;

  late TextEditingController nameController;
  late TextEditingController descController;

  _CriticalInjuryEditState({required this.criticalInjury, required this.onClose}){
      nameController = TextEditingController(text: criticalInjury.name)
          ..addListener(() =>
            setState(() => criticalInjury.name = nameController.text)
          );
      descController = TextEditingController(text: criticalInjury.desc)
          ..addListener(() =>
            criticalInjury.desc = descController.text
          );
    }

  @override
  Widget build(BuildContext context) {
    return DropdownButtonHideUnderline(
      child: Padding(
        padding: MediaQuery.of(context).viewInsets.add(EdgeInsets.only(left: 15, right: 15)),
        child: Wrap(
          children: [
            Container(height: 15),
            TextField(
              textCapitalization: TextCapitalization.words,
              controller: nameController,
              decoration: InputDecoration(
                    labelText: AppLocalizations.of(context)!.injury,
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
              child: DropdownButton<int>(
                isDense: true,
                isExpanded: true,
                items: [
                  DropdownMenuItem(
                    child: Text(AppLocalizations.of(context)!.severityLevel1),
                    value: 0,
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
                onChanged: (value) =>
                  setState(() => criticalInjury.severity = value ?? 0),
                value: criticalInjury.severity,
                onTap: () => FocusScope.of(context).unfocus(),
              )
            ),
            ButtonBar(
              children: [
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
                )
              ],
            )
          ],
        )
      )
    );
  }
}