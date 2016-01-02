package byContext

trait ValueProvider {
  def get(ctx:QueryContext) : Either[ByContextError,Value]
}
