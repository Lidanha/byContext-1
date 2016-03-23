package rules

import byContext.model.Probe
import byContext.model.ValueRelevancy._
import byContext.rules.{NumberEquals, NumberGreaterThan}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class NumberRulesTests extends WordSpecLike with Matchers with MockFactory with RulesTestsHelper 
  with Creators{
  "NumberEquals" must {
    "simple" in {
      val p = mock[Probe]
      (p setRelevancy _).expects(Relevant).once()

      new NumberEquals("subj1", 7).evaluate(ctx("subj1"->7),p)
    }
  }
  "NumberGreaterThan" must {
    "report relevant when number is greater than the supplied number" in {
      val p = mock[Probe]
      (p setRelevancy _).expects(Relevant).once()

      new NumberGreaterThan("subj",1).evaluate(ctx("subj"->2),p)
    }
    "report not relevant when number equals to the supplied number" in {
      val p = mock[Probe]
      (p setRelevancy _).expects(NotRelevant).once()

      new NumberGreaterThan("subj",1).evaluate(ctx("subj"->1),p)
    }
    "report not relevant when number smaller than the supplied number" in {
      val p = mock[Probe]
      (p setRelevancy _).expects(NotRelevant).once()

      new NumberGreaterThan("subj",2).evaluate(ctx("subj"->1),p)
    }
  }
}
