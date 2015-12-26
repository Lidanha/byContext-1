package byContext.score

trait ValueProvider {
  def get(ctx:QueryContext) : Either[ByContextError,Value]
}
