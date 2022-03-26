import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';

class Frame extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => FrameState();
}

class FrameState extends State<Frame>{

  bool expanded = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Container(
            color: Theme.of(context).primaryColor,
            child: Column(
              children: [
                NavItem(
                  title: "SWAssistant",
                  icon: Icon(Icons.menu),
                  onTap: () => setState(() => expanded = !expanded),
                )
              ]
            )
          ),
          AnimatedPositioned(
            left: expanded ? 250 : 50,
            height: MediaQuery.of(context).size.height,
            width: MediaQuery.of(context).size.width - 50,
            child: OverflowBox(
              maxHeight: MediaQuery.of(context).size.height,
              maxWidth: MediaQuery.of(context).size.width - 50,
              child: Align(
                alignment: Alignment.topLeft,
                child: Stack(
                  children: [
                    Card(
                      key: Key("hi"),
                      elevation: 10,
                      shape: BeveledRectangleBorder(
                        borderRadius: BorderRadius.horizontal(left: Radius.circular(25))
                      ),
                      child: Center(
                        child: Text("YOO")
                      ),
                      margin: EdgeInsets.zero
                    ),
                    if(expanded) GestureDetector(
                      // behavior: HitTestBehavior.translucent,
                      child: Container(
                        decoration: ShapeDecoration(
                          shape: BeveledRectangleBorder(
                            borderRadius: BorderRadius.horizontal(left: Radius.circular(25))
                          ),
                          color: Colors.black.withOpacity(0.25)
                        ),
                      ),
                      onTap: () => setState(() => expanded = !expanded),
                    ),
                  ]
                )
              )
            ),
            duration: const Duration(milliseconds: 300)
          )
        ]
      )
    );
  }
}

class NavItem extends StatelessWidget{

  final String title;
  final Icon icon;
  final void Function() onTap;

  NavItem({required this.title, required this.icon, required this.onTap});

  @override
  Widget build(BuildContext context) => ListTile(
    title: Text(title),
    leading: icon,
    onTap: onTap,
    contentPadding: EdgeInsets.only(left: 15),
  );
}