package byContext.score.valueContainers

import byContext.valueContainers.ValueContainer
import byContext.{QueryContext, ByContextError}

trait ArrayValueContainer extends ValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Array[Any]]
}
