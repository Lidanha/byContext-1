package byContext.score.valueContainers

import byContext.exceptions.ByContextError
import byContext.model.QueryContext
import byContext.valueContainers.ValueContainer

trait ArrayValueContainer extends ValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Array[Any]]
}
