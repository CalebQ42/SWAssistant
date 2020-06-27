import 'package:flutter/material.dart';
import 'package:swassistant/SW.dart';

import 'package:swassistant/Preferences.dart' as Preferences;
import 'package:swassistant/dice/Sides.dart';

class DiceResults{
  int _number = 0;
  List<Result> _reses = new List<Result>();
  var subtractMode = false;
  var problem = false;
  var resList = new List();
  var diceNames = new List();

  void addSimpleSide(SimpleSide ss, String diceName){
    diceNames.add(diceName);
    resList.add(ss);
    if(ss.isInt()){
      _number += subtractMode ? -ss.intSide() : ss.intSide();
    }else if(ss.stringSide() !=""){
      if(has(ss.stringSide()))
        _reses[indexOf(ss.stringSide())].value += subtractMode ? -1 : 1;
      else
        _reses.add(Result(ss.stringSide(), subtractMode ? -1: 1));
    }
  }
  void addComplexSide(ComplexSide cs, String diceName){
    diceNames.add(diceName);
    resList.add(cs);
    if(cs.number != null)
      _number += subtractMode ? -cs.number : cs.number;
    for(ComplexSidePart csp in cs.parts){
      if(has(csp.name))
        _reses[indexOf(csp.name)].value += subtractMode ? -csp.value : csp.value;
      else
        _reses.add(Result(csp.name, subtractMode ? -csp.value: csp.value));
    }
  }
  bool has(String name) => _reses.any((r)=>r.name == name);
  int indexOf(String name){
    var out = _reses.indexWhere((res)=>res.name == name);
    out ??=-1;
    return out;
  }
  int size() => _reses.length;
  void set(String name, int value)=>_reses.firstWhere((res)=>res.name==name,orElse: ()=>null)?.value = value;
  int get(String name){
    var out = _reses.firstWhere((res)=>res.name==name,orElse: ()=>null)?.value;
    out ??=0;
    return out;
  }
  int getNum() => _number;
  void combineWith(DiceResults dr){
    for(int i = 0; i<dr.resList.length;i++){
      if(dr.resList[i] is SimpleSide)
        addSimpleSide(dr.resList[i], dr.diceNames[i]);
      else if(dr.resList[i] is ComplexSide)
        addComplexSide(dr.resList[i], dr.diceNames[i]);
    }
  }
  bool isNumOnly() => _reses.length == 0;
  bool hasNumRes() => resList.any((element){
    return (element is int) || (element is ComplexSide && element.number != null) ? true : false;
  });
  void showResultDialog({SW app, BuildContext context, String problemMessage = ""}){
    if(app.prefs.getBool(Preferences.dice)!=null && app.prefs.getBool(Preferences.dice))
      showIndividualDialog(context);
    else
      showCombinedDialog(context);
  }
  void showCombinedDialog(BuildContext bc){
    var children = new List<Widget>();
    if(hasNumRes()){
      children.add(
        new Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            new Text("Number:"),
            new Text(
              _number.toString(),
              style: Theme.of(bc).textTheme.headline6.copyWith(fontWeight: FontWeight.bold),
            )
          ],
        )
      );
    }
    for(var r in _reses){
      children.add(new Row(
        mainAxisSize: MainAxisSize.max,
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          new Text(r.name,
            textAlign: TextAlign.start,
          ),
          new Text(r.value.toString(),
            textAlign: TextAlign.end,
            style: Theme.of(bc).textTheme.headline6.copyWith(fontWeight: FontWeight.bold)
          )
        ],
      ));
    }
    showDialog(
      context: bc,
      builder: (bc) => AlertDialog(
        content: new SingleChildScrollView(
          child: new Column(
            children: children,
          )
        ),
        actions: [
            new FlatButton(
              child: new Text("Individual"),
              onPressed: (){
                Navigator.pop(bc);
                showIndividualDialog(bc);
              }
            ),
            new FlatButton(
              child: new Text("Cancel"),
              onPressed: ()=> Navigator.pop(bc)
            )
          ]
      )
    );
  }
  void showIndividualDialog(BuildContext bc){
    var children = List<Widget>();
    for(int i = 0; i < resList.length; i++){
      children.add(new Text(
        diceNames[i] + ": " + resList[i].toString(),
        textAlign: TextAlign.center,
      ));
    }
    showDialog(
      context: bc,
      builder: (bc){
        return new AlertDialog(
          content: new SingleChildScrollView(
            child: new Column(
              children: children,
            )
          ),
          actions: [
            new FlatButton(
              child: new Text("Combined"),
              onPressed: (){
                Navigator.pop(bc);
                showCombinedDialog(bc);
              }
            ),
            new FlatButton(
              child: new Text("Cancel"),
              onPressed: ()=> Navigator.pop(bc)
            )
          ]
        );
      }
    );
  }
  String toString(){
    var out = "";
    if(_number !=1){
      out+=_number.toString();
    }
    _reses.forEach((r)=> out += ", "+r.value.toString()+" "+r.name);
    return out;
  }
}

class Result{
  String name;
  int value;
  Result(this.name,this.value);
  String toString() => value.toString() + " " + name;
}