package byContext.score.valueContainers

import byContext.{QueryContext, ByContextError}

trait ObjectValueContainer {
  def get(ctx:QueryContext) : Either[ByContextError,Array[(String,Any)]]
}
