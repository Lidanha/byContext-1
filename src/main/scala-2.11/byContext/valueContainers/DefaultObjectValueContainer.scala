package byContext.score.valueContainers

import byContext.exceptions.ByContextError
import byContext.model.{PossibleValue, QueryContext}
import byContext.score.ScoreCalculator
//TODO:consider the need for this class, it does the same as ArrayValueContainer with the addition of casting the result into a tuple
class DefaultObjectValueContainer(calculator: ScoreCalculator,
                                  possibleValues:Array[PossibleValue],
                                  minResultItemsCount:Int)
  extends ObjectValueContainer {
  override def get(ctx: QueryContext): Either[ByContextError, Array[(String,Any)]] = {
    val arrayContainer = new DefaultArrayValueContainer(calculator,possibleValues, minResultItemsCount)
    arrayContainer.get(ctx) match {
      case left @ Left(e) => Left(e)
        //TODO:think of a way to avoid this cast, maybe add validation on creation, could also rely on validation on loading of data
      case Right(res) => Right(res.map(_.asInstanceOf[(String,Any)]))
    }
  }
}
