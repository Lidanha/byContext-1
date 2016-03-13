package byContext.rules

import byContext.{ValueRelevancy, _}

object OrRuleContainer{
  def apply(left:FilterRule, right:FilterRule) = new OrRuleContainer(left,right)
}
class OrRuleContainer (left:FilterRule, right:FilterRule) extends FilterRule {
  override def evaluate(ctx: QueryContext, probe:Probe) : Unit = {
    import ValueRelevancy._

    val internalProbe = new Probe {
      override def setRelevancy(r: ValueRelevancy): Unit = r match {
        case Relevant | Neutral => probe.setRelevancy(r)
      }
    }

    left.evaluate(ctx,internalProbe)
    right.evaluate(ctx,internalProbe)
  }
}
