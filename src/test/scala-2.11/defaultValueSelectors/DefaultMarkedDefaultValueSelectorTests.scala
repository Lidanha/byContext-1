package defaultValueSelectors

import byContext.defaultValueSelection.DefaultMarkedDefaultValueSelector
import byContext.exceptions.{MultipleValuesMarkedAsDefaultError, NoValuesMarkedAsDefaultError}
import org.scalatest.{EitherValues, Matchers, WordSpecLike}
import rules.Creators

class DefaultMarkedDefaultValueSelectorTests extends WordSpecLike
  with Matchers with EitherValues with Creators {
  "DefaultMarkedDefaultValueSelector" must {
    "select the single item that is marked as default" in {
      new DefaultMarkedDefaultValueSelector().select(
        Array(valueWithScore("", 1, isDefault = true),
          valueWithScore("", 2),
          valueWithScore("", 3),
          valueWithScore("", 4),
          valueWithScore("", 10),
          valueWithScore("", 5),
          valueWithScore("", 6))).right.value should be(valueWithScore("", 1, isDefault = true))
    }
    "return NoValuesMarkedAsDefaultError if there are no marked as default" in {
      new DefaultMarkedDefaultValueSelector().select(
        Array(valueWithScore("", 1),
          valueWithScore("", 2),
          valueWithScore("", 3),
          valueWithScore("", 4),
          valueWithScore("", 10),
          valueWithScore("", 5),
          valueWithScore("", 6))).left.value shouldBe a[NoValuesMarkedAsDefaultError]
    }
    "return MultipleValuesMarkedAsDefaultError if there are multiple values marked as default" in {
      new DefaultMarkedDefaultValueSelector().select(
        Array(valueWithScore("", 1, isDefault = true),
          valueWithScore("", 2),
          valueWithScore("", 3),
          valueWithScore("", 4),
          valueWithScore("", 10, isDefault = true),
          valueWithScore("", 5),
          valueWithScore("", 6))).left.value shouldBe a[MultipleValuesMarkedAsDefaultError]
    }
  }
}