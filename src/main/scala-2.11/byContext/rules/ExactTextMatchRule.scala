package byContext.rules

import byContext.{FilterRule, QueryContext, ValueRelevancy}
import ValueRelevancy._

class ExactTextMatchRule(val subject:String, val value:String) extends FilterRule{
  override def evaluate(ctx: QueryContext): ValueRelevancy =
    ctx.ctx.get(subject)
      .fold(ValueRelevancy.Neutral)(v=> if(v == value) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant)
}
