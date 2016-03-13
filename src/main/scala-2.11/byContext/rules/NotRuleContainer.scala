package byContext.rules

import byContext.{FilterRule, Probe, QueryContext}

object NotRuleContainer{
  def apply(rule: FilterRule) = new NotRuleContainer(rule)
}
class NotRuleContainer(rule: FilterRule) extends FilterRule{
  import byContext.ValueRelevancy._
  override def evaluate(ctx: QueryContext, probe:Probe): Unit = {
    val internalProbe = new Probe{
      override def setRelevancy(r: ValueRelevancy): Unit = r match {
        case Neutral => probe.setRelevancy(Neutral)
        case Relevant => probe.setRelevancy(NotRelevant)
        case NotRelevant => probe.setRelevancy(Relevant)
      }
    }
    rule.evaluate(ctx,internalProbe)
  }
}
