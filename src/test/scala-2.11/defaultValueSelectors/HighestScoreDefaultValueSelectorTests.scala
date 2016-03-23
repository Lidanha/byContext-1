package defaultValueSelectors

import byContext.defaultValueSelection.HighestScoreDefaultValueSelector
import byContext.exceptions.{EmptyValuesWithScoreProvidedError, MultipleValuesWithSameScoreError}
import byContext.score.ValueWithScore
import org.scalatest.{EitherValues, Matchers, WordSpecLike}
import rules.Creators

class HighestScoreDefaultValueSelectorTests extends WordSpecLike with Matchers with EitherValues
  with Creators{
  "HighestScoreDefaultValueSelector" must {
    "select the single item with highest score" in {
      new HighestScoreDefaultValueSelector().select(
        Array(valueWithScore("",1),
          valueWithScore("",2),
          valueWithScore("",3),
          valueWithScore("",4),
          valueWithScore("",10),
          valueWithScore("",5),
          valueWithScore("",6))).right.value should be (valueWithScore("",10))
    }
    "return ValuesWithSameScoreError if more than one value has the same score" in {
      new HighestScoreDefaultValueSelector().select(
        Array(valueWithScore("",1),
          valueWithScore("",2),
          valueWithScore("",3),
          valueWithScore("",4),
          valueWithScore("",10),
          valueWithScore("",10),
          valueWithScore("",6))).left.value shouldBe a[MultipleValuesWithSameScoreError]
    }
    "return EmptyValuesWithScoreProvidedError if empty list of values is provided" in {
      new HighestScoreDefaultValueSelector().select(
        Array.empty[ValueWithScore]).left.value shouldBe a[EmptyValuesWithScoreProvidedError]
    }
  }
}
