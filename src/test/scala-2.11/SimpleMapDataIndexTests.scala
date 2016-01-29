import byContext.SimpleMapDataIndex
import org.scalatest.{Matchers, WordSpecLike}

class SimpleMapDataIndexTests extends WordSpecLike  with Matchers{
  "SimpleMapDataIndex" must {
    "create simple, flat index" in {
      new SimpleMapDataIndex(Map("root"->1)).getItem("root") should be (Some(1))
    }
  }
}
