package com.dslplatform.json

object CollectionSerializer extends Serializer {
  def isDefinedAt(value: Any) = value match {
    case _: Array[_]
       | _: TraversableOnce[_]
       | _: java.lang.Iterable[_] => true

    case _ => false
  }

  def write(sw: JsonWriter, value: Any): Unit =
    value match {
      case la: Array[Long] => NumberConverter.serialize(la, sw)
      case ia: Array[Int] => NumberConverter.serialize(ia, sw)
      case sa: Array[Short] => NumberConverter.serialize(sa, sw)
      case ba: Array[Byte] => BinaryConverter.serialize(ba, sw)
      case da: Array[Double] => NumberConverter.serialize(da, sw)
      case fa: Array[Float] => NumberConverter.serialize(fa, sw)
      case ba: Array[Boolean] => BoolConverter.serialize(ba, sw)
      case ca: Array[Char] => sw.writeString(new String(ca))

      case array: Array[_] => serialize(sw, array)
      case traversableOnce: TraversableOnce[_] => serialize(sw, traversableOnce)
      case iterable: java.lang.Iterable[_] => serialize(sw, iterable)
    }

  private[this] def serialize(sw: JsonWriter, values: Array[_]): Unit = {
    var index = 0
    var token = '[': Byte
    while (index < values.length) {
      sw.writeByte(token)
      ObjectSerializer.write(sw, values(index))
      token = ','
      index += 1
    }
    sw.writeByte(']')
  }

  private[this] def serialize(sw: JsonWriter, values: TraversableOnce[_]): Unit = {
    var token = '[': Byte
    for (value <- values) {
      sw.writeByte(token)
      ObjectSerializer.write(sw, value)
      token = ','
    }
    sw.writeByte(']')
  }

  private[this] def serialize(sw: JsonWriter, values: java.lang.Iterable[_]): Unit = {
    val iterator = values.iterator
    var token = '[': Byte
    while (iterator.hasNext) {
      sw.writeByte(token)
      ObjectSerializer.write(sw, iterator.next())
      token = ','
    }
    sw.writeByte(']')
  }
}
