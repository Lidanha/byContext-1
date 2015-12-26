package byContext.score.rules

import byContext.score.ValueRelevancy._
import byContext.score.{QueryContext, FilterRule, ValueRelevancy}

class ExactTextMatchRule(val subject:String, val value:String) extends FilterRule{
  override def evaluate(ctx: QueryContext): ValueRelevancy =
    ctx.ctx.get(subject)
      .fold(ValueRelevancy.Neutral)(v=> if(v == value) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant)
}
