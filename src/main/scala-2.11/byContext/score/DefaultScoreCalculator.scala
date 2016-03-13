package byContext.score

import byContext.{ValueRelevancy, PossibleValue, QueryContext}
import com.typesafe.scalalogging.StrictLogging

class DefaultScoreCalculator extends ScoreCalculator with StrictLogging{
  override def calculate(ctx: QueryContext, possibleValues:Array[PossibleValue]): Array[ValueWithScore] = {
    val ctxString = ctx.toString()
    possibleValues
      .map(v => (v, {
        val evalResult = v.rules.map(_.evaluate(ctx))
        logger.debug(s"value ${v.value.toString} evaluation result: ${evalResult.mkString("-")} for ctx: $ctxString")
        evalResult
      }))
      .collect {
        case (v:PossibleValue, evalResults) if !evalResults.exists(_ == ValueRelevancy.NotRelevant) =>
          val score = evalResults.count(_ == ValueRelevancy.Relevant)
          ValueWithScore(v.value, score, v.settings)
      }
  }
}
