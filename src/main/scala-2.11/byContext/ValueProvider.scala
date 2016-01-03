package byContext

import byContext.score.QueryContext

trait ValueProvider {
  def get(ctx:QueryContext) : Either[ByContextError,Any]
}
