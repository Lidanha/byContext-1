package defaultValueSelectors

import byContext.defaultValueSelection.DefaultMarkedDefaultValueSelector
import byContext.exceptions.{NoValuesMarkedAsDefaultError, MultipleValuesMarkedAsDefaultError}
import byContext.model.PossibleValueSettings
import byContext.score.ValueWithScore
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class DefaultMarkedDefaultValueSelectorTests extends WordSpecLike with Matchers with EitherValues {
  "DefaultMarkedDefaultValueSelector" must {
    "select the single item that is marked as default" in {
      new DefaultMarkedDefaultValueSelector().select(
        Array(ValueWithScore("", 1, PossibleValueSettings(isDefault = true)),
          ValueWithScore("", 2, PossibleValueSettings()),
          ValueWithScore("", 3, PossibleValueSettings()),
          ValueWithScore("", 4, PossibleValueSettings()),
          ValueWithScore("", 10, PossibleValueSettings()),
          ValueWithScore("", 5, PossibleValueSettings()),
          ValueWithScore("", 6, PossibleValueSettings()))).right.value should be(ValueWithScore("", 1, PossibleValueSettings(true)))
    }
    "return NoValuesMarkedAsDefaultError if there are no marked as default" in {
      new DefaultMarkedDefaultValueSelector().select(
        Array(ValueWithScore("", 1, PossibleValueSettings()),
          ValueWithScore("", 2, PossibleValueSettings()),
          ValueWithScore("", 3, PossibleValueSettings()),
          ValueWithScore("", 4, PossibleValueSettings()),
          ValueWithScore("", 10, PossibleValueSettings()),
          ValueWithScore("", 5, PossibleValueSettings()),
          ValueWithScore("", 6, PossibleValueSettings()))).left.value shouldBe a[NoValuesMarkedAsDefaultError]
    }
    "return MultipleValuesMarkedAsDefaultError if there are multiple values marked as default" in {
      new DefaultMarkedDefaultValueSelector().select(
        Array(ValueWithScore("", 1, PossibleValueSettings(isDefault = true)),
          ValueWithScore("", 2, PossibleValueSettings()),
          ValueWithScore("", 3, PossibleValueSettings()),
          ValueWithScore("", 4, PossibleValueSettings()),
          ValueWithScore("", 10, PossibleValueSettings(isDefault = true)),
          ValueWithScore("", 5, PossibleValueSettings()),
          ValueWithScore("", 6, PossibleValueSettings()))).left.value shouldBe a[MultipleValuesMarkedAsDefaultError]
    }
  }
}