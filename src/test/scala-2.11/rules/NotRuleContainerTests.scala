package rules

import byContext.QueryContext
import byContext.rules.NotRuleContainer
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class NotRuleContainerTests extends WordSpecLike with Matchers with RulesTestsHelper with MockFactory {
  import byContext.ValueRelevancy._
  "NotRuleContainer" must {
    "evaluate to relevant in internal rule evaluates to NotRelevant" in {
      new NotRuleContainer(notRelevant).evaluate(QueryContext(),probeThatExpects(Relevant))
    }
    "evaluate to NotRelevant in internal rule evaluates to Relevant" in {
      new NotRuleContainer(relevant).evaluate(QueryContext(),probeThatExpects(NotRelevant))
    }
    "evaluate to Neutral in internal rule evaluates to Neutral" in {
      new NotRuleContainer(neutral).evaluate(QueryContext(),probeThatExpects(Neutral))
    }
  }
}
