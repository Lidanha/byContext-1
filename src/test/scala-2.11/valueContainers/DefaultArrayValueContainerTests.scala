package valueContainers

import _root_.rules.ContextHelper
import byContext._
import byContext.score.valueContainers.DefaultArrayValueContainer
import byContext.score._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class DefaultArrayValueContainerTests extends WordSpecLike with Matchers with EitherValues with MockFactory with ContextHelper{
  val emptyValues = Array.empty[PossibleValue]

  def calc(values: Array[Any]): ScoreCalculator = {
    val calculator = stub[ScoreCalculator]
    (calculator.calculate _)
      .when(emptyContext, emptyValues)
      .returns(values.map(ValueWithScore(_,1, PossibleValueSettings())))
    calculator
  }

  def arr(values: Array[Any])(minAllowedItemsCount:Int) =
    new DefaultArrayValueContainer(calc(values), emptyValues, minAllowedItemsCount)

  "DefaultArrayValueContainer" must {
    "return Left(MinimumResultItemsCountError) when score calculator returns less items than the minimum num configured" in {
      arr(Array.empty[Any])(1).get(emptyContext).left.value shouldBe a[MinimumResultItemsCountError]
    }
    "return a right with an array with the provided values - a single value" in {
      arr(Array(""))(1).get(emptyContext).right.value should be (Array(""))
    }
    "return a right with an array with the provided values - multiple values" in {
      arr(Array(1,2))(1).get(emptyContext).right.value should be (Array(1,2))
    }
  }
}