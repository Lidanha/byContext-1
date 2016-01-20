package byContext.score.valueContainers

import byContext.ByContextError
import byContext.score.{MinimumResultItemsCountError, QueryContext, PossibleValue, ScoreCalculator}

class DefaultArrayValueContainer(calculator: ScoreCalculator, possibleValues:Array[PossibleValue], minResultItemsCount:Int)
  extends ArrayValueContainer{

  override def get(ctx: QueryContext): Either[ByContextError, Array[Any]] = {
    calculator.calculateScoreForRelevantValues(ctx, possibleValues) match {
      case res if res.size < minResultItemsCount => Left(new MinimumResultItemsCountError())
      case res => Right(res.map(_.value))
    }
  }
}
