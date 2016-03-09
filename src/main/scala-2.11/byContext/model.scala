package byContext

import byContext.ValueRelevancy.ValueRelevancy

case class QueryContext(private val items:(String,Any)*){
  val map = Map(items:_*)
  def get(key:String):Option[Any] = map.get(key)

  override def toString() : String = {
    def formatItems = {
      items.map{
        x=>s"${x._1}->${x._2}"
      }.mkString(" - ")
    }
    s"{QueryContext: ${formatItems}"
  }
}

object ValueRelevancy extends Enumeration{
  type ValueRelevancy = Value
  val NotRelevant,Neutral, Relevant = Value
}

trait FilterRule{
  def evaluate(ctx:QueryContext):ValueRelevancy
}
case class PossibleValueSettings(isDefault:Boolean=false)
case class PossibleValue(value:Any, rules:Iterable[FilterRule], settings:PossibleValueSettings = PossibleValueSettings())