import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:in_app_purchase/in_app_purchase.dart';
import 'package:darkstorm_common/bottom.dart';
import 'package:swassistant/sw.dart';

class GPlayDonateDialog extends StatefulWidget{

  final List<ProductDetails> prods;
  final indHolder = _IndHolder();

  GPlayDonateDialog(this.prods, {Key? key}) : super(key: key){
    prods.sort((one,two) =>
      (100*(one.rawPrice - two.rawPrice)).floor()
    );
  }

  @override
  State<StatefulWidget> createState() => _GPlayDonateState();

  void show(BuildContext context) {
    var bot = Bottom(
      buttons: (co) => [
        TextButton(
          onPressed: indHolder.index == null ? null : () {
            InAppPurchase.instance.buyConsumable(
              purchaseParam: PurchaseParam(productDetails: prods[indHolder.index!])
            );
            Navigator.of(co).pop();
          },
          child: Text(AppLocalizations.of(co)!.gPlayPurchase)
        ),
        TextButton(
          onPressed: () => Navigator.pop(co),
          child: Text(MaterialLocalizations.of(co).cancelButtonLabel),
        )
      ],
      child: (c) => this,
    );
    indHolder.bot = bot;
    bot.show(context);
  }
}

class _GPlayDonateState extends State<GPlayDonateDialog>{

  _GPlayDonateState();

  @override
  Widget build(BuildContext context) =>
    Wrap(
      children: List.generate(widget.prods.length,
        (index) =>
          RadioListTile<int>(
            value: index,
            groupValue: widget.indHolder.index,
            onChanged: (i) => setState((){
              widget.indHolder.index = i;
              widget.indHolder.bot?.updateButtons();
            }),
            title: Text(SW.of(context).locale.gPlayDonate(widget.prods[index].price)),
          )
      ),
    );
}

class _IndHolder {
  int? index;
  Bottom? bot;
}