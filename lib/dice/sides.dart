class SimpleSide{
  String _value;

  SimpleSide(this._value);

  SimpleSide clone() => SimpleSide(_value);
  void setString(String side) => _value = side;
  @override
  String toString() => _value;
}

class ComplexSide{
  List<ComplexSidePart> parts;

  ComplexSide({this.parts = const[]});

  ComplexSide clone() => ComplexSide(parts: List.from(parts));
  @override
  String toString() => parts.join(", ");
}

class ComplexSidePart{
  String name;
  int value;

  ComplexSidePart({this.name = "", this.value = 0});

  ComplexSidePart clone() => ComplexSidePart(name: name, value:value);
  @override
  String toString() => value != 1 ? value.toString() + " " + name : name;
}