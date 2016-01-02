package byContext

import byContext.score.{ByContextError, QueryContext}

trait ValueProvider {
  def get(ctx:QueryContext) : Either[ByContextError,Any]
}
