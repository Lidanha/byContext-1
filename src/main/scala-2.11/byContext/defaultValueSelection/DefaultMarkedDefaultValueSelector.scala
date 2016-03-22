package byContext.defaultValueSelection

import byContext.exceptions.{MultipleValuesMarkedAsDefaultError, NoValuesMarkedAsDefaultError, DefaultValueSelectorError}
import byContext.model.PossibleValueSettings
import byContext.score.ValueWithScore

class DefaultMarkedDefaultValueSelector extends DefaultValueSelector {
  override def select(valuesWithScore: Iterable[ValueWithScore]): Either[DefaultValueSelectorError, ValueWithScore] = {
    val defaultValues = valuesWithScore.collect {
      case v@ValueWithScore(_, _, PossibleValueSettings(true)) => v
    }.toArray

    defaultValues.size match {
      case 0 => Left(NoValuesMarkedAsDefaultError(defaultValues))
      case 1 => Right(defaultValues.head)
      case size if size > 1 => Left(MultipleValuesMarkedAsDefaultError(defaultValues))
    }
  }
}
