package byContext.score

import byContext.model.{PossibleValue, ProbeImpl, QueryContext}
import com.typesafe.scalalogging.StrictLogging

class DefaultScoreCalculator extends ScoreCalculator with StrictLogging{
  override def calculate(ctx: QueryContext, possibleValues:Array[PossibleValue]): Array[ValueWithScore] = {
    possibleValues
      .map{
        case v@PossibleValue(_, ruleOpt,_,_) =>
          val probe = new ProbeImpl()
          ruleOpt.map(_.evaluate(ctx,probe))
          (v,probe)
      }
      .collect {
        case (possibleValue, probe) if !(probe.getNotRelevantCount > 0) =>
          val score = probe.getRelevantCount

          ValueWithScore(possibleValue, score)
      }
  }
}
