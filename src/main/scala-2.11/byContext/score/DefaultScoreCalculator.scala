package byContext.score

import byContext.{PossibleValue, ProbeImpl, QueryContext}
import com.typesafe.scalalogging.StrictLogging

class DefaultScoreCalculator extends ScoreCalculator with StrictLogging{
  override def calculate(ctx: QueryContext, possibleValues:Array[PossibleValue]): Array[ValueWithScore] = {
    possibleValues
      .map{
        case v@PossibleValue(_, ruleOpt,_) =>
          val probe = new ProbeImpl()
          ruleOpt.map(_.evaluate(ctx,probe))
          (v,probe)
      }
      .collect {
        case (possibleValue, probe) if !(probe.getNotRelevantCount > 0) =>
          val score = probe.getRelevantCount

          ValueWithScore(possibleValue.value, score, possibleValue.settings)
      }
  }
}
