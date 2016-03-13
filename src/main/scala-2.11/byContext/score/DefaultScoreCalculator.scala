package byContext.score

import byContext._

class DefaultScoreCalculator extends ScoreCalculator{
  override def calculate(ctx: QueryContext, possibleValues:Array[PossibleValue]): Array[ValueWithScore] = {
    possibleValues
      .collect{
        case v@PossibleValue(_, Some(rule),_) =>
          val probe = new ProbeImpl()
          rule.evaluate(ctx,probe)
          (v,probe)
      }
      .collect {
        case (possibleValue, probe) if !(probe.getNotRelevantCount > 0) =>
          val score = probe.getRelevantCount

          ValueWithScore(possibleValue.value, score, possibleValue.settings)
      }
  }
}
