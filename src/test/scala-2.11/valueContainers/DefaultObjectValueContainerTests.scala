package valueContainers

import byContext.{MinimumResultItemsCountError, PossibleValue, QueryContext}
import byContext.score.valueContainers.DefaultObjectValueContainer
import byContext.score._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class DefaultObjectValueContainerTests extends WordSpecLike with Matchers with MockFactory with EitherValues {
  val emptyctx = QueryContext()
  val emptyValues = Array.empty[PossibleValue]

  def calc(values: Array[Any]): ScoreCalculator = {
    val calculator = stub[ScoreCalculator]
    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx, emptyValues)
      .returns(values.map(ValueWithScore(_, 1)))
    calculator
  }

  def obj(values:Array[Any])(minResultItemsCount:Int) = new DefaultObjectValueContainer(calc(values), emptyValues, minResultItemsCount)

  "DefaultObjectValueContainer" must {
    "return Left(MinimumResultItemsCountError) when score calculator returns less items than the minimum num configured" in {
      obj(Array.empty[Any])(1).get(emptyctx).left.value shouldBe a[MinimumResultItemsCountError]
    }
    "return a right with an array with the provided values - a single value" in {
      obj(Array("1"->1))(1).get(emptyctx).right.value should be (Array("1"->1))
    }
    "return a right with an array with the provided values - multiple values" in {
      obj(Array("1"->1, "2"->2))(1).get(emptyctx).right.value should be (Array("1"->1, "2"->2))
    }
  }
}
