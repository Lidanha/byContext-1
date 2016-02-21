package byContext.score

import byContext.{PossibleValue, QueryContext}

trait ScoreCalculator {
  def calculate(ctx:QueryContext, possibleValues:Array[PossibleValue]):Array[ValueWithScore]
}


