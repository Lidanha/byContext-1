package byContext.exceptions

import byContext.score.ValueWithScore

case class MultipleValuesWithSameScoreError(conflictingValues:Array[ValueWithScore]) extends DefaultValueSelectorError {
  override def toString() : String = {
    s"{MultipleValuesWithSameScoreError ${conflictingValues.map(_.value).mkString(" - ")}}"
  }
}
