package rules

import byContext.{FilterRule, Probe, QueryContext, ValueRelevancy}
import org.scalamock.scalatest.MockFactory

trait RulesTestsHelper {
  this:MockFactory=>

  import ValueRelevancy._
  val relevant = new AnyRef with FilterRule {
    override def evaluate(ctx: QueryContext,probe:Probe): Unit = probe setRelevancy Relevant
  }
  val notRelevant = new AnyRef with FilterRule {
    override def evaluate(ctx: QueryContext,probe:Probe): Unit = probe setRelevancy NotRelevant
  }
  val neutral = new AnyRef with FilterRule {
    override def evaluate(ctx: QueryContext,probe:Probe): Unit = probe setRelevancy Neutral
  }
}
