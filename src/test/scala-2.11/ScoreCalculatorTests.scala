import byContext.score.ValueRelevancy.ValueRelevancy
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
    val possibleValue = PossibleValue(SingleValue("v"), Array(Rules.relevant))

    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(possibleValue))

    calcResult.size should be (1)
    calcResult.head.score should be (1)
    calcResult.head.value should be (SingleValue("v"))
  }
  test("a single value with a single neutral rule should be selected"){
    val possibleValue = PossibleValue(SingleValue("v"), Array(Rules.neutral))

    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(possibleValue))

    calcResult.size should be (1)
    calcResult.head.score should be (0)
    calcResult.head.value should be (SingleValue("v"))
  }
  test("a single value without rulesshould be selected"){
    val possibleValue = PossibleValue(SingleValue("v"), Array.empty[FilterRule])
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(possibleValue))

    calcResult.size should be (1)
    calcResult.head.score should be (0)
    calcResult.head.value should be (SingleValue("v"))
  }
  test("an empty list of values should return an empty calc result"){
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array.empty[PossibleValue])

    calcResult.size should be (0)
  }
  test("a single value with a not relevant rule is not selected"){
    val possibleValue = PossibleValue(SingleValue("a"),Array(Rules.notRelevant))
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(possibleValue))

    calcResult.size should be (0)
  }
  test("two values one has a relevant rule and the other with a not relevant rule selects the value with the relevant rule"){
    val relevant = PossibleValue(SingleValue("a"),Array(Rules.relevant))
    val notRelevant = PossibleValue(SingleValue("b"),Array(Rules.notRelevant))
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(relevant, notRelevant))

    calcResult.size should be (1)
    calcResult.head.score should be (1)
    calcResult.head.value should be (SingleValue("a"))
  }
  test("two values one has a relevant rule and the other a neutral rule selects both, with higher score to the first"){
    val relevant = PossibleValue(SingleValue("a"),Array(Rules.relevant))
    val neutral = PossibleValue(SingleValue("b"),Array(Rules.neutral))
    val calcResult = new DefaultScoreCalculator().calculateScoreForRelevantValues(QueryContext(), Array(relevant, neutral))

    calcResult.size should be (2)
    val sorted = calcResult.sortBy(_.score).reverse
    sorted.head.score should be (1)
    sorted.head.value should be (SingleValue("a"))

    sorted(1).value should be (SingleValue("b"))
    sorted(1).score should be (0)
  }
}