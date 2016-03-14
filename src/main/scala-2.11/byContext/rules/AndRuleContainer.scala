package byContext.rules

import byContext.{ValueRelevancy, FilterRule, Probe, QueryContext}

import scala.collection.mutable.ListBuffer

object AndRuleContainer{
  def apply(left:FilterRule, right:FilterRule) = new AndRuleContainer(left,right)
}
class AndRuleContainer(left:FilterRule, right:FilterRule) extends FilterRule{
  override def evaluate(ctx: QueryContext, probe:Probe): Unit = {
    import ValueRelevancy._

    val rels = ListBuffer[ValueRelevancy]()

    val internalProbe = new Probe{
      override def setRelevancy(r: ValueRelevancy): Unit = rels += r
    }

    left.evaluate(ctx,internalProbe)
    right.evaluate(ctx,internalProbe)

    if(rels contains NotRelevant) probe.setRelevancy(NotRelevant) else rels.foreach(probe.setRelevancy(_))
  }
}
