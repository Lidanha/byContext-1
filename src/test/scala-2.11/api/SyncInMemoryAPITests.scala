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
          "1" relevantWhen("subj1" is "value1"),
          "2" relevantWhen("subj2" is "value2")
        )(true)
      ),
      "2"-> Map(
        "1" -> array(
          "1" relevantWhen("subj1" is "value1"),
          "2" relevantWhen("subj2" is "value2"),
          "3" relevantWhen("subj1" is "value2"),
          "4" relevantWhen("subj1" is "value1"),
          "5" relevantWhen("subj2" is "value2")
        )(1)
      )
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
    "select the relevant values of an array" in {
      val res1 = Await.result(api.get("3.2.1",QueryContext("subj1" -> "value1")), 1 second)
      res1 should be (Array("1","2","4","5"))

      val res1_2 = Await.result(api.get("3.2.1",QueryContext("subj1" -> "value1", "subj2" -> "value2")), 1 second)
      res1_2 should be (Array("1","2","4","5"))

      val res1_1 = Await.result(api.get("3.2.1",QueryContext("subj1" -> "value1", "subj2" -> "some_val")), 1 second)
      res1_1 should be (Array("1","4"))

      val res2 = Await.result(api.get("3.2.1",QueryContext("subj1" -> "value2")), 1 second)
      res2 should be (Array("2","3","5"))

      val res2_1 = Await.result(api.get("3.2.1",QueryContext("subj1" -> "value2", "subj2" -> "value2")), 1 second)
      res2 should be (Array("2","3","5"))

      val res3 = Await.result(api.get("3.2.1",QueryContext("subj2" -> "value2")), 1 second)
      res3 should be (Array("1","2","3","4","5"))
    }
  }
}