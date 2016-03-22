package rules

import byContext.api.QueryBuilder
import byContext.model.{Probe, ValueRelevancy}
import byContext.rules.{AndRuleContainer, TextMatch}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class AndRuleContainerTests extends WordSpecLike with Matchers with MockFactory with RulesTestsHelper with ContextHelper {
  import ValueRelevancy._
  "AndRuleContainer" must {
    "evaluate to NotRelevant when relevant + notRelevant are passed in" in {
      def p = {
        val probe: Probe = mock[Probe]
        (probe setRelevancy _).expects(NotRelevant).once()
        probe
      }

      new AndRuleContainer(relevant, notRelevant).evaluate(new QueryBuilder{item("subj1"->"v1")},p)
      new AndRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v2"))
        .evaluate(new QueryBuilder{item("subj1"->"v1")},p)
    }
    "evaluate to NotRelevant when notRelevant + neutral are passed in" in {
      def p = {
        val probe: Probe = mock[Probe]
        (probe setRelevancy _).expects(NotRelevant).once()
        probe
      }

      new AndRuleContainer(neutral, notRelevant).evaluate(new QueryBuilder{item("subj1"->"v1")},p)
      new AndRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v2"))
        .evaluate(new QueryBuilder{item("subj1"->"v1")},p)
    }
    "evaluate to Relevant when relevant + relevant are passed in" in {
      def p = {
        val probe: Probe = mock[Probe]
        (probe setRelevancy _).expects(Relevant).twice()
        probe
      }

      new AndRuleContainer(relevant, relevant).evaluate(new QueryBuilder{item("subj1"->"v1")},p)
      new AndRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v1"))
        .evaluate(new QueryBuilder{item("subj1"->"v1")},p)
    }
    "evaluate to relevant when relevant + neutral are passed in" in {
      def p = {
        val probe: Probe = mock[Probe]
        (probe setRelevancy _).expects(Relevant).once()
        (probe setRelevancy _).expects(Neutral).once()
        probe
      }

      new AndRuleContainer(relevant, neutral).evaluate(new QueryBuilder{item("subj1"->"v1")},p)
      new AndRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v1"))
        .evaluate(new QueryBuilder{item("subj1"->"v1")},p)
    }
    "evaluate to neutral when neutral + neutral are passed in" in {
      def p = {
        val probe: Probe = mock[Probe]
        (probe setRelevancy _).expects(Neutral).twice()
        probe
      }

      new AndRuleContainer(neutral, neutral).evaluate(new QueryBuilder{item("subj1"->"v1")},p)
      new AndRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj2"->"v3"))
        .evaluate(new QueryBuilder{item("subj1"->"v1")},p)
    }
  }
}
