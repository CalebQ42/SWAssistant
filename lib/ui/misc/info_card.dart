import 'package:flutter/material.dart';
import 'package:swassistant/sw.dart';

class InfoCard extends StatefulWidget {
  final String title;
  final bool shown;
  final Widget contents;
  final void Function(bool)? onShowChanged;

  const InfoCard(
      {super.key,
      this.shown = true,
      required this.contents,
      this.title = "",
      this.onShowChanged});

  @override
  State<StatefulWidget> createState() => _InfoCardState();
}

class _InfoCardState extends State<InfoCard> {
  late bool shown;

  @override
  void initState() {
    super.initState();
    shown = widget.shown;
  }

  @override
  Widget build(BuildContext context) {
    var app = SW.of(context);
    return ExpansionTile(
      childrenPadding: const EdgeInsets.all(10),
      expansionAnimationStyle: AnimationStyle(
        duration: app.globalDuration,
        reverseDuration: app.globalDuration,
      ),
      title: AnimatedAlign(
        curve: Curves.easeOutBack,
        duration: app.globalDuration,
        alignment: shown ? Alignment.center : Alignment.centerLeft,
        child: AnimatedDefaultTextStyle(
          duration: app.globalDuration,
          style: shown
              ? Theme.of(context)
                  .textTheme
                  .titleMedium!
                  .copyWith(color: Theme.of(context).colorScheme.primary)
              : Theme.of(context).textTheme.titleMedium!,
          child: Text(
            widget.title,
          ),
        ),
      ),
      expandedAlignment: Alignment.topCenter,
      onExpansionChanged: (b) {
        setState(() {
          shown = b;
        });
        if (widget.onShowChanged != null) widget.onShowChanged!(b);
      },
      initiallyExpanded: shown,
      children: [widget.contents],
    );
  }
}
