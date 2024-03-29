package valueContainers

import _root_.rules.{Creators}
import byContext.exceptions.MinimumResultItemsCountError
import byContext.model.PossibleValue
import byContext.score._
import byContext.score.valueContainers.DefaultArrayValueContainer
import org.scalamock.scalatest.MockFactory
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class DefaultArrayValueContainerTests extends WordSpecLike with Matchers
  with EitherValues with MockFactory with Creators{
  val emptyValues = Array.empty[PossibleValue]

  def calc(values: Array[Any]): ScoreCalculator = {
    val calculator = stub[ScoreCalculator]
    (calculator.calculate _)
      .when(emptyCTX, emptyValues)
      .returns(values.map(valueWithScore(_,1)))
    calculator
  }

  def arr(values: Array[Any])(minAllowedItemsCount:Int) =
    new DefaultArrayValueContainer("",calc(values), emptyValues, minAllowedItemsCount)

  "DefaultArrayValueContainer" must {
    "return Left(MinimumResultItemsCountError) when score calculator returns less items than the minimum num configured" in {
      arr(Array.empty[Any])(1).get(emptyCTX).left.value shouldBe a[MinimumResultItemsCountError]
    }
    "return a right with an array with the provided values - a single value" in {
      arr(Array(""))(1).get(emptyCTX).right.value should be (Array(""))
    }
    "return a right with an array with the provided values - multiple values" in {
      arr(Array(1,2))(1).get(emptyCTX).right.value should be (Array(1,2))
    }
  }
}