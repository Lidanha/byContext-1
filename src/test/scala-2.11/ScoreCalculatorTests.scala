import byContext.{PossibleValue, FilterRule, QueryContext, ValueRelevancy}
import ValueRelevancy.ValueRelevancy
import byContext.score._
import org.scalatest.{FunSuite, Matchers}

class ScoreCalculatorTests extends FunSuite with Matchers{
  object Rules{
    val relevant = new AnyRef with FilterRule {
      override def evaluate(ctx: QueryContext): ValueRelevancy = ValueRelevancy.Relevant
    }
    val notRelevant = new AnyRef with FilterRule {
      override def evaluate(ctx: QueryContext): ValueRelevancy = ValueRelevancy.NotRelevant
    }
    val neutral = new AnyRef with FilterRule {
      override def evaluate(ctx: QueryContext): ValueRelevancy = ValueRelevancy.Neutral
    }
  }

  test("a single value with a single relevant rule should be selected"){
    val possibleValue = PossibleValue("v", Array(Rules.relevant))

    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(possibleValue))

    calcResult.size should be (1)
    calcResult.head.score should be (1)
    calcResult.head.value should be ("v")
  }
  test("a single value with a single neutral rule should be selected"){
    val possibleValue = PossibleValue("v", Array(Rules.neutral))

    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(possibleValue))

    calcResult.size should be (1)
    calcResult.head.score should be (0)
    calcResult.head.value should be ("v")
  }
  test("a single value without rules should be selected"){
    val possibleValue = PossibleValue("v", Array.empty[FilterRule])
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(possibleValue))

    calcResult.size should be (1)
    calcResult.head.score should be (0)
    calcResult.head.value should be ("v")
  }
  test("an empty list of values should return an empty calc result"){
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array.empty[PossibleValue])

    calcResult.size should be (0)
  }
  test("a single value with a not relevant rule is not selected"){
    val possibleValue = PossibleValue("a",Array(Rules.notRelevant))
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(possibleValue))

    calcResult.size should be (0)
  }
  test("two values one has a relevant rule and the other with a not relevant rule selects the value with the relevant rule"){
    val relevant = PossibleValue("a",Array(Rules.relevant))
    val notRelevant = PossibleValue("b",Array(Rules.notRelevant))
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(relevant, notRelevant))

    calcResult.size should be (1)
    calcResult.head.score should be (1)
    calcResult.head.value should be ("a")
  }
  test("two values one has a relevant rule and the other a neutral rule selects both, with higher score to the first"){
    val relevant = PossibleValue("a",Array(Rules.relevant))
    val neutral = PossibleValue("b",Array(Rules.neutral))
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(relevant, neutral))

    calcResult.size should be (2)
    val sorted = calcResult.sortBy(_.score).reverse
    sorted.head.score should be (1)
    sorted.head.value should be ("a")

    sorted(1).value should be ("b")
    sorted(1).score should be (0)
  }
  test("for multiple ValueWithScore and allow multiple should return ??"){
    val relevant1 = PossibleValue("a",Array(Rules.relevant))
    val relevant2 = PossibleValue("b",Array(Rules.relevant))
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(relevant1, relevant2))

    calcResult.size should be (2)
    calcResult.head.score should be (1)
    calcResult.head.value should be ("a")

    calcResult(1).score should be (1)
    calcResult(1).value should be ("b")
  }

}