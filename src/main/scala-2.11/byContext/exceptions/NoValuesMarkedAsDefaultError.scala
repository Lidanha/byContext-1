package byContext.exceptions

import byContext.score.ValueWithScore

case class NoValuesMarkedAsDefaultError(conflictingValues:Array[ValueWithScore]) extends DefaultValueSelectorError
