package valueContainers

import byContext._
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
  def selector() = new DefaultValueSelector {
    override def select(valuesWithScore: Iterable[ValueWithScore]): Either[CouldNotSelectDefaultValueError, ValueWithScore] = {
      Right(valuesWithScore.head)
    }
  }

  def single(values: Array[Any], required: Boolean): DefaultSingleValueContainer = {
    new DefaultSingleValueContainer(calc(values), emptyValues, selector(), required)
  }

  "DefaultSingleValueContainer" must {
    "return a right with a single value when score calculator returns a single result" in {
      single(Array("a"), true).get(emptyctx).right.value should be("a")
    }
    "test the case that more than one value returns" in {
      ???
    }
    "return a left with RequiredValueMissingError when score calculator returns no values and a value is required" in {
      single(Array.empty[Any], true).get(emptyctx).left.value shouldBe a[RequiredValueMissingError]
    }
    "return a right with None when score calculator returns no values and a value is not required" in {
      single(Array.empty[Any], false).get(emptyctx).right.value should be(None)
    }
  }
}
