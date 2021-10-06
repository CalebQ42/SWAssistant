import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

class DonateDialog extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text("There are two options for donations: Github Sponsors or Google Play.", textAlign: TextAlign.center,),
        Text("Google Play:", style: Theme.of(context).textTheme.headline6),
        Text("Easy to use with support for most currencies, but donations are capped and Google takes a 15% cut", textAlign: TextAlign.center,),
        Text("Github Sponsors:", style: Theme.of(context).textTheme.headline6),
        Text("You to donate either one time or on a monthly basis, with custom amounts, AND 100% of your money goes to the developer", textAlign: TextAlign.center,),
        Row(
          children: [
            TextButton(
              onPressed: () =>
                _launchURL("https://github.com/sponsors/CalebQ42"),
              child: Text("Github Sponsors")
            ),
            TextButton(
              onPressed: (){
                //TODO: donate amount dialog
              },
              child: Text("Google Play"),
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