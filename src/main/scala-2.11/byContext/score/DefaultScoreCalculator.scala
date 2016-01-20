package byContext.score

import byContext.{ValueRelevancy, PossibleValue, QueryContext}

class DefaultScoreCalculator extends ScoreCalculator{
  override def calculateScoreForRelevantValues(ctx: QueryContext, possibleValues:Array[PossibleValue]): Array[ValueWithScore] = {
    possibleValues
      .map(v => (v.value, v.rules.map(_.evaluate(ctx))))
      .collect {
        case (value, rulesEvaluationResult) if !rulesEvaluationResult.exists(_ == ValueRelevancy.NotRelevant) =>
          val score = rulesEvaluationResult.count(_ == ValueRelevancy.Relevant)

          ValueWithScore(value, score)
      }
  }
}
