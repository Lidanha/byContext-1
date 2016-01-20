package byContext.score.valueContainers

import byContext.ByContextError
import byContext.score.{MinimumResultItemsCountError, QueryContext, PossibleValue, ScoreCalculator}

class DefaultObjectValueContainer(calculator: ScoreCalculator,
                                  possibleValues:Array[PossibleValue],
                                  minResultItemsCount:Int) extends ObjectValueContainer{
  override def get(ctx: QueryContext): Either[ByContextError, Array[(String,Any)]] = {
    calculator.calculateScoreForRelevantValues(ctx, possibleValues) match {
      case res if res.size < minResultItemsCount => Left(new MinimumResultItemsCountError())
        //TODO: needs to think of a better design to avoid this cast
      case res => Right(res.map(_.value.asInstanceOf[(String,Any)]))
    }
  }
}
