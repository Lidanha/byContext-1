package byContext.defaultValueSelection

import byContext.exceptions.{MultipleValuesWithSameScoreError, EmptyValuesWithScoreProvidedError, DefaultValueSelectorError}
import byContext.score.ValueWithScore

class HighestScoreDefaultValueSelector extends DefaultValueSelector {
  override def select(valuesWithScore: Iterable[ValueWithScore]): Either[DefaultValueSelectorError, ValueWithScore] = {
    if(valuesWithScore.isEmpty) {
      Left(EmptyValuesWithScoreProvidedError())
    }else{
      val valuesWithMaxScore = extractValuesWithMaxScore(valuesWithScore)
      valuesWithMaxScore.size match {
        case 1 => Right(valuesWithMaxScore.head)
        case size if size > 1 => Left(new MultipleValuesWithSameScoreError(valuesWithScore.toArray))
      }
    }
  }

  def extractValuesWithMaxScore(valuesWithScore: Iterable[ValueWithScore]): Iterable[ValueWithScore] = {
    val (_, valuesWithMaxScore) = valuesWithScore.foldLeft((-1, List[ValueWithScore]())) {
      (tuple, current) =>
        val (maxScore, values) = tuple
        if (current.score > maxScore) {
          (current.score, List(current))
        } else if (current.score == maxScore) {
          (current.score, current :: values)
        } else {
          tuple
        }
    }
    valuesWithMaxScore
  }
}
