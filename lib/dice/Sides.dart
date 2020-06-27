class SimpleSide{
  String _value;

  SimpleSide(this._value);

  SimpleSide clone() => SimpleSide(_value);
  int intSide() => int.parse(_value);
  String stringSide() => _value;
  bool isInt() => int.tryParse(_value) != null;
  void setString(String side) => _value = side;
  void setInt(int side)=> _value = side.toString();
  String toString()=>_value;
}

class ComplexSide{
  int number;
  List<ComplexSidePart> parts = new List<ComplexSidePart>();

  ComplexSide({this.number,this.parts});

  ComplexSide clone() => ComplexSide(number:number,parts:new List<ComplexSidePart>.from(parts));
  String toString(){
    var out = new List<String>();
    if(number != null)
      out.add(number.toString());
    parts.forEach((s)=>out.add(s.toString()));
    return out.join(", ");
  }
}

class ComplexSidePart{
  String name;
  int value;

  ComplexSidePart({this.name = "",this.value = 0});

  ComplexSidePart clone() => ComplexSidePart(name:name,value:value);
  String toString() => value.toString() + " " + name;
}