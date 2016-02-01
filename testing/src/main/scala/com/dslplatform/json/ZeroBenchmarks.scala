package com.dslplatform.json

import java.io.ByteArrayOutputStream

import com.dslplatform.json.zero.DslJsonZero

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Array(Mode.Throughput))
@Fork(2)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
class ZeroBenchmarks {
  var intArray: Array[Int] = _
  var complexNumericSeq: Seq[_] = _

  var baos: ByteArrayOutputStream = _

  var objectMapper: ObjectMapper = _
  var jsonWriter: JsonWriter = _

  @Setup
  def initialize(): Unit = {
    intArray = (1 to 100).toArray
    complexNumericSeq = Seq(1 to 95, Array(Long.MaxValue, Long.MinValue), 3.14)

    baos = new ByteArrayOutputStream(10000)

    objectMapper = new ObjectMapper()
      .registerModule(DefaultScalaModule)
      .registerModule(new AfterburnerModule)
    jsonWriter = new JsonWriter(10000)
  }

  @Benchmark
  def intArrayJackson(bh: Blackhole): Unit = {
    baos.reset()
    objectMapper.writeValue(baos, intArray)
    bh.consume(baos)
  }

  @Benchmark
  def intArrayDslJson(bh: Blackhole): Unit = {
    baos.reset()
    jsonWriter.reset()
    com.dslplatform.json.NumberConverter.serialize(intArray, jsonWriter)
    jsonWriter.toStream(baos)
    bh.consume(baos)
  }

  @Benchmark
  def intArrayDslJsonZero(bh: Blackhole): Unit = {
    baos.reset()
    val json = DslJsonZero.serialize(1 to 100)
    baos.write(json.getBytes("UTF-8"))
    bh.consume(baos)
  }

  @Benchmark
  def complexNumericSeqJackson(bh: Blackhole): Unit = {
    baos.reset()
    objectMapper.writeValue(baos, complexNumericSeq)
    bh.consume(baos)
  }

  @Benchmark
  def complexNumericSeqDslJson(bh: Blackhole): Unit = {
    baos.reset()
    jsonWriter.reset()
    jsonWriter.write(JsonWriter.ARRAY_START)
    val cns0 = complexNumericSeq(0).asInstanceOf[IndexedSeq[Int]]
    var index = 0
    var token = JsonWriter.ARRAY_START
    while (index < cns0.size) {
      jsonWriter.write(token)
      NumberConverter.serialize(cns0(index), jsonWriter)
      index += 1
      token = JsonWriter.COMMA
    }
    jsonWriter.write(JsonWriter.ARRAY_END)
    NumberConverter.serialize(complexNumericSeq(1).asInstanceOf[Array[Long]], jsonWriter)
    NumberConverter.serialize(complexNumericSeq(2).asInstanceOf[java.lang.Double], jsonWriter)
    jsonWriter.write(JsonWriter.ARRAY_END)
    bh.consume(baos)
  }

  @Benchmark
  def complexNumericSeqDslJsonZero(bh: Blackhole): Unit = {
    baos.reset()
    val json = DslJsonZero.serialize(Seq(1 to 10, Array(Long.MaxValue, Long.MinValue), 3.14))
    baos.write(json.getBytes("UTF-8"))
    bh.consume(baos)
  }
}
