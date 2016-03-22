import byContext.model.PossibleValue
import byContext.score.DefaultScoreCalculator
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}
import rules.{ContextHelper, RulesTestsHelper}

class ScoreCalculatorTests extends WordSpecLike with Matchers with RulesTestsHelper with MockFactory with ContextHelper{
  "ScoreCalculator" must {
    "a single value with a single relevant rule should be selected" in {
      val calcResult = new DefaultScoreCalculator().calculate(emptyContext, Array(PossibleValue("v", Some(relevant))))

      calcResult.size should be (1)
      calcResult.head.score should be (1)
      calcResult.head.value should be ("v")
    }
    "a single value with a single neutral rule should be selected" in {
      val calcResult = new DefaultScoreCalculator().calculate(emptyContext, Array(PossibleValue("v", Some(neutral))))

      calcResult.size should be (1)
      calcResult.head.score should be (0)
      calcResult.head.value should be ("v")
    }
    "a single value without rules should be selected" in {
      val calcResult = new DefaultScoreCalculator().calculate(emptyContext, Array(PossibleValue("v", None)))

      calcResult.size should be (1)
      calcResult.head.score should be (0)
      calcResult.head.value should be ("v")
    }
    "an empty list of values should return an empty calc result" in {
      val calcResult = new DefaultScoreCalculator().calculate(emptyContext, Array.empty[PossibleValue])

      calcResult.size should be (0)
    }
    "a single value with a not relevant rule is not selected" in {
      val calcResult = new DefaultScoreCalculator().calculate(emptyContext, Array(PossibleValue("a",Some(notRelevant))))

      calcResult.size should be (0)
    }
    "two values one has a relevant rule and the other with a not relevant rule selects the value with the relevant rule" in {
      val calcResult = new DefaultScoreCalculator().calculate(emptyContext,
        Array(PossibleValue("a",Some(relevant)), PossibleValue("b",Some(notRelevant))))

      calcResult.size should be (1)
      calcResult.head.score should be (1)
      calcResult.head.value should be ("a")
    }
    "two values one has a relevant rule and the other a neutral rule selects both, with higher score to the first" in {
      val calcResult = new DefaultScoreCalculator().calculate(emptyContext,
        Array(PossibleValue("a",Some(relevant)), PossibleValue("b",Some(neutral))))

      calcResult.size should be (2)
      val sorted = calcResult.sortBy(_.score).reverse
      sorted.head.score should be (1)
      sorted.head.value should be ("a")

      sorted(1).value should be ("b")
      sorted(1).score should be (0)
    }
    "two relevant values, return both" in {
      val calcResult = new DefaultScoreCalculator().calculate(emptyContext,
        Array(PossibleValue("a",Some(relevant)), PossibleValue("b",Some(relevant))))

      calcResult.size should be (2)
      calcResult.head.score should be (1)
      calcResult.head.value should be ("a")

      calcResult(1).score should be (1)
      calcResult(1).value should be ("b")
    }
  }
}