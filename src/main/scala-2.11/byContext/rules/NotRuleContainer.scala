package byContext.rules

import byContext.ValueRelevancy.ValueRelevancy
import byContext.{QueryContext, FilterRule}

class NotRuleContainer(rule: FilterRule) extends FilterRule{
  override def evaluate(ctx: QueryContext): ValueRelevancy = {
    import byContext.ValueRelevancy._
    rule.evaluate(ctx) match {
      case Relevant => NotRelevant
      case NotRelevant => Relevant
      case Neutral => Neutral
    }
  }
}
