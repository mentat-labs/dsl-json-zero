package com.dslplatform.json

trait Serializer extends PartialFunction[Any, String] {
  protected[this] val jsonWriter = new ThreadLocal[JsonWriter] {
    override def initialValue: JsonWriter = new JsonWriter()
  }

  def write(sw: JsonWriter, value: Any): Unit

  def apply(value: Any): String = {
    val sw = jsonWriter.get
    sw.reset()
    write(sw, value)
    sw.toString
  }

  def unapply(value: Any): Option[String] =
    this.lift(value)
}
