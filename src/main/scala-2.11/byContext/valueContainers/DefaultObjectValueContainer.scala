package byContext.score.valueContainers

import byContext.{MinimumResultItemsCountError, PossibleValue, QueryContext, ByContextError}
import byContext.score.{ScoreCalculator}
//TODO:consider the need for this class, it does the same as ArrayValueContainer with the addition of casting the result into a tuple
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
