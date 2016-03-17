package byContext

import byContext.score.ValueWithScore

abstract class DefaultValueSelectorError extends ByContextError
case class MultipleValuesWithSameScoreError(conflictingValues:Array[ValueWithScore]) extends DefaultValueSelectorError {
  override def toString() : String = {
    s"{MultipleValuesWithSameScoreError ${conflictingValues.map(_.value).mkString(" - ")}}"
  }
}
case class MultipleValuesMarkedAsDefaultError(conflictingValues:Array[ValueWithScore]) extends DefaultValueSelectorError
case class NoValuesMarkedAsDefaultError(conflictingValues:Array[ValueWithScore]) extends DefaultValueSelectorError
case class DefaultValueSelectorAggregateErrors(errors:Array[DefaultValueSelectorError]) extends DefaultValueSelectorError

case class EmptyValuesWithScoreProvidedError() extends DefaultValueSelectorError

case class RequiredValueMissingError() extends ByContextError
case class MinimumResultItemsCountError() extends ByContextError

case class StringInterpolationFailed(msg:String) extends ByContextError