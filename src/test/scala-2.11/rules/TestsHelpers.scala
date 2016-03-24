package rules

import byContext.index.{DataIndex, IndexBuilderInspector, MapDataIndex, TreeVisitor}
import byContext.model._
import byContext.score.ValueWithScore
import byContext.writers.Writer
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

trait WireupHelpers{
  def toIndex (data:Map[String,Any]) : DataIndex = {
    val indexBuilder = new IndexBuilderInspector()
    val visitor = new TreeVisitor()
    visitor.handle(data, Writer.NoOp, handlers = Seq(indexBuilder))
    new MapDataIndex(indexBuilder.getIndex)
  }

}
trait Creators{
  def valueWithScore(v:Any, score:Int, isDefault:Boolean=false)=
    ValueWithScore(PossibleValue(v,None,PossibleValueSettings(isDefault)),score)
  val emptyCTX = ctx()
  def ctx(items:(String,Any)*) = new QueryContext {
    val map = items.map{
      x=>
        if(x._2.isInstanceOf[Int]) (x._1, x._2.asInstanceOf[Int].toDouble) else x
    }.toMap
    override def getQueryParamAs[T](key: String): Option[T] = {
      map.get(key).map(_.asInstanceOf[T])
    }

    override def notify[E <: Event](e: E): Unit = {}
  }
}

