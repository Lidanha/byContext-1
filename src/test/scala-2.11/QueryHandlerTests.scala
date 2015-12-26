import byContext.score._
import org.scalatest.{Matchers, FunSuite}

class QueryHandlerTests extends FunSuite with Matchers{
  val emptyctx = QueryContext()

  test("ss"){
    val writer = new MapObjectWriter()
    new QueryHandler()
      .query(emptyctx, LeafNode("root", "root", SingleValueProvider("root value")), writer)

    println(writer.targetMap)

    writer.targetMap should be (Map("root" -> "root value"))
  }
  test("a1"){
    val writer = new MapObjectWriter()
    new QueryHandler()
      .query(emptyctx,
        DataObjectNode(Map(
          "child1" -> LeafNode("child1", "child1", SingleValueProvider("child1"))
        )
        ), writer
      )

    println(writer.targetMap)

    writer.targetMap should be (
      Map("child1" -> "child1")
    )
  }
  test("a2"){
    val writer = new MapObjectWriter()
    new QueryHandler()
      .query(emptyctx,
        DataObjectNode(Map(
          "root" -> DataObjectNode(Map("child1" -> LeafNode("child1", "root.child1", SingleValueProvider("child1"))))
        )
        ), writer
      )

    println(writer.targetMap)

    writer.targetMap should be (
      Map("root" -> Map("child1" -> "child1"))
    )
  }
  test("a3"){
    val writer = new MapObjectWriter()
    new QueryHandler()
      .query(emptyctx,
        DataObjectNode(Map(
          "root" -> DataObjectNode(Map(
            "child1" -> DataObjectNode(Map(
              "child1.1" -> LeafNode("child1.1", "root.child1.child1.1", SingleValueProvider("child1.1")),
              "child1.2" -> LeafNode("child1.2", "root.child1.child1.2", SingleValueProvider("child1.2"))
            )
          ))
        )
        )
      ), writer)

    println(writer.targetMap)

    writer.targetMap should be (
      Map("root" ->
        Map("child1" ->
          Map(
            "child1.1" -> "child1.1",
            "child1.2" -> "child1.2")))
    )
  }
}
