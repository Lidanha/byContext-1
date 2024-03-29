package api

import byContext.MetadataCollectorExtensionFactory
import byContext.api.{EmbeddedAPIBuilder, QueryBuilder}
import byContext.data.ScalaCodeDataSource
import byContext.score.DefaultScoreCalculator
import org.scalatest.{Matchers, WordSpecLike}
import rules.{WireupHelpers}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
class SyncInMemoryAPITests extends WordSpecLike with Matchers with ScalaCodeDataSource with WireupHelpers{
  implicit val scoreCalculator = new DefaultScoreCalculator()
  val globals = Map(
    "simple-string-to-interpolate" -> "simple-string",
    "filtered-string-to-interpolate" -> Map(
      "1"->Map(
        "4"->filterSingle(
          "inter1"  withRules("subj1" is "v1"),
          "inter2"  withRules("subj1" is "v2"),
          ""        withRules("sub1" is "v3")
        )(true)
      )
    ),
    "value-to-be-referenced" -> "value-to-be-referenced"
  )
  val dataSet = Map(
    "1"->"1",
    "2"->Map(
      "1"->"2.1",
      "2"->"2.2",
      "3"->"to be interpolated"
    ),
    "3"->Map(
      "1"->Map(
        "1"->"3.1.1",
        "2"->filterSingle(
          "1".withRules("subj1" is "value1"),
          "2".withRules("subj2" is "value2"),
          "3".withRules("subj2" isNot "value2")
        )(true),
        "3"->filterSingle(
          "1".withRules("subj1" isNot "value1")
        )(true)
      ),
      "2"-> Map(
        "1" -> filterArray(
          "1" withRules("subj1" is "value1") withMetadata("ver"->1,"id"->"id_1"),
          "2" withRules("subj2" is "value2") withMetadata("ver"->2,"id"->"id_2"),
          "3" withRules("subj1" is "value2") withMetadata("ver"->3,"id"->"id_3"),
          "4" withRules("subj1" is "value1") withMetadata("ver"->4,"id"->"id_4"),
          "5" withRules("subj2" is "value2") withMetadata("ver"->5,"id"->"id_5")
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
    )(true),
    "6" -> filterSingle(
      "1" withRules(("subj1" is "value1" and "subjNum".smallerThanOrEquals(10)) or("subj1" is "value2" and "subjNum".smallerThanOrEquals(7))),
      "2".setAs.defaultValue
    )(true),
    /*"ref-supported-for-globals"->valueRef("3"),*/
    "ref"->valueRef("value-to-be-referenced"),
    "stringInterpolation" -> interpolated("test interpolated <<simple-string-to-interpolate>> !!!"),
    "stringInterpolation_filtered" -> interpolated("test interpolated <<filtered-string-to-interpolate.1.4>> !!!")
  )

  implicit val ec = ExecutionContext.global

  val api = EmbeddedAPIBuilder(dataSet,Some(globals))
  "SyncInMemoryAPI with RecursiveQueryHandler" must {
    "return simple raw value a couple of levels deep" in {
      val res = Await.result(api.get("3.1.1",QueryBuilder()), 1 second)
      res should be (Map("query-result"->"3.1.1"))
    }
    "select the relevant value a couple of levels deep" in {
      
      val res = Await.result(api.get("3.1.2",new QueryBuilder{item("subj1" -> "value1")}), 1 second)
      res should be (Map("query-result"->"1"))
    }
    "select the relevant value with a NOT container rule" in {
      val res = Await.result(api.get("3.1.2",new QueryBuilder{item("subj2" -> "value1")}), 1 second)
      res should be (Map("query-result"->"3"))
    }
    "select the relevant values of an array" in {
      val res1 = Await.result(api.get("3.2.1",new QueryBuilder{item("subj1" -> "value1")}), 1 second)
      res1 should be (Map("query-result"->Iterable("1","2","4","5")))

      val res1_2 = Await.result(api.get("3.2.1",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2" -> "value2")
      }), 1 second)
      res1_2 should be (Map("query-result"->Iterable("1","2","4","5")))

      val res1_1 = Await.result(api.get("3.2.1",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2" -> "some_val")
      }), 1 second)
      res1_1 should be (Map("query-result"->Iterable("1","4")))

      val res2 = Await.result(api.get("3.2.1",new QueryBuilder{item("subj1" -> "value2")}), 1 second)
      res2 should be (Map("query-result"->Iterable("2","3","5")))

      val res2_1 = Await.result(api.get("3.2.1",new QueryBuilder{
        item("subj1" -> "value2")
        item("subj2" -> "value2")
      }), 1 second)
      res2 should be (Map("query-result"->Iterable("2","3","5")))

      val res3 = Await.result(api.get("3.2.1",new QueryBuilder{item("subj2" -> "value2")}), 1 second)
      res3 should be (Map("query-result"->Iterable("1","2","3","4","5")))
    }
    "complex and or combination" in {
      Await.result(api.get("4",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2"->"some-value")
        item("ss"->23)
      }), 1 second) should be (Map("query-result"->"1"))

      Await.result(api.get("4",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2"->"oo")
      }), 1 second) should be (Map("query-result"->"2"))

