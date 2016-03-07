package byContext.rules

import byContext.ValueRelevancy.ValueRelevancy
import byContext.{FilterRule, QueryContext, ValueRelevancy}

class AndRuleContainer(left:FilterRule, right:FilterRule) extends FilterRule{
  override def evaluate(ctx: QueryContext): ValueRelevancy = {
    import ValueRelevancy._
    (left.evaluate(ctx),right.evaluate(ctx)) match {
      case (Relevant,Relevant) => Relevant
      case (Neutral,Neutral) => Neutral
      case (_,NotRelevant) | (NotRelevant,_)=> NotRelevant
      case (Relevant,Neutral) | (Neutral,Relevant) => Relevant
    }
  }
}
