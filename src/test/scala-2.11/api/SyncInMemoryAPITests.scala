package api

import byContext.api.SyncInMemoryAPI
import byContext.data.ScalaCodeDataSource
import byContext.score.DefaultScoreCalculator
import byContext.{QueryContext, RecursiveQueryHandler, SimpleMapDataIndex}
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.Await
import scala.concurrent.duration._
class SyncInMemoryAPITests extends WordSpecLike with Matchers with ScalaCodeDataSource{
  implicit val scoreCalculator = new DefaultScoreCalculator()
  val simpleIndex = new SimpleMapDataIndex(Map(
    "1"->"1",
    "2"->Map(
      "1"->"2.1",
      "2"->"2.2"
    ),
    "3"->Map(
      "1"->Map(
        "1"->"3.1.1",
        "2"->single(
          v("1", "subj1" is "value1"),
          v("2", "subj2" is "value2")
        )(true)
      ),
      "2"->"3.2"
    )
  ))
  val api = new SyncInMemoryAPI(simpleIndex,new RecursiveQueryHandler())

  "SyncInMemoryAPI with RecursiveQueryHandler" must {
    "return simple raw value a couple of levels deep" in {
      val res = Await.result(api.get("3.1.1",QueryContext()), 1 second)
      res should be ("3.1.1")
    }
    "select the relevant value a couple of levels deep" in {
      val res = Await.result(api.get("3.1.2",QueryContext("subj1" -> "value1")), 1 second)
      res should be ("1")
    }
  }
}