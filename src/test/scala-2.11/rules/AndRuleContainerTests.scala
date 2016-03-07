package rules

import byContext.rules.{AndRuleContainer, TextMatch}
import byContext.{QueryContext, ValueRelevancy}
import org.scalatest.{Matchers, WordSpecLike}

class AndRuleContainerTests extends WordSpecLike with Matchers with RulesTestsHelper {
  import ValueRelevancy._
  "AndRuleContainer" must {
    "evaluate to NotRelevant when relevant + notRelevant are passed in" in {
      new AndRuleContainer(relevant, notRelevant).evaluate(QueryContext("subj1"->"v1")) should be (NotRelevant)
      new AndRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v2")).evaluate(QueryContext("subj1"->"v1")) should be (NotRelevant)
    }
    "evaluate to NotRelevant when notRelevant + neutral are passed in" in {
      new AndRuleContainer(neutral, notRelevant).evaluate(QueryContext("subj1"->"v1")) should be (NotRelevant)
      new AndRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v2")).evaluate(QueryContext("subj1"->"v1")) should be (NotRelevant)
    }
    "evaluate to Relevant when relevant + relevant are passed in" in {
      new AndRuleContainer(relevant, relevant).evaluate(QueryContext("subj1"->"v1")) should be (Relevant)
      new AndRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v1")).evaluate(QueryContext("subj1"->"v1")) should be (Relevant)
    }
    "evaluate to relevant when relevant + neutral are passed in" in {
      new AndRuleContainer(relevant, neutral).evaluate(QueryContext("subj1"->"v1")) should be (Relevant)
      new AndRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v1")).evaluate(QueryContext("subj1"->"v1")) should be (Relevant)
    }
    "evaluate to neutral when neutral + neutral are passed in" in {
      new AndRuleContainer(neutral, neutral).evaluate(QueryContext("subj1"->"v1")) should be (Neutral)
      new AndRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj2"->"v3")).evaluate(QueryContext("subj1"->"v1")) should be (Neutral)
    }
  }
}
