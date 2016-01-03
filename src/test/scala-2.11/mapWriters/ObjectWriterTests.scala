package mapWriters

import byContext.writers.WriterUnsupportedOperationException
import byContext.writers.map.{MapPropertyWriter, MapObjectWriter}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class ObjectWriterTests extends FlatSpec with Matchers{
  "MapObjectWriter" should "throw WriterUnsupportedOperationException if write is called" in {
    val writer = new MapObjectWriter(mutable.Map())
    writer.getPropertyWriter("propName") shouldBe a[MapPropertyWriter]
  }
  it should "throw UnsupportedOperationException if write is called" in {
    val writer = new MapObjectWriter(mutable.Map())
    a[WriterUnsupportedOperationException] should be thrownBy {
      writer.write("")
    }
  }
  it should "throw UnsupportedOperationException if getCollectionWriter is called" in {
    val writer = new MapObjectWriter(mutable.Map())
    a[WriterUnsupportedOperationException] should be thrownBy {
      writer.getCollectionWriter()
    }
  }
  it should "throw UnsupportedOperationException if getObjectWriter is called" in {
    val writer = new MapObjectWriter(mutable.Map())
    a[WriterUnsupportedOperationException] should be thrownBy {
      writer.getObjectWriter()
    }
  }

}
