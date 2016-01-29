package api

import byContext.api.SyncInMemoryAPI
import byContext.valueContainers.RawValueContainer
import byContext.{QueryContext, RecursiveQueryHandler, SimpleMapDataIndex}
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._
class SyncInMemoryAPITests extends WordSpecLike with Matchers{
  val simpleIndex = new SimpleMapDataIndex(Map(
    "1"->"1",
    "2"->Map(
      "1"->"2.1",
      "2"->"2.2"
    ),
    "3"->Map(
      "1"->Map(
        "1"->RawValueContainer("3.1.1"),
        "2"->"3.1.2"
      ),
      "2"->"3.2"
    )
  ))
  "SyncInMemoryAPI with RecursiveQueryHandler" must {
    "" in {
      val api = new SyncInMemoryAPI(simpleIndex,new RecursiveQueryHandler())
      val res = Await.result(api.get("3.1.1",QueryContext()), 1 second)
      res should be ("3.1.1")
    }
  }


}
