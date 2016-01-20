package valueContainers

import byContext.score._
import byContext.score.valueContainers.DefaultSingleValueContainer
import org.scalamock.scalatest.MockFactory
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class DefaultSingleValueContainerTests extends WordSpecLike with Matchers with MockFactory with EitherValues {
  val emptyctx = QueryContext()
  val emptyValues = Array.empty[PossibleValue]

  def calc(values: Array[Any]): ScoreCalculator = {
    val calculator = stub[ScoreCalculator]
    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx, emptyValues)
      .returns(values.map(ValueWithScore(_, 1)))
    calculator
  }

  def single(values: Array[Any], required: Boolean): DefaultSingleValueContainer = {
    new DefaultSingleValueContainer(calc(values), emptyValues, required)
  }

  "DefaultSingleValueContainer" must {
    "return a right with a single value when score calculator returns a single result" in {
      single(Array("a"), true).get(emptyctx).right.value should be("a")
    }
    "return a left with MultipleValuesNotAllowedError when score calculator returns multiple values" in {
      single(Array("a", "a"), true).get(emptyctx).left.value shouldBe a[MultipleValuesNotAllowedError]
    }
    "return a left with RequiredValueMissingError when score calculator returns no values and a value is required" in {
      single(Array.empty[Any], true).get(emptyctx).left.value shouldBe a[RequiredValueMissingError]
    }
    "return a right with None when score calculator returns no values and a value is not required" in {
      single(Array.empty[Any], false).get(emptyctx).right.value should be(None)
    }
  }
}
