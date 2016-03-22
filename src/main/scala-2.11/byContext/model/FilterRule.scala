package byContext.model

trait FilterRule {
  def evaluate(ctx:QueryContext, probe: Probe):Unit
}
