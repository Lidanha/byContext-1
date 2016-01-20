package byContext.score.valueContainers

import byContext.ByContextError
import byContext.score.QueryContext

trait ObjectValueContainer {
  def get(ctx:QueryContext) : Either[ByContextError,Array[(String,Any)]]
}
