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

trait Probe{
  def setRelevancy(r:ValueRelevancy) : Unit
}
private[byContext] class ProbeImpl() extends Probe {
  private[this] var relevantCount : Int = 0
  private[this] var notRelevantCount : Int = 0
  private[this] var neutralCount : Int = 0

  def getRelevantCount = relevantCount
  def getNotRelevantCount = notRelevantCount
  def getNeutralCount = neutralCount

  import ValueRelevancy._
  override def setRelevancy(r: ValueRelevancy): Unit = r match {
    case Relevant => relevantCount += 1
    case NotRelevant => notRelevantCount += 1
    case Neutral => neutralCount += 1
  }
}
trait FilterRule{
  def evaluate(ctx:QueryContext, probe: Probe):Unit
}
case class PossibleValueSettings(isDefault:Boolean=false)
case class PossibleValue(value:Any, rule:Option[FilterRule], settings:PossibleValueSettings = PossibleValueSettings())
