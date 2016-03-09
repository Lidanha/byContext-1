package defaultValueSelectors

import byContext.score.ValueWithScore
import byContext.{EmptyValuesWithScoreProvidedError, HighestScoreDefaultValueSelector, MultipleValuesWithSameScoreError, PossibleValueSettings}
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class HighestScoreDefaultValueSelectorTests extends WordSpecLike with Matchers with EitherValues{
  "HighestScoreDefaultValueSelector" must {
    "select the single item with highest score" in {
      new HighestScoreDefaultValueSelector().select(
        Array(ValueWithScore("",1, PossibleValueSettings()),
          ValueWithScore("",2, PossibleValueSettings()),
          ValueWithScore("",3, PossibleValueSettings()),
          ValueWithScore("",4, PossibleValueSettings()),
          ValueWithScore("",10, PossibleValueSettings()),
          ValueWithScore("",5, PossibleValueSettings()),
          ValueWithScore("",6, PossibleValueSettings()))).right.value should be (ValueWithScore("",10, PossibleValueSettings()))
    }
    "return ValuesWithSameScoreError if more than one value has the same score" in {
      new HighestScoreDefaultValueSelector().select(
        Array(ValueWithScore("",1, PossibleValueSettings()),
          ValueWithScore("",2, PossibleValueSettings()),
          ValueWithScore("",3, PossibleValueSettings()),
          ValueWithScore("",4, PossibleValueSettings()),
          ValueWithScore("",10, PossibleValueSettings()),
          ValueWithScore("",10, PossibleValueSettings()),
          ValueWithScore("",6, PossibleValueSettings()))).left.value shouldBe a[MultipleValuesWithSameScoreError]
    }
    "return EmptyValuesWithScoreProvidedError if empty list of values is provided" in {
      new HighestScoreDefaultValueSelector().select(
        Array.empty[ValueWithScore]).left.value shouldBe a[EmptyValuesWithScoreProvidedError]
    }
  }
}
