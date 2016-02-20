package byContext.score.valueContainers

import byContext._
import byContext.score._

class DefaultSingleValueContainer(calculator: ScoreCalculator, possibleValues:Array[PossibleValue],
                                  defaultValueSelector: DefaultValueSelector,
                                  isRequired:Boolean) extends SingleValueContainer{
  override def get(ctx: QueryContext): Either[ByContextError, Any] = {
    calculator.calculateScoreForRelevantValues(ctx, possibleValues) match {
      case res if res.size == 1 => Right(res.head.value)
      case res if res.size > 1 =>
        defaultValueSelector.select(res) match {
          case Right(valueWithScore) => Right(valueWithScore.value)
          case left @ Left(err) => left
        }
      case res if res.size == 0 && isRequired=> Left(new RequiredValueMissingError())
      case res if res.size == 0 && !isRequired=> Right(None)
    }
  }
}
