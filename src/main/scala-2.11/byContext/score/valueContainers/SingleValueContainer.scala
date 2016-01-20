package byContext.score.valueContainers

import byContext.ByContextError
import byContext.score._

trait SingleValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Any]
}




