

import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:swassistant/sw.dart';

class Loading extends StatelessWidget{

  final RouteSettings afterLoad;

  const Loading({Key? key, required this.afterLoad}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    SW.of(context).postInit().then((_){
      Navigator.pushNamedAndRemoveUntil(context, afterLoad.name ?? "", (route) => false, arguments: afterLoad.arguments);
    });
    return WillPopScope(
      onWillPop: () => Future.value(false),
      child: Scaffold(
        body: Column(
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const CircularProgressIndicator(),
            Container(height: 10),
            Text(
              AppLocalizations.of(context)!.loadingDialog,
              style: Theme.of(context).textTheme.displaySmall,
              textAlign: TextAlign.center,
            )
          ],
        )
      )
    );
  }
}