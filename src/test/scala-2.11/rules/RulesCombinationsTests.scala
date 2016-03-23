package rules

import byContext.data.ScalaCodeDataSource
import byContext.model.Probe
import byContext.model.ValueRelevancy._
import byContext.rules._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

import scala.collection.mutable.ListBuffer

class RulesCombinationsTests extends WordSpecLike with Matchers with RulesTestsHelper with MockFactory with Creators{
  "rules combinations" must {
    "or -> and -> simple" in {
      val rule =
        OrRuleContainer(
          AndRuleContainer(
            relevant,
            NotRuleContainer(neutral)
          ),
          relevant
        )

      val probe = mock[Probe]
      (probe setRelevancy _).expects(Neutral).once()
      (probe setRelevancy _).expects(Relevant).twice()

      rule.evaluate(emptyCTX,probe)
    }
    "complex" in {
      val rule =
        OrRuleContainer(
          AndRuleContainer(
            relevant,
            AndRuleContainer(
              NotRuleContainer(notRelevant),
              relevant
            )
          ),
          relevant
        )

      val probe = mock[Probe]
      (probe setRelevancy _).expects(Relevant) repeated 4 times

      rule.evaluate(emptyCTX,probe)
    }
    "complex from api tests" in {
      val x= new ScalaCodeDataSource{}
      import x._

      val rule = ("subj1" is "value1" and "subj2".isNot("oo") and "subj3".greaterThan(33)) or("ss" is 22)

      val rels = ListBuffer[ValueRelevancy]()

      val p = new Probe {
        override def setRelevancy(r: ValueRelevancy): Unit = rels += r
      }
      rule.evaluate(ctx("subj1"->"value1"), p)

      rels.count(_ == Relevant) should be (1)
    }
    "(text match and number smaller than or equals) or (text match and number smaller than or equals)" in {
      val x= new ScalaCodeDataSource{}
      import x._

      val rule = ("subj1" is "value1" and "subjNum".smallerThanOrEquals(10)) or("subj1" is "value2" and "subjNum".smallerThanOrEquals(7))

      val rels = ListBuffer[ValueRelevancy]()

      val p = new Probe {
        override def setRelevancy(r: ValueRelevancy): Unit = rels += r
      }
      rule.evaluate(ctx("subj1"->"value1", "subjNum"->9), p)

      rels.size should be (2)
      rels.count(_ == Relevant) should be (2)
    }
  }
}
