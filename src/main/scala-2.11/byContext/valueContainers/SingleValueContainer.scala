package byContext.score.valueContainers

import byContext.exceptions.ByContextError
import byContext.model.QueryContext
import byContext.valueContainers.ValueContainer

trait SingleValueContainer extends ValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Any]
}




