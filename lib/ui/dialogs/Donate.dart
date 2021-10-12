import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class DonateDialog extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text(AppLocalizations.of(context)!.donoOptions, style: Theme.of(context).textTheme.headline5, textAlign: TextAlign.center,),
        Text(AppLocalizations.of(context)!.gPlay, style: Theme.of(context).textTheme.headline6),
        Text(AppLocalizations.of(context)!.gPlayDesc, textAlign: TextAlign.center,),
        Text(AppLocalizations.of(context)!.sponsors, style: Theme.of(context).textTheme.headline6),
        Text(AppLocalizations.of(context)!.sponsorsDesc, textAlign: TextAlign.center,),
        Row(
          children: [
            TextButton(
              onPressed: () =>
                _launchURL("https://github.com/sponsors/CalebQ42"),
              child: Text(AppLocalizations.of(context)!.sponsors)
            ),
            TextButton(
              onPressed: (){
                //TODO: donate amount dialog
              },
              child: Text(AppLocalizations.of(context)!.gPlay),
            )
          ],
        )
      ]
    );
  }

  static void _launchURL(String url) async {
    if (await canLaunch(url))
      await launch(url);
  }
}