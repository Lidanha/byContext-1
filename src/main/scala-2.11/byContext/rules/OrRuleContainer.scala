package byContext.rules

import byContext.ValueRelevancy._
import byContext.{ValueRelevancy, QueryContext, FilterRule}

class OrRuleContainer (left:FilterRule, right:FilterRule) extends FilterRule{
  override def evaluate(ctx: QueryContext): ValueRelevancy = {
    import ValueRelevancy._
    (left.evaluate(ctx),right.evaluate(ctx)) match {
      case (Relevant,Relevant) => Relevant
      case (Neutral,Neutral) => Neutral
      case (_,Relevant) | (Relevant,_)=> Relevant
      case (NotRelevant,Neutral) | (Neutral,NotRelevant) => NotRelevant
    }
  }
}
