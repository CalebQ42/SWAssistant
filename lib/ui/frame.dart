import 'package:flutter/material.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/sw.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

import 'package:swassistant/preferences.dart' as preferences;
import 'package:swassistant/ui/dialogs/destiny.dart';

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
  bool _hidden = true;
  String _selected = "";

  bool get hidden => _hidden;
  set selected(String s) {
    if(s != _selected) {
      _selected = s;
      if(s == "/intro" || s == "/loading") {
        _hidden = true;
      }else{
        _hidden = false;
      }
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
    }
    return const BeveledRectangleBorder(
      borderRadius: BorderRadius.horizontal(left: Radius.circular(25))
    );
  }
  BeveledRectangleBorder get topItemShape {
    if(thin){
      return const BeveledRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(25))
      );
    }
    return const BeveledRectangleBorder(
      borderRadius: BorderRadius.only(topLeft: Radius.circular(25))
    );
  }
  EdgeInsets get topItemPadding {
    if (thin) {
      return const EdgeInsets.symmetric(horizontal: 15);
    }
    return const EdgeInsets.only(left: 15);
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
    var height = MediaQuery.of(context).size.height - MediaQuery.of(context).viewPadding.top - MediaQuery.of(context).viewPadding.bottom;
    var width = MediaQuery.of(context).size.width - MediaQuery.of(context).viewPadding.left - MediaQuery.of(context).viewPadding.right;
    var overlayH = thin && !_hidden ? height - 50 : height;
    var overlayW = thin || _hidden ? width : width - 50;
    var tween = Tween<Offset>(
      begin: Offset.zero,
      end: Offset(
        thin ? 0 : 200 / overlayW,
        thin ? ((height*.5)-50) / overlayH : 0
      )
    );
    var title = "SWAssistant";
    if(!expanded && thin){
      switch(_selected){
        case "/characters":
          title = AppLocalizations.of(context)!.characters;
          break;
        case "/vehicles":
          title = AppLocalizations.of(context)!.vehicles;
          break;
        case "/minions":
          title = AppLocalizations.of(context)!.minions;
          break;
        case "/settings":
          title = AppLocalizations.of(context)!.settings;
          break;
        case "/trash":
          title = AppLocalizations.of(context)!.trash;
          break;
      }
    }
    var chillin = [
      Row(
        children: [
          Expanded(
            child: NavItem(
              title: AnimatedSwitcher(
                duration: const Duration(milliseconds: 300),
                child: Text(
                  title,
                  style: Theme.of(context).primaryTextTheme.subtitle1,
                  key: Key(title),
                ),
              ),
              icon: Icon(
                Icons.menu,
                color: Theme.of(context).primaryTextTheme.subtitle1?.color
              ),
              expanded: expanded,
              autoClose: false,
              onTap: hidden ? null : expand,
              // animate: false
            )
          ),
          if(thin) IconButton(
            onPressed: hidden ? null : () => SWDiceHolder().showDialog(context, showInstant: true),
            icon: Icon(
              Icons.casino_outlined,
              color: Theme.of(context).primaryTextTheme.subtitle1?.color
            )
          ),
          if(thin && _selected == "/gm") IconButton(
            onPressed: hidden ? null : () => DestinyDialog().show(context),
            icon: Icon(
              Icons.tonality,
              color: Theme.of(context).primaryTextTheme.subtitle1?.color
            )
          ),
        ]
      ),
      if((thin ? (height*.5) + 50 : height) >= 440) const Spacer(),
      NavItem(
        icon: Icon(
          Icons.contacts,
          color: Theme.of(context).primaryTextTheme.subtitle1?.color,
        ),
        title: Text(
          AppLocalizations.of(context)!.gmMode,
          style: Theme.of(context).primaryTextTheme.subtitle1
        ),
        onTap: hidden || _selected == "/gm" ? null : () => SW.of(context).nav()?.pushNamed("/gm"),
        expanded: expanded,
        selected: _selected == "/gm"
      ),
      NavItem(
        icon: Icon(
          Icons.face,
          color: Theme.of(context).primaryTextTheme.subtitle1?.color
        ),
        title: Text(
          AppLocalizations.of(context)!.characters,
          style: Theme.of(context).primaryTextTheme.subtitle1
        ),
        onTap: hidden || _selected == "/characters" ? null : () => SW.of(context).nav()?.pushNamed("/characters"),
        expanded: expanded,
        selected: _selected == "/characters"
      ),
      NavItem(
        icon: Icon(
          Icons.supervisor_account,
          color: Theme.of(context).primaryTextTheme.subtitle1?.color
        ),
        title: Text(
          AppLocalizations.of(context)!.minions,
          style: Theme.of(context).primaryTextTheme.subtitle1
        ),
        onTap: hidden || _selected == "/minions" ? null : () => SW.of(context).nav()?.pushNamed("/minions"),
        expanded: expanded,
        selected: _selected == "/minions"
      ),
      NavItem(
        icon: Icon(
          Icons.motorcycle,
          color: Theme.of(context).primaryTextTheme.subtitle1?.color
        ),
        title: Text(
          AppLocalizations.of(context)!.vehicles,
          style: Theme.of(context).primaryTextTheme.subtitle1
        ),
        onTap: hidden || _selected == "/vehicles" ? null : () => SW.of(context).nav()?.pushNamed("/vehicles"),
        expanded: expanded,
        selected: _selected == "/vehicles"
      ),
      if((thin ? (height*.5) + 50 : height) >= 440) const Spacer(),
      NavItem(
        icon: Icon(
          Icons.settings,
          color: Theme.of(context).primaryTextTheme.subtitle1?.color
        ),
        title: Text(
          AppLocalizations.of(context)!.settings,
          style: Theme.of(context).primaryTextTheme.subtitle1
        ),
        onTap: hidden || _selected == "settings" ? null : () => SW.of(context).nav()?.pushNamed("/settings"),
        expanded: expanded,
        selected: _selected == "/settings"
      ),
      NavItem(
        icon: Icon(
          Icons.delete,
          color: Theme.of(context).primaryTextTheme.subtitle1?.color
        ),
        title: Text(
          AppLocalizations.of(context)!.trash,
          style: Theme.of(context).primaryTextTheme.subtitle1
        ),
        onTap: hidden || _selected == "trash" ? null : () => SW.of(context).nav()?.pushNamed("/trash"),
        expanded: expanded,
        selected: _selected == "/trash"
      ),
      if(!thin) NavItem(
        icon: Icon(
          Icons.casino_outlined,
          color: Theme.of(context).primaryTextTheme.subtitle1?.color
        ),
        title: Text(
          AppLocalizations.of(context)!.dice,
          style: Theme.of(context).primaryTextTheme.subtitle1
        ),
        onTap: hidden ? null : () => SWDiceHolder().showDialog(context, showInstant: true),
        expanded: expanded,
      )
    ];
    var scrolPhys = (hidden || (thin && !expanded)) ? const NeverScrollableScrollPhysics() : const BouncingScrollPhysics();
    return Scaffold(
      backgroundColor: Theme.of(context).primaryColor,
      body: SafeArea(
        child: Stack(
          children: [
            Positioned(
              left: 0,
              top: 0,
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
              left: thin || hidden ? 0 : 50,
              top: thin && !hidden ? 50 : 0,
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
                        child: Padding(
                          padding: MediaQuery.of(context).viewInsets,
                          child: widget.child ?? Container(),
                        )
                      ),
                      margin: EdgeInsets.zero,
                      clipBehavior: Clip.hardEdge,
                    ),
                    if(expanded) GestureDetector(
                      child: Container(
                        decoration: ShapeDecoration(
                          color: Theme.of(context).brightness == Brightness.dark && SW.of(context).getPreference(preferences.amoled, false) ?
                              Colors.black.withOpacity(.75) : Colors.black.withOpacity(0.25),
                          shape: shape
                        )
                      ),
                      onTap: expanded ? expand : null,
                      behavior: expanded ? HitTestBehavior.opaque : HitTestBehavior.translucent,
                    )
                  ]
                ),
              )
            )
          ]
        )
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
            Padding(
              padding: const EdgeInsets.all(15),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                mainAxisSize: MainAxisSize.min,
                children: [
                  icon,
                  AnimatedSize(
                    duration: const Duration(milliseconds: 300),
                    child: AnimatedSwitcher(
                      transitionBuilder: (wid, anim) =>
                        ClipRect(
                          child: SlideTransition(
                            position: Tween(begin: const Offset(0.0,1.0), end: Offset.zero).animate(anim),
                            child: Center(child: wid)
                          )
                        ),
                      layoutBuilder: (child, oldStack){
                        List<Widget> newStack = [];
                        for(var chil in oldStack){
                          newStack.add(
                            SizedOverflowBox(
                              size: Size.zero,
                              child: chil
                            )
                          );
                        }
                        return Stack(
                          alignment: AlignmentDirectional.center,
                          children:[
                            ...newStack,
                            child!,
                          ]
                        );
                      },
                      duration: const Duration(milliseconds: 300),
                      child: selected ? Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          const SizedBox(height: 5),
                          Container(width: 10, height: 2, color: Theme.of(context).primaryTextTheme.bodyText1?.color),
                        ],
                      ) : Container(),
                    )
                  )
                ],
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