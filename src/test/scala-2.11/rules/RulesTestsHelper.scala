package rules

import byContext.ValueRelevancy._
import byContext.{ValueRelevancy, QueryContext, FilterRule}

trait RulesTestsHelper{
  val relevant = new AnyRef with FilterRule {
    override def evaluate(ctx: QueryContext): ValueRelevancy = ValueRelevancy.Relevant
  }
  val notRelevant = new AnyRef with FilterRule {
    override def evaluate(ctx: QueryContext): ValueRelevancy = ValueRelevancy.NotRelevant
  }
  val neutral = new AnyRef with FilterRule {
    override def evaluate(ctx: QueryContext): ValueRelevancy = ValueRelevancy.Neutral
  }
}
