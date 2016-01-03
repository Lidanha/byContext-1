package byContext.score

import byContext.ByContextError

case class MultipleValuesNotAllowedError() extends ByContextError
case class RequiredValueMissingError() extends ByContextError