import 'package:flutter/material.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class NameCard extends StatefulWidget {

  const NameCard({Key? key}) : super(key: key);
  
  @override
  State createState() => NameCardState();
}

class NameCardState extends State<NameCard> with StatefulCard {

  var edit = false;

  @override
  get defaultEdit {
    if(Editable.of(context) is Character){
      return Editable.of(context).name == AppLocalizations.of(context)!.newCharacter;
    }else if(Editable.of(context) is Minion){
      return Editable.of(context).name == AppLocalizations.of(context)!.newMinion;
    }
    return Editable.of(context).name == AppLocalizations.of(context)!.newVehicle;
  }

  @override
  set editing(bool b) => setState(() => edit = b);

  TextEditingController? nameController;

  @override
  Widget build(BuildContext context) {
    if(nameController == null){
      nameController = TextEditingController(text: Editable.of(context).name);
      nameController!.addListener(() => Editable.of(context).name = nameController!.text);
    }
    return EditingText(
      editing: edit,
      heroTag: Editable.of(context).uid,
      editableBackup: Editable.of(context),
      controller: nameController,
      style: Theme.of(context).textTheme.headlineSmall,
      textAlign: TextAlign.center,
      initialText: Editable.of(context).name,
      defaultSave: true,
      textCapitalization: TextCapitalization.words,
    );
  }
}