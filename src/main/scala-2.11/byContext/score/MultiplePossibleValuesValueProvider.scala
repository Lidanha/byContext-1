package byContext.score

import byContext.{ByContextError}
/*
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
}*/
trait SingleValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Any]
}
class DefaultSingleValueContainer(calculator: ScoreCalculator, possibleValues:Array[PossibleValue],
                                  isRequired:Boolean) extends SingleValueContainer{
  override def get(ctx: QueryContext): Either[ByContextError, Any] = {
    calculator.calculateScoreForRelevantValues(ctx, possibleValues) match {
      case res if res.size == 1 => Right(res.head.value)
      case res if res.size > 1 => Left(new MultipleValuesNotAllowedError())
      case res if res.size == 0 && isRequired=> Left(new RequiredValueMissingError())
      case res if res.size == 0 && !isRequired=> Right(None)
    }
  }
}
trait ArrayValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Array[Any]]
}
class ArrayValueContainerImpl(calculator: ScoreCalculator,
                              possibleValues:Array[PossibleValue],
                              minResultItemsCount:Int) extends ArrayValueContainer{
  override def get(ctx: QueryContext): Either[ByContextError, Array[Any]] = {
    calculator.calculateScoreForRelevantValues(ctx, possibleValues) match {
      case res if res.size < minResultItemsCount => Left(new MinimumResultItemsCountError())
      case res => Right(res.map(_.value))
    }
  }
}
trait ObjectValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Array[(String,Any)]]
}
class ObjectValueContainerImpl(calculator: ScoreCalculator,
                              possibleValues:Array[PossibleValue],
                              minResultItemsCount:Int) extends ObjectValueContainer{
  override def get(ctx: QueryContext): Either[ByContextError, Array[(String,Any)]] = {
    calculator.calculateScoreForRelevantValues(ctx, possibleValues) match {
      case res if res.size < minResultItemsCount => Left(new MinimumResultItemsCountError())
      case res => Right(res.map(_.value.asInstanceOf[(String,Any)]))
    }
  }
}