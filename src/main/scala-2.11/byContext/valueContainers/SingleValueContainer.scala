package byContext.score.valueContainers

import byContext.valueContainers.ValueContainer
import byContext.{ByContextError, QueryContext}

trait SingleValueContainer extends ValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Any]
}




