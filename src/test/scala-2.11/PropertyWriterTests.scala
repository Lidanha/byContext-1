import byContext.writers.PropertyWriter
import org.scalamock.scalatest.MockFactory
import org.scalatest.{EitherValues, Matchers, FunSuite}
import scala.collection._
import scala.collection.mutable.ListBuffer

class PropertyWriterTests  extends FunSuite with Matchers with MockFactory with EitherValues{

  test("property writer must not change map if no method had been called on him"){
    val writer = new PropertyWriter(mutable.Map(), "prop")
    writer.map should be (mutable.Map())
  }
  test("property writer should set prop value"){
    val writer = new PropertyWriter(mutable.Map(), "prop")
    writer.write("value")
    writer.map should be (mutable.Map("prop" -> "value"))
  }
  test("property writer should set prop value as a ListBuffer"){
    val writer = new PropertyWriter(mutable.Map(), "prop")
    writer.getCollectionWriter().write("value")
    writer.map should be (mutable.Map("prop" -> ListBuffer("value")))
  }
}
