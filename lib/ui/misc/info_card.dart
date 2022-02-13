import 'package:flutter/material.dart';
import 'package:swassistant/profiles/character.dart';
import 'package:swassistant/profiles/minion.dart';
import 'package:swassistant/profiles/vehicle.dart';
import 'package:swassistant/profiles/utils/editable.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/ui/misc/edit_content.dart';
import 'package:swassistant/ui/misc/editing_text.dart';

class InfoCard extends StatefulWidget{
  
  final String title;
  final bool shown;
  final Widget contents;
  final void Function(bool)? onShowChanged;

  const InfoCard({Key? key, this.shown = true, required this.contents, this.title = "", this.onShowChanged}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _InfoCardState();
}

class _InfoCardState extends State<InfoCard>{

  late bool shown;

  @override
  void initState() {
    super.initState();
    shown = widget.shown;
  }

  @override
  Widget build(BuildContext context) =>
    ExpansionTile(
      childrenPadding: const EdgeInsets.all(10),
      title: AnimatedAlign(
        curve: Curves.easeOutBack,
        duration: const Duration(milliseconds: 300),
        alignment: shown ? Alignment.center : Alignment.centerLeft,
        child: AnimatedDefaultTextStyle(
          duration: const Duration(milliseconds: 300),
          child: Text(widget.title,),
          style: shown ? Theme.of(context).textTheme.subtitle1!.copyWith(color: Theme.of(context).primaryColor) : Theme.of(context).textTheme.subtitle1!,
        )
      ),
      children: [widget.contents],
      expandedAlignment: Alignment.topCenter,
      onExpansionChanged: (b){
        setState((){
          shown = b;
        });
        if(widget.onShowChanged != null) widget.onShowChanged!(b);
      },
      initiallyExpanded: shown,
    );
}