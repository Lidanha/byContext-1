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
    (calculator.calculate _)
      .when(emptyctx, emptyValues)
      .returns(values.map(ValueWithScore(_, 1, PossibleValueSettings())))
    calculator
  }
  def selector() = new DefaultValueSelector {
    override def select(valuesWithScore: Iterable[ValueWithScore]): Either[MultipleValuesWithSameScoreError, ValueWithScore] = {
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
    "calls DefaultValueSelector when more than one value returns and returns the value that is selected by DefaultValueSelector" in {
      val valueWithScores = Array(ValueWithScore("1",1, PossibleValueSettings()),ValueWithScore("2",2, PossibleValueSettings()))
      val selectorMock = mock[DefaultValueSelector]
      (selectorMock.select _).expects(valueWithScores.toIterable).returning(Right(ValueWithScore("1",1, PossibleValueSettings())))

      val calcStub = stub[ScoreCalculator]
      (calcStub.calculate _).when(*,*).returns(valueWithScores)
      new DefaultSingleValueContainer(calcStub, emptyValues, selectorMock, true).get(emptyctx).right.value should be ("1")

      single(Array("a"), true).get(emptyctx).right.value should be("a")
    }
    "return a left with RequiredValueMissingError when score calculator returns no values and a value is required" in {
      single(Array.empty[Any], true).get(emptyctx).left.value shouldBe a[RequiredValueMissingError]
    }
    "return a right with None when score calculator returns no values and a value is not required" in {
      single(Array.empty[Any], false).get(emptyctx).right.value should be(None)
    }
  }
}
