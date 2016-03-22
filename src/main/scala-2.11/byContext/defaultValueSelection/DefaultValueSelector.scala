package byContext.defaultValueSelection

import byContext.exceptions.DefaultValueSelectorError
import byContext.score.ValueWithScore

trait DefaultValueSelector {
  def select(valuesWithScore:Iterable[ValueWithScore]) : Either[DefaultValueSelectorError,ValueWithScore]
}





