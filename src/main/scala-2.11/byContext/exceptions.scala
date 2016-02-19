package byContext

case class CouldNotSelectDefaultValueError() extends ByContextError
case class RequiredValueMissingError() extends ByContextError
case class MinimumResultItemsCountError() extends ByContextError