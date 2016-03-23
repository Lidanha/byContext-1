package byContext.exceptions

case class StringInterpolationFailed(inner:Throwable) extends ByContextError