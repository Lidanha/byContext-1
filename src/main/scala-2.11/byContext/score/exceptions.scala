package byContext.score

class ByContextError

case class MultipleValuesNotAllowedError() extends ByContextError

case class RequiredValueMissingError() extends ByContextError