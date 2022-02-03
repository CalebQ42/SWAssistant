import 'package:flutter/material.dart';

class SWAppBar extends AppBar{

  SWAppBar(BuildContext context, {Key? key, Widget? title, List<Widget>? additionalActions, List<PopupMenuItem>? additionalPopupActions,
      Map<String, Function>? popupFunctions, PreferredSizeWidget? bottom, required Color backgroundColor}) : 
        super(key: key, backgroundColor: backgroundColor, title: title, actions: _getActions(context, additionalActions, additionalPopupActions, popupFunctions), bottom: bottom);

  static List<Widget> _getActions(BuildContext context, List<Widget>? additionalActions, List<PopupMenuItem>? additionalPopupActions, Map<String, Function>? popupFunctions) =>
    <Widget>[...?additionalActions, _getPopupMenu(context, additionalPopupActions ?? [], popupFunctions ?? {})];
  
  static PopupMenuButton _getPopupMenu(BuildContext context, List<PopupMenuItem> additionalPopupActions, Map<String, Function> popupFunctions){
    return PopupMenuButton(
      itemBuilder: (context) => additionalPopupActions,
      onSelected:(t) {
        if(popupFunctions[t] != null){
          popupFunctions[t]!();
        }
      }
    );
  }
}
