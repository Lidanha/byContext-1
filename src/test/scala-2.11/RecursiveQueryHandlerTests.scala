import byContext.exceptions.ByContextError
import byContext.model.QueryContext
import byContext.queryHandler.RecursiveQueryHandler
import byContext.score.valueContainers.{ArrayValueContainer, ObjectValueContainer, SingleValueContainer}
import byContext.writers.Writer
import byContext.writers.map.MapObjectWriter
import org.scalatest.{Matchers, WordSpecLike}
import rules.Creators

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class RecursiveQueryHandlerTests extends WordSpecLike with Matchers with Creators{
  def input(map:Map[String,Any]) : Any = {
    val result = mutable.Map[String, Any]()
    val writer = new Writer{
      override def write(value: Any): Unit = ???

      override def getCollectionWriter(): Writer = ???

      override def getObjectWriter(): Writer = new MapObjectWriter(result)

      override def getPropertyWriter(propertyName: String): Writer = ???
    }

    new RecursiveQueryHandler().query(emptyCTX,map, writer)

    result
  }
  def single(value:Any): SingleValueContainer = new SingleValueContainer {
    override def get(ctx: QueryContext): Either[ByContextError, Any] = Right(value)
  }
  def obj(values:Array[(String,Any)]): ObjectValueContainer = new ObjectValueContainer {
    override def get(ctx: QueryContext): Either[ByContextError, Map[String, Any]] = Right(values.toMap)
  }
  def arr(values:Iterable[Any]):ArrayValueContainer = new ArrayValueContainer {
    override def get(ctx: QueryContext): Either[ByContextError, Array[Any]] = Right(values.toArray)
  }

  "RecursiveQueryHandler" must {
    "one property -> single" in {
      input(
        Map("root" ->
          single("root value"))
      ) should be (collection.mutable.Map("root" -> "root value"))
    }
    "one property -> object with one property -> object value" in {
      input(Map("root" ->
        obj(Array("child1" -> "child1"))
      )
      ) should be (collection.mutable.Map("root" -> Map("child1" -> "child1")))
    }
    "aa" in {
      input(Map("root" ->
        obj(Array("child1" ->
          obj(Array(
            "child1.1" -> "child1.1",
            "child1.2" -> "child1.2"
          ))
        ))

      )) should be (
        collection.mutable.Map("root" ->
          collection.mutable.Map("child1" ->
            collection.mutable.Map(
              "child1.1" -> "child1.1",
              "child1.2" -> "child1.2")))
      )
    }
    "arr" in {
      input(Map("root" ->
        arr(Array("child1","child2"))
      )) should be (collection.mutable.Map("root" -> ListBuffer("child1","child2")))
    }
  }
}
