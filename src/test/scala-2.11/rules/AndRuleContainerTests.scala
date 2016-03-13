package rules

import byContext.rules.{AndRuleContainer, TextMatch}
import byContext.{QueryContext, ValueRelevancy}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class AndRuleContainerTests extends WordSpecLike with Matchers with MockFactory with RulesTestsHelper {
  import ValueRelevancy._
  "AndRuleContainer" must {
    "evaluate to NotRelevant when relevant + notRelevant are passed in" in {
      new AndRuleContainer(relevant, notRelevant).evaluate(QueryContext("subj1"->"v1"),probeThatExpects(NotRelevant))
      new AndRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v2"))
        .evaluate(QueryContext("subj1"->"v1"),probeThatExpects(NotRelevant))
    }
    "evaluate to NotRelevant when notRelevant + neutral are passed in" in {
      new AndRuleContainer(neutral, notRelevant).evaluate(QueryContext("subj1"->"v1"),probeThatExpects(NotRelevant))
      new AndRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v2"))
        .evaluate(QueryContext("subj1"->"v1"),probeThatExpects(NotRelevant))
    }
    "evaluate to Relevant when relevant + relevant are passed in" in {
      new AndRuleContainer(relevant, relevant).evaluate(QueryContext("subj1"->"v1"),probeThatExpects(Relevant))
      new AndRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v1"))
        .evaluate(QueryContext("subj1"->"v1"),probeThatExpects(Relevant))
    }
    "evaluate to relevant when relevant + neutral are passed in" in {
      new AndRuleContainer(relevant, neutral).evaluate(QueryContext("subj1"->"v1"),probeThatExpects(Relevant))
      new AndRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v1"))
        .evaluate(QueryContext("subj1"->"v1"),probeThatExpects(Relevant))
    }
    "evaluate to neutral when neutral + neutral are passed in" in {
      new AndRuleContainer(neutral, neutral).evaluate(QueryContext("subj1"->"v1"),probeThatExpects(Neutral))
      new AndRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj2"->"v3"))
        .evaluate(QueryContext("subj1"->"v1"),probeThatExpects(Relevant))
    }
  }
}
