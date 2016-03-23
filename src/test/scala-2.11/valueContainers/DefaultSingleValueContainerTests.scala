package valueContainers

import _root_.rules.{Creators}
import byContext.defaultValueSelection.DefaultValueSelector
import byContext.exceptions.{MultipleValuesWithSameScoreError, RequiredValueMissingError}
import byContext.model.PossibleValue
import byContext.score._
import byContext.score.valueContainers.DefaultSingleValueContainer
import org.scalamock.scalatest.MockFactory
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class DefaultSingleValueContainerTests extends WordSpecLike with Matchers with MockFactory
  with EitherValues with Creators{
  val emptyValues = Array.empty[PossibleValue]

  def calc(values: Array[Any]): ScoreCalculator = {
    val calculator = stub[ScoreCalculator]
    (calculator.calculate _)
      .when(emptyCTX, emptyValues)
      .returns(values.map(valueWithScore(_,1)))
    calculator
  }
  def selector() = new DefaultValueSelector {
    override def select(valuesWithScore: Iterable[ValueWithScore]): Either[MultipleValuesWithSameScoreError, ValueWithScore] = {
      Right(valuesWithScore.head)
    }
  }

  def single(values: Array[Any], required: Boolean): DefaultSingleValueContainer = {
    new DefaultSingleValueContainer("", calc(values), emptyValues, selector(), required)
  }

  "DefaultSingleValueContainer" must {
    "return a right with a single value when score calculator returns a single result" in {
      single(Array("a"), true).get(emptyCTX).right.value should be("a")
    }
    "calls DefaultValueSelector when more than one value returns and returns the value that is selected by DefaultValueSelector" in {
      val valueWithScores = Array(valueWithScore("1",1),valueWithScore("2",2))
      val selectorMock = mock[DefaultValueSelector]
      (selectorMock.select _).expects(valueWithScores.toIterable).returning(Right(valueWithScore("1",1)))

      val calcStub = stub[ScoreCalculator]
      (calcStub.calculate _).when(*,*).returns(valueWithScores)
      new DefaultSingleValueContainer("", calcStub, emptyValues, selectorMock, true)
        .get(emptyCTX).right.value should be ("1")

      single(Array("a"), true).get(emptyCTX).right.value should be("a")
    }
    "return a left with RequiredValueMissingError when score calculator returns no values and a value is required" in {
      single(Array.empty[Any], true).get(emptyCTX).left.value shouldBe a[RequiredValueMissingError]
    }
    "return a right with None when score calculator returns no values and a value is not required" in {
      single(Array.empty[Any], false).get(emptyCTX).right.value should be(None)
    }
  }
}
