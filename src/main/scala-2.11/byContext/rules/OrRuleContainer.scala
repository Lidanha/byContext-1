package byContext.rules

import byContext.model.{FilterRule, Probe, QueryContext, ValueRelevancy}

import scala.collection.mutable.ListBuffer

case class OrRuleContainer (left:FilterRule, right:FilterRule) extends FilterRule {
  override def evaluate(ctx: QueryContext, probe:Probe) : Unit = {
    import ValueRelevancy._
    val rels = ListBuffer[ValueRelevancy]()
    val internalProbe = new Probe{
      override def setRelevancy(r: ValueRelevancy): Unit = rels += r
    }

    left.evaluate(ctx,internalProbe)
    right.evaluate(ctx,internalProbe)

    if(rels.contains(Relevant)) {
      rels
        .collect{
          case rl@Relevant => rl
          case rl@Neutral => rl
        }
        .foreach(probe.setRelevancy)
    } else {
      rels.foreach(probe.setRelevancy)
    }
  }
}
