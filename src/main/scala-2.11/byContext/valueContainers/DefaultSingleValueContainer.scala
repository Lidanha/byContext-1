package byContext.score.valueContainers

import byContext.defaultValueSelection.DefaultValueSelector
import byContext.exceptions.{ByContextError, RequiredValueMissingError}
import byContext.index.{TreeNodeConverter, VisitContext}
import byContext.model.{ValueSelected, PossibleValue, QueryContext}
import byContext.score._

class DefaultSingleValueContainer(path:String, calculator: ScoreCalculator, possibleValues:Array[PossibleValue],
                                  defaultValueSelector: DefaultValueSelector,
                                  isRequired:Boolean) extends SingleValueContainer{
  override def get(ctx: QueryContext): Either[ByContextError, Any] = {
    calculator.calculate(ctx, possibleValues) match {
      case res if res.size == 1 =>
        val selected = res.head.possibleValue
        ctx.notify(ValueSelected(path, selected.metadata))
        Right(selected.value)
      case res if res.size > 1 =>
        defaultValueSelector.select(res) match {
          case Right(valueWithScore) =>
            val selected = valueWithScore.possibleValue
            ctx.notify(ValueSelected(path, selected.metadata))
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

class SingleValueConverter(scoreCalculator: ScoreCalculator, defaultValueSelector: DefaultValueSelector)
  extends TreeNodeConverter{
  override val convert:PartialFunction[(VisitContext,Any),Any] = {
    case (visitContext, marker:SingleValueMarker)=>new DefaultSingleValueContainer(
      visitContext.absolutePath,scoreCalculator,marker.possibleValues,
      defaultValueSelector,marker.isRequired
    )
  }
}
