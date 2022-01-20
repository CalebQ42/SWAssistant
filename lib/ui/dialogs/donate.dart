import 'package:flutter/material.dart';
import 'package:in_app_purchase/in_app_purchase.dart';
import 'package:swassistant/ui/dialogs/gplay_donate.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class DonateDialog extends StatelessWidget{

  const DonateDialog({Key? key}) : super(key: key);

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
              onPressed: () {
                Navigator.pop(context);
                _launchURL("https://github.com/sponsors/CalebQ42");
              },
              child: Text(AppLocalizations.of(context)!.sponsors)
            ),
            TextButton(
              onPressed: () async{
                if (!await InAppPurchase.instance.isAvailable()){
                  ScaffoldMessenger.of(context).clearSnackBars();
                  ScaffoldMessenger.of(context).showSnackBar(SnackBar(
                    content: Text(AppLocalizations.of(context)!.gPlayUnavailable),
                  ));
                }else{
                  InAppPurchase.instance.queryProductDetails({
                    "donate1",
                    "donate5",
                    "donate10",
                    "donate20",
                  }).then((value) {
                    Navigator.of(context).pop();
                    GPlayDonateDialog(value.productDetails).show(context);
                  });
                }
              },
              child: Text(AppLocalizations.of(context)!.gPlay),
            )
          ],
        )
      ]
    );
  }

  static void _launchURL(String url) async {
    if (!await launch(url)) throw 'Could not launch $url';
  }
}