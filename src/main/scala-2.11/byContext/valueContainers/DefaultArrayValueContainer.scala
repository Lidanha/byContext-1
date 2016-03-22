package byContext.score.valueContainers

import byContext.exceptions.{MinimumResultItemsCountError, ByContextError}
import byContext.model.{PossibleValue, QueryContext}
import byContext.score.{ScoreCalculator}

class DefaultArrayValueContainer(calculator: ScoreCalculator, possibleValues:Array[PossibleValue], minResultItemsCount:Int)
  extends ArrayValueContainer{

  override def get(ctx: QueryContext): Either[ByContextError, Array[Any]] = {
    calculator.calculate(ctx, possibleValues) match {
      case res if res.size < minResultItemsCount => Left(new MinimumResultItemsCountError())
      case res => Right(res.map(_.value))
    }
  }
}
