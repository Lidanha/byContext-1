package byContext.rules

import byContext.{FilterRule, Probe, QueryContext}

object AndRuleContainer{
  def apply(left:FilterRule, right:FilterRule) = new AndRuleContainer(left,right)
}
class AndRuleContainer(left:FilterRule, right:FilterRule) extends FilterRule{
  override def evaluate(ctx: QueryContext, probe:Probe): Unit = {
    left.evaluate(ctx,probe)
    right.evaluate(ctx,probe)
  }
}
