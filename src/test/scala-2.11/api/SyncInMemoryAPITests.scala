package api

import byContext.api.{QueryBuilder, SyncInMemoryAPI}
import byContext.data.ScalaCodeDataSource
import byContext.score.DefaultScoreCalculator
import byContext.{RecursiveQueryHandler, SimpleMapDataIndex}
import org.scalatest.{Matchers, WordSpecLike}
import rules.ContextHelper

import scala.concurrent.{ExecutionContext, Await}
import scala.concurrent.duration._
class SyncInMemoryAPITests extends WordSpecLike with Matchers with ScalaCodeDataSource with ContextHelper{
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
        "2"->filterSingle(
          "1" relevantWhen("subj1" is "value1"),
          "2" relevantWhen("subj2" is "value2"),
          "3" relevantWhen("subj2" isNot "value2")
        )(true),
        "3"->filterSingle(
          "1" relevantWhen("subj1" isNot "value1")
        )(true)
      ),
      "2"-> Map(
        "1" -> filterArray(
          "1" relevantWhen("subj1" is "value1"),
          "2" relevantWhen("subj2" is "value2"),
          "3" relevantWhen("subj1" is "value2"),
          "4" relevantWhen("subj1" is "value1"),
          "5" relevantWhen("subj2" is "value2")
        )(1)
      )
    ),
    "4"->filterSingle(
      "1".setAs.defaultValue.withRules(("subj1" is "value1" and "subj2".isNot("oo")) or("ss" is 22)),
      "2".withRules(("subj1" is "value1" and "subj2".is("oo")) or("ss" isNot 22))
    )(true),
    "5"->filterSingle(
      "1".setAs.defaultValue.withRules(("subj1" is "value1" and "subj2".isNot("oo")) or("ss" is 22)),
      "2".withRules(("subj1" is "value1" and "subj2".is("oo")) or("ss" isNot 22)),
      "3".withRules(("subj1" is "value1" and "subj2".isNot("oo")) or(("ss" is 22) and ("subj3" is 34)))
    )(true)
  ))
  implicit val ec = ExecutionContext.global

  val api = new SyncInMemoryAPI(simpleIndex,new RecursiveQueryHandler())
  "SyncInMemoryAPI with RecursiveQueryHandler" must {
    "return simple raw value a couple of levels deep" in {

      val res = Await.result(api.get("3.1.1",QueryBuilder()), 1 second)
      res should be ("3.1.1")
    }
    "select the relevant value a couple of levels deep" in {
      
      val res = Await.result(api.get("3.1.2",new QueryBuilder{item("subj1" -> "value1")}), 1 second)
      res should be ("1")
    }
    "select the relevant value with a NOT container rule" in {
      val res = Await.result(api.get("3.1.2",new QueryBuilder{item("subj2" -> "value1")}), 1 second)
      res should be ("3")
    }
    "select the relevant values of an array" in {
      val res1 = Await.result(api.get("3.2.1",new QueryBuilder{item("subj1" -> "value1")}), 1 second)
      res1 should be (Array("1","2","4","5"))

      val res1_2 = Await.result(api.get("3.2.1",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2" -> "value2")
      }), 1 second)
      res1_2 should be (Array("1","2","4","5"))

      val res1_1 = Await.result(api.get("3.2.1",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2" -> "some_val")
      }), 1 second)
      res1_1 should be (Array("1","4"))

      val res2 = Await.result(api.get("3.2.1",new QueryBuilder{item("subj1" -> "value2")}), 1 second)
      res2 should be (Array("2","3","5"))

      val res2_1 = Await.result(api.get("3.2.1",new QueryBuilder{
        item("subj1" -> "value2")
        item("subj2" -> "value2")
      }), 1 second)
      res2 should be (Array("2","3","5"))

      val res3 = Await.result(api.get("3.2.1",new QueryBuilder{item("subj2" -> "value2")}), 1 second)
      res3 should be (Array("1","2","3","4","5"))
    }
    "complex and or combination" in {
      Await.result(api.get("4",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2"->"some-value")
        item("ss"->23)
      }), 1 second) should be ("1")
      Await.result(api.get("4",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2"->"oo")
      }), 1 second) should be ("2")
      Await.result(api.get("4",new QueryBuilder{item("ss"->22)}), 1 second) should be ("1")
      Await.result(api.get("4",new QueryBuilder{item("ss"->2)}), 1 second) should be ("2")
      Await.result(api.get("4",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2"->"aa")
        item("subj3"->34)
      }), 1 second) should be ("1")
    }
    "complex and or combination - 2" in {
      val api1 = new SyncInMemoryAPI(new SimpleMapDataIndex(Map(
        "1" -> filterSingle(
          "1".withRules(("subj1" is "value1" and "subj2".isNot("oo") and "subj3".greaterThan(33)) or("ss" is 22))
        )(true)
      )),new RecursiveQueryHandler())
      Await.result(api1.get("1",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2"->"aa")
        item("subj3"->34)
      }), 1 second) should be ("1")
    }
  }
}