import byContext.{IndexItem, SimpleMapDataIndex}
import org.scalatest.{Matchers, WordSpecLike}

class SimpleMapDataIndexTests extends WordSpecLike  with Matchers{
  "SimpleMapDataIndex" must {
    "create simple, flat index" in {
      new SimpleMapDataIndex(Map("root"->1)).getItem("root") should be (Some(IndexItem("root",1)))
    }
    "two roots one flat and one object with one level" in {
      val index = new SimpleMapDataIndex(Map(
        "root-1"->1,
        "root-2"->Map(
          "child1"->1.1,
          "child2"->1.2
        )
      ))

      index getItem "root-1"  should be (Some(IndexItem("root-1",1)))
      index getItem "root-2"  should be (Some(IndexItem("root-2",Map(
        "child1"->1.1,
        "child2"->1.2
      ))))
      index getItem "root-2.child1"  should be (Some(IndexItem("child1",1.1)))
      index getItem "root-2.child2"  should be (Some(IndexItem("child2",1.2)))
    }
    "two roots one flat and one object with two levels and one object with three" in {
      val index = new SimpleMapDataIndex(Map(
        "1"->"1",
        "2"->Map(
          "1"->"2.1",
          "2"->"2.2"
      ),
        "3"->Map(
          "1"->Map(
            "1"->"3.1.1",
            "2"->"3.1.2"
            ),
          "2"->"3.2"
      )
      ))

      index getItem "1"  should be (Some(IndexItem("1","1")))
      index getItem "2"  should be (Some(IndexItem("2",Map(
        "1"->"2.1",
        "2"->"2.2"
      ))))
      index getItem "2.1"  should be (Some(IndexItem("1","2.1")))
      index getItem "2.2"  should be (Some(IndexItem("2","2.2")))
      index getItem "3"  should be (Some(IndexItem("3",Map(
        "1"->Map(
          "1"->"3.1.1",
          "2"->"3.1.2"
        ),
        "2"->"3.2"
      ))))
      index getItem "3.1"  should be (Some(IndexItem("1",Map(
        "1"->"3.1.1",
        "2"->"3.1.2"
      ))))
      index getItem "3.1.1"  should be (Some(IndexItem("1","3.1.1")))
      index getItem "3.1.2"  should be (Some(IndexItem("2","3.1.2")))
      index getItem "3.2"  should be (Some(IndexItem("2","3.2")))
    }
  }
}
