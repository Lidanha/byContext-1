import byContext.{MultipleValuesWithSameScoreError, EmptyValuesWithScoreProvidedError, HighestScoreDefaultValueSelector}
import byContext.score.ValueWithScore
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class HighestScoreDefaultValueSelectorTests extends WordSpecLike with Matchers with EitherValues{
  "HighestScoreDefaultValueSelector" must {
    "select the single item with highest score" in {
      new HighestScoreDefaultValueSelector().select(
        Array(ValueWithScore("",1),
          ValueWithScore("",2),
          ValueWithScore("",3),
          ValueWithScore("",4),
          ValueWithScore("",10),
          ValueWithScore("",5),
          ValueWithScore("",6))).right.value should be (ValueWithScore("",10))
    }
    "return ValuesWithSameScoreError if more than one value has the same score" in {
      new HighestScoreDefaultValueSelector().select(
        Array(ValueWithScore("",1),
          ValueWithScore("",2),
          ValueWithScore("",3),
          ValueWithScore("",4),
          ValueWithScore("",10),
          ValueWithScore("",10),
          ValueWithScore("",6))).left.value shouldBe a[MultipleValuesWithSameScoreError]
    }
    "return EmptyValuesWithScoreProvidedError if empty list of values is provided" in {
      new HighestScoreDefaultValueSelector().select(
        Array.empty[ValueWithScore]).left.value shouldBe a[EmptyValuesWithScoreProvidedError]
    }

  }
}
