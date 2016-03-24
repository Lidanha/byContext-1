package byContext.score.valueContainers

import byContext.exceptions.{MinimumResultItemsCountError, ByContextError}
import byContext.index.{TreeNodeConverter, VisitContext}
import byContext.model.{ValueSelected, PossibleValue, QueryContext}
import byContext.score.{ScoreCalculator}

class DefaultArrayValueContainer(path:String, calculator: ScoreCalculator,
                                 possibleValues:Array[PossibleValue], minResultItemsCount:Int)
  extends ArrayValueContainer{

  override def get(ctx: QueryContext): Either[ByContextError, Array[Any]] = {
    calculator.calculate(ctx, possibleValues) match {
      case res if res.size < minResultItemsCount => Left(new MinimumResultItemsCountError())
      case res =>

        Right(res.view.zipWithIndex.map{
          x=>
            val (v,index) = x
            ctx.notify(ValueSelected(s"$path [$index]",metadata = v.possibleValue.metadata))
            v.possibleValue.value
        }.toArray)
    }
  }
}

trait ArrayValueMarker{
  val possibleValues:Array[PossibleValue]
  val minResultItemsCount:Int
}

class ArrayValueConverter(calculator: ScoreCalculator) extends TreeNodeConverter{
  val convert : PartialFunction[(VisitContext,Any),Any] = {
    case (ctx,marker:ArrayValueMarker) =>
      new DefaultArrayValueContainer(ctx.absolutePath,calculator,marker.possibleValues, marker.minResultItemsCount)
  }
}