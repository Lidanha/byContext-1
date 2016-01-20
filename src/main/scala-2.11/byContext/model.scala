package byContext

import byContext.ValueRelevancy.ValueRelevancy

case class QueryContext(ctx:Map[String,String] = Map())

object ValueRelevancy extends Enumeration{
  type ValueRelevancy = Value
  val NotRelevant,Neutral, Relevant = Value
}

trait FilterRule{
  def evaluate(ctx:QueryContext):ValueRelevancy
}
case class PossibleValue(value:Any, rules:Iterable[FilterRule])