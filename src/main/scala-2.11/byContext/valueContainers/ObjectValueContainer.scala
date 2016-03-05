package byContext.score.valueContainers

import byContext.valueContainers.ValueContainer
import byContext.{QueryContext, ByContextError}

trait ObjectValueContainer extends ValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Array[(String,Any)]]
}
