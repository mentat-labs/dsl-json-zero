package com.dslplatform.json

import java.util.UUID

object ObjectSerializer extends Serializer {
  def isDefinedAt(value: Any) = value match {
    case null
       | _: Long
       | _: Int
       | _: Short
       | _: Byte
       | _: Double
       | _: Float
       | _: Boolean
       | _: String
       | _: Char
       | _: java.math.BigDecimal
       | _: BigDecimal
       | _: UUID
       | _: Array[_]
       | _: TraversableOnce[_]
       | _: java.lang.Iterable[_] => true

    case _ => false
  }

  def write(sw: JsonWriter, value: Any): Unit =
    value match {
      case null =>
        sw.writeNull()
      case l: Long =>
        NumberConverter.serialize(l, sw)
      case i: Int =>
        NumberConverter.serialize(i, sw)
      case s: Short =>
        NumberConverter.serialize(s.toInt, sw)
      case b: Byte =>
        NumberConverter.serialize(b.toInt, sw)
      case s: Double =>
        NumberConverter.serialize(s, sw)
      case f: Float =>
        NumberConverter.serialize(f, sw)
      case b: Boolean =>
        BoolConverter.serialize(b, sw)
      case s: String =>
        sw.writeString(s)
      case c: Char =>
        sw.writeString(c.toString)
      case bd: java.math.BigDecimal =>
        NumberConverter.serialize(bd, sw)
      case bd: BigDecimal =>
        NumberConverter.serialize(bd.bigDecimal, sw)
      case u: UUID =>
        UUIDConverter.serialize(u, sw)
      case value =>
        CollectionSerializer.write(sw, value)
    }
}
