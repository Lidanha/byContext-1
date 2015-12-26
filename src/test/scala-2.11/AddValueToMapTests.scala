import byContext.score._
import org.scalatest.{Matchers, FunSuite}

class AddValueToMapTests extends FunSuite with Matchers {
  test("aa"){
    val target = scala.collection.mutable.Map[String,AnyRef]()
    /*val value : Value = */

    AddValueToMap(
      "root", ObjectValue(
        Map("akka" -> ObjectValue(Map(
            "a" -> SingleValue("leaf1").asInstanceOf[Value],
            "b" -> SingleValue("leaf2").asInstanceOf[Value]
            )
        ), "leaf-next to akka" -> SingleValue("leaf3").asInstanceOf[Value]
        )), target)

    target.foreach(kv => println(s"${kv._1} -> ${kv._2}"))
  }
}
