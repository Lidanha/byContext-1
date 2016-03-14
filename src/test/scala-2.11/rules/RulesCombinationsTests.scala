package rules

import byContext.ValueRelevancy._
import byContext.data.ScalaCodeDataSource
import byContext.rules._
import byContext.{Probe, QueryContext}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

import scala.collection.mutable.ListBuffer

class RulesCombinationsTests extends WordSpecLike with Matchers with RulesTestsHelper with MockFactory{
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

      rule.evaluate(QueryContext(),probe)
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

      rule.evaluate(QueryContext(),probe)
    }
    "complex from api tests" in {
      val x= new ScalaCodeDataSource{}
      import x._

      val rule = ("subj1" is "value1" and "subj2".isNot("oo") and "subj3".greaterThan(33)) or("ss" is 22)

      val rels = ListBuffer[ValueRelevancy]()

      val p = new Probe {
        override def setRelevancy(r: ValueRelevancy): Unit = rels += r
      }
      rule.evaluate(QueryContext("subj1"->"value1"), p)

      rels.count(_ == Relevant) should be (1)
    }
  }
}
