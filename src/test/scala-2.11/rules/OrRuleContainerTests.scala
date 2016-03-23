package rules

import byContext.model.{Probe, ValueRelevancy}
import byContext.rules.{OrRuleContainer, TextMatch}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class OrRuleContainerTests extends WordSpecLike with Matchers with RulesTestsHelper with MockFactory with Creators{
  import ValueRelevancy._
  "OrRuleContainer" must {
    "evaluate to Relevant when relevant + notRelevant are passed in" in {
      def p = {
        val p = mock[Probe]
        (p setRelevancy _).expects(Relevant).once()
        p
      }
      new OrRuleContainer(relevant, notRelevant).evaluate(emptyCTX, p)
      new OrRuleContainer(notRelevant, relevant).evaluate(emptyCTX,p)
      new OrRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v2")).evaluate(ctx("subj1"->"v1"),p)
    }
    "evaluate to NotRelevant when notRelevant + neutral are passed in" in {
      def p = {
        val p = mock[Probe]
        (p setRelevancy _).expects(NotRelevant).once()
        (p setRelevancy _).expects(Neutral).once()
        p
      }

      new OrRuleContainer(neutral, notRelevant).evaluate(ctx("subj1"->"v1"),p)
      new OrRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v2")).evaluate(ctx("subj1"->"v1"),p)
    }
    "evaluate to Relevant when relevant + relevant are passed in" in {
      def p = {
        val p = mock[Probe]
        (p setRelevancy _).expects(Relevant).twice()
        p
      }

      new OrRuleContainer(relevant, relevant).evaluate(ctx("subj1"->"v1"),p)
      new OrRuleContainer(TextMatch("subj1"->"v1"), TextMatch("subj1"->"v1")).evaluate(ctx("subj1"->"v1"),p)
    }
    "evaluate to relevant when relevant + neutral are passed in" in {
      def p = {
        val p = mock[Probe]
        (p setRelevancy _).expects(Relevant).once()
        (p setRelevancy _).expects(Neutral).once()
        p
      }

      new OrRuleContainer(relevant, neutral).evaluate(ctx("subj1"->"v1"),p)
      new OrRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj1"->"v1")).evaluate(ctx("subj1"->"v1"),p)
    }
    "evaluate to neutral when neutral + neutral are passed in" in {
      def p = {
        val p = mock[Probe]
        (p setRelevancy _).expects(Neutral).twice()
        p
      }

      new OrRuleContainer(neutral, neutral).evaluate(ctx("subj1"->"v1"),p)
      new OrRuleContainer(TextMatch("subj2"->"v1"), TextMatch("subj2"->"v3")).evaluate(ctx("subj1"->"v1"),p)
    }
  }
}
