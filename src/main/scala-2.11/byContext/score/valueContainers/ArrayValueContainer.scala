package byContext.score.valueContainers

import byContext.ByContextError
import byContext.score.QueryContext

trait ArrayValueContainer {
  def get(ctx:QueryContext) : Either[ByContextError,Array[Any]]
}
