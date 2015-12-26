package byContext.score

class SingleValueProvider(val value:AnyRef) extends ValueProvider{
  override def get(ctx: QueryContext): Either[ByContextError, Value] = Right(SingleValue(value))
}
object SingleValueProvider{
  def apply(value:AnyRef) = new SingleValueProvider(value)
}
