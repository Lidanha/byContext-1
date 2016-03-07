import byContext.score._
import byContext.{FilterRule, PossibleValue, QueryContext}
import org.scalatest.{FunSuite, Matchers}
import rules.RulesTestsHelper

class ScoreCalculatorTests extends FunSuite with Matchers with RulesTestsHelper{
  test("a single value with a single relevant rule should be selected"){
    val calcResult = new DefaultScoreCalculator().calculate(QueryContext(), Array(PossibleValue("v", Array(relevant))))

    calcResult.size should be (1)
    calcResult.head.score should be (1)
    calcResult.head.value should be ("v")
  }
  test("a single value with a single neutral rule should be selected"){
    val calcResult = new DefaultScoreCalculator().calculate(QueryContext(), Array(PossibleValue("v", Array(neutral))))

    calcResult.size should be (1)
    calcResult.head.score should be (0)
    calcResult.head.value should be ("v")
  }
  test("a single value without rules should be selected"){
    val calcResult = new DefaultScoreCalculator().calculate(QueryContext(), Array(PossibleValue("v", Array.empty[FilterRule])))

    calcResult.size should be (1)
    calcResult.head.score should be (0)
    calcResult.head.value should be ("v")
  }
  test("an empty list of values should return an empty calc result"){
    val calcResult = new DefaultScoreCalculator().calculate(QueryContext(), Array.empty[PossibleValue])

    calcResult.size should be (0)
  }
  test("a single value with a not relevant rule is not selected"){
    val calcResult = new DefaultScoreCalculator().calculate(QueryContext(), Array(PossibleValue("a",Array(notRelevant))))

    calcResult.size should be (0)
  }
  test("two values one has a relevant rule and the other with a not relevant rule selects the value with the relevant rule"){
    val calcResult = new DefaultScoreCalculator().calculate(QueryContext(),
      Array(PossibleValue("a",Array(relevant)), PossibleValue("b",Array(notRelevant))))

    calcResult.size should be (1)
    calcResult.head.score should be (1)
    calcResult.head.value should be ("a")
  }
  test("two values one has a relevant rule and the other a neutral rule selects both, with higher score to the first"){
    val calcResult = new DefaultScoreCalculator().calculate(QueryContext(),
      Array(PossibleValue("a",Array(relevant)), PossibleValue("b",Array(neutral))))

    calcResult.size should be (2)
    val sorted = calcResult.sortBy(_.score).reverse
    sorted.head.score should be (1)
    sorted.head.value should be ("a")

    sorted(1).value should be ("b")
    sorted(1).score should be (0)
  }
  test("two relevant values, return both"){
    val calcResult = new DefaultScoreCalculator().calculate(QueryContext(),
      Array(PossibleValue("a",Array(relevant)), PossibleValue("b",Array(relevant))))

    calcResult.size should be (2)
    calcResult.head.score should be (1)
    calcResult.head.value should be ("a")

    calcResult(1).score should be (1)
    calcResult(1).value should be ("b")
  }

}