import byContext.writers.WriterUnsupportedOperationException
import byContext.writers.map.{MapObjectWriter, MapCollectionWriter, MapPropertyWriter}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection._

class PropertyWriterTests  extends FlatSpec with Matchers{

  "MapPropertyWriter" should "not change map if no method had been called on him" in {
    val writer = new MapPropertyWriter(mutable.Map(), "prop")
    writer.map should be (mutable.Map())
  }
  it should "set prop value when write is called" in {
    val writer = new MapPropertyWriter(mutable.Map(), "prop")
    writer.write("value")
    writer.map should be (mutable.Map("prop" -> "value"))
  }
  it should "return a MapCollectionWriter when getCollectionWriter is called" in {
    new MapPropertyWriter(mutable.Map(), "prop")
      .getCollectionWriter() shouldBe a[MapCollectionWriter]
  }
  it should "return a MapObjectWriter when getObjectWriter is called" in {
    new MapPropertyWriter(mutable.Map(), "prop")
      .getObjectWriter() shouldBe a[MapObjectWriter]
  }
  it should "throw UnsupportedOperationException when getPropertyWriter is called" in {
    val writer = new MapPropertyWriter(mutable.Map(), "prop")
    a[WriterUnsupportedOperationException] should be thrownBy {
      writer.getPropertyWriter("")
    }
  }
  it should "throw UnsupportedOperationException when write is called twice" in {
    val writer = new MapPropertyWriter(mutable.Map(), "prop")
    writer.write("")
    a[WriterUnsupportedOperationException] should be thrownBy {
      writer.write("")
    }
  }
}
