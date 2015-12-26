import byContext.score._
import org.scalatest.{Matchers, FunSuite}

class QueryHandlerTests extends FunSuite with Matchers{
  val emptyctx = QueryContext()

  test("ss"){
    val res = new QueryHandler()
      .query(emptyctx, Leaf("root", "root", SingleValueProvider("root value")))

    println(res)

    res should be (Map("root" -> "root value"))
  }
  test("a1"){
    val res = new QueryHandler()
      .query(emptyctx,
        DataObject(Map(
          "child1" -> Leaf("child1", "child1", SingleValueProvider("child1"))
        )
        )
      )

    println(res)

    res should be (
      Map("child1" -> "child1")
    )
  }
  test("a2"){
    val res = new QueryHandler()
      .query(emptyctx,
        DataObject(Map(
          "root" -> DataObject(Map("child1" -> Leaf("child1", "root.child1", SingleValueProvider("child1"))))
        )
        )
      )

    println(res)

    res should be (
      Map("root" -> Map("child1" -> "child1"))
    )
  }
  test("a3"){
    val res = new QueryHandler()
      .query(emptyctx,
        DataObject(Map(
          "root" -> DataObject(Map(
            "child1" -> DataObject(Map(
              "child1.1" -> Leaf("child1.1", "root.child1.child1.1", SingleValueProvider("child1.1")),
              "child1.2" -> Leaf("child1.2", "root.child1.child1.2", SingleValueProvider("child1.2"))
            )
          ))
        )
        )
      ))

    println(res)

    res should be (
      Map("root" ->
        Map("child1" ->
          Map(
            "child1.1" -> "child1.1",
            "child1.2" -> "child1.2")))
    )
  }
}
