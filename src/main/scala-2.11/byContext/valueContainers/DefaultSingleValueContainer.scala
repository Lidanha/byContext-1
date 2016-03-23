package byContext.score.valueContainers

import byContext.defaultValueSelection.DefaultValueSelector
import byContext.exceptions.{ByContextError, RequiredValueMissingError}
import byContext.model.{PossibleValue, QueryContext}
import byContext.rawInputHandling.{DataSetItem, DataSetItemConverter}
import byContext.score._

class DefaultSingleValueContainer(path:String, calculator: ScoreCalculator, possibleValues:Array[PossibleValue],
                                  defaultValueSelector: DefaultValueSelector,
                                  isRequired:Boolean) extends SingleValueContainer{
  override def get(ctx: QueryContext): Either[ByContextError, Any] = {
    calculator.calculate(ctx, possibleValues) match {
      case res if res.size == 1 =>
        val selected = res.head.possibleValue
        ctx.valueSelected(path, selected.metadata)
        Right(selected.value)
      case res if res.size > 1 =>
        defaultValueSelector.select(res) match {
          case Right(valueWithScore) =>
            val selected = valueWithScore.possibleValue
            ctx.valueSelected(path, selected.metadata)
            Right(selected.value)
          case left @ Left(err) => left
        }
      case res if res.size == 0 && isRequired=> Left(new RequiredValueMissingError())
      case res if res.size == 0 && !isRequired=> Right(None)
    }
  }
}

trait SingleValueMarker{
  val possibleValues:Array[PossibleValue]
  val isRequired:Boolean
}

class SingleValueConverter(scoreCalculator: ScoreCalculator, defaultValueSelector: DefaultValueSelector) extends DataSetItemConverter {
  override def convert: PartialFunction[DataSetItem, Any] = {
    case DataSetItem(currentPath,nodeName,marker:SingleValueMarker) =>
      new DefaultSingleValueContainer(
        currentPath,scoreCalculator,marker.possibleValues,
        defaultValueSelector,marker.isRequired
      )
  }
}
