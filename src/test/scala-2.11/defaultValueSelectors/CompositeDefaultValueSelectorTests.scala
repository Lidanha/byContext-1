package defaultValueSelectors

import byContext.defaultValueSelection.{CompositeDefaultValueSelector, DefaultValueSelector}
import byContext.exceptions.{DefaultValueSelectorAggregateErrors, DefaultValueSelectorError}
import byContext.model.PossibleValueSettings
import byContext.score.ValueWithScore
import byContext._
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class CompositeDefaultValueSelectorTests extends WordSpecLike with Matchers with EitherValues {
  val valueWithScore_1 = ValueWithScore("1",0,PossibleValueSettings())
  val valueWithScore_2 = ValueWithScore("2",0,PossibleValueSettings())
  def errSelector = new DefaultValueSelector{
    override def select(valuesWithScore: Iterable[ValueWithScore]) = Left(new DefaultValueSelectorError{})
  }
  def successSelector(v:ValueWithScore) = new DefaultValueSelector{
    override def select(valuesWithScore: Iterable[ValueWithScore]) = Right(v)
  }
  "CompositeDefaultValueSelector" must {
    "return a ValueWithScore when at least one selector had succeeded - 1" in {
      new CompositeDefaultValueSelector(Array(errSelector, successSelector(valueWithScore_1)))
        .select(Iterable.empty[ValueWithScore]).right.get should be (valueWithScore_1)
    }
    "return a ValueWithScore when at least one selector had succeeded - 2" in {
      new CompositeDefaultValueSelector(Array(errSelector, errSelector, successSelector(valueWithScore_1), errSelector))
        .select(Iterable.empty[ValueWithScore]).right.get should be (valueWithScore_1)
    }
    "returns the result of the first successful selector" in {
      new CompositeDefaultValueSelector(Array(
        errSelector, errSelector,
        successSelector(valueWithScore_1), errSelector, successSelector(valueWithScore_2)
      )).select(Iterable.empty[ValueWithScore]).right.get should be (valueWithScore_1)
    }
    "return aggregate error when all selectors faile" in {
      new CompositeDefaultValueSelector(Array(errSelector, errSelector,errSelector))
        .select(Iterable.empty[ValueWithScore]).left.get shouldBe a [DefaultValueSelectorAggregateErrors]
    }
  }
}
