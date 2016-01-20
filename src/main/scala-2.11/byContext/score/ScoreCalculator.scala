package byContext.score

import byContext.{PossibleValue, QueryContext}

trait ScoreCalculator {
  def calculateScoreForRelevantValues(ctx:QueryContext, possibleValues:Array[PossibleValue]):Array[ValueWithScore]
}


