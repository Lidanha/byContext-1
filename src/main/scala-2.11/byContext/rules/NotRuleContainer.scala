package byContext.rules

import byContext.model.{FilterRule, Probe, QueryContext, ValueRelevancy}

case class NotRuleContainer(rule: FilterRule) extends FilterRule{
  import ValueRelevancy._
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
