package mapWriters

import byContext.writers.WriterUnsupportedOperationException
import byContext.writers.map.{MapCollectionWriter, MapObjectWriter}
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer

class CollectionWriterTests extends FlatSpec with Matchers{

  "MapCollectionWriter" should "add a new item to target list with each call to write" in {
    val writer = new MapCollectionWriter(ListBuffer())
    writer.write("a")
    writer.write("b")
    writer.write("c")

    writer.targetList should have size 3
    writer.targetList should contain ("a")
    writer.targetList should contain ("b")
    writer.targetList should contain ("c")
  }
  it should "return a MapCollectionWriter when getCollectionWriter is called" in {
    new MapCollectionWriter(ListBuffer())
      .getCollectionWriter() shouldBe a[MapCollectionWriter]
  }
  it should "return a MapObjectWriter when getObjectWriter is called" in {
    new MapCollectionWriter(ListBuffer())
      .getObjectWriter() shouldBe a[MapObjectWriter]
  }
  it should "throw UnsupportedOperationException when getPropertyWriter is called" in {
    val writer = new MapCollectionWriter(ListBuffer())
    a[WriterUnsupportedOperationException] should be thrownBy {
      writer.getPropertyWriter("")
    }
  }
}
