package byContext.score.valueContainers

import byContext.exceptions.{MinimumResultItemsCountError, ByContextError}
import byContext.model.{PossibleValue, QueryContext}
import byContext.rawInputHandling.{DataSetItem, DataSetItemConverter}
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
            ctx.valueSelected(s"$path [$index]",metadata = v.possibleValue.metadata)
            v.possibleValue.value
        }.toArray)
    }
  }
}

trait ArrayValueMarker{
  val possibleValues:Array[PossibleValue]
  val minResultItemsCount:Int
}

class ArrayValueConverter(calculator: ScoreCalculator) extends DataSetItemConverter{
  override def convert: PartialFunction[DataSetItem, Any] = {
    case DataSetItem(currentPath,nodeName,marker:ArrayValueMarker) =>
      new DefaultArrayValueContainer(currentPath,calculator,marker.possibleValues, marker.minResultItemsCount)
  }
}