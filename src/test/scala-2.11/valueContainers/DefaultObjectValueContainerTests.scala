package valueContainers

import byContext.exceptions.MinimumResultItemsCountError
import byContext.model.PossibleValue
import byContext.score._
import byContext.score.valueContainers.DefaultObjectValueContainer
import org.scalamock.scalatest.MockFactory
import org.scalatest.{EitherValues, Matchers, WordSpecLike}
import rules.{Creators}

class DefaultObjectValueContainerTests extends WordSpecLike with Matchers
  with MockFactory with EitherValues with Creators{
  val emptyValues = Array.empty[PossibleValue]

  def calc(values: Array[Any]): ScoreCalculator = {
    val calculator = stub[ScoreCalculator]
    (calculator.calculate _)
      .when(emptyCTX, emptyValues)
      .returns(values.map(valueWithScore(_,1)))
    calculator
  }

  def obj(values:Array[Any])(minResultItemsCount:Int) =
    new DefaultObjectValueContainer("", calc(values), emptyValues, minResultItemsCount)

  "DefaultObjectValueContainer" must {
    "return Left(MinimumResultItemsCountError) when score calculator returns less items than the minimum num configured" in {
      obj(Array.empty[Any])(1).get(emptyCTX).left.value shouldBe a[MinimumResultItemsCountError]
    }
    "return a right with an array with the provided values - a single value" in {
      obj(Array("1"->1))(1).get(emptyCTX).right.value should be (Map("1"->1))
    }
    "return a right with an array with the provided values - multiple values" in {
      obj(Array("1"->1, "2"->2))(1).get(emptyCTX).right.value should be (Map("1"->1, "2"->2))
    }
  }
}
