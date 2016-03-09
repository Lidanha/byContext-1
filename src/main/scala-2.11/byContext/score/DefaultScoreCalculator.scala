package byContext.score

import byContext.{ValueRelevancy, PossibleValue, QueryContext}

class DefaultScoreCalculator extends ScoreCalculator{
  override def calculate(ctx: QueryContext, possibleValues:Array[PossibleValue]): Array[ValueWithScore] = {
    possibleValues
      .map(v => (v, v.rules.map(_.evaluate(ctx))))
      .collect {
        case (possibleValue:PossibleValue, rulesEvaluationResult) if !rulesEvaluationResult.exists(_ == ValueRelevancy.NotRelevant) =>
          val score = rulesEvaluationResult.count(_ == ValueRelevancy.Relevant)

          ValueWithScore(possibleValue.value, score, possibleValue.settings)
      }
  }
}
