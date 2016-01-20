package byContext.score.valueContainers

import byContext.{ByContextError, QueryContext}

trait SingleValueContainer{
  def get(ctx:QueryContext) : Either[ByContextError,Any]
}




