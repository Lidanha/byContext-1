package byContext

case class MultipleValuesNotAllowedError() extends ByContextError
case class RequiredValueMissingError() extends ByContextError
case class MinimumResultItemsCountError() extends ByContextError