import 'package:flutter/material.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/sw.dart';
import 'package:swassistant/ui/screens/editable_list.dart';
import 'package:swassistant/ui/screens/settings.dart';

class Frame extends StatefulWidget {

  final Widget? child;

  const Frame({Key? key, this.child}) : super(key: key);

  @override
  State<StatefulWidget> createState() => FrameState();
}

class FrameState extends State<Frame> with SingleTickerProviderStateMixin {

  bool expanded = false;
  late AnimationController animCont;

  @override
  void initState() {
    animCont = AnimationController(vsync: this, duration: const Duration(milliseconds: 300));
    super.initState();
  }

  void expand(){
    setState(() {
      expanded = !expanded;
      if(expanded){
        animCont.forward();
      }else{
        animCont.reverse();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    var thin = MediaQuery.of(context).size.height > MediaQuery.of(context).size.width;
    ShapeBorder shape;
    if(thin){
      shape = const BeveledRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(25))
      );
    }else{
      shape = const BeveledRectangleBorder(
        borderRadius: BorderRadius.horizontal(left: Radius.circular(25))
      );
    }
    var overlayH = thin ? MediaQuery.of(context).size.height - 50 : MediaQuery.of(context).size.height;
    var overlayW = thin ? MediaQuery.of(context).size.width : MediaQuery.of(context).size.width - 50;
    var tween = Tween<Offset>(
      begin: Offset.zero,
      end: Offset(
        thin ? 0 : 200 / overlayW,
        thin ? ((MediaQuery.of(context).size.height*.5)-50) / overlayH : 0
      )
    );
    return Navigator(
      onGenerateRoute: (rs) => MaterialPageRoute(
        builder: (c) => Scaffold(
          backgroundColor: Theme.of(context).primaryColor,
          body: Stack(
            children: [
              Positioned(
                height: thin ? MediaQuery.of(context).size.height*.5 : MediaQuery.of(context).size.height,
                width: thin ? MediaQuery.of(context).size.width : 250,
                child: Column(
                  children: [
                    Row(
                      children: [
                        Expanded(child: NavItem(
                          title: Text("SWAssistant"),
                          icon: Icon(Icons.menu),
                          onTap: expand,
                          expanded: expanded,
                        )),
                        if(thin) IconButton(
                          onPressed: () => SWDiceHolder().showDialog(context, showInstant: true),
                          icon: Icon(Icons.casino_outlined)
                        )
                      ]
                    ),
                    Spacer(),
                    NavItem(
                      icon: Icon(Icons.person),
                      title: Text("Profile"),
                      onTap: () => SW.of(context).nav()?.pushNamed("/characters"),
                          expanded: expanded,
                    ),
                    Spacer(),
                    AnimatedSwitcher(
                      duration: const Duration(milliseconds: 300),
                      child: (expanded) ?
                        NavItem( //TODO: Slide transition
                          icon: Icon(Icons.settings),
                          title: Text("Settings"),
                          onTap: () => SW.of(context).nav()?.pushNamed("/settings"),
                          expanded: expanded,
                        ) : Container(height: 50),
                    ),
                    NavItem(
                      icon: Icon(Icons.casino_outlined),
                      title: Text("Dice"),
                      onTap: () => SWDiceHolder().showDialog(context, showInstant: true),
                          expanded: expanded,
                    )
                  ]
                )
              ),
              AnimatedPositioned(
                duration: Duration.zero,
                left: thin ? 0 : 50,
                top: thin ? 50 : 0,
                height: overlayH,
                width: overlayW,
                child: SlideTransition(
                  key: UniqueKey(),
                  position: tween.animate(animCont),
                  child: Stack(
                    children: [
                      Card(
                        elevation: 10,
                        shape: shape,
                        child: widget.child,
                        margin: EdgeInsets.zero
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
              // AnimatedBuilder(
              //   animation: valueNotifier,
              //   builder: (context, wid) => 
              //     SlideTransition(position: position)
              //   child: 
              // ),
              // AnimatedPositioned(
              //   left: !thin ? expanded ? 250 : 50 : null,
              //   top: thin ? expanded ? MediaQuery.of(context).size.height*0.75 : 50 : null,
              //   height: overlayH,
              //   width: overlayW,
              //   child: OverflowBox(
              //     maxHeight: overlayH,
              //     maxWidth: overlayW,
              //     child: Align(
              //       alignment: Alignment.topLeft,
              //       child: Stack(
              //         children: [
              //           Card(
              //             key: Key("hi"),
              //             elevation: 10,
              //             shape: shape,
              //             child: Center(
              //               child: Text("YOO")
              //             ),
              //             margin: EdgeInsets.zero
              //           ),
              //           if(expanded) GestureDetector(
              //             child: Container(
              //               decoration: ShapeDecoration(
              //                 shape: shape,
              //                 color: Colors.black.withOpacity(0.25)
              //               ),
              //             ),
              //             onTap: () => setState(() => expanded = !expanded),
              //           ),
              //         ]
              //       )
              //     )
              //   ),
              //   duration: const Duration(milliseconds: 300)
              // )
            ]
          )
        )
      )
    );
  }
}

class NavItem extends StatelessWidget{

  final Widget title;
  final Icon icon;
  final void Function() onTap;
  final bool expanded;

  const NavItem({Key? key, required this.title, required this.icon, required this.onTap, required this.expanded}) : super(key: key);

  @override
  Widget build(BuildContext context) =>
    InkResponse(
      containedInkWell: true,
      highlightShape: BoxShape.rectangle,
      onTap: onTap,
      child: AnimatedAlign(
        duration: const Duration(milliseconds: 300),
        alignment: expanded ? Alignment.center : Alignment.centerLeft,
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Padding(
              padding: EdgeInsets.all(15),
              child: icon
            ),
            Align(
              child: title,
              alignment: Alignment.centerLeft,
            )
          ]
        )
      )
    );
}