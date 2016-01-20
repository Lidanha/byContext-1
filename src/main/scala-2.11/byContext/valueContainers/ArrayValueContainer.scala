package byContext.score.valueContainers

import byContext.{QueryContext, ByContextError}

trait ArrayValueContainer {
  def get(ctx:QueryContext) : Either[ByContextError,Array[Any]]
}
