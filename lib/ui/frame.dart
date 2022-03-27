import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:swassistant/dice/swdice_holder.dart';
import 'package:swassistant/ui/screens/dice_roller.dart';

class Frame extends StatefulWidget {

  const Frame({Key? key}) : super(key: key);

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
    print(250/(MediaQuery.of(context).size.width - 50));
    var tween = Tween<Offset>(begin: Offset.zero, end: Offset(thin ? 0 : 250/(MediaQuery.of(context).size.width - 50), thin ? .75 : 0));
    return Scaffold(
      backgroundColor: Theme.of(context).primaryColor,
      body: Stack(
        children: [
          Positioned(
            height: thin ? MediaQuery.of(context).size.height*.75 : MediaQuery.of(context).size.height,
            width: thin ? MediaQuery.of(context).size.width : 250,
            child: Column(
              children: [
                Row(
                  children: [
                    Expanded(child: NavItem(
                      title: Text("SWAssistant"),
                      icon: Icon(Icons.menu),
                      onTap: () {
                        setState(() => expanded = !expanded);
                        if(expanded){
                          animCont.forward();
                        }else{
                          animCont.reverse();
                        }
                      },
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
                  onTap: () => print("TODO"),
                ),
                Spacer(),
                NavItem(
                  icon: Icon(Icons.casino_outlined),
                  title: Text("Dice"),
                  onTap: () => SWDiceHolder().showDialog(context, showInstant: true),
                )
              ]
            )
          ),
          SlideTransition(
            position: tween.animate(animCont),
            child: Stack(
              children: [
                Card(
                  key: Key("hi"),
                  elevation: 10,
                  shape: shape,
                  child: Center(
                    child: Text("YOO")
                  ),
                  margin: EdgeInsets.zero
                ),
                if(expanded) GestureDetector(
                  child: Container(
                    decoration: ShapeDecoration(
                      shape: shape,
                      color: Colors.black.withOpacity(0.25)
                    ),
                  ),
                  onTap: () => setState(() => expanded = !expanded),
                ),
              ]
            ),
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
    );
  }
}

class NavItem extends StatelessWidget{

  final Widget title;
  final Icon icon;
  final void Function() onTap;

  const NavItem({Key? key, required this.title, required this.icon, required this.onTap}) : super(key: key);

  @override
  Widget build(BuildContext context) => ListTile(
    title: title,
    leading: icon,
    onTap: onTap,
    contentPadding: EdgeInsets.only(left: 15),
  );
}