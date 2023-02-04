import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:in_app_purchase/in_app_purchase.dart';
import 'package:swassistant/ui/dialogs/gplay_donate.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:url_launcher/url_launcher_string.dart';

class DonateDialog extends StatelessWidget{

  const DonateDialog({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var scafMes = ScaffoldMessenger.of(context);
    var loc = AppLocalizations.of(context);
    return Column(
      children: [
        Text(AppLocalizations.of(context)!.donoOptions, style: Theme.of(context).textTheme.headlineSmall, textAlign: TextAlign.center,),
        if(!kIsWeb) Text(AppLocalizations.of(context)!.gPlay, style: Theme.of(context).textTheme.titleLarge),
        if(!kIsWeb) Text(AppLocalizations.of(context)!.gPlayDesc, textAlign: TextAlign.center,),
        Text(AppLocalizations.of(context)!.sponsors, style: Theme.of(context).textTheme.titleLarge),
        Text(AppLocalizations.of(context)!.sponsorsDesc, textAlign: TextAlign.center,),
        ButtonBar(
          children: [
            TextButton(
              onPressed: () {
                Navigator.pop(context);
                _launchURL("https://github.com/sponsors/CalebQ42");
              },
              child: Text(AppLocalizations.of(context)!.sponsors)
            ),
            if(!kIsWeb) TextButton(
              onPressed: () async{
                if (!await InAppPurchase.instance.isAvailable()){
                  scafMes.clearSnackBars();
                  scafMes.showSnackBar(SnackBar(
                    content: Text(loc!.gPlayUnavailable),
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
    if (!await launchUrlString(url)) throw 'Could not launch $url';
  }
}