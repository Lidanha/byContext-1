package byContext.score.valueContainers

import byContext.exceptions.ByContextError
import byContext.model.{PossibleValue, QueryContext}
import byContext.score.ScoreCalculator
//TODO:consider the need for this class, it does the same as ArrayValueContainer with the addition of casting the result into a tuple
// in the constructor - get an array of String->possible value and at get - return a  Map[String,Any]
class DefaultObjectValueContainer(path:String, calculator: ScoreCalculator,
                                  possibleValues:Array[PossibleValue],
                                  minResultItemsCount:Int)
  extends ObjectValueContainer {

  val arrayContainer = new DefaultArrayValueContainer(path,calculator,possibleValues, minResultItemsCount)

  override def get(ctx: QueryContext): Either[ByContextError, Map[String,Any]] = {
    arrayContainer.get(ctx) match {
      case left @ Left(e) => Left(e)
        //TODO:think of a way to avoid this cast, maybe add validation on creation, could also rely on validation on loading of data
      case Right(res) => Right(res.map(_.asInstanceOf[(String,Any)]).toMap)
    }
  }
}