      Await.result(api.get("4",new QueryBuilder{item("ss"->22)}), 1 second) should be (Map("query-result"->"1"))
      Await.result(api.get("4",new QueryBuilder{item("ss"->2)}), 1 second) should be (Map("query-result"->"2"))

      Await.result(api.get("4",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2"->"aa")
        item("subj3"->34)
      }), 1 second) should be (Map("query-result"->"1"))
    }
    "complex and or combination - 2" in {
      val api1 = EmbeddedAPIBuilder(Map(
        "1" -> filterSingle(
          "1".withRules(("subj1" is "value1" and "subj2".isNot("oo") and "subj3".greaterThan(33)) or("ss" is 22))
        )(true)
      ))
      Await.result(api1.get("1",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2"->"aa")
        item("subj3"->34)
      }), 1 second) should be (Map("query-result"->"1"))
    }
    "(text match and number smaller than or equals) or (text match and number smaller than or equals)" in {
      Await.result(api.get("4",new QueryBuilder{
        item("subj1"->"value1")
        item("subjNum"->7)
      }), 1 second) should be (Map("query-result"->"1"))
    }
    "value ref" in {
      val res = Await.result(api.get("ref",new QueryBuilder{
        item("subj1" -> "value1")
        item("subj2" -> "some_val")
      }), 1 second)
      res should be (Map("query-result"->"value-to-be-referenced"))
    }
    "string interpolation" in {
      val res = Await.result(api.get("stringInterpolation",new QueryBuilder()), 1 second)
      res should be (Map("query-result"->"test interpolated simple-string !!!"))
    }
    "string interpolation with interpolated value filtered" in {
      val res = Await.result(api.get("stringInterpolation_filtered",new QueryBuilder{
        item("subj1"->"v1")
      }), 1 second)
      res should be (Map("query-result"->"test interpolated inter1 !!!"))
    }
    "string interpolation with empty and filtered interpolated value" in {
      val res = Await.result(api.get("stringInterpolation_filtered",new QueryBuilder{
        item("subj1"->"v3")
      }), 1 second)
      res should be (Map("query-result"->"test interpolated  !!!"))
    }
    "collect metadata version field" in {

      val api1 = EmbeddedAPIBuilder(dataSet,Some(globals),Map("version"->new MetadataCollectorExtensionFactory("ver")))

      val query = new QueryBuilder{item("subj1" -> "value1")} withExtensions Seq("version")

      val res1 = Await.result(api1.get("3.2.1",query), 1 second)

      res1.get("query-result") should be (Some(Iterable("1","2","4","5")))
      res1.get("version") should be (Some(Map(
        "3.2.1 [0]"->1,
        "3.2.1 [1]"->2,
        "3.2.1 [2]"->4,
        "3.2.1 [3]"->5
      ))
      )
    }
    "collect metadata id field" in {

      val api1 = EmbeddedAPIBuilder(dataSet,Some(globals),Map("id-collector"->new MetadataCollectorExtensionFactory("id")))

      val query = new QueryBuilder{item("subj1" -> "value1")} withExtensions Seq("id-collector")

      val res1 = Await.result(api1.get("3.2.1",query), 1 second)

      res1.get("query-result") should be (Some(Iterable("1","2","4","5")))
      res1.get("id-collector") should be (Some(Map(
        "3.2.1 [0]"->"id_1",
        "3.2.1 [1]"->"id_2",
        "3.2.1 [2]"->"id_4",
        "3.2.1 [3]"->"id_5"
      ))
      )
    }
    "collect metadata id and version fields" in {

      val api1 = EmbeddedAPIBuilder(dataSet,Some(globals),Map(
        "id-collector"->new MetadataCollectorExtensionFactory("id"),
        "version"->new MetadataCollectorExtensionFactory("ver")
      ))

      val query = new QueryBuilder{item("subj1" -> "value1")} withExtensions Seq("id-collector", "version")

      val res1 = Await.result(api1.get("3.2.1",query), 1 second)

      res1.get("query-result") should be (Some(Iterable("1","2","4","5")))
      res1.get("id-collector") should be (Some(Map(
        "3.2.1 [0]"->"id_1",
        "3.2.1 [1]"->"id_2",
        "3.2.1 [2]"->"id_4",
        "3.2.1 [3]"->"id_5"
      ))
      )
      res1.get("version") should be (Some(Map(
        "3.2.1 [0]"->1,
        "3.2.1 [1]"->2,
        "3.2.1 [2]"->4,
        "3.2.1 [3]"->5
      )))
    }
    /*"string interpolation is supported only for globals" in {
      intercept[RuntimeException]{
        val res = Await.result(api.get("stringInterpolation_filtered",new QueryBuilder{
          item("subj1"->"v3")
        }), 1 second)
        res should be ("test interpolated  !!!")
      }
    }
    "value ref is supported only for globals" in {
      intercept[RuntimeException]{
        val res = Await.result(api.get("ref",new QueryBuilder{
          item("subj1" -> "value1")
          item("subj2" -> "some_val")
        }), 1 second)
        res should be (Array("1","4"))
      }
    }*/
  }
}