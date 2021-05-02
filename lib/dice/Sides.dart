class SimpleSide{
  String _value;

  SimpleSide(this._value);

  SimpleSide clone() => SimpleSide(_value);
  void setString(String side) => _value = side;
  String toString() => _value;
}

class ComplexSide{
  List<ComplexSidePart> parts;

  ComplexSide({List<ComplexSidePart> parts}) :
    this.parts = parts ?? [];

  ComplexSide clone() => ComplexSide(parts: new List.from(parts));
  String toString() => parts.join(", ");
}

class ComplexSidePart{
  String name;
  int value;

  ComplexSidePart({this.name = "", this.value = 0});

  ComplexSidePart clone() => ComplexSidePart(name: name, value:value);
  String toString() => value != 1 ? value.toString() + " " + name : name;
}