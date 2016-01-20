import byContext._
import byContext.score.valueContainers.{ObjectValueContainer, SingleValueContainer}
import byContext.writers.map.MapObjectWriter
import org.scalatest.{FunSuite, Matchers}

class QueryHandlerTests extends FunSuite with Matchers{
  val emptyctx = QueryContext()

  def input(map:Map[String,Any]) : collection.mutable.Map[String,Any] = {
    val writer = new MapObjectWriter(collection.mutable.Map[String,Any]())
    new QueryHandler().query(emptyctx,map, writer)
    writer.map
  }
  def single(value:Any): SingleValueContainer = new SingleValueContainer {
    override def get(ctx: QueryContext): Either[ByContextError, Any] = Right(value)
  }
  def obj(values:Array[(String,Any)]): ObjectValueContainer = new ObjectValueContainer {
    override def get(ctx: QueryContext): Either[ByContextError, Array[(String, Any)]] = Right(values)
  }

  test("one property -> single"){
    input(
      Map("root" ->
        single("root value"))
    ) should be (collection.mutable.Map("root" -> "root value"))
  }
  test("one property -> object with one property -> object value"){
    input(Map("root" ->
      obj(Array("child1" -> "child1"))
    )
    ) should be (collection.mutable.Map("root" -> Map("child1" -> "child1")))
  }
  test("aa"){
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
}
