package rules

import byContext.model.{Probe, ValueRelevancy}
import byContext.rules.NotRuleContainer
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class NotRuleContainerTests extends WordSpecLike with Matchers with RulesTestsHelper with MockFactory with Creators{
  import ValueRelevancy._
  "NotRuleContainer" must {
    "evaluate to relevant in internal rule evaluates to NotRelevant" in {
      val p = mock[Probe]
      (p setRelevancy _).expects(Relevant).once()
      new NotRuleContainer(notRelevant).evaluate(emptyCTX,p)
    }
    "evaluate to NotRelevant in internal rule evaluates to Relevant" in {
      val p = mock[Probe]
      (p setRelevancy _).expects(NotRelevant).once()
      new NotRuleContainer(relevant).evaluate(emptyCTX,p)
    }
    "evaluate to Neutral in internal rule evaluates to Neutral" in {
      val p = mock[Probe]
      (p setRelevancy _).expects(Neutral).once()

      new NotRuleContainer(neutral).evaluate(emptyCTX,p)
    }
  }
}
