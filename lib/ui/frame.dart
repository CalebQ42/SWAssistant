import 'package:flutter/material.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/sw.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class Frame extends StatefulWidget {

  final Widget? child;

  const Frame({Key? key, this.child}) : super(key: key);

  @override
  State<StatefulWidget> createState() => FrameState();

  static FrameState of(BuildContext context) => context.findAncestorStateOfType<FrameState>()!;
}

class FrameState extends State<Frame> with SingleTickerProviderStateMixin {

  bool expanded = false;
  late AnimationController animCont;
  ScrollController scrol = ScrollController();
  bool _hidden = false;
  String _selected = "";

  bool get hidden => _hidden;
  set hidden(bool b) {
    if(b != _hidden) {
      _hidden = b;
      WidgetsBinding.instance?.addPostFrameCallback((_) {
        setState(() {});
      });
    }
  }
  set selected(String s) {
    if(s != _selected) {
      _selected = s;
      WidgetsBinding.instance?.addPostFrameCallback((_) {
        setState(() {});
      });
    }
  }

  bool get thin => MediaQuery.of(context).size.height > MediaQuery.of(context).size.width - 50;
  BeveledRectangleBorder get shape {
    if(thin){
      return const BeveledRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(25))
      );
    }else{
      return const BeveledRectangleBorder(
        borderRadius: BorderRadius.horizontal(left: Radius.circular(25))
      );
    }
  }

  @override
  void initState() {
    animCont = AnimationController(vsync: this, duration: const Duration(milliseconds: 300));
    super.initState();
  }

  void expand(){
    setState(() {
      expanded = !expanded;
      if(scrol.hasClients){
        scrol.animateTo(0, duration: const Duration(milliseconds: 100), curve: Curves.linear);
      }
      if(expanded){
        animCont.forward();
      }else{
        animCont.reverse();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    var height = MediaQuery.of(context).size.height - MediaQuery.of(context).padding.top - MediaQuery.of(context).padding.bottom;
    var width = MediaQuery.of(context).size.width - MediaQuery.of(context).padding.left - MediaQuery.of(context).padding.right;
    var overlayH = thin ? height - 50 : height;
    var overlayW = thin ? width : width - 50;
    var tween = Tween<Offset>(
      begin: Offset.zero,
      end: Offset(
        thin ? 0 : 200 / overlayW,
        thin ? ((height*.5)-50) / overlayH : 0
      )
    );
    var chillin = [
      Row(
        children: [
          Expanded(child: NavItem(
            title: const Text("SWAssistant"),
            icon: const Icon(Icons.menu),
            expanded: expanded,
            autoClose: false,
            onTap: hidden ? null : expand,
            // animate: false
          )),
          if(thin) IconButton(
            onPressed: hidden ? null : () => SWDiceHolder().showDialog(context, showInstant: true),
            icon: const Icon(Icons.casino_outlined)
          )
        ]
      ),
      if((thin ? (height*.5) + 50 : height) >= 440) const Spacer(),
      NavItem(
        icon: const Icon(Icons.contacts),
        title: Text(AppLocalizations.of(context)!.gmMode),
        onTap: hidden ? null : () => SW.of(context).nav()?.pushNamed("/gm"),
        expanded: expanded,
        selected: _selected == "/gm"
      ),
      NavItem(
        icon: const Icon(Icons.face),
        title: Text(AppLocalizations.of(context)!.characters),
        onTap: hidden ? null : () => SW.of(context).nav()?.pushNamed("/characters"),
        expanded: expanded,
        selected: _selected == "/characters"
      ),
      NavItem(
        icon: const Icon(Icons.supervisor_account),
        title: Text(AppLocalizations.of(context)!.minions),
        onTap: hidden ? null : () => SW.of(context).nav()?.pushNamed("/minions"),
        expanded: expanded,
        selected: _selected == "/minions"
      ),
      NavItem(
        icon: const Icon(Icons.motorcycle),
        title: Text(AppLocalizations.of(context)!.vehicles),
        onTap: hidden ? null : () => SW.of(context).nav()?.pushNamed("/vehicles"),
        expanded: expanded,
        selected: _selected == "/vehicles"
      ),
      if((thin ? (height*.5) + 50 : height) >= 440) const Spacer(),
      NavItem(
        icon: const Icon(Icons.settings),
        title: Text(AppLocalizations.of(context)!.settings),
        onTap: hidden ? null : () => SW.of(context).nav()?.pushNamed("/settings"),
        expanded: expanded,
        selected: _selected == "/settings"
      ),
      NavItem(
        icon: const Icon(Icons.delete),
        title: Text(AppLocalizations.of(context)!.trash),
        onTap: hidden ? null : () => SW.of(context).nav()?.pushNamed("/trash"),
        expanded: expanded,
        selected: _selected == "/trash"
      ),
      if(!thin) NavItem(
        icon: const Icon(Icons.casino_outlined),
        title: Text(AppLocalizations.of(context)!.dice),
        onTap: hidden ? null : () => SWDiceHolder().showDialog(context, showInstant: true),
        expanded: expanded,
      )
    ];
    var scrolPhys = (hidden || (thin && !expanded)) ? const NeverScrollableScrollPhysics() : const BouncingScrollPhysics();
    return Scaffold(
      backgroundColor: Theme.of(context).primaryColor,
      body: Stack(
        children: [
          Positioned(
            left: MediaQuery.of(context).padding.left,
            top: MediaQuery.of(context).padding.top,
            height: thin ? height*.5 : height,
            width: thin ? width : 250,
            child: SizedOverflowBox(
              size: Size(
                thin ? width : 250,
                thin ? height*.5 : height
              ),
              child: ((thin ? (height*.5) + 50 : height) < 440) ? ListView(
                controller: scrol,
                children: chillin,
                physics: scrolPhys,
              ) : Column(
                children: chillin
              )
            )
          ),
          AnimatedPositioned(
            duration: const Duration(milliseconds: 300),
            left: hidden ? MediaQuery.of(context).padding.left : (thin ? 0 : 50) + MediaQuery.of(context).padding.left,
            top: hidden ? MediaQuery.of(context).padding.top : (thin ? 50 : 0) + MediaQuery.of(context).padding.top,
            height: hidden ? height : overlayH,
            width: hidden ? width : overlayW,
            child: SlideTransition(
              key: UniqueKey(),
              position: tween.animate(animCont),
              child: Stack(
                children: [
                  Card(
                    elevation: 10,
                    shape: shape,
                    child: MediaQuery(
                      data: MediaQueryData(
                        size: Size(overlayW, overlayH)
                      ),
                      child: widget.child ?? Container(),
                    ),
                    margin: EdgeInsets.zero,
                    clipBehavior: Clip.hardEdge,
                  ),
                  AnimatedSwitcher(
                    child: expanded ? GestureDetector(
                      child: Container(
                        decoration: ShapeDecoration(
                          color: Colors.black.withOpacity(0.25),
                          shape: shape
                        )
                      ),
                      onTap: expanded ? expand : null,
                      behavior: expanded ? HitTestBehavior.opaque : HitTestBehavior.translucent,
                    ) : null,
                    duration: const Duration(milliseconds: 300),
                  )
                ]
              ),
            )
          )
        ]
      )
    );
  }
}

class NavItem extends StatelessWidget{

  final Widget title;
  final Icon icon;
  final void Function()? onTap;
  final bool expanded;
  final bool autoClose;
  final bool animate;
  final bool selected;

  const NavItem({Key? key, this.animate = true, this.autoClose = true, this.selected = false, required this.title, required this.icon, this.onTap, required this.expanded}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    InkResponse(
      containedInkWell: true,
      highlightShape: BoxShape.rectangle,
      onTap: () {
        if(autoClose && Frame.of(context).expanded) Frame.of(context).expand();
        if(onTap != null) onTap!();
      },
      child: AnimatedAlign(
        duration: const Duration(milliseconds: 300),
        alignment: (animate && expanded) ? Alignment.center : Alignment.centerLeft,
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            AnimatedSwitcher(
              duration: const Duration(milliseconds: 200),
              child: Padding(
                padding: const EdgeInsets.all(15),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    icon,
                    if(selected) const SizedBox(height: 5),
                    if(selected) Container(width: 10, height: 2, color: Colors.white),
                  ],
                )
              )
            ),
            AnimatedSwitcher(
              duration: const Duration(milliseconds: 300),
              child: Frame.of(context).thin || expanded ? Align(
                child: title,
                alignment: Alignment.centerLeft,
              ) : Container()
            )
          ]
        )
      )
    );
}