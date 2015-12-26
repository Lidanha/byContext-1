package byContext.score

import byContext.score.ValueRelevancy.ValueRelevancy

case class QueryContext(ctx:Map[String,String] = Map())

trait Value{}
case class SingleValue(actualValue:AnyRef) extends Value
case class ObjectValue(actualValue:Map[String,Value]) extends Value
case class ArrayValue(actualValue:Array[Value]) extends Value
case object MissingValue extends Value

object ValueRelevancy extends Enumeration{
  type ValueRelevancy = Value
  val NotRelevant,Neutral, Relevant = Value
}

trait FilterRule{
  def evaluate(ctx:QueryContext):ValueRelevancy
}
case class PossibleValue(value:Value, rules:Iterable[FilterRule])