package byContext.score

import byContext.ValueProvider

class MultiplePossibleValuesValueProvider(calculator: ScoreCalculator,
                                          possibleValues:Array[PossibleValue],
                                          allowMultiple:Boolean,
                                          isRequired:Boolean)
  extends ValueProvider{
  override def get(ctx:QueryContext) : Either[ByContextError,Any] = {
    calculator.calculateScoreForRelevantValues(ctx, possibleValues) match {
      case res if res.size == 1 => Right(res.head.value)
      case res if res.size > 1 && allowMultiple => Right(res.map(_.value))
      case res if res.size == 0 && !isRequired=> Right(None)
      case res if res.size > 1 && !allowMultiple => Left(new MultipleValuesNotAllowedError())
      case res if res.size == 0 && isRequired=> Left(new RequiredValueMissingError())
    }
  }
}
