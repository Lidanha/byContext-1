package rules

import byContext.api.QueryBuilder
import byContext._
import byContext.index.{IndexBuilderInspector, MapDataIndex, DataIndex}
import byContext.model.{FilterRule, Probe, ValueRelevancy, QueryContext}
import byContext.rawInputHandling.DataSetVisitor
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

trait ContextHelper{
  val emptyContext = new QueryBuilder()
}

trait WireupHelpers{
  def toIndex (data:Map[String,Any]) : DataIndex = {
    val indexBuilder = new IndexBuilderInspector()
    val dataSetVisitor = new DataSetVisitor()
    dataSetVisitor.visit(data, inspectors = Seq(indexBuilder))
    new MapDataIndex(indexBuilder.getIndex)
  }
}
