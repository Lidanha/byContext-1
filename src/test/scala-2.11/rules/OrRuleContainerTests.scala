package rules

import byContext.rules.{OrRuleContainer, TextMatch}
import byContext.{QueryContext, ValueRelevancy}
import org.scalatest.{Matchers, WordSpecLike}

class OrRuleContainerTests extends WordSpecLike with Matchers with RulesTestsHelper {
  import ValueRelevancy._
  "OrRuleContainer" must {
    "evaluate to Relevant when relevant + notRelevant are passed in" in {
      new OrRuleContainer(relevant, notRelevant).evaluate(QueryContext()) should be (Relevant)
      new OrRuleContainer(notRelevant, relevant).evaluate(QueryContext()) should be (Relevant)
      new OrRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v2")).evaluate(QueryContext("subj1"->"v1")) should be (Relevant)
    }
    "evaluate to NotRelevant when notRelevant + neutral are passed in" in {
      new OrRuleContainer(neutral, notRelevant).evaluate(QueryContext("subj1"->"v1")) should be (NotRelevant)
      new OrRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v2")).evaluate(QueryContext("subj1"->"v1")) should be (NotRelevant)
    }
    "evaluate to Relevant when relevant + relevant are passed in" in {
      new OrRuleContainer(relevant, relevant).evaluate(QueryContext("subj1"->"v1")) should be (Relevant)
      new OrRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v1")).evaluate(QueryContext("subj1"->"v1")) should be (Relevant)
    }
    "evaluate to relevant when relevant + neutral are passed in" in {
      new OrRuleContainer(relevant, neutral).evaluate(QueryContext("subj1"->"v1")) should be (Relevant)
      new OrRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v1")).evaluate(QueryContext("subj1"->"v1")) should be (Relevant)
    }
    "evaluate to neutral when neutral + neutral are passed in" in {
      new OrRuleContainer(neutral, neutral).evaluate(QueryContext("subj1"->"v1")) should be (Neutral)
      new OrRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj2"->"v3")).evaluate(QueryContext("subj1"->"v1")) should be (Neutral)
    }
  }
}
