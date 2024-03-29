package byContext.rules

import byContext.model.{FilterRule, Probe, ValueRelevancy, QueryContext}

import scala.collection.mutable.ListBuffer

case class AndRuleContainer(left:FilterRule, right:FilterRule) extends FilterRule{
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
