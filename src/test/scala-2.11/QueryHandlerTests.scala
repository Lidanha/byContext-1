import byContext.score._
import byContext._
import org.scalatest.{FunSuite, Matchers}

class QueryHandlerTests extends FunSuite with Matchers{
  val emptyctx = QueryContext()

  def provider(value:AnyRef) : ValueProvider = new ValueProvider {
    override def get(ctx: QueryContext): Either[ByContextError, Any] = Right(value)
  }
  test("a1"){
    val writer = new ObjectWriter(collection.mutable.Map[String,Any]())
    new QueryHandler()
      .query(emptyctx, Map("root" -> Single(provider("root value"))), writer)

    println(writer.map)

    writer.map should be (collection.mutable.Map("root" -> "root value"))
  }
  test("a2"){
    val writer = new ObjectWriter(collection.mutable.Map[String,Any]())
    new QueryHandler()
      .query(emptyctx,Map("root" -> DataObjectFiltered(provider(Array("child1" -> "child1")))), writer)

    println(writer.map)

    writer.map should be (collection.mutable.Map("root" -> Map("child1" -> "child1")))
  }
  test("a3"){
    val writer = new ObjectWriter(collection.mutable.Map[String,Any]())
    new QueryHandler()
      .query(emptyctx,Map("root" ->
        DataObjectFiltered(provider(Array("child1" ->
          DataObjectFiltered(provider(Array(
            "child1.1" -> "child1.1",
            "child1.2" -> "child1.2"
          )))
          )))

      ), writer)

    println(writer.map)

    writer.map should be (
      collection.mutable.Map("root" ->
        collection.mutable.Map("child1" ->
          collection.mutable.Map(
            "child1.1" -> "child1.1",
            "child1.2" -> "child1.2")))
    )
  }
}
