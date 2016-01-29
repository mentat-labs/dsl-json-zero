package com.dslplatform.json.zero

import java.util.UUID

import org.specs2.mutable._

class ZeroSerializationTest extends Specification {
  override def is = s2"""
    testObjects  $testObjects
    testArrays   $testArrays
    testSeqs     $testSeqs
"""

  private val check = {
    import com.fasterxml.jackson.databind.ObjectMapper
    import com.fasterxml.jackson.module.scala.DefaultScalaModule
    
    val objectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
    objectMapper.writeValueAsString(_: Any)
  }

  def testObjects = {
    DslJsonZero.serialize(null) ==== check(null)
    DslJsonZero.serialize(1234567890123456789L) ==== check(1234567890123456789L)
    DslJsonZero.serialize(1234567890) ==== check(1234567890)
    DslJsonZero.serialize(12345: Short) ==== check(12345: Short)
    DslJsonZero.serialize(123: Byte) ==== check(123: Byte)
    DslJsonZero.serialize(0.1234567890123456) ==== check(0.1234567890123456)
    DslJsonZero.serialize(0.1234567f) ==== check(0.1234567f)
    DslJsonZero.serialize(true) ==== check(true)
    DslJsonZero.serialize("0123456789") ==== check("0123456789")
    DslJsonZero.serialize('0') ==== check('0')
    DslJsonZero.serialize(new java.math.BigDecimal("123456790.1234567890")) ==== check(new java.math.BigDecimal("123456790.1234567890"))
    DslJsonZero.serialize(BigDecimal("123456790.1234567890")) ==== check(BigDecimal("123456790.1234567890"))
    DslJsonZero.serialize(UUID.fromString("1-2-3-4-5")) ==== check(UUID.fromString("1-2-3-4-5"))
  }

  def testArrays = {
    DslJsonZero.serialize(Array(Long.MinValue, Long.MaxValue)) ==== check(Array(Long.MinValue, Long.MaxValue))
    DslJsonZero.serialize(Array(Int.MinValue, Int.MaxValue)) ==== check(Array(Int.MinValue, Int.MaxValue))
    DslJsonZero.serialize(Array(Short.MinValue, Short.MaxValue)) ==== check(Array(Short.MinValue, Short.MaxValue))
    DslJsonZero.serialize(Array(Byte.MinValue, Byte.MaxValue)) ==== check(Array(Byte.MinValue, Byte.MaxValue))
    DslJsonZero.serialize(Array(Double.MinValue, Double.MaxValue)) ==== check(Array(Double.MinValue, Double.MaxValue))
    DslJsonZero.serialize(Array(Float.MinValue, Float.MaxValue)) ==== check(Array(Float.MinValue, Float.MaxValue))
    DslJsonZero.serialize(Array(false, true)) ==== check(Array(false, true))
    DslJsonZero.serialize("ABCDEFGHIJ0123456789".toCharArray) ==== check("ABCDEFGHIJ0123456789".toCharArray)
    DslJsonZero.serialize(Array("ABCDEFGHIJ", "0123456789")) ==== check(Array("ABCDEFGHIJ", "0123456789"))
    DslJsonZero.serialize(Array[java.lang.Long](Long.MinValue, Long.MaxValue)) ==== check(Array[java.lang.Long](Long.MinValue, Long.MaxValue))
    DslJsonZero.serialize(Array[java.lang.Integer](Int.MinValue, Int.MaxValue)) ==== check(Array[java.lang.Integer](Int.MinValue, Int.MaxValue))
    DslJsonZero.serialize(Array[java.lang.Short](Short.MinValue, Short.MaxValue)) ==== check(Array[java.lang.Short](Short.MinValue, Short.MaxValue))
    DslJsonZero.serialize(Array[java.lang.Byte](Byte.MinValue, Byte.MaxValue)) ==== check(Array[java.lang.Byte](Byte.MinValue, Byte.MaxValue))
    DslJsonZero.serialize(Array[java.lang.Double](Double.MinValue, Double.MaxValue)) ==== check(Array[java.lang.Double](Double.MinValue, Double.MaxValue))
    DslJsonZero.serialize(Array[java.lang.Float](Float.MinValue, Float.MaxValue)) ==== check(Array[java.lang.Float](Float.MinValue, Float.MaxValue))
    DslJsonZero.serialize(Array[java.lang.Boolean](false, true)) ==== check(Array[java.lang.Boolean](false, true))
    DslJsonZero.serialize("ABCDEFGHIJ0123456789".toCharArray.map(java.lang.Character.valueOf)) ==== check("ABCDEFGHIJ0123456789".toCharArray.map(java.lang.Character.valueOf))
    DslJsonZero.serialize(Array[BigDecimal](-1.01, 0, 1.01).map(_.bigDecimal)) ==== check(Array[BigDecimal](-1.01, 0, 1.01).map(_.bigDecimal))
    DslJsonZero.serialize(Array[BigDecimal](-1.01, 0, 1.01)) ==== check(Array[BigDecimal](-1.01, 0, 1.01))
    DslJsonZero.serialize(Array(UUID.fromString("1-2-3-4-5"), UUID.fromString("0-0-0-0-0"))) ==== check(Array(UUID.fromString("1-2-3-4-5"), UUID.fromString("0-0-0-0-0")))
  }

  def testSeqs = {
    pending
  }
}
