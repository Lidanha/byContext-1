import byContext.HighestScoreDefaultValueSelector
import byContext.score.ValueWithScore
import org.scalatest.{EitherValues, Matchers, WordSpecLike}

class HighestScoreDefaultValueSelectorTests extends WordSpecLike with Matchers with EitherValues{
  "HighestScoreDefaultValueSelector" must {
    "select the single item with highest score" in {
      select(
        Array(ValueWithScore("",1),
          ValueWithScore("",2))).right.value should be (ValueWithScore("",2))
    }

  }
  def select(values:Iterable[ValueWithScore]) = new HighestScoreDefaultValueSelector().select(values)
}
