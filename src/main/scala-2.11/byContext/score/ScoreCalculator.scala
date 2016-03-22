package byContext.score

import byContext.model.{PossibleValue, QueryContext}

trait ScoreCalculator {
  def calculate(ctx:QueryContext, possibleValues:Array[PossibleValue]):Array[ValueWithScore]
}


