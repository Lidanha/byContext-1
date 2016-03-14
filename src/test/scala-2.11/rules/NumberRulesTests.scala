package rules

import byContext.{Probe, QueryContext}
import byContext.ValueRelevancy._
import byContext.rules.NumberEquals
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class NumberRulesTests extends WordSpecLike with Matchers with MockFactory with RulesTestsHelper{
  "NumberEquals" must {
    "" in {
      val p = mock[Probe]
      (p setRelevancy _).expects(Relevant).once()

      new NumberEquals[Int]("subj1", 7).evaluate(QueryContext("subj1"->7),p)
    }
  }
}
