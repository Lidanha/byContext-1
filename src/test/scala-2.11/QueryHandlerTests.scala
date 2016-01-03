import byContext.score._
import byContext._
import byContext.writers.map.MapObjectWriter
import org.scalatest.{FunSuite, Matchers}

class QueryHandlerTests extends FunSuite with Matchers{
  val emptyctx = QueryContext()

  def provider(value:AnyRef) : ValueProvider = new ValueProvider {
    override def get(ctx: QueryContext): Either[ByContextError, Any] = Right(value)
  }
  test("a1"){
    val writer = new MapObjectWriter(collection.mutable.Map[String,Any]())
    new QueryHandler()
      .query(emptyctx, Map("root" -> SingleFiltered(provider("root value"))), writer)

    writer.map should be (collection.mutable.Map("root" -> "root value"))
  }
  test("a2"){
    val writer = new MapObjectWriter(collection.mutable.Map[String,Any]())
    new QueryHandler()
      .query(emptyctx,Map("root" -> ObjectFiltered(provider(Array("child1" -> "child1")))), writer)

    writer.map should be (collection.mutable.Map("root" -> Map("child1" -> "child1")))
  }
  test("a3"){
    val writer = new MapObjectWriter(collection.mutable.Map[String,Any]())
    new QueryHandler()
      .query(emptyctx,Map("root" ->
        ObjectFiltered(provider(Array("child1" ->
          ObjectFiltered(provider(Array(
            "child1.1" -> "child1.1",
            "child1.2" -> "child1.2"
          )))
          )))

      ), writer)

    writer.map should be (
      collection.mutable.Map("root" ->
        collection.mutable.Map("child1" ->
          collection.mutable.Map(
            "child1.1" -> "child1.1",
            "child1.2" -> "child1.2")))
    )
  }
}
