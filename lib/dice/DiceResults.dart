import 'package:flutter/material.dart';

class DiceResults{
  int _number = 0;
  List<Result> _reses = new List<Result>();
  var subtractMode = false;
  var problem = false;
  var resList = new List();
  var diceNames = new List();
  void add(Result res, String diceName){
    if(subtractMode)
      res.value *= -1;
    resList.add(new Result(res.name,res.value));
    diceNames.add(diceName);
    if (has(res.name))
      _reses[indexOf(res.name)].value += res.value;
    else
      _reses.add(res);
  }
  void addNum(int i, String diceName){
    if (subtractMode)
      i*=-1;
    _number += i;
    resList.add(i);
    diceNames.add(diceName);
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
      if(dr.resList[i] is int)
        addNum(dr.resList[i], dr.diceNames[i]);
      else if(dr.resList[i] is Result)
        add(dr.resList[i], dr.diceNames[i]);
    }
  }
  bool isNumOnly() => resList.length == 0;
  void showResultDialog(BuildContext bc, String problemMessage){
    //TODO: option to get individual dialog first
    showCombinedDialog(bc);
  }
  void showCombinedDialog(BuildContext bc){
    var children = new List<Widget>();
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