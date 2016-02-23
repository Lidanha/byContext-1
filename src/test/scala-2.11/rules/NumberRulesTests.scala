package rules

import byContext.{ValueRelevancy, QueryContext}
import byContext.rules.NumberEquals
import org.scalatest.{Matchers, WordSpecLike}

class NumberRulesTests extends WordSpecLike with Matchers {
  "NumberEquals" must {
    "" in {
      new NumberEquals[Int]("subj1", 7).evaluate(QueryContext("subj1"->7)) should be (ValueRelevancy.Relevant)
    }
  }
}
